package com.example.lib;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class BingFix {
    /**
     * 执行修复
     * @param context  为了使用者方便  传递的参数由ClassLoader改为Context
     * @param patch
     * dex--->classLoader
     *      classLoader不是直接使用dex，而是使用dex优化过之后的（dexopt）odex文件
     *      classLoader会把优化之后的dex文件存放在odex地址中（私有的地址）
     *      因为第一次需要优化成odex，所以第一次比较慢，以后会快
     *
     *   Multidex有可能会导致ANR
     *       第一次执行的话会将你的dex会执行一个叫优化的操作，可能会很耗时，这是在主线程，如果dex文件大的话会更耗时，容易出现ANR
     *
     *
     *
     */
    public static void installPatch(Context context, String patch){
        //data/data/包名/fiels
        File cacheDir = context.getCacheDir();
        //PathClassLoader
        ClassLoader classLoader = context.getClassLoader();

        Field pathListField = null;
        try {
            pathListField = ShareReflectUtils.getField(classLoader, "pathList");
            //DexPathList
            Object pathList = pathListField.get(classLoader);

            //Element数组
            Field dexElementsField = ShareReflectUtils.getField(pathList, "dexElements");
            Object[] oldElements = (Object[]) dexElementsField.get(pathList);

            File file = new File(patch);
            ArrayList<File> files = new ArrayList<>();
            files.add(file);
            //执行makePathElements  让我们的补丁包变成一个Element数组
            Method makePathElements = ShareReflectUtils.getMethod(pathList, "makePathElements", List.class,File.class,List.class);
            //由于makePathElements似静态方法，所以invoke的第一个参数不用传当前类的实例，传null就行
            ArrayList<IOException> suppressExceptions = new ArrayList<>();
            Object[] newElements = (Object[]) makePathElements.invoke(null, files, cacheDir, suppressExceptions);

            //要替换系统原来的Element数组的新数组
            Object[] repalceElements = (Object[]) Array.newInstance(oldElements.getClass().getComponentType(), oldElements.length + newElements.length);
            //补丁包陷进去
            System.arraycopy(newElements,0,repalceElements,0,newElements.length);
            System.arraycopy(oldElements,0,repalceElements,newElements.length,newElements.length);
            //替换
            dexElementsField.set(pathList,repalceElements);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
