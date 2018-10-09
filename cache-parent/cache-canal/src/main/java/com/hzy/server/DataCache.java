package com.hzy.server;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.ignite.Ignite;
import org.apache.ignite.IgniteCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.alibaba.otter.canal.protocol.CanalEntry.EventType;
import com.hzy.pojo.SynchData;
import com.hzy.service.observerService;
import com.hzy.utils.ResultUtil;

/**
 * 
 * 数据的同步处理
 * 
 * @author  liuchangsong
 * @version  [版本号, 2018年9月28日]
 * @see  [相关类/方法]
 * @since  [产品/模块版本]
 */
@Component
public class DataCache {
	
	@Autowired
	private Ignite ignite;
	@Resource(name="CacheTable")
	private Map<String,String> map;
	@Autowired
	private ApplicationContext applicationContext;
	
	/**
	 * 进行同步数据
	 * @param synchData         将要同步的数据
	 * @return                   处理结果
	 * @see [类、类#方法、类#成员]
	 */
	public ResultUtil<Object> dispose(SynchData<Object> synchData) {
		
		IgniteCache<String, Object> cache = ignite.cache(synchData.getSchemaName());
		if(cache==null)
			return new ResultUtil<Object>("500", null, "该数据库未创建缓存！");
		
		/**
		 * 多表操作
		 */
		String name = map.get(synchData.getTable())+"ServiceImpl";
		//通过bean的名子在spring容器中获取实例
		observerService observer=null;
		try {
			observer=(observerService) applicationContext.getBean(name);
			//如果没有实例的话就当作单表进行处理
			System.err.println(synchData.getTable()+"为多表操作！");
			if(synchData.getEventType()==EventType.DELETE) {
				boolean result = observer.remove(synchData);
				return new ResultUtil<Object>(result==true?"200":"500",result,result==true?"成功移除"+synchData.getTable()+"的数据！":synchData.getTable()+"此条数据暂未添加到缓存！");
			}else if(synchData.getEventType()==EventType.UPDATE) {
				boolean result = observer.update(synchData);
				return new ResultUtil<Object>(result==true?"200":"500",result,result==true?"成功修改"+synchData.getTable()+"的数据！":synchData.getTable()+"此条数据暂未添加到缓存！");
			}else if(synchData.getEventType()==EventType.INSERT) {
				boolean result = observer.add(synchData);
				return new ResultUtil<Object>(result==true?"200":"500",result,result==true?"成功修改"+synchData.getTable()+"的数据！":synchData.getTable()+"此条数据暂未添加到缓存！");
			}else {
				return new ResultUtil<Object>("202", null, synchData.getEventType()+"操作不需要做同步！");
			}
		} catch (Exception e) {
			System.err.println(synchData.getTable()+"为单表操作！");
		}
		
		
		/**
		 * 单表操作
		 */
		
		if(synchData.getEventType()==EventType.DELETE) {
			boolean result = cache.remove(synchData.getCacheKey());
			return new ResultUtil<Object>(result==true?"200":"500",result,result==true?"成功移除"+synchData.getTable()+"的数据！":synchData.getTable()+"此条数据暂未添加到缓存！");
		}else if(synchData.getEventType()==EventType.UPDATE) {
			boolean result = cache.replace(synchData.getCacheKey(),synchData.getAfterData());
			return new ResultUtil<Object>(result==true?"200":"500",result,result==true?"成功修改"+synchData.getTable()+"的数据！":synchData.getTable()+"此条数据暂未添加到缓存！");
		}else {
			return new ResultUtil<Object>("202", null, synchData.getEventType()+"操作不需要做同步！");
		}
		
			
			
	}
}
