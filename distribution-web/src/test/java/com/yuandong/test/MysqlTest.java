package com.yuandong.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.util.Assert;

import com.yuandong.WebApplication;
import com.yuandong.common.support.jpa.parameter.LinkEnum;
import com.yuandong.common.support.jpa.parameter.Operator;
import com.yuandong.common.support.jpa.parameter.Predicate;
import com.yuandong.entity.Role;
import com.yuandong.entity.User;
import com.yuandong.entity.UserRole;
import com.yuandong.service.RoleService;
import com.yuandong.service.UserRoleService;
import com.yuandong.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
//指定我们SpringBoot工程的Application启动类
@SpringApplicationConfiguration(classes = WebApplication.class)
@PropertySource(value={"classpath:application-test.properties"})
//由于是Web项目，Junit需要模拟ServletContext，因此我们需要给我们的测试类加上@WebAppConfiguration
@WebAppConfiguration
public class MysqlTest {
    @Autowired
    UserService userService;
    @Autowired
    RoleService roleService;
    @Autowired
    UserRoleService userRoleService;

    @Before
    public void initData(){
       
        try {
        	 userService.deleteAll();//*6+kfdjOeJBd
             roleService.deleteAll();
             userRoleService.deleteAll();

             Role role = new Role();
             role.setName("admin");
             roleService.save(role);
             Assert.notNull(role.getId());

             User user = new User();
             user.setUserName("user");
             user.setEmail("704233326@qq.com");
             BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
             user.setPassword(bpe.encode("user"));
             userService.save(user);
             Assert.notNull(user.getId());
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
    
    @Autowired
    private CacheManager cacheManager;
    @Test
    public void testEchache() throws Exception{
    	// 获取ehcache配置文件中的一个cache
    	Cache sample = cacheManager.getCache("sample");
    	// 获取页面缓存
//    	BlockingCache cache = new BlockingCache(cacheManager.getEhcache("SimplePageCachingFilter"));
    	// 添加数据到缓存中
    	sample.put("key", "val");
    	// 获取缓存中的对象，注意添加到cache中对象要序列化 实现Serializable接口
    	String result = sample.get("key",String.class);
    	int tmp = 0;
    	while(tmp < 100){
    		result = sample.get("key",String.class);
			System.out.println(result);
			tmp++;
		}
    	// 删除缓存
    	sample.evict("key");
    	sample.clear();
    	 
    	// 获取缓存管理器中的缓存配置名称
    	for (String cacheName : cacheManager.getCacheNames()) {
    	    System.out.println(cacheName);
    	}
    }

    @Test
    public void insertUserRoles(){
    	try {
    		User user = userService.findOne("userName=?", "user");

            List<Role> roles = roleService.findAll();
            Assert.notNull(roles);
            
            for(Role role : roles){
            	 UserRole userRole = new UserRole();
                 userRole.setUserId(user.getId());
                 userRole.setRoleId(role.getId());
                 userRoleService.save(userRole);
            }
           
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
    
    @Test
    public void testQuery(){
    	try {
    		Predicate p = new Predicate("userName", "user", LinkEnum.EQ);
    		List<User> users = userService.findAll(Operator.AND, p);
    		users.stream().map(u->u.getUserName()).forEach(System.out::println);
		} catch (Exception e) {
			e.printStackTrace();
		}
        
    }
}
