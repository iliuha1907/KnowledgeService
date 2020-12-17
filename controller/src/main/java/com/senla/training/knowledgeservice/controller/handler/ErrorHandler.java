package com.senla.training.knowledgeservice.controller.handler;

import com.senla.training.knowledgeservice.common.exception.BusinessException;
import com.senla.training.knowledgeservice.common.exception.IncorrectWorkException;
import com.senla.training.knowledgeservice.dto.dto.message.MessageDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.TransactionException;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.persistence.PersistenceException;

@ControllerAdvice
@EnableWebMvc
public class ErrorHandler extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(ErrorHandler.class);

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<MessageDto> handleCustomException(BusinessException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageDto(ex.getMessage()));
    }

    @ExceptionHandler(IncorrectWorkException.class)
    public ResponseEntity<MessageDto> handleCustomException(IncorrectWorkException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageDto(ex.getMessage()));
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<MessageDto> handlePersistenceException(PersistenceException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageDto("Error at working with database"));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MessageDto> handleIllegalArgumentException(
            IllegalArgumentException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageDto("Error at working with given parameters"));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<MessageDto> handleIllegalStateException(
            IllegalArgumentException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageDto("State of application is incorrect"));
    }

    @ExceptionHandler(TransactionException.class)
    public ResponseEntity<MessageDto> handleTransactionException(TransactionException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageDto("Error at working with database"));
    }

    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<MessageDto> handleUnsupportedOperationException(
            UnsupportedOperationException ex) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageDto(ex.getMessage()));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            @Nonnull HttpRequestMethodNotSupportedException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new MessageDto("Requested method is not supported"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            @Nonnull HttpMediaTypeNotSupportedException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new MessageDto("Media type is not supported"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            @Nonnull HttpMediaTypeNotAcceptableException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(new MessageDto("Media type is not acceptable"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleMissingPathVariable(
            @Nonnull MissingPathVariableException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageDto("Missing path variable"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            @Nonnull MissingServletRequestParameterException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageDto("Missing servlet request parameter"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleServletRequestBindingException(
            @Nonnull ServletRequestBindingException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageDto("Error at servlet request binding"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleConversionNotSupported(
            @Nonnull ConversionNotSupportedException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageDto("Conversion not supported"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleTypeMismatch(@Nonnull TypeMismatchException ex,
                                                        @Nonnull HttpHeaders headers,
                                                        @Nonnull HttpStatus status,
                                                        @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageDto("Type mismatch"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleHttpMessageNotReadable(
            @Nonnull HttpMessageNotReadableException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageDto("Message is not readable"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleHttpMessageNotWritable(
            @Nonnull HttpMessageNotWritableException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageDto("Message is not writeable"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            @Nonnull MethodArgumentNotValidException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageDto("Method argument is not valid"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleMissingServletRequestPart(
            @Nonnull MissingServletRequestPartException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageDto("Missing servlet request parts"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleBindException(@Nonnull BindException ex,
                                                         @Nonnull HttpHeaders headers,
                                                         @Nonnull HttpStatus status,
                                                         @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new MessageDto("Error at binding"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            @Nonnull NoHandlerFoundException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageDto("No handler found"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleAsyncRequestTimeoutException(
            @Nonnull AsyncRequestTimeoutException ex,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest webRequest) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new MessageDto("Async request timeout error"));
    }

    @Override
    @Nonnull
    protected ResponseEntity<Object> handleExceptionInternal(
            @Nonnull Exception ex,
            @Nullable Object body,
            @Nonnull HttpHeaders headers,
            @Nonnull HttpStatus status,
            @Nonnull WebRequest request) {
        LOGGER.error(ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new MessageDto("Internal error"));
    }
}
