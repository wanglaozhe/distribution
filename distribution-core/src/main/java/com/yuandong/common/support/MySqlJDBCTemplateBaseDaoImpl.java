package com.yuandong.common.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.yuandong.common.exception.SystemException;
import com.yuandong.common.rowmapper.EntityRowMapper;
/**
 * jdbctemplate处理
 * @author Administrator
 *
 */
public class MySqlJDBCTemplateBaseDaoImpl {
	
	 protected Logger log = Logger.getLogger(this.getClass());

	    @Autowired
	    @Qualifier("primaryJdbcTemplate")
	    protected JdbcTemplate firstJdbcTemplate;
	    
	    
	   public JdbcTemplate getJdbcTemplate(){
		   return firstJdbcTemplate;
	   }

	    protected NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate(){
	        if(namedParameterJdbcTemplate == null){
	            namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(getJdbcTemplate());
	        }
	        return namedParameterJdbcTemplate;
	    }



	    protected <T> List<T> queryForList(String sql,Pageable pageable,Class<T> entityClass) throws SystemException,RuntimeException{
	    	StringBuffer sqlBf = new StringBuffer(sql);
	        sqlBf.append(" LIMIT ")
	                .append(pageable.getOffset())
	                .append(",")
	                .append(pageable.getPageSize());
	        return queryForList(sqlBf.toString(),entityClass);
	    }
	    
	    protected Map<String,Object> queryForMap(String sql) throws SystemException,RuntimeException{
	        return this.queryForMap(sql, null);
	    }
	    
	    protected Map<String,Object> queryForMap(String sql,Object[] params) throws SystemException,RuntimeException{
	        if(params == null || params.length == 0){
	            return getJdbcTemplate().queryForMap(sql);
	        }else {
	            return getJdbcTemplate().queryForMap(sql, params);
	        }
	    }

	    protected List<Map<String,Object>> queryForList(String sql,Object[] params, Pageable pageable) throws SystemException,RuntimeException{
	        StringBuffer sqlBf = new StringBuffer(sql);
	        sqlBf.append(" LIMIT ")
	                .append(pageable.getOffset())
	                .append(",")
	                .append(pageable.getPageSize());
	        if(params == null || params.length == 0){
	            return getJdbcTemplate().queryForList(sqlBf.toString());
	        }else {
	            return getJdbcTemplate().queryForList(sqlBf.toString(), params);
	        }
	    }

	    protected List<Map<String,Object>> queryForListNopage(String sql,Object[] params) throws SystemException,RuntimeException{
	        if(params == null || params.length == 0){
	            return getJdbcTemplate().queryForList(sql);
	        }else {
	            return getJdbcTemplate().queryForList(sql, params);
	        }
	    }

	    protected List<Map<String,Object>> queryForList(String sql, Pageable pageable) throws SystemException,RuntimeException{
	        return this.queryForList(sql,null,pageable);
	    }

	    /**
	     * 列表查询封装方法，返回数据直接封装到指定的class类型对象中
	     * @param sql 查询的sql语句，sql中可以包含limit分页部分
	     * @param params 参数列表 当sql不需要参数时，可以为null
	     * @param entityClass 返回集合内部对象的类型
	     * @param <T>
	     * @return
	     * @throws SystemException
	     * @throws RuntimeException
	     */
	    protected <T> List<T> queryForList(String sql,Object[] params,Class<T> entityClass)throws SystemException,RuntimeException{
	        if(params == null || params.length == 0){
	            return getJdbcTemplate().query(sql,(EntityRowMapper<T>)EntityRowMapper.getInstance(entityClass));
	        }
	        return getJdbcTemplate().query(sql,params,(EntityRowMapper<T>)EntityRowMapper.getInstance(entityClass));
	    }

	    /**
	     * 列表查询封装方法，返回数据直接封装到指定的class类型对象中
	     * @param sql 查询的sql语句，sql中可以包含limit分页部分
	     * @param paramMap 参数列表 当sql不需要参数时，可以为null
	     * @param entityClass 返回集合内部对象的类型
	     * @param <T>
	     * @return
	     * @throws SystemException
	     * @throws RuntimeException
	     */
	    protected <T> List<T> queryForList(String sql,Class<T> entityClass,Map<String,Object> paramMap)throws SystemException,RuntimeException{
	        if(paramMap == null || paramMap.size() == 0){
//	            getJdbcTemplate()Util.getgetJdbcTemplate()ByNodeCode(nodeCode).queryForList()
	            return getNamedParameterJdbcTemplate().query(sql,(EntityRowMapper<T>)EntityRowMapper.getInstance(entityClass));
	        }
	        return getNamedParameterJdbcTemplate().query(sql,paramMap,(EntityRowMapper<T>)EntityRowMapper.getInstance(entityClass));
	    }

	    /**
	     * 列表查询封装方法，返回数据直接封装到指定的class类型对象中
	     * @param sql 查询的sql语句，且该语句不需要参数传入
	     * @param entityClass 返回集合内部对象的类型
	     * @param <T>
	     * @return
	     * @throws SystemException
	     * @throws RuntimeException
	     */
	    protected <T> List<T> queryForList(String sql,Class<T> entityClass)throws SystemException,RuntimeException{
	        return queryForList(sql,(Object[])null,entityClass);
	    }

