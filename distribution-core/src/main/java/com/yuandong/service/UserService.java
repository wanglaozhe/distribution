package com.yuandong.service;

import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yuandong.common.Constant;
import com.yuandong.common.support.AbstractBaseService;
import com.yuandong.common.support.jpa.parameter.LinkEnum;
import com.yuandong.common.support.jpa.parameter.Operator;
import com.yuandong.common.support.jpa.parameter.Predicate;
import com.yuandong.entity.User;
import com.yuandong.entity.UserRole;
import com.yuandong.repository.RoleRepository;
import com.yuandong.repository.UserRoleRepository;
import com.yuandong.vo.UserDetailVo;

/**
 * @author <a href="mailto:704233326@qq.com">chenqi</a>
 * @date 2018-07-08
 *
 * @version 1.0
 */
@Service
@CacheConfig(cacheNames={Constant.EHCACHE_USER})
public class UserService extends AbstractBaseService<User, String> {
	
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private UserRoleRepository userRoleRepository;
	
	/* cache使用：
	 	@Caching(evict={
			@CacheEvict(value = Constant.USERCACHEFOROBJECT , key="#user.id"),
			@CacheEvict(value = Constant.USERCACHEFORQUERY,allEntries=true),
			@CacheEvict(value = Constant.ROLECACHEFORQUERY,allEntries=true,condition = "#userRoles != null")
		})
		
		@Cacheable(value = Constant.USERCACHEFOROBJECT,  key="#id")
		
		@CacheEvict(value = CACHE_NAME_B, key = "'user_' + #id") //这是清除缓存
		
		@Cacheable(value = Constant.USERCACHEFORQUERY)//使用默认规则生成key
		
		@CacheEvict(value = Constant.ROLECACHEFORQUERY,allEntries=true)
		
		
	 */
	
	
	
	@Cacheable(key="'findUserDetailVoByUserName'+#userName")
	public UserDetailVo findUserDetailVoByUserName(String userName){
		User user = this.findOne("userName=?", userName);
		if(user != null){
			UserDetailVo userDetailVo = new UserDetailVo();
			BeanUtils.copyProperties(user, userDetailVo);
			List<UserRole> urs = userRoleRepository.findAll("userId=?", user.getId());
			if(urs != null && urs.size() > 0){
				List<String> roleIds = urs.stream().map(UserRole::getRoleId).collect(Collectors.toList());
				Predicate predicate = new Predicate("id", roleIds, LinkEnum.IN);
				userDetailVo.setRoles(roleRepository.findAll(Operator.AND, predicate));
			}
			return userDetailVo;
		}
		return null;
	}
	
	
	public UserDetailVo findUserDetailVoById(String id){
		User user = this.findOne(id);
		if(user != null){
			UserDetailVo userDetailVo = new UserDetailVo();
			BeanUtils.copyProperties(user, userDetailVo);
			List<UserRole> urs = userRoleRepository.findAll("userId=?", user.getId());
			if(urs != null && urs.size() > 0){
				List<String> roleIds = urs.stream().map(UserRole::getRoleId).collect(Collectors.toList());
				Predicate predicate = new Predicate("id", roleIds, LinkEnum.IN);
				userDetailVo.setRoles(roleRepository.findAll(Operator.AND, predicate));
			}
			return userDetailVo;
		}
		return null;
	}
	
	@Transactional
	public void saveUser(UserDetailVo userDetailVo){
		User user = null;
		if(StringUtils.isBlank(userDetailVo.getId())){
			user = new User();
		}else{
			user = this.findOne(userDetailVo.getId());
		}
		if(user != null){
			BeanUtils.copyProperties(userDetailVo, user);
			user = this.update(user);
			final String userId = user.getId();
			List<UserRole> urs = userRoleRepository.findAll("userId=?", userId);
			userRoleRepository.deleteInBatch(urs);
			if(userDetailVo.getRoles() != null && userDetailVo.getRoles().size() > 0){
				urs = userDetailVo.getRoles().stream().map(r->{return new UserRole(userId,r.getId());}).collect(Collectors.toList());
				userRoleRepository.save(urs);
			}
		}
	}
}