package com.yenicilh.chatapp.common.exception;

import com.yenicilh.chatapp.common.exception.chat.ChatException;
import com.yenicilh.chatapp.common.exception.entity.ErrorDetail;
import com.yenicilh.chatapp.common.exception.message.MessageException;
import com.yenicilh.chatapp.common.exception.user.UserException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@RestControllerAdvice
public class GlobalException {

    @ExceptionHandler(UserException.class)
    public ResponseEntity<ErrorDetail> userExceptionHandler(UserException exception, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(exception.getMessage(), request.getDescription(false),
                LocalDateTime.now());
        return new ResponseEntity<ErrorDetail>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MessageException.class)
    public ResponseEntity<ErrorDetail> messageExceptionHandler(Exception exception, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(
                exception.getMessage(), request.getDescription(false), LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetail> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception,
                                                                              WebRequest request) {
        String error = exception.getBindingResult().getFieldError().getDefaultMessage();
        ErrorDetail errorDetail = new ErrorDetail("Validation Error",error, LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ErrorDetail> noHandlerFoundExceptionHandler(NoHandlerFoundException exception,
                                                                      WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail("Endpoint not found", request.getDescription(false),
                LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> exceptionHandler(Exception exception, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(exception.getMessage(), request.getDescription(false),
                LocalDateTime.now());
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ChatException.class)
    public ResponseEntity<ErrorDetail> chatExceptionHandler(Exception exception, WebRequest request) {
        ErrorDetail errorDetail = new ErrorDetail(
                exception.getMessage(), request.getDescription(false), LocalDateTime.now()
        );
        return new ResponseEntity<>(errorDetail, HttpStatus.BAD_REQUEST);
    }

}
