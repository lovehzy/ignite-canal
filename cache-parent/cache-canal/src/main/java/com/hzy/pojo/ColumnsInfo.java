package com.hzy.pojo;

public class ColumnsInfo {
	/**
	 * 字段名称
	 */
	private String name;
	
	/**
	 * 字段值
	 */
	private String value;
	
	/**
	 * 字段类型
	 */
	private String type;
	
	/**
	 * 字段是否修改
	 */
	private boolean updated;
	/**
	 * 是否为主键
	 */
	private boolean isKey;
	
	public ColumnsInfo() {
		super();
	}
	
	public ColumnsInfo(String name, String value, String type, boolean updated, boolean isKey) {
		super();
		this.name = name;
		this.value = value;
		this.type = type;
		this.updated = updated;
		this.isKey = isKey;
	}

	public boolean isKey() {
		return isKey;
	}

	public void setKey(boolean isKey) {
		this.isKey = isKey;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		return "ColumnsInfo [name=" + name + ", value=" + value + ", type=" + type + ", updated=" + updated + ", isKey="
				+ isKey + "]";
	}

	
	
}
