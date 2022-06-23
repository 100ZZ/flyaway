package com.lihuia.flyaway.response;

import lombok.Builder;
import lombok.Data;

/**
 * @author lihuia.com
 * @date 2022/6/20 8:37 PM
 */

@Data
@Builder
public class ResponseStatus {

    private Integer code;
    private String message;
    private Boolean success;
    private Long currentTime;
}
