package com.ddf.boot.capableadmin.infra.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.SaTokenContextException;
import com.ddf.boot.common.api.exception.BaseErrorCallbackCode;
import com.ddf.boot.common.api.model.common.response.ResponseData;
import com.ddf.boot.common.mvc.exception200.AbstractExceptionHandler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * <p>全局异常处理</p >
 *
 * @author rebot
 * @version 1.0
 * @since 2023/07/04 17:24
 */
@Component
@RestControllerAdvice
public class PrettyAdminExceptionHandler extends AbstractExceptionHandler {

	/**
	 * 处理未登录异常
	 */
	@ExceptionHandler(value = {NotLoginException.class})
	public ResponseData<?> handlerNotLoginException(Exception e) {
		return ResponseData.failure(BaseErrorCallbackCode.UNAUTHORIZED);
	}

	/**
	 * 处理无权限异常
	 */
	@ExceptionHandler(NotPermissionException.class)
	public ResponseData<?> handlerNotPermissionException(NotPermissionException e) {
		return ResponseData.failure(BaseErrorCallbackCode.ACCESS_FORBIDDEN);
	}


	@Override
	public ResponseData<?> handlerException(Exception exception, HttpServletRequest httpServletRequest,
			HttpServletResponse response) {
		return super.handlerException(exception, httpServletRequest, response);
	}
}
