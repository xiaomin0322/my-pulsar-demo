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
	 * esSql��ѯ sql�������Ҫ�õ������ĵط��� ����#{param} ����ʽ���Σ�#{}�����м�ĵ��ʾ��ǵڶ��������е�ĳ����
	 * 
	 * @param sql       sql���
	 * @param params    ���� һ��key��Ӧһ������
	 * @param className Ŀ�����class
	 * @return
	 */
	public static <T> List<T> query(String sql, Map<String, Object> params, Class<T> className) {
		if (StrUtil.isBlank(sql)) {
			return new ArrayList<>();
		}
		log.debug("es��ѯsql��� >>> {}", sql);
		log.debug("es��ѯparam��� >>> {}", JSONUtil.toJsonStr(params));

		// ת��sql����
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
	 * modelת��ʵ����
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
				// �ж��ǲ��ǻ�����������
				if (!isBasicType(className)) {
					// ����ʵ��
					T t = className.newInstance();
					for (int i = 0; i < fields.length; i++) {
						// ���ø����Կ����޸�
						fields[i].setAccessible(true);
						for (int j = 0; j < esModel.getColumns().size(); j++) {
							// �ж���������es���ص�����һ��
							if (fields[i].getName().equals(esModel.getColumns().get(j).getName())) {
								String type = esModel.getColumns().get(j).getType();
								Object val = row.get(j);
								// ʱ��ת��Time // ����ı׶ˡ�ʱ��ת��û�а취ʶ����ֻ��ͨ�����������û��Time����������ж��ǲ���ʱ��
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
					// ������������
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
	 * ��Ҫ���Es���ص�����ת�����������Ͱ�װ��
	 * 
	 * @author youao.du@gmail.com
	 * @time 23:11
	 * @param type es������з��ص�����
	 * @param val  ��Ҫת����ֵ
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
			// ��ֹʱ��ת������
			try {
				return new Date(Long.parseLong(val + "")); // ʱ���ת��
			} catch (NumberFormatException e) {
				return DateUtil.parseDate(val + ""); // ʱ��ת��Ϊyyyy-MM-dd HH:mm:ss ���Ը���
			}
		}
		return null;
	}

	/**
	 * �ж��ǲ��ǻ����������Ͱ�װ��
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
	 * ���ES�������ݶ����Dto
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
	 * EsQuery��ʹ��sql��ѯ�����᷵�ص���
	 * 
	 * @author youao.du@gmail.com
	 * @time 23:13
	 */
	@Data
	static class Columns {
		/**
		 * ����
		 */
		private String name;

		/**
		 * �е�����
		 */
		private String type;
	}
}
