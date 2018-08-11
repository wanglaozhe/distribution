package com.yuandong.repository;

import org.springframework.stereotype.Repository;
import com.yuandong.common.support.jpa.repository.BaseRepository;
import com.yuandong.entity.MyCollection;

/**
 * @author <a href="mailto:704233326@qq.com">chenqi</a>
 * @date 2018-07-15
 *
 * @version 1.0
 */
@Repository
public interface MyCollectionRepository extends BaseRepository<MyCollection, String> {

}