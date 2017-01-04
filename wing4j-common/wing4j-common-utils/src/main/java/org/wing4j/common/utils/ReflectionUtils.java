package org.wing4j.common.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射工具类
 */
public abstract class ReflectionUtils {
	/**
	 * 获取一个类所有的字段
	 * @param clazz 提取类
	 * @return 字段
	 */
	public static List<Field> getFields(Class<?> clazz) {
		//获取当前类以及父类的字段
		List<Field> allFields = new ArrayList<Field>(20);
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			allFields.add(field);
		}
		//递归获取超类的字段
		Class<?> superClazz = clazz.getSuperclass();
		if (superClazz == Object.class) {
			return allFields;
		}
		List<Field> superFields = getFields(superClazz);
		allFields.addAll(superFields);
		return allFields;
	}

	/**
	 * 获取一个类所有的字段，不包括transient标注的字段
	 * @param clazz 提取类
	 * @return 字段
	 */
	public static List<Field> getFieldsExcludeTransient(Class<?> clazz) {
		List<Field> allFields = new ArrayList<Field>();
		List<Field> fields = getFields(clazz);
		for (Field field : fields) {
			if (Modifier.isTransient(field.getModifiers())) {
				continue;
			}
			allFields.add(field);
		}
		return allFields;
	}
}
