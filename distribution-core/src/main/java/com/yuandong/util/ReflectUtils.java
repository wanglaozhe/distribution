package com.yuandong.util;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Table;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.yuandong.common.exception.SystemException;
import com.yuandong.common.support.BaseEntity;

/**
 * 利用java反射处理@注解声明的工具类
 * 目前处理的标签有：@SqlTable,@SqlColumn,@Key
 * Created by chenqi on 2016/4/20.
 */
public class ReflectUtils {
    private static final Logger log = LoggerFactory.getLogger(ReflectUtils.class);

//    private static final String IS = "is";
//    private static final String GET = "get";
//    private static final String SET = "set";
//    private static final String SEPARATOR = ",";

    /**
     * 获取指定class对象在数据库对应的数据库表名称，
     * 未定义@SqlTable标签 默认返回类名
     * @param entityClass
     * @return
     */
    public static String getTableNameByClass(Class entityClass) throws SystemException{
        String tablename = null;
        //通过isAnnotationPresent判断是否存在注解
        if(entityClass.isAnnotationPresent(Table.class)){
            //获取类的注解
        	Table sqlTable = (Table)entityClass.getAnnotation(Table.class);
            //获取注解参数值
            if(StringUtils.isBlank(sqlTable.name())) {
                tablename =entityClass.getSimpleName();
            }else{
                tablename = sqlTable.name();
            }
        }else{
            //试图获取一个未经@SqlTable声明的对象所对应的数据库表名，唯有@SqlTable声明的对象才能获取数据库对应表名
            throw new SystemException(SystemException.ISNOT_ANOTATION_SQLTABLE);
        }
        return tablename;
    }


//    /**
//     *利用反射将指定对象的属性字段值修改为指定值
//     * @param obj 需要设置属性值的对象
//     * @param  methodName2MethodMap entity类的所有方法名与方法的映射
//     * @param fieldName 需要设置的属性名
//     * @param fieldValue 需要设置的属性值
//     * @throws NoSuchFieldException
//     * @throws NoSuchMethodException
//     * @throws InvocationTargetException
//     * @throws IllegalAccessException
//     */
//    public static void setFieldValueByFieldName(Object obj,Map<String,Method> methodName2MethodMap,String fieldName,Object fieldValue) throws InvocationTargetException, IllegalAccessException {
//        //获取属性的get或is方法获取对象属性值
//        StringBuffer sb = new StringBuffer(15);
//        sb.append(SET).append(String.valueOf(fieldName.charAt(0)).toUpperCase()).append(fieldName.substring(1));
//        log.debug(sb.toString() + " value="+fieldValue);
//        methodName2MethodMap.get(sb.toString()).invoke(obj,fieldValue);
//    }



    /**
     * 加载class对应对象中所有用@Column标记的属性与对应数据库字段的映射关系
     * 只有用@SqlColumn标记的属性才返回
     * @param entityClass
     * @return
     */
    public static Map<String,String> getAllClassField2SqlColumnFieldMap(Class entityClass){
    	Map<String,String> map = new HashMap<String, String>();
    	Class tmpClass = entityClass;
        while(true){
        	//获取全部字段
            Field[] fields = tmpClass.getDeclaredFields();
            if(fields != null && fields.length > 0){
                for(Field field:fields) {
                    //获取注解
                    if (field.isAnnotationPresent(Column.class)) {
                        Column sqlColumn = field.getAnnotation(Column.class);
                        String columnName = sqlColumn.name();
                        if (StringUtils.isBlank(columnName)) {
                            columnName = field.getName();
                        }
                        map.put(field.getName(),columnName);
                    }
                }
            }
            if(tmpClass == BaseEntity.class){
            	break; 
            }
            tmpClass = tmpClass.getSuperclass();
    	}
        return map;
    }



    /**
     * 加载class每一属性字段与java类型的映射
     * @param entityClass
     * @return
     */
    public static Map<String,Class> getAllClassField2JavaTypeMap(Class entityClass){
        Map<String,Class>  map = new HashMap<String, Class>();
        Class tmpClass = entityClass;
        while(true){
        	 //获取全部字段
            Field[] fields = tmpClass.getDeclaredFields();
            if(fields != null && fields.length > 0){
                for(Field field:fields) {
                    //获取注解
                    map.put(field.getName(),field.getType());
                }
            }
            if(tmpClass == BaseEntity.class){
            	break; 
            }
            tmpClass = tmpClass.getSuperclass();
        }
        return map;
    }

//    /**
//     * 加载entity类的所有方法名与方法的映射
//     * @param entityClass
//     * @return
//     */
//    public static Map<String,Method> getAllMethodName2MethodMap(Class entityClass){
//        Map<String,Method> map = new HashMap<String,Method>();
//        Class tmpClass = entityClass;
//        while(true){
//        	Method[] methods = tmpClass.getDeclaredMethods();
//            if(methods != null && methods.length > 0) {
//                for (Method method : tmpClass.getDeclaredMethods()) {
//                    map.put(method.getName(),method);
//                }
//            }
//        	 if(tmpClass == BaseEntity.class){
//             	break; 
//             }
//             tmpClass = tmpClass.getSuperclass();
//        }
//        return map;
//    }


//    /**
//     * 测试
//     * @param args
//     */
//    public static void main(String[] args) {
//
//        Object user = new User();
//        ((User)user).setName("chenqi");
//        try {
////            System.out.print(getAllClassField2FieldValueMap(user));
////            System.out.println(getAllClassField2TableFieldMap(User.class));
////            System.out.println(getAllClassFile2JavaTypeMap(User.class));
//
////            Field field = user.getClass().getField("name");
////            Method method = user.getClass().getDeclaredMethod("getName");
////            System.out.println(method.getName());
////            System.out.println( method.invoke(user));
//
//            int i = 0;
//            System.out.println(changeEntityToKVString(new Location()));
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

}
