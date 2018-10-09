package com.hzy.pojo;

import java.util.Date;
import java.util.List;

import com.alibaba.otter.canal.protocol.CanalEntry.EventType;

public class SynchData<T> {
	
	/**
	 * 文件名称
	 */
	private String fileName;
	
	/**
	 * 数据库名称
	 */
	private String schemaName;
	
	/**
	 * 表名
	 */
	private String tableName;
	
	/**
	 * 执行时间
	 */
	private Date executeTime;

	/**
	 * 事件类型
	 */
	private EventType eventType;
	/**
	 * 修改前数据
	 */
	private T beforeData;
	/**
	 * 修改后数据
	 */
	private T afterData;
	/**
	 * 修改前的字段信息
	 */
	private List<ColumnsInfo> beforeColumnsList;
	
	/**
	 * 修改后的字段信息
	 */
	private List<ColumnsInfo> afterColumnsList;
	/**
	 * 表全名
	 */
	private String table;
	/**
	 * 缓存中对应的键
	 */
	private String cacheKey;

	public SynchData() {
		super();
	}

	

	public SynchData(String fileName, String schemaName, String tableName, Date executeTime, EventType eventType,
			T beforeData, T afterData, List<ColumnsInfo> beforeColumnsList, List<ColumnsInfo> afterColumnsList,
			String table, String cacheKey) {
		super();
		this.fileName = fileName;
		this.schemaName = schemaName;
		this.tableName = tableName;
		this.executeTime = executeTime;
		this.eventType = eventType;
		this.beforeData = beforeData;
		this.afterData = afterData;
		this.beforeColumnsList = beforeColumnsList;
		this.afterColumnsList = afterColumnsList;
		this.table = table;
		this.cacheKey = cacheKey;
	}



	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}

	public String getTable() {
		return table;
	}
	
	public void setTable(String table) {
		this.table = table;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Date getExecuteTime() {
		return executeTime;
	}

	public void setExecuteTime(Date executeTime) {
		this.executeTime = executeTime;
	}

	public EventType getEventType() {
		return eventType;
	}

	public void setEventType(EventType eventType) {
		this.eventType = eventType;
	}

	public T getBeforeData() {
		return beforeData;
	}

	public void setBeforeData(T beforeData) {
		this.beforeData = beforeData;
	}

	public T getAfterData() {
		return afterData;
	}

	public void setAfterData(T afterData) {
		this.afterData = afterData;
	}

	public List<ColumnsInfo> getBeforeColumnsList() {
		return beforeColumnsList;
	}

	public void setBeforeColumnsList(List<ColumnsInfo> beforeColumnsList) {
		this.beforeColumnsList = beforeColumnsList;
	}

	public List<ColumnsInfo> getAfterColumnsList() {
		return afterColumnsList;
	}

	public void setAfterColumnsList(List<ColumnsInfo> afterColumnsList) {
		this.afterColumnsList = afterColumnsList;
	}



	@Override
	public String toString() {
		return "SynchData [fileName=" + fileName + ", schemaName=" + schemaName + ", tableName=" + tableName
				+ ", executeTime=" + executeTime + ", eventType=" + eventType + ", beforeData=" + beforeData
				+ ", afterData=" + afterData + ", beforeColumnsList=" + beforeColumnsList + ", afterColumnsList="
				+ afterColumnsList + ", table=" + table + ", cacheKey=" + cacheKey + "]";
	}

	
}