	    /**
	     * 分页查询
	     * @param sql 查询的sql语句，且该语句不能包括limit分页部分字符串
	     *             sql事例：SELECT COLUMN1 COLUMN2 FROM TABLE　WHERE COLUMN3=? AND COLUMN4=5 ORDER BY COLUMN6
	     * @param params 参数列表 不需要参数的可以为空
	     * @param page 最小为1
	     * @param pageSize 分页大小
	     * @param entityClass
	     * @param <T>
	     * @return
	     * @throws SystemException
	     * @throws RuntimeException
	     */
	    protected <T> Page<T> queryForPage(String sql, Object[] params, Pageable pageable, Class<T> entityClass)throws SystemException,RuntimeException{
	    	if(StringUtils.isBlank(sql)){
	            return null;
	        }
	        long totalRecord = 0;
	        List<T> content = null;
	        int fromStrIndex = sql.toUpperCase().indexOf("FROM");
	        //构造统计总记录sql
	        String countSql = "SELECT COUNT(1) " + sql.substring(fromStrIndex);
	        if(params == null || params.length == 0){
	        	totalRecord = getJdbcTemplate().queryForObject(countSql,Long.class);
	        }else {
	        	totalRecord = getJdbcTemplate().queryForObject(countSql,params,Long.class);
	        }
	       //查询数据
	        StringBuffer sqlBf = new StringBuffer(sql);
	        sqlBf.append(" LIMIT ")
	                .append(pageable.getOffset())
	                .append(",")
	                .append(pageable.getPageSize());
	        if(params == null || params.length == 0){
	        	content = queryForList(sqlBf.toString(),entityClass);
	        }else {
	        	content = queryForList(sqlBf.toString(),params,entityClass);
	        }
	        return new PageImpl<T>(content, pageable, totalRecord);
	    }

	    /**
	     * 分页查询
	     * @param sql 查询的sql语句，且该语句不能包括limit分页部分字符串
	     *             sql事例：SELECT COLUMN1 COLUMN2 FROM TABLE　WHERE COLUMN3=? AND COLUMN4=5 ORDER BY COLUMN6
	     * @param params 参数列表 不需要参数的可以为空
	     * @param page 最小为1
	     * @param pageSize 分页大小
	     * @return
	     * @throws SystemException
	     * @throws RuntimeException
	     */
	    protected  Page<Map<String,Object>> queryForPage(String sql, Object[] params, Pageable pageable)throws SystemException,RuntimeException{
	        if(StringUtils.isBlank(sql)){
	            return null;
	        }
	        long totalRecord = 0;
	        List<Map<String,Object>> content = null;
	        
	        int fromStrIndex = sql.toUpperCase().indexOf("FROM");
	        //构造统计总记录sql
	        String countSql = "SELECT COUNT(1) " + sql.substring(fromStrIndex);

	        if(params == null || params.length == 0){
	        	totalRecord = getJdbcTemplate().queryForObject(countSql,Long.class);
	        }else {
	        	totalRecord = getJdbcTemplate().queryForObject(countSql,params,Long.class);
	        }

	        //查询数据
	        StringBuffer sqlBf = new StringBuffer(sql);
	        sqlBf.append(" LIMIT ")
	                .append(pageable.getOffset())
	                .append(",")
	                .append(pageable.getPageSize());
	        if(params == null || params.length == 0){
	        	content = getJdbcTemplate().queryForList(sqlBf.toString());
	        }else {
	        	content = getJdbcTemplate().queryForList(sqlBf.toString(),params);
	        }
	        return new PageImpl<Map<String,Object>>(content, pageable, totalRecord);
	    }

	    /**
	     * 分页查询(占位符参数)
	     * @param sql 查询的sql语句，且该语句不能包括limit分页部分字符串
	     *             sql事例：SELECT COLUMN1 COLUMN2 FROM TABLE　WHERE COLUMN3=? AND COLUMN4=5 ORDER BY COLUMN6
	     * @param paramMap 参数列表 占位符名称：参数中 不需要参数的可以为空
	     * @param page 最小为1
	     * @param pageSize 分页大小
	     * @param entityClass
	     * @param <T>
	     * @return
	     * @throws SystemException
	     * @throws RuntimeException
	     */
	    protected <T> Page<T> queryForPage(String sql, Pageable pageable, Class<T> entityClass,Map<String,Object> paramMap)throws SystemException,RuntimeException{
	    	if(StringUtils.isBlank(sql)){
	            return null;
	        }
	        long totalRecord = 0;
	        List<T> content = null;
	        int fromStrIndex = sql.toUpperCase().indexOf("FROM");
	        //构造统计总记录sql
	        String countSql = "SELECT COUNT(1) " + sql.substring(fromStrIndex);
	        if(paramMap == null || paramMap.size() == 0){
	        	totalRecord = getJdbcTemplate().queryForObject(countSql,Long.class);
	        }else {
	        	totalRecord = getNamedParameterJdbcTemplate().queryForObject(countSql,paramMap,Long.class);
	        }

	        //查询数据
	        StringBuffer sqlBf = new StringBuffer(sql);
	        sqlBf.append(" LIMIT ")
	                .append(pageable.getOffset())
	                .append(",")
	                .append(pageable.getPageSize());
	        if(paramMap == null || paramMap.size() == 0){
	        	content = queryForList(sqlBf.toString(),entityClass);
	        }else {
	        	content = queryForList(sqlBf.toString(), entityClass,paramMap);
	        }
	        return new PageImpl<T>(content, pageable, totalRecord);
	    }

