package com.yuandong.common.support.jpa.repository;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.query.QueryUtils;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import com.yuandong.common.support.jpa.exception.DataException;
import com.yuandong.common.support.jpa.parameter.Operator;
import com.yuandong.common.support.jpa.parameter.Predicate;

public class BaseRepositoryImpl<T,ID extends Serializable> extends SimpleJpaRepository<T,ID> implements BaseRepository<T,ID> {
    private final EntityManager entityManager;
    private final JpaEntityInformation<T, ?> entityInformation;

    public BaseRepositoryImpl(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
        this.entityInformation = entityInformation;
    }

    @Override
    public T findOne(String condition, Object... values) {
        if(StringUtils.isEmpty(condition)){
            throw new NullPointerException("条件不能为空!");
        }
        T result = null;
        try {
            result = (T) createQuery(condition, values).getSingleResult();
        } catch (NoResultException e) {
            e.printStackTrace();
        }
        return result;

    }

    @Override
    public List<T> findAll(Iterable<Predicate> predicates, Operator operator) {
        return new JpqlQueryHolder(predicates,operator).createQuery().getResultList();
    }
    
    @Override
	public List<T> findAll(Operator operator, Predicate... predicates) {
    	Iterable<Predicate> iters = null;
		if(predicates != null && predicates.length > 0){
			iters = Arrays.asList(predicates);
		}
		return findAll(iters,operator);
	}

    @Override
    public List<T> findAll(Iterable<Predicate> predicates, Operator operator, Sort sort) {
        return new JpqlQueryHolder(predicates,operator,sort).createQuery().getResultList();
    }
    
    @Override
	public List<T> findAll(Operator operator, Sort sort,Predicate... predicates) {
		Iterable<Predicate> iters = null;
		if(predicates != null && predicates.length > 0){
			iters = Arrays.asList(predicates);
		}
		return findAll(iters,operator,sort);
	}

    @Override
    public Page<T> findAll(Iterable<Predicate> predicates, Operator operator, Pageable pageable) {
        if(pageable==null){
            return new PageImpl<T>((List<T>) findAll(predicates,operator));
        }

        Long total = count(predicates,operator);

        Query query = new JpqlQueryHolder(predicates,operator,pageable.getSort()).createQuery();
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<T> content = total > pageable.getOffset() ? query.getResultList() : Collections.<T> emptyList();

        return new PageImpl<T>(content, pageable, total);
    }
    
    @Override
	public Page<T> findAll(Operator operator, Pageable pageable,Predicate... predicates) {
    	Iterable<Predicate> iters = null;
		if(predicates != null && predicates.length > 0){
			iters = Arrays.asList(predicates);
		}
		return findAll(iters,operator,pageable);
	}

    @Override
    public List<T> findAll(String condition, Object... objects) {
        return createQuery(condition, objects).getResultList();
    }


    @Override
    public List<T> findAll(String condition, Sort sort, Object... objects) {
        return createQuery(condition, sort, objects).getResultList();
    }

