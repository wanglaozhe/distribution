package com.yuandong.common.support.jpa.repository;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import com.yuandong.common.support.jpa.parameter.Operator;
import com.yuandong.common.support.jpa.parameter.Predicate;

@NoRepositoryBean
public interface BaseRepository<T, ID extends Serializable> extends JpaRepository<T,ID> {

    T findOne(String condition, Object... objects);

    List<T> findAll(String condition, Object... objects);

    List<T> findAll(Iterable<Predicate> predicates, Operator operator);
    
    List<T> findAll(Operator operator,Predicate... predicates);

    List<T> findAll(Iterable<Predicate> predicates, Operator operator, Sort sort);
    
    List<T> findAll(Operator operator, Sort sort,Predicate... predicates);

    Page<T> findAll(Iterable<Predicate> predicates, Operator operator, Pageable pageable);
    
    Page<T> findAll(Operator operator, Pageable pageable,Predicate... predicates);

    long count(Iterable<Predicate> predicates, Operator operator);

    List<T> findAll(String condition, Sort sort, Object... objects);

    Page<T> findAll(String condition, Pageable pageable, Object... objects);

    long count(String condition, Object... objects);

    void deleteByIds(Iterable<ID> ids);

    Class<T> getEntityClass();

    List<Map<String,Object>> nativeQuery4Map(String sql);

    Page<Map> nativeQuery4Map(String sql, Pageable pageable);

    Object nativeQuery4Object(String sql);
}
