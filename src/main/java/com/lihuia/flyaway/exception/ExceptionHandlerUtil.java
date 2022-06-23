package com.lihuia.flyaway.exception;

import com.lihuia.flyaway.response.ResponseCodeEnum;
import com.lihuia.flyaway.response.ResponseStatus;
import com.lihuia.flyaway.response.ResponseUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author lihuia.com
 * @date 2022/6/20 8:36 PM
 */

@Slf4j
@RestControllerAdvice
public class ExceptionHandlerUtil {

    /**
     * 格式化异常
     *
     * @param ex
     * @param logInfo
     * @return
     */
    private ResponseStatus formatException(Exception ex, String logInfo) {
        log.warn(logInfo);
        //log.warn(StringUtil.getStackTraceAsString(ex));
        ResponseCodeEnum.SYSTEM_ERROR.setMessage(logInfo);
        return ResponseUtil.buildResponseStatus(ResponseCodeEnum.SYSTEM_ERROR);
    }

    /**
     * 格式化异常信息
     *
     * @param ex
     * @param logInfo
     * @param responseCodeEnum
     * @return
     */
    private ResponseStatus formatException(Exception ex, String logInfo, ResponseCodeEnum responseCodeEnum) {
        log.warn(logInfo);
        //log.warn(StringUtil.getStackTraceAsString(ex));
        responseCodeEnum.setMessage(logInfo);
        return ResponseUtil.buildResponseStatus(responseCodeEnum);
    }

    @ExceptionHandler(value = FlyawayException.class)
    public Object jsonException(FlyawayException ex) {
        String logInfo = String.format("接口测试平台异常, 异常信息: %s", ex.getMessage());
        if (ex.getResponseCodeEnum() != null && ex.getResponseCodeEnum() != ResponseCodeEnum.SYSTEM_ERROR) {
            return formatException(ex, ex.getMessage(), ResponseCodeEnum.FAIL);

        } else {
            return formatException(ex, logInfo);
        }
    }
}
