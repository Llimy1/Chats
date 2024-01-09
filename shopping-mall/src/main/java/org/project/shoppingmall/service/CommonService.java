package org.project.shoppingmall.service;

import org.project.shoppingmall.dto.common.CommonResponseDto;
import org.project.shoppingmall.type.ResponseStatus;
import org.springframework.stereotype.Service;

@Service
public class CommonService {

    public CommonResponseDto<Object> successResponse(String message, Object data) {
        return CommonResponseDto.builder()
                .status(ResponseStatus.SUCCESS.getDescription())
                .message(message)
                .data(data)
                .build();
    }

    public CommonResponseDto<Object> errorResponse(String message) {
        return CommonResponseDto.builder()
                .status(ResponseStatus.FAIL.getDescription())
                .message(message)
                .data(null)
                .build();
    }
}
