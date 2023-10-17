package com.example.projecthope.handler;


import com.example.projecthope.dto.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wanggang
 * <p>
 * create by WG on 2019/7/9 21:57
 */

@ControllerAdvice
@Slf4j
public class AppWideExceptionHandler {


	/**
	 * 处理所有异常，并记录日志
	 *
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Throwable.class)
	@ResponseBody
	public Result<?> throwableHandler(Throwable e, HttpServletRequest request) {
		log.error(request.getRequestURI() + "AppWideExceptionHandler[exceptionHandler] url:{} ", request.getRequestURI(), e);
		return Result.failureData(e);
	}
}
