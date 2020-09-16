package com.dx.common.core.config;

import com.dx.common.core.exception.DxException;
import com.dx.common.core.exception.UnauthorizedException;
import com.dx.common.core.r.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionConfig {

    @ExceptionHandler(value = Exception.class)
    public R<?> handleException(Exception ex, HttpServletRequest request) {
        log.error("---系统未知异常：{}", request.getRequestURI(), ex);
        return R.fail(500, "系统未知异常");
    }

    @ExceptionHandler(value = MaxUploadSizeExceededException.class)
    public R<?> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, HttpServletRequest request) {
        log.error("---文件过大异常：{}", request.getRequestURI());
        return R.fail(499, "文件超出限制大小");
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public R<?> handleMissingServletRequestParameterException(MissingServletRequestParameterException ex, HttpServletRequest request) {
        log.error("---接口参数{}不匹配：{}", ex.getParameterName(), request.getRequestURI());
        return R.fail(400, "参数:" + ex.getParameterName() + "不能为空");
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    public R<?> handleUnauthorizedException(UnauthorizedException ex, HttpServletRequest request) {
        log.error("---没有权限，token非法或者不存在：{}", request.getRequestURI());
        return R.fail(401, "没有权限");
    }

    @ExceptionHandler(value = UnsupportedOperationException.class)
    public R<?> handleUnsupportedOperationException(HttpServletRequest request) {
        log.error("---不支持的操作：{}", request.getRequestURI());
        return R.fail(400, "不支持的操作");
    }

    @ExceptionHandler(value = DxException.class)
    public R<?> handleDxException(DxException ex, HttpServletRequest request) {
        log.error("---业务异常{}：{}", request.getRequestURI(), ex.getR().getError());
        return ex.getR();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public R<?> validationBodyException(MethodArgumentNotValidException exception,
                                        HttpServletRequest request) {
        BindingResult result = exception.getBindingResult();
        StringBuilder sb = new StringBuilder();

        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p -> sb.append(p.getDefaultMessage()));
        }
        log.error("Valid Err：{} -->{}", request.getRequestURI(), sb.toString());
        return R.fail(400, sb.toString());
    }
}
