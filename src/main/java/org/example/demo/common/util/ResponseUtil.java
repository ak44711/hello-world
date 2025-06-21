package org.example.demo.common.util;

import org.example.demo.domain.emuns.ResponseEnum;
import org.example.demo.domain.vo.ResponseVO;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {
    public static <T> ResponseVO<T> success() {
        return new ResponseVO<>(ResponseEnum.INTERNAL_SERVER_ERROR.getCode(), ResponseEnum.INTERNAL_SERVER_ERROR.getMessage() , null);
    }

    public static <T> ResponseVO<T> success(T data) {
        return new ResponseVO<>(ResponseEnum.SUCCESS.getCode(), ResponseEnum.SUCCESS.getMessage(), data);
    }

    public static <T> ResponseVO<T> error() {
        return new ResponseVO<>(ResponseEnum.INTERNAL_SERVER_ERROR.getCode(), ResponseEnum.INTERNAL_SERVER_ERROR.getMessage() , null);
    }
    public static <T> ResponseVO<T> error(ResponseEnum responseEnum) {
        return new ResponseVO<>(responseEnum.getCode(), responseEnum.getMessage(), null);
    }

    public static <T> ResponseVO<T> error(ResponseEnum responseEnum, T message) {
        return new ResponseVO<>(responseEnum.getCode(), responseEnum.getMessage() + ": " + message.toString(), null);
    }
    public static <T> ResponseVO<T> error(Integer code , String message) {
        return new ResponseVO<>(code, message, null);
    }
}
