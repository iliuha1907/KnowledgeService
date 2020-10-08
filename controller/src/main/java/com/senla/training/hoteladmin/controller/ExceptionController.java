package com.senla.training.hoteladmin.controller;

import com.senla.training.hoteladmin.dto.ErrorMessageDto;
import com.senla.training.hoteladmin.exception.BusinessException;
import com.senla.training.hoteladmin.exception.IncorrectWorkException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionController {

    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ErrorMessageDto handleCustomException(BusinessException ex) {
        return new ErrorMessageDto(ex.getClass().getSimpleName(), ex.getMessage());
    }

    @ExceptionHandler(IncorrectWorkException.class)
    @ResponseBody
    public ErrorMessageDto handleCustomException(IncorrectWorkException ex) {
        return new ErrorMessageDto(ex.getClass().getSimpleName(), ex.getMessage());
    }
}
