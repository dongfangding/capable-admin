package com.ddf.boot.capableadmin.config;

import com.ddf.boot.common.api.model.common.response.ResponseData;
import com.ddf.boot.common.mvc.exception200.AbstractExceptionHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * <p>全局异常处理</p >
 *
 * @author rebot
 * @version 1.0
 * @since 2023/07/04 17:24
 */
@Component
@RestControllerAdvice(basePackages = "com.ddf.boot.capableadmin.controller")
public class PrettyAdminExceptionHandler extends AbstractExceptionHandler {

    @Override
    public ResponseData<?> handlerException(Exception exception, HttpServletRequest httpServletRequest,
            HttpServletResponse response) {
        return super.handlerException(exception, httpServletRequest, response);
    }
}
