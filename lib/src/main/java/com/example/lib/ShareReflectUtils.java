package com.example.lib;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具类
 */
public class ShareReflectUtils {
    /**
     * 反射获取一个属性
     * @param instance  pathClassLoader
     * @param name
     * @return
     */
    public static Field getField(Object instance,String name) throws NoSuchFieldException {

        for (Class<?> clazz = instance.getClass();clazz!=null;clazz = clazz.getSuperclass()){
            try {
                Field declaredField = clazz.getDeclaredField(name);
                //设置权限
                declaredField.setAccessible(true);
                return declaredField;
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        throw new NoSuchFieldException("field"+name+"没有找到在"+instance.getClass());
    }

    /**
     * 反射获取一个方法
     * @param instance  pathClassLoader
     * @param name
     * @return
     */
    public static Method getMethod(Object instance,String name,Class<?>... parameterType) throws NoSuchMethodException {

        for (Class<?> clazz = instance.getClass();clazz!=null;clazz = clazz.getSuperclass()){
            try {
                Method declaredMethod = clazz.getDeclaredMethod(name,parameterType);
                //设置权限
                declaredMethod.setAccessible(true);
                return declaredMethod;
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        throw new NoSuchMethodException("method"+name+"没有找到在"+instance.getClass());
    }
}
