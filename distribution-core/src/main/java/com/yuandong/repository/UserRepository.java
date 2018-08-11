package com.yuandong.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.yuandong.common.support.jpa.repository.BaseRepository;
import com.yuandong.entity.User;

/**
 * @author <a href="mailto:704233326@qq.com">chenqi</a>
 * @date 2018-07-08
 *
 * @version 1.0
 */
@Repository
public interface UserRepository extends BaseRepository<User, String> {

	@Query("select t from User t where t.userName =?1 and t.email =?2")
    User findByNameAndEmail(String userName, String email);

    @Query("select t from User t where t.userName like :userName")
    Page<User> findByName(@Param("userName") String userName, Pageable pageRequest);
}