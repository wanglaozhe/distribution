package com.yuandong.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuandong.entity.Role;
import com.yuandong.entity.User;
import com.yuandong.service.RoleService;
import com.yuandong.service.UserService;
import com.yuandong.util.ResultGenerator;
import com.yuandong.vo.RestResult;
import com.yuandong.vo.UserDetailVo;

@Controller
@RequestMapping("/user")
@Validated
public class UserController {
    
	private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private ResultGenerator generator;

    @Value("${securityconfig.urlroles}")
    private String urlroles;

    @RequestMapping("/index")
    public String index(ModelMap model, Principal user) throws Exception{
        Authentication authentication = (Authentication)user;
        List<String> userroles = new ArrayList<>();
        for(GrantedAuthority ga : authentication.getAuthorities()){
            userroles.add(ga.getAuthority());
        }

        boolean newrole=false,editrole=false,deleterole=false;
        if(!StringUtils.isEmpty(urlroles)) {
            String[] resouces = urlroles.split(";");
            for (String resource : resouces) {
                String[] urls = resource.split("=");
                if(urls[0].indexOf("new") > 0){
                    String[] newroles = urls[1].split(",");
                    for(String str : newroles){
                        str = str.trim();
                        if(userroles.contains(str)){
                            newrole = true;
                            break;
                        }
                    }
                }else if(urls[0].indexOf("edit") > 0){
                    String[] editoles = urls[1].split(",");
                    for(String str : editoles){
                        str = str.trim();
                        if(userroles.contains(str)){
                            editrole = true;
                            break;
                        }
                    }
                }else if(urls[0].indexOf("delete") > 0){
                    String[] deleteroles = urls[1].split(",");
                    for(String str : deleteroles){
                        str = str.trim();
                        if(userroles.contains(str)){
                            deleterole = true;
                            break;
                        }
                    }
                }
            }
        }

        model.addAttribute("newrole", newrole);
        model.addAttribute("editrole", editrole);
        model.addAttribute("deleterole", deleterole);

        return "user/index";
    }

    @RequestMapping(value="/{id}")
    public String show(ModelMap model,@PathVariable String id) {
        UserDetailVo userDetailVo = userService.findUserDetailVoById(id);
        model.addAttribute("user",userDetailVo);
        return "user/show";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public RestResult getList(int page,int size,@NotNull(message="userName不能为空") String useerName) {
        page = page < 1? 0:page -1;
//      Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.ASC, "id"));
        Pageable pageable = new PageRequest(page, size);
        return generator.getSuccessResult(userService.findAll(pageable));
      
    }

    @RequestMapping("/new")
    public String create(ModelMap model,User user){
        List<Role> roles = roleService.findAll();

        model.addAttribute("departments",null);
        model.addAttribute("roles", roles);
        model.addAttribute("user", user);
        return "user/new";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    @ResponseBody
    public RestResult save(@Valid UserDetailVo userDetailVo) throws Exception{
        BCryptPasswordEncoder bpe = new BCryptPasswordEncoder();
        userDetailVo.setPassword(bpe.encode(userDetailVo.getPassword()));
        userService.saveUser(userDetailVo);
        logger.info("新增->ID="+userDetailVo.getId());
        return generator.getSuccessResult();
    }

    @RequestMapping(value="/edit/{id}")
    public String update(ModelMap model,@PathVariable String id){
        UserDetailVo userDetailVo = userService.findUserDetailVoById(id);
        if(userDetailVo.getRoles() != null && userDetailVo.getRoles().size() > 0){
	       model.put("rids", userDetailVo.getRoles().stream().map(Role::getId).collect(Collectors.toList()));
        }else{
        	model.put("rids","");
        }
        List<Role> roles = roleService.findAll();
        model.addAttribute("user",userDetailVo);
        model.addAttribute("roles", roles);
        return "user/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value="/update")
    @ResponseBody
    public RestResult update(@Valid UserDetailVo user) throws Exception{
        userService.saveUser(user);
        logger.info("修改->ID="+user.getId());
        return generator.getSuccessResult();
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    @ResponseBody
    public RestResult delete(@PathVariable String id) throws Exception{
        userService.delete(id);
        logger.info("删除->ID="+id);
        return generator.getSuccessResult();
    }
}
