package org.project.shoppingmall.dto.common;

import lombok.Builder;
import lombok.Getter;

@Getter
public class CommonResponseDto<Data> {

    private final String status;
    private final String message;
    private final Data data;

    @Builder
    public CommonResponseDto(String status, String message, Data data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }
}
