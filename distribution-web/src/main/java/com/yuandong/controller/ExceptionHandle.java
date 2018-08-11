package com.yuandong.controller;

import javax.validation.ConstraintViolationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import com.yuandong.common.exception.SystemException;
import com.yuandong.util.ResultGenerator;
import com.yuandong.vo.RestResult;

@ControllerAdvice
public class ExceptionHandle {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private ResultGenerator generator;

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public RestResult Handle(Exception e) {
		// 将系统异常以打印出来
		logger.info("[系统异常]{}", e);
		if (e instanceof ConstraintViolationException) {
			ConstraintViolationException validException = (ConstraintViolationException) e;
			String errorMessage = validException.getConstraintViolations()
					.iterator().next().getMessage();
			return generator.getFailResult(errorMessage);
		} else if (e instanceof DataIntegrityViolationException) {
			return generator.getFailResult("违反主键/唯一约束条件");
		} else if (e instanceof SystemException) {
			SystemException sysException = (SystemException) e;
			return generator.getFailResult(sysException.getMessage());
		} else if (e instanceof AccessDeniedException) {
			AccessDeniedException accessException = (AccessDeniedException) e;
			return generator.getFailResult("没有访问权限！");
		} else {
			return generator.getFailResult("系统错误：" + e.getMessage());
		}

	}
}
