package com.yuandong.common.support;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.yuandong.common.support.jpa.parameter.Operator;
import com.yuandong.common.support.jpa.parameter.Predicate;
import com.yuandong.common.support.jpa.repository.BaseRepository;

public abstract class AbstractBaseService<M extends BaseEntity<ID>, ID extends Serializable>  {

	protected Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
    protected BaseRepository<M, ID> baseRepository;


    /**
	 *  
	 * @param m
	 * @return 
	 * @see com.ibm.common.service.BaseService#save(M) 
	 */
    @Transactional
	public M save(M m) {
        return baseRepository.save(m);
    }
    @Transactional
    public void deleteAll() {
    	baseRepository.deleteAll();
    }

    /**
	 *  
	 * @param m
	 * @return 
	 * @see com.ibm.common.service.BaseService#saveAndFlush(M) 
	 */
    @Transactional
	public M saveAndFlush(M m) {
        m = save(m);
        baseRepository.flush();
        return m;
    }

    /**
	 *  
	 * @param m
	 * @return 
	 * @see com.ibm.common.service.BaseService#update(M) 
	 */
    @Transactional
	public M update(M m) {
        return baseRepository.save(m);
    }

    /**
	 *  
	 * @param id 
	 * @see com.ibm.common.service.BaseService#delete(ID) 
	 */
    @Transactional
	public void delete(ID id) {
        baseRepository.delete(id);
    }

    /**
	 *  
	 * @param m 
	 * @see com.ibm.common.service.BaseService#delete(M) 
	 */
    @Transactional
	public void delete(M m) {
        baseRepository.delete(m);
    }

    /**
	 *  
	 * @param ids 
	 * @see com.ibm.common.service.BaseService#delete(ID[]) 
	 */
    @Transactional
	public void delete(Iterable<ID> ids) {
        baseRepository.deleteByIds(ids);
    }
    
    /**
	 *  
	 * @param id
	 * @return 
	 * @see com.ibm.common.service.BaseService#findOne(ID) 
	 */
	public M findOne(ID id) {
        return baseRepository.findOne(id);
    }

    /**
	 *  
	 * @param id
	 * @return 
	 * @see com.ibm.common.service.BaseService#exists(ID) 
	 */
	public boolean exists(ID id) {
        return baseRepository.exists(id);
    }

    /**
	 *  
	 * @return 
	 * @see com.ibm.common.service.BaseService#count() 
	 */
	public long count() {
        return baseRepository.count();
    }

    /**
	 *  
	 * @return 
	 * @see com.ibm.common.service.BaseService#findAll() 
	 */
	public List<M> findAll() {
        return baseRepository.findAll();
    }

    /**
	 *  
	 * @param sort
	 * @return 
	 * @see com.ibm.common.service.BaseService#findAll(org.springframework.data.domain.Sort) 
	 */
	public List<M> findAll(Sort sort) {
        return baseRepository.findAll(sort);
    }

    /**
	 *  
	 * @param pageable
	 * @return 
	 * @see com.ibm.common.service.BaseService#findAll(org.springframework.data.domain.Pageable) 
	 */
	public Page<M> findAll(Pageable pageable) {
        return baseRepository.findAll(pageable);
    }
	
	public M findOne(String condition, Object... objects){
		return baseRepository.findOne(condition, objects);
	}

	public List<M> findAll(String condition, Object... objects){
		return baseRepository.findAll(condition, objects);
	}
	    
	public List<M> findAll(Operator operator,Predicate... predicates){
		return baseRepository.findAll(operator,predicates);
	}
	    
	public List<M> findAll(Iterable<Predicate> predicates, Operator operator){
		return baseRepository.findAll(predicates, operator);
	}

	public List<M> findAll(Iterable<Predicate> predicates, Operator operator, Sort sort){
		return baseRepository.findAll(predicates, operator, sort);
	}
	
	public List<M> findAll(Operator operator, Sort sort,Predicate... predicates){
		return baseRepository.findAll(operator,sort,predicates);
	}

	public Page<M> findAll(Iterable<Predicate> predicates, Operator operator, Pageable pageable){
    	return baseRepository.findAll(predicates, operator, pageable);
    }
	
	public Page<M> findAll(Operator operator, Pageable pageable,Predicate... predicates){
		return baseRepository.findAll(operator,pageable,predicates);
	}

    public long count(Iterable<Predicate> predicates, Operator operator){
    	return baseRepository.count(predicates, operator);
    }

    public List<M> findAll(String condition, Sort sort, Object... objects){
    	return baseRepository.findAll(condition, sort, objects);
    }

    public Page<M> findAll(String condition, Pageable pageable, Object... objects){
    	return baseRepository.findAll(condition,pageable,objects);
    }

    public long count(String condition, Object... objects){
    	return baseRepository.count(condition, objects);
    }

    
    public Class<M> getEntityClass(){
    	return baseRepository.getEntityClass();
    }

    public List<Map<String,Object>> nativeQuery4Map(String sql){
    	return baseRepository.nativeQuery4Map(sql);
    }

    public Page<Map> nativeQuery4Map(String sql, Pageable pageable){
    	return baseRepository.nativeQuery4Map(sql, pageable);
    }

    public Object nativeQuery4Object(String sql){
    	return baseRepository.nativeQuery4Object(sql);
    }
}
