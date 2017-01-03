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
	 * 获取一个类对象所以的字段
	 * @param clazz 提取类对象
	 * @return 字段列表
	 */
	public static List<Field> getFields(Class<?> clazz) {
		//获取当前类的字段
		List<Field> result = new ArrayList<Field>(20);
		Field[] fields = clazz.getDeclaredFields();
		for (Field field : fields) {
			result.add(field);
		}
		//递归调用获取超类的字段
		Class<?> superClass = clazz.getSuperclass();
		if (superClass == Object.class) {
			return result;
		}
		List<Field> superFields = getFields(superClass);
		result.addAll(superFields);
		return result;
	}

	/**
	 * 获取字段，排除有transient关键字的字段
	 * @param clazz 提取类对象
	 * @return 字段列表
	 */
	public static List<Field> getFieldsExcludeTransient(Class<?> clazz) {
		List<Field> result = new ArrayList<Field>();
		List<Field> fields = getFields(clazz);
		for (Field field : fields) {
			if (Modifier.isTransient(field.getModifiers())) {
				continue;
			}
			result.add(field);
		}
		return result;
	}
}
