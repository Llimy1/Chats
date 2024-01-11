package org.project.shoppingmall.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.shoppingmall.dto.common.CommonResponseDto;
import org.project.shoppingmall.exception.Duplication;
import org.project.shoppingmall.service.CommonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class ExceptionAdviceController {

    private final CommonService commonService;

    // Duplication Exception
    @ExceptionHandler(Duplication.class)
    public ResponseEntity<Object> duplicationException(Duplication ndi) {
        log.warn("handleDuplicationException", ndi);
        CommonResponseDto<Object> commonResponseDto = commonService.errorResponse(ndi.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(commonResponseDto);
    }
    
    // Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception e) {
        log.warn("handleAllException", e);
        CommonResponseDto<Object> commonResponseDto = commonService.errorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonResponseDto);
    }
}
