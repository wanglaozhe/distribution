package com.yuandong.common.rowmapper;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.jdbc.core.RowMapper;

import com.yuandong.common.exception.SystemException;
import com.yuandong.util.ReflectUtils;

/**
 * Created by chenqi on 2016/4/21.
 */
public class EntityRowMapper<T> implements RowMapper<T> {
    protected Logger log = Logger.getLogger(EntityRowMapper.class);

    private Class<T> entityClass;
    
    private Map<String, String> field2SqlColumnNameMap;
    private Map<String, Class> field2JavaTypeMap;
    
    private static Map<String, EntityRowMapper> entityName2RowMapperMap = new HashMap<String, EntityRowMapper>();

    private EntityRowMapper(Class<T> entityClass) {
        this.entityClass = entityClass;
        this.field2SqlColumnNameMap = ReflectUtils.getAllClassField2SqlColumnFieldMap(entityClass);
        if (field2SqlColumnNameMap != null || field2SqlColumnNameMap.size() > 0) {
        	this.field2JavaTypeMap = ReflectUtils.getAllClassField2JavaTypeMap(entityClass);
        }
        entityName2RowMapperMap.put(entityClass.getName(), this);
    }
    
    public static EntityRowMapper getInstance(Class entityClass){
    	EntityRowMapper rowMapper = entityName2RowMapperMap.get(entityClass.getName());
    	if(rowMapper == null){
    		rowMapper = new EntityRowMapper(entityClass);
    	}
    	return rowMapper;
    }
   

    @Override
    public T mapRow(ResultSet rs, int i) throws SystemException {
        if(rs == null){
            return null;
        }
        T entity = null;
        String field = null;
        try {
            if (field2SqlColumnNameMap == null || field2SqlColumnNameMap.size() == 0) {
                return null;
            }
            entity =  entityClass.newInstance();//BeanUtils.instantiate(entityClass);
            BeanWrapper wrapper = PropertyAccessorFactory.forBeanPropertyAccess(entity);
            Set<String> rsColumns = getResultSetMetaData(rs);

            for (Map.Entry<String, String> entry : field2SqlColumnNameMap.entrySet()) {
                if(rsColumns.contains(entry.getValue())) {
                    field = entry.getKey();
                    Object value = null;
                    value = getColumnValue(rs, entry.getValue(), field2JavaTypeMap.get(field));
                    if (value != null) {
                    	wrapper.setPropertyValue(field, value);
                    }
                }
            }
        } catch (Exception e) {
            if(field != null){
                throw new SystemException( new StringBuffer().append("设置").append(entityClass.getName()).append("《").append(field).append("》字段属性值异常").toString());
            }else {
                throw new SystemException("将resultSet数据绑定到《" + entityClass + "》类中异常");
            }
        }
        return entity;
    }


    /**
     *     取得当前查询结果集的字段信息。
     *   @return  字段名称：字段类型
     *   @exception     SQLException     Description   of   the   Exception
     */
    public static Set<String> getResultSetMetaData(ResultSet rs) throws SQLException {
        Set<String> set = new HashSet<String>();
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        for(int i = 0;i<columnCount;i++) {
//            int cursor = i + 1;
              set.add(meta.getColumnLabel(i+1));
//            map.put(meta.getColumnName(cursor),meta.getColumnType(cursor));
        }
        return set;
    }

    public static <T> Object getColumnValue(ResultSet rs,String columnName,Class<T> valueClass) throws SQLException {
//    	System.out.println("columnName==="+columnName+"  valueClass=="+valueClass);
    	if(valueClass == String.class){
            return rs.getString(columnName);
        }
        if(valueClass == Integer.class || valueClass == int.class){
            return rs.getInt(columnName);
        }
        if(valueClass == Long.class || valueClass == long.class){
            return rs.getLong(columnName);
        }
        if(valueClass == Float.class || valueClass == float.class){
            return rs.getFloat(columnName);
        }
        if(valueClass == Double.class || valueClass == double.class){
            return rs.getDouble(columnName);
        }
        if(valueClass == Short.class || valueClass == short.class){
            return rs.getShort(columnName);
        }
        if(valueClass == Byte.class || valueClass == byte.class){
            return rs.getByte(columnName);
        }
        if(valueClass == Timestamp.class){
            return rs.getTimestamp(columnName);
        }
        if(valueClass == Date.class){
            return rs.getTimestamp(columnName);
        }
        if(valueClass == Time.class){
            return rs.getTime(columnName);
        }
        if(valueClass == Boolean.class || valueClass == boolean.class){
            return rs.getBoolean(columnName);
        }
        if(valueClass == Byte[].class || valueClass == byte[].class){
            return rs.getBytes(columnName);
        }
        if(valueClass == BigDecimal.class){
            return rs.getBigDecimal(columnName);
        }
        return rs.getObject(columnName);
    }
}
