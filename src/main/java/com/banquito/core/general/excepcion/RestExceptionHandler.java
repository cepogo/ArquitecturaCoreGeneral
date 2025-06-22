package com.banquito.core.general.excepcion;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Collections;
import java.util.Map;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CrearEntidadException.class)
    protected ResponseEntity<Object> handleCreateEntityException(CrearEntidadException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(ActualizarEntidadException.class)
    protected ResponseEntity<Object> handleUpdateEntityException(ActualizarEntidadException ex) {
        return buildResponseEntity(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler(EntidadNoEncontradaException.class)
    protected ResponseEntity<Object> handleEntityNotFoundException(EntidadNoEncontradaException ex) {
        return buildResponseEntity(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    private ResponseEntity<Object> buildResponseEntity(HttpStatus status, String message) {
        Map<String, Object> body = Collections.singletonMap("error", message);
        return new ResponseEntity<>(body, status);
    }
}
