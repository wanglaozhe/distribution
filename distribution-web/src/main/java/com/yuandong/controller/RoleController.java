package com.yuandong.controller;

import java.security.Principal;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuandong.entity.Role;
import com.yuandong.service.RoleService;
import com.yuandong.util.ResultGenerator;
import com.yuandong.vo.RestResult;

@Controller
@RequestMapping("/role")
@Validated
public class RoleController {
   
	private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private RoleService roleService;
    @Autowired
    private ResultGenerator generator;

    @RequestMapping("/index")
    public String index(ModelMap model, Principal user) throws Exception{
        return "role/index";
    }

    @RequestMapping(value="/{id}")
    public String show(ModelMap model,@PathVariable String id) {
        Role role = roleService.findOne(id);
        model.addAttribute("role",role);
        return "role/show";
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public RestResult getList(int page,int size) {
    	page = page < 1? 0:page -1;
        Pageable pageable = new PageRequest(page, size);
        return generator.getSuccessResult(roleService.findAll(pageable));
    }

    @RequestMapping("/new")
    public String create(){
        return "role/new";
    }

    @RequestMapping(value="/save", method = RequestMethod.POST)
    @ResponseBody
    public RestResult save(@Valid Role role) throws Exception{
        roleService.save(role);
        logger.info("新增->ID="+role.getId());
        return generator.getSuccessResult();
    }

    @RequestMapping(value="/edit/{id}")
    public String update(ModelMap model,@PathVariable String id){
        Role role = roleService.findOne(id);
        model.addAttribute("role",role);
        return "role/edit";
    }

    @RequestMapping(method = RequestMethod.POST, value="/update")
    @ResponseBody
    public RestResult update(@Valid Role role) throws Exception{
    	Role r = roleService.findOne(role.getId());
    	BeanUtils.copyProperties(role, r);
        roleService.update(r);
        logger.info("修改->ID="+role.getId());
        return generator.getSuccessResult();
    }

    @RequestMapping(value="/delete/{id}",method = RequestMethod.GET)
    @ResponseBody
    public RestResult delete(@PathVariable String id) throws Exception{
        roleService.delete(id);
        logger.info("删除->ID="+id);
        return generator.getSuccessResult();
    }
}
