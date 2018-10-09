package com.hzy.utils;

import java.io.Serializable;

public class ResultUtil<T> implements Serializable{

	private static final long serialVersionUID = 4691673012175662450L;
	
	private String status="200";
	private T result;
	private String comment;
	
	
	
	public ResultUtil() {
		super();
	}
	public ResultUtil(String status, T result, String comment) {
		super();
		this.status = status;
		this.result = result;
		this.comment = comment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public T getResult() {
		return result;
	}
	public void setResult(T result) {
		this.result = result;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	@Override
	public String toString() {
		return "ResultUtil [status=" + status + ", result=" + result + ", comment=" + comment + "]";
	}
	
}