    @Override
    public Page<T> findAll(String condition, Pageable pageable, Object... objects) {

        if(pageable==null){
            return new PageImpl<T>((List<T>) findAll(condition,objects));
        }

        Long total = count(condition,objects);

        Query query = createQuery(condition, pageable.getSort(), objects);
        query.setFirstResult(pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<T> content = total > pageable.getOffset() ? query.getResultList() : Collections.<T> emptyList();

        return new PageImpl<T>(content, pageable, total);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        return this.findAll("", pageable);
    }

    @Override
    public List<T> findAll(Iterable<ID> ids) {
        return this.findAll("x."+getIdName()+" in ?1",ids);
    }


    @Override
    public long count(String condition, Object... objects) {
        return new JpqlQueryHolder(condition,objects).createCountQuery().getSingleResult();
    }

    @Override
    public long count(Iterable<Predicate> predicates, Operator operator) {
        return new JpqlQueryHolder(predicates,operator).createCountQuery().getSingleResult();
    }

    @Override
    public void deleteByIds(Iterable<ID> ids) {
        List<T> tlist = super.findAll(ids);
        super.deleteInBatch(tlist);
    }


    @Override
    public Class<T> getEntityClass() {
        return entityInformation.getJavaType();
    }

    public String getIdName(){
        Class<?> entityClass = getEntityClass();
        do{
            Field[] fields = entityClass.getDeclaredFields();
            for(Field field:fields){
                if(field.getAnnotation(Id.class)!=null){
                    return field.getName();
                }
            }
            entityClass = entityClass.getSuperclass();
        }while (entityClass != Object.class);
        throw new DataException(DataException.noID,"未设置主键");
    }

    @Override
    public List<Map<String, Object>> nativeQuery4Map(String sql) {
        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        return nativeQuery.getResultList();
    }

    @Override
    public Page<Map> nativeQuery4Map(String sql, Pageable pageable) {
        Query nativeQuery = entityManager.createNativeQuery(sql);
        nativeQuery.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        nativeQuery.setFirstResult(pageable.getOffset());
        nativeQuery.setMaxResults(pageable.getPageSize());

        Query countNativeQuery = entityManager.createNativeQuery("select count(*) from ("+sql+") a");
        long total = Long.valueOf(String.valueOf(countNativeQuery.getSingleResult()));

        return new PageImpl<Map>(nativeQuery.getResultList(),pageable,total);
    }

    @Override
    public Object nativeQuery4Object(String sql) {
        Query nativeQuery = entityManager.createNativeQuery(sql);

        List results=nativeQuery.getResultList();
        if(results==null || results.size()==0){
            return null;
        }
        try{
            if(results.size()>1){
                throw new RuntimeException("结果应当只有一个，但是发现了"+results.size()+"个。");
            }
            return results.get(0);
        }catch (NoResultException e){
            return null ;
        }

    }

    private TypedQuery createCountQuery(String condition, Object[] objects){

        JpqlQueryHolder queryHolder = new JpqlQueryHolder(condition,objects);

        return queryHolder.createCountQuery();
    }

    /**
     * 声明entityClass的查询
     */
    private Query createQuery(String condition, Sort sort, Object[] objects) {

        JpqlQueryHolder queryHolder = new JpqlQueryHolder(condition,sort,objects);

        return queryHolder.createQuery();
    }

    /**
     * 声明entityClass的查询
     */
    private Query createQuery(String condition, Object[] objects) {
        return createQuery(condition, null, objects);
    }


    private class JpqlQueryHolder {

        //别名
        private final String ALIAS = "x";

        //QUERY ALL
        private final String FIND_ALL_QUERY_STRING = "from %s "+ALIAS;

        //传入的condition 排除列表
        private final String[] IGNORE_CONSTAINS_CHARSEQUENCE = {"where","WHERE","from","FROM"};

        private String condition = null;
        private Sort sort;
        private Object[] objects;
        private Iterable<Predicate> predicates;
        private Operator operator = Operator.AND;

        private JpqlQueryHolder(Iterable<Predicate> predicates, Operator operator, Sort sort) {
            this.predicates = predicates;
            this.operator = operator;
            this.sort = sort;
        }

        private JpqlQueryHolder(Iterable<Predicate> predicates , Operator operator ) {
            this.operator = operator;
            this.predicates = predicates;
        }

        private JpqlQueryHolder(String condition, Sort sort, Object[] objects) {
            this(condition,objects);
            this.sort = sort;
        }

        private JpqlQueryHolder(String condition, Object[] objects) {

            if(StringUtils.startsWithAny(condition,IGNORE_CONSTAINS_CHARSEQUENCE)){
                throw new DataException(DataException.noPermission,"查询条件中只能包含WHERE条件表达式!");
            }
            this.condition = StringUtils.trimToNull(condition);
            this.objects = objects;
        }

        private Query createQuery(){
            StringBuilder sb = new StringBuilder();
            // select x from table
            sb.append(QueryUtils.getQueryString(FIND_ALL_QUERY_STRING, entityInformation.getEntityName()))
                    //where
                    .append(applyCondition());

            Query query = entityManager.createQuery(QueryUtils.applySorting(sb.toString(), sort, ALIAS));
            applyQueryParameter(query);
            return query;
        }

        private TypedQuery<Long> createCountQuery(){
            String ql = String.format(QueryUtils.COUNT_QUERY_STRING, ALIAS, "%s");
            ql = QueryUtils.getQueryString(ql, entityInformation.getEntityName());
            ql += applyCondition();

            TypedQuery<Long> query = entityManager.createQuery(ql,Long.class);
            applyQueryParameter(query);
            return query;
        }

        private List<String> map2Conditions(){
            if(predicates==null||!predicates.iterator().hasNext()){
                return new ArrayList<String>();
            }
            List<String> conditions = new ArrayList<String>();

            Iterator<Predicate> iterator = predicates.iterator();
            int index = 0 ;
            while (iterator.hasNext()){
                Predicate predicate = iterator.next();
                if(predicate.getKey()==null){
                    continue;
                }
                conditions.add(predicate.toCondition(String.valueOf(index)));
                index++ ;
            }
            return conditions;
        }

        private String applyCondition(){
            List<String> conditions = map2Conditions();
            if(condition!=null) {
                conditions.add(condition);
            }
            condition = StringUtils.join(conditions, " " + operator.name() + " ");
            return StringUtils.isEmpty(condition)?"":" where "+condition;
        }

        private void applyQueryParameter(Query query){
            if(objects!=null){
                int i = 0;
                for(Object value:objects){
                    i++;
                    query.setParameter(i,value);
                }
            }
            if(predicates!=null&&predicates.iterator().hasNext()){
                int index = 0 ;
                Iterator<Predicate> iterator = predicates.iterator();
                while (iterator.hasNext()){
                    Predicate predicate = iterator.next();
                    predicate.setParameter(query,String.valueOf(index));
                    index++ ;
                }
            }
        }
    }
}
