package org.project.chats.dto.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL) // DTO 를 JSON으로 변환 시 null값인 field 제외
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
