package com.hzy.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.protocol.CanalEntry.Column;
import com.alibaba.otter.canal.protocol.CanalEntry.Entry;
import com.alibaba.otter.canal.protocol.CanalEntry.EntryType;
import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.alibaba.otter.canal.protocol.CanalEntry.RowChange;
import com.alibaba.otter.canal.protocol.CanalEntry.RowData;
import com.google.common.collect.Lists;
import com.hzy.pojo.ColumnsInfo;
import com.hzy.pojo.SynchData;

/**
 * 
 * canal工具
 * 将canal客户端监听到的数据List<Entry>转换成List<SynchData<Object>>
 * 
 * @author  liuchangsong
 * @version  [版本号, 2018年9月27日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */

@Component
public class CanalUtil {
               
	@Resource(name = "CacheTable")
	private Map<String, String> cacheTable;           //需要做同步的表以及对应的bean全类名
	@Value("${canal.server.classPath}")
	private String path;
	
	/**
	 * 对外提供的一个转换方法
	 * @param entrys    canal监测到的数据
	 * @return          转换后的数据List<SynchData<Object>>
	 * @see [类、类#方法、类#成员]
	 */
	public List<SynchData<Object>> entryToSynchData(List<Entry> entrys) {
		Set<String> keys = cacheTable.keySet();       //获取需要做同步表
		List<SynchData<Object>> synchDates=new ArrayList<>();   //定义返回数据
		for (Entry entry : entrys) {
			String table = entry.getHeader().getSchemaName() + "." + entry.getHeader().getTableName();
			//判断该条数据是否需要同步，如果不需要就步进行转换
			if (!keys.contains(table)) {
				continue;
			}
			if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN
					|| entry.getEntryType() == EntryType.TRANSACTIONEND) {
				continue;
			}

			if (entry.getEntryType() == EntryType.TRANSACTIONBEGIN
					|| entry.getEntryType() == EntryType.TRANSACTIONEND) {
				continue;
			}
			//调用convert方法进行转换
			List<SynchData<Object>> synchDate = convert(entry);
			synchDates.addAll(synchDate);
		}
		return synchDates;
	}
	
	/**
	 * 具体的转换方法
	 * @param        需要转换的数据Entry
	 * @return       转换后的数据List<SynchData<Object>>
	 * @see [类、类#方法、类#成员]
	 */
	private List<SynchData<Object>> convert(Entry entry) {
		String table = entry.getHeader().getSchemaName() + "." + entry.getHeader().getTableName();
		List<ColumnsInfo> beforeList = new ArrayList<>();        //修改前的字段信息
		List<ColumnsInfo> afterList = new ArrayList<>();         //修改后的字段信息
		Object beforeData = null;       						 //修改后的数据
		Object afterData= null;       						     //修改后的数据
		String cacheKey;										 //缓存的键
		List<SynchData<Object>> synchDatas=new ArrayList<>();    //定义返回数据
		RowChange rowChage = null;                               //该表中所有需要同步的所有行数据
		
		try {
			rowChage = RowChange.parseFrom(entry.getStoreValue());
		} catch (Exception e) {
			throw new RuntimeException("ERROR ## parser of eromanga-event has an error , data:" + entry.toString(), e);
		}
		
		EventType eventType =rowChage.getEventType();             //操作类型
		//遍历行数据
		for(RowData rowData:rowChage.getRowDatasList()) {
			if (eventType == EventType.DELETE) {
				beforeList.addAll(switchColumn(rowData.getBeforeColumnsList()));
				beforeData=switchBean(table, beforeList);
				cacheKey=createCacheKey(entry.getHeader().getTableName(),beforeList);
			} else if (eventType == EventType.INSERT) {
				afterList.addAll(switchColumn(rowData.getAfterColumnsList()));
				afterData=switchBean(table, afterList);
				cacheKey=createCacheKey(entry.getHeader().getTableName(),afterList);
			} else {
				beforeList.addAll(switchColumn(rowData.getBeforeColumnsList()));
				afterList.addAll(switchColumn(rowData.getAfterColumnsList()));
				beforeData=switchBean(table, beforeList);
				afterData=switchBean(table, afterList);
				cacheKey=createCacheKey(entry.getHeader().getTableName(),beforeList);
			}
			//封装转换后的数据
			SynchData<Object> synchData = new SynchData<>(entry.getHeader().getLogfileName(),
					entry.getHeader().getSchemaName(),
					entry.getHeader().getTableName(),
					new Date(entry.getHeader().getExecuteTime()),
					eventType,
					beforeData,
					afterData,
					beforeList,
					afterList,
					table,
					cacheKey);
			synchDatas.add(synchData);
		}
		return synchDatas;
	}
	
	
	/**
	 * 将字段信息封装成JavaBean
	 * @param table         该字段对应的表名
	 * @param columns       将要转换的字段
	 * @return              转换后的bean
	 * @see [类、类#方法、类#成员]
	 */
	@SuppressWarnings({ "unchecked", "unused" })
	private Object switchBean(String table,List<ColumnsInfo> columns){
		//通过表名获取对应的JavaBean名
		String beanName =path+cacheTable.get(table);
		//通过全类名创建对应的class对象
		Class<Object> clazz = null;
		Object init=null;
		try {
			clazz=(Class<Object>) Class.forName(beanName);
		} catch (ClassNotFoundException e) {
			return null;
		}
		try { 
			//创建实例
			init = clazz.newInstance();

			if(columns!=null) {
				//遍历所有的字段
				for (ColumnsInfo col: columns) {
					Object value = TableUtil.zhType(col.getType(),col.getValue());
					//将字段名转换成属性名
					String  fieldName= TableUtil.zhColumn(col.getName());
					//获取JavaBean里面的所有属性
					Field[] fields = clazz.getDeclaredFields();
					//遍历属性进行赋值
					for(Field f:fields) {
						if(f.getName().equals(fieldName)) {
							f.setAccessible(true); 
							f.set(init, value);
						}
					}
				}
			}
			} catch (Exception e) {
					e.printStackTrace();
					System.err.println("javaBean:"+beanName+"转换失败！");
			}
				
		return init;
	}
	/**
	 * 转换字段信息
	 * @param columns        //转换前数据List<Column>
	 * @return               //转换后的数据List<ColumnsInfo>
	 * @see [类、类#方法、类#成员]
	 */
	private List<ColumnsInfo> switchColumn(List<Column> columns) {  
		List<ColumnsInfo> columnsInfos = Lists.newArrayList();
		for (Column column : columns) {
			ColumnsInfo columnsInfo = new ColumnsInfo(column.getName(),
				column.getValue(), 
				column.getMysqlType(),
				column.getUpdated(),
				column.getIsKey());
			columnsInfos.add(columnsInfo);
		}
		return columnsInfos;  
	}
	/**
	 * 根据主键创建缓存的键
	 * @param columns        字段信息
	 * @return               缓存的键
	 * @see [类、类#方法、类#成员]
	 */
	public String createCacheKey(String tableName,List<ColumnsInfo> columns) {
		String cacheKey=null;
		for (ColumnsInfo columnsInfo : columns) {
			if(columnsInfo.isKey()) {
				cacheKey=tableName+"."+columnsInfo.getValue();
				break;
			}
		}
		return cacheKey;
	}
}
