package org.example.demo.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.example.demo.common.util.ResponseUtil;
import org.example.demo.domain.emuns.ResponseEnum;
import org.example.demo.domain.vo.ResponseVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private final ObjectMapper objectMapper = new ObjectMapper(); // Jackson 用于 JSON 处理


    @ExceptionHandler(CustomException.class)
    @ResponseBody
    public ResponseVO<String> handleCustomException(CustomException e) {

        return ResponseUtil.error(e.getResponseEnum(), e.getMessage());

    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    public ResponseVO<List<String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        BindingResult result = ex.getBindingResult();
        List<String> errors = result.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseUtil.error(ResponseEnum.BAD_REQUEST, errors);
    }


    // 处理 JSON 解析异常
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseBody
    public ResponseVO<String> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return ResponseUtil.error(ResponseEnum.BAD_REQUEST, "JSON 格式错误：" + e.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseBody
    public ResponseVO<String> handleExpiredJwtException(ExpiredJwtException e, HttpServletResponse response) {
        response.setStatus(401);
        return ResponseUtil.error(ResponseEnum.BAD_REQUEST, "JWT过期，请重新登录：" + e.getMessage());
    }


    // 处理 BadCredentialsException 异常
    @ExceptionHandler(BadCredentialsException.class)
    public void handleBadCredentialsException(HttpServletResponse response) throws IOException {
        sendJsonResponse(response, HttpStatus.UNAUTHORIZED.value(), new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(), "无效的凭证", null));
    }

    // 处理 AccessDeniedException 异常
    @ExceptionHandler(AccessDeniedException.class)
    public void handleAccessDeniedException(HttpServletResponse response) throws IOException {
        sendJsonResponse(response, HttpStatus.FORBIDDEN.value(), new ResponseVO<>(HttpStatus.FORBIDDEN.value(), "没有访问权限", null));
    }

    // 处理认证失败的其他异常
    @ExceptionHandler(AuthenticationException.class)
    public void handleAuthenticationException(HttpServletResponse response) throws IOException {
        sendJsonResponse(response, HttpStatus.UNAUTHORIZED.value(), new ResponseVO<>(HttpStatus.UNAUTHORIZED.value(), "认证失败", null));
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseVO<String> handleException(Exception e, HttpServletResponse response) {
        // 输出错误
        log.error(e.getMessage());
        response.setStatus(500);
        return ResponseUtil.error(ResponseEnum.INTERNAL_SERVER_ERROR, "服务器错误：" + e.getMessage());
    }

    private void sendJsonResponse(HttpServletResponse response, int status, ResponseVO<?> responseVO) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(responseVO));
    }

}