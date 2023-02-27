package com.highgo.opendbt.common.bean;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class ResultTO<T> {

	private T obj; // 返回前端的结果集
	private boolean success = true; // 请求是否成功
	private String message; // 请求失败错误信息
	private int code = 200; // 状态码
	private String token; // token

	public T getObj() {
		return obj;
	}

	public boolean isSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public int getCode() {
		return code;
	}

	public String getToken() {
		return token;
	}

	public static <T> ResultTO<T> OK() {
		return new ResultTO<T>();
	}

	public static <T> ResultTO<T> OK(T data) {
		return new ResultTO<T>(data).setMessage("操作成功");
	}

	public static <T> ResultTO<T> OK(T data, String token) {
		return new ResultTO<T>(data, token);
	}

	public static <T> ResultTO<T> TOKEN(String token) {
		return new ResultTO<T>(token);
	}

	public static <T> ResultTO<T> FAILURE(String message) {
		return new ResultTO<T>(false, message);
	}

	public static <T> ResultTO<T> FAILURE(String message, int code) {
		return new ResultTO<T>(false, message, code);
	}

	public ResultTO() {
		super();
	}

	public ResultTO(T obj) {
		super();
		this.obj = obj;
	}

	public ResultTO(T obj, String token) {
		super();
		this.obj = obj;
		this.token = token;
	}

	public ResultTO(String token) {
		super();
		this.token = token;
	}

	public ResultTO(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}

	public ResultTO(boolean success, String message, int code) {
		super();
		this.success = success;
		this.message = message;
		this.code = code;
	}

}
