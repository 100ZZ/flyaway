package com.lihuia.flyaway.response;

/**
 * @author lihuia.com
 * @date 2022/6/20 10:27 PM
 */

public class ResponseUtil {


    /**
     * 返回成功状态，包含数据
     * @param data
     * @param <T>
     * @return
     */
    public static <T> Response<T> buildSuccessResponse(T data){
        return buildResponse(data, ResponseCodeEnum.SUCCESS);
    }

    /**
     * 返回成功状态，不包含数据
     * @return
     */
    public static ResponseStatus success(){
        return buildResponseStatus(ResponseCodeEnum.SUCCESS);
    }

    /**
     * 返回失败状态
     * @param responseCodeEnum
     * @return
     */
    public static ResponseStatus buildFailResponse(ResponseCodeEnum responseCodeEnum){
        ResponseCodeEnum.FAIL.setMessage(responseCodeEnum.getMessage());
        return buildResponseStatus(ResponseCodeEnum.FAIL);
    }

    /**
     * 返回Response，包含数据
     * @param data
     * @param responseCodeEnum
     * @return
     */
    public static Response buildResponse(Object data, ResponseCodeEnum responseCodeEnum){
        return Response.builder()
                .code(responseCodeEnum.getCode())
                .message(responseCodeEnum.getMessage())
                .success(responseCodeEnum.getSuccess())
                .currentTime(System.currentTimeMillis())
                .data(data)
                .build();
    }

    /**
     * 返回success Response，包含数据
     * @param data
     * @return
     */
    public static Response genSuccessResponse(Object data){
        ResponseCodeEnum resp = ResponseCodeEnum.SUCCESS;
        return Response.builder()
                .code(resp.getCode())
                .message(resp.getMessage())
                .success(resp.getSuccess())
                .currentTime(System.currentTimeMillis())
                .data(data)
                .build();
    }

    /**
     * 返回状态ResponseStatus
     * @param responseCodeEnum
     * @return
     */
    public static ResponseStatus buildResponseStatus(ResponseCodeEnum responseCodeEnum){
        return ResponseStatus.builder()
                .code(responseCodeEnum.getCode())
                .message(responseCodeEnum.getMessage())
                .success(responseCodeEnum.getSuccess())
                .currentTime(System.currentTimeMillis())
                .build();
    }
}
