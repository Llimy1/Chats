package org.project.chats.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.project.chats.dto.common.CommonResponseDto;
import org.project.chats.exception.Duplication;
import org.project.chats.exception.JwtException;
import org.project.chats.exception.LoginException;
import org.project.chats.exception.NotFoundException;
import org.project.chats.service.CommonService;
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
        log.error("handleDuplicationException = {}", ndi.getMessage());
        CommonResponseDto<Object> commonResponseDto = commonService.errorResponse(ndi.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(commonResponseDto);
    }

    // NotFound Exception
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> notFoundException(NotFoundException nfe) {
        log.error("handleNotFoundException = {}", nfe.getMessage());
        CommonResponseDto<Object> commonResponseDto = commonService.errorResponse(nfe.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(commonResponseDto);
    }

    // IllegalAccessError
    @ExceptionHandler(IllegalAccessError.class)
    public ResponseEntity<Object> illegalAccessException(IllegalAccessError iae) {
        log.error("handleIllegalAccessError = {}", iae.getMessage());
        CommonResponseDto<Object> commonResponseDto = commonService.errorResponse(iae.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(commonResponseDto);
    }

    // JwtException
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Object> illegalAccessException(JwtException jep) {
        log.error("handleIllegalAccessError = {}", jep.getMessage());
        CommonResponseDto<Object> commonResponseDto = commonService.errorResponse(jep.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(commonResponseDto);
    }

    // LoginException
    @ExceptionHandler(LoginException.class)
    public ResponseEntity<Object> loginException(LoginException lep) {
        log.error("handleLoginException = {}", lep.getMessage());
        CommonResponseDto<Object> commonResponseDto = commonService.errorResponse(lep.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(commonResponseDto);
    }
    
    // Exception
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> exception(Exception e) {
        log.error("handleAllException", e);
        CommonResponseDto<Object> commonResponseDto = commonService.errorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(commonResponseDto);
    }
}