	    /**
	     * 分页查询
	     * @param sql 查询的sql语句，且该语句不能包括limit分页部分字符串
	     *             sql事例：SELECT COLUMN1 COLUMN2 FROM TABLE　WHERE COLUMN3=? AND COLUMN4=5 ORDER BY COLUMN6
	     * @param page 最小为1
	     * @param pageSize 分页大小
	     * @param entityClass
	     * @param <T>
	     * @return
	     * @throws SystemException
	     * @throws RuntimeException
	     */
	    protected <T> Page<T> queryForPage(String sql, Pageable pageable, Class<T> entityClass)throws SystemException,RuntimeException{
	        return queryForPage(sql,null, pageable, entityClass);
	    }

	    /**
	     * 分页查询
	     * @param sql 查询的sql语句，且该语句不能包括limit分页部分字符串
	     *             sql事例：SELECT COLUMN1 COLUMN2 FROM TABLE　WHERE COLUMN3=? AND COLUMN4=5 ORDER BY COLUMN6
	     * @param page 最小为1
	     * @param pageSize 分页大小
	     * @return
	     * @throws SystemException
	     * @throws RuntimeException
	     */
	    protected Page<Map<String,Object>> queryForPage(String sql, Pageable pageable)throws SystemException,RuntimeException{
	        return this.queryForPage(sql,null,pageable);
	    }

	    /**
	     * 批量执行sql
	     * @param sql  UPDATE USER SET　name=? WHERE ID=?
	     * @param paramLists 参数集合，每个集合的Object[]对象代表一条sql的参数列表
	     * @return
	     * @throws SystemException
	     * @throws RuntimeException
	     */
	    protected int[] excuteBatch(String sql,List<Object[]> paramLists)throws SystemException,RuntimeException{
	        return getJdbcTemplate().batchUpdate(sql,paramLists);
	    }

	    /**
	     * 批量执行sql
	     * @param sql DELETE FROM USER WHERE ID=?
	     * @param params 参数集合，每个Object对象代表一条sql的一个参数
	     * @return
	     * @throws SystemException
	     * @throws RuntimeException
	     */
	    protected <T> int[] excuteBatch(String sql,T[] params)throws SystemException,RuntimeException{
	        if(params != null && params.length > 0){
	            List<Object[]> ls = new ArrayList<Object[]>();
	            for(T o : params){
	                ls.add(new Object[]{o});
	            }
	            return excuteBatch(sql,ls);
	        }else{
	            throw new SystemException(SystemException.MISS_PARAM);
	        }
	    }

	    /**
	     * 查询单一对象（主要用于查询结果为java基本数据类型的查询）
	     * @param sql 例如：SELECT COUNT(1) FROM USER
	     * @param params
	     * @param entityClass
	     * @param <T>
	     * @return
	     * @throws SystemException
	     * @throws RuntimeException
	     */
	    protected <T> T queryForObject(String sql,Object[] params,Class<T> entityClass)throws SystemException,RuntimeException{
	        return getJdbcTemplate().queryForObject(sql,params,(EntityRowMapper<T>)EntityRowMapper.getInstance(entityClass));
	    }

	    /**
	     * 查询单一对象（主要用于查询结果为java基本数据类型的查询）
	     * @param sql 例如：SELECT COUNT(1) FROM USER
	     * @param paramMap
	     * @param entityClass
	     * @param <T>
	     * @return
	     * @throws SystemException
	     * @throws RuntimeException
	     */
	    protected <T> T queryForObject(String sql,Map<String,Object> paramMap,Class<T> entityClass)throws SystemException,RuntimeException{
	        return getNamedParameterJdbcTemplate().queryForObject(sql,paramMap,(EntityRowMapper<T>)EntityRowMapper.getInstance(entityClass));
	    }

	    /**
	     * 查询单一对象（主要用于查询结果为java基本数据类型的查询）
	     * @param sql 例如：SELECT COUNT(1) FROM USER
	     * @param entityClass
	     * @param <T>
	     * @return
	     * @throws SystemException
	     * @throws RuntimeException
	     */
	    protected <T> T queryForObject(String sql,Class<T> entityClass)throws SystemException,RuntimeException{
	    	return getJdbcTemplate().queryForObject(sql, (EntityRowMapper<T>)EntityRowMapper.getInstance(entityClass));
	    }

}
