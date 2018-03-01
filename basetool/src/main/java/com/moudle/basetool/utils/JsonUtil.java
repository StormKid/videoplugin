package com.moudle.basetool.utils;

import android.text.TextUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class JsonUtil {
	private static final String TAG = "JsonUtil";
	private static Gson gson = new GsonBuilder().setLenient().setPrettyPrinting().setDateFormat("yyyy-MM-dd HH:mm:ss").create();

	public static String to(Object o) {
		String json = gson.toJson(o);
		return json;
	}

	public static <T> ArrayList<T> fromList(String json, Class<T> cls) {
		if (TextUtils.isEmpty(json)) {
			return new ArrayList<T>();
		}
		ArrayList<T> result = null;
		Type type = new ListParameterizedType(cls);
		try {
			result = gson.fromJson(json, type);
			return result;
		} catch (Exception e) {
			LogUtil.Companion.logE(TAG, "parser error");
		}
		return null;
	}

	public static <T> T from(String json, Class<T> cls) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		T result = null;
		try {
			result = gson.fromJson(json, cls);
			return result;
		} catch (Exception e) {
			LogUtil.Companion.logE(TAG, "parser error");
		}
		return null;
	}

	public static <T> T from(String json, Type typeOfT) {
		if (TextUtils.isEmpty(json)) {
			return null;
		}
		T result = null;
		try {
			result = gson.fromJson(json, typeOfT);
			return result;
		} catch (Exception e) {
			LogUtil.Companion.logE(TAG, "parser error");
		}
		return null;
	}

	private static class ListParameterizedType implements ParameterizedType {

		private Type type;

		private ListParameterizedType(Type type) {
			this.type = type;
		}

		public Type[] getActualTypeArguments() {
			return new Type[]{type};
		}

		@Override
		public Type getRawType() {
			return ArrayList.class;
		}

		@Override
		public Type getOwnerType() {
			return null;
		}

	}

}
