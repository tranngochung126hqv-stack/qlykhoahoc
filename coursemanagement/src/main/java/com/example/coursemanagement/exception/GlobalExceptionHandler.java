package com.example.coursemanagement.exception;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
@ControllerAdvice
public class GlobalExceptionHandler {
 @ExceptionHandler(IllegalArgumentException.class)
 @ResponseStatus(HttpStatus.NOT_FOUND)
 public String notFound(IllegalArgumentException ex,Model model){model.addAttribute("message",ex.getMessage());return "error";}
 @ExceptionHandler(Exception.class)
 @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
 public String general(Exception ex,Model model){model.addAttribute("message","Đã xảy ra lỗi khi xử lý yêu cầu. Vui lòng kiểm tra dữ liệu và thử lại.");return "error";}
}
