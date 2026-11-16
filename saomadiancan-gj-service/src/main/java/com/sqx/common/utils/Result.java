package com.sqx.common.utils;

import com.sqx.modules.utils.DataSourceMessageSource;
import org.apache.http.HttpStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 */

@Component
public class Result extends HashMap<String, Object> {
	private static final long serialVersionUID = 1L;

	@Autowired
	private DataSourceMessageSource dataSourceMessageSource;

	public static Result result;

	@PostConstruct
	public void init() {
		result = this;
		result.dataSourceMessageSource = this.dataSourceMessageSource;
	}

	public Result() {
		put("code", 0);
		put("msg", "success");
	}

	public static Result upStatus(Integer rows) {
		return rows > 0 ? success() : error();
	}

	public static Result error() {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, result.dataSourceMessageSource.getSourceFromCache("50001", null, null));
	}

	public static Result error(String msg) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, result.dataSourceMessageSource.getSourceFromCache(msg, null, null));
	}
	public static Result error(String msg,String... str) {
		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, result.dataSourceMessageSource.getSourceFromCache(msg,str, null));
	}
	public static Result error(int code, String msg) {
		Result r = new Result();
		r.put("code", code);
		r.put("msg", result.dataSourceMessageSource.getSourceFromCache(msg, null, null));
		return r;
	}

	public static Result success(String msg,String ... str) {
		Result r = new Result();
		r.put("msg", result.dataSourceMessageSource.getSourceFromCache(msg, str, null));
		return r;
	}

	public static Result success(Map<String, Object> map) {
		Result r = new Result();
		r.putAll(map);
		return r;
	}

	public static Result success() {
		return new Result();
	}

	@Override
	public Result put(String key, Object value) {
		super.put(key, value);
		return this;
	}
}
