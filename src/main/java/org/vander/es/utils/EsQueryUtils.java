package org.vander.es.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import lombok.Data;

public class EsQueryUtils {

	private static String esUrl = "http://127.0.0.1:9200/_sql?format=json";

	/** logger */
	private static final Logger log = LoggerFactory.getLogger(EsQueryUtils.class);

	/**
	 * esSql查询 sql语句中需要用到参数的地方， 请用#{param} 的形式修饰，#{}符号中间的单词就是第二个参数中的某个键
	 * 
	 * @param sql       sql语句
	 * @param params    参数 一个key对应一个参数
	 * @param className 目标类的class
	 * @return
	 */
	public static <T> List<T> query(String sql, Map<String, Object> params, Class<T> className) {
		if (StrUtil.isBlank(sql)) {
			return new ArrayList<>();
		}
		log.debug("es查询sql入参 >>> {}", sql);
		log.debug("es查询param入参 >>> {}", JSONUtil.toJsonStr(params));

		// 转换sql参数
		if (params != null && params.size() != 0) {
			params.keySet().forEach(key -> {
				sql.replace("#{" + key + "}", params.toString());
			});
		}

		Map<String, Object> query = new HashMap<>();
		query.put("query", sql);

		JSONObject response = JSONUtil.parseObj(HttpUtil.post(esUrl, JSONUtil.toJsonStr(query)));

		List<Columns> columns = JSONUtil.toList(JSONUtil.parseArray(response.getStr("columns")), Columns.class);
		List<List> rows = JSONUtil.toList(JSONUtil.parseArray(response.getStr("rows")), List.class);
//        List<List> rows = JSONObject.parseArray(response.getString("rows"), List.class);
		EsModel esModel = new EsModel(columns, rows);

		return esModelToJavaObject(esModel, className);
	}

	/**
	 * model转换实体类
	 * 
	 * @author youao.du@gmail.com
	 * @param esModel
	 * @param className
	 * @param           <T>
	 * @return
	 */
	private static <T> List<T> esModelToJavaObject(EsModel esModel, Class<T> className) {
		Field[] fields = className.getDeclaredFields();
		List<T> result = new ArrayList<>(esModel.getRows().size());
		esModel.getRows().forEach(row -> {
			try {
				// 判断是不是基本数据类型
				if (!isBasicType(className)) {
					// 新增实体
					T t = className.newInstance();
					for (int i = 0; i < fields.length; i++) {
						// 设置该属性可以修改
						fields[i].setAccessible(true);
						for (int j = 0; j < esModel.getColumns().size(); j++) {
							// 判断属性名和es返回的列名一致
							if (fields[i].getName().equals(esModel.getColumns().get(j).getName())) {
								String type = esModel.getColumns().get(j).getType();
								Object val = row.get(j);
								// 时间转换Time // 这里的弊端。时间转换没有办法识别。我只能通过这个列名有没有Time这个单词来判断是不是时间
								if (esModel.getColumns().get(j).getName().toLowerCase().contains("time"))
									type = "time";
								Object o = castValue(type, val);
								fields[i].set(t, o);
							}
						}
					}
					result.add(t);
				} else {
					String type = esModel.getColumns().get(0).type;
					Object val = row.get(0);
					// 基本数据类型
					Object o = castValue(type, val);

					result.add((T) o);
				}
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
		});

		return result;
	}

	/**
	 * 主要针对Es返回的类型转换成数据类型包装类
	 * 
	 * @author youao.du@gmail.com
	 * @time 23:11
	 * @param type es结果集中返回的类型
	 * @param val  需要转换的值
	 */
	private static Object castValue(String type, Object val) {
		if (StrUtil.isBlank(type) || val == null) {
			return null;
		}
		if ("text".equals(type)) {
			return val.toString();
		} else if ("long".equals(type)) {
			return Long.parseLong(val + "");
		} else if ("time".equals(type)) {
			// 防止时间转换错误
			try {
				return new Date(Long.parseLong(val + "")); // 时间戳转换
			} catch (NumberFormatException e) {
				return DateUtil.parseDate(val + ""); // 时间转换为yyyy-MM-dd HH:mm:ss 可以更换
			}
		}
		return null;
	}

	/**
	 * 判断是不是基本数据类型包装类
	 * 
	 * @author youao.du@gmail.com
	 * @time 22:47
	 * @params
	 */
	private static boolean isBasicType(Class className) {
		if (className == null) {
			return false;
		} else if (className.equals(String.class)) {
			return true;
		} else if (className.equals(Integer.class)) {
			return true;
		} else if (className.equals(Long.class)) {
			return true;
		} else if (className.equals(Short.class)) {
			return true;
		} else if (className.equals(Double.class)) {
			return true;
		} else if (className.equals(Float.class)) {
			return true;
		} else if (className.equals(Character.class)) {
			return true;
		} else if (className.equals(Byte.class)) {
			return true;
		}

		return false;
	}

	public static void main(String[] args) {
		List<Long> query = query("select page_count from library limit 10", null, Long.class);
		query.forEach(it -> System.out.println(it));

		List<Test> listMap = query("select * from library limit 10", null, Test.class);
		listMap.forEach(it -> System.out.println(it));
	}

	/**
	 * 针对ES返回数据定义的Dto
	 * 
	 * @author youao.du@gmail.com
	 * @time 23:12
	 */
	@Data
	static class EsModel {
		private List<Columns> columns;
		private List<List> rows;

		public EsModel(List<Columns> columns, List<List> rows) {
			this.columns = columns;
			this.rows = rows;
		}
	}

	/**
	 * EsQuery中使用sql查询。他会返回的列
	 * 
	 * @author youao.du@gmail.com
	 * @time 23:13
	 */
	@Data
	static class Columns {
		/**
		 * 列名
		 */
		private String name;

		/**
		 * 列的类型
		 */
		private String type;
	}
}
