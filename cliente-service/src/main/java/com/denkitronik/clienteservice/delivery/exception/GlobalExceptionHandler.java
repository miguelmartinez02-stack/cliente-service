package com.denkitronik.clienteservice.delivery.exception;

import com.denkitronik.clienteservice.domain.exception.ClienteNotFoundException;
import com.denkitronik.clienteservice.domain.exception.ClienteServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // 404 Not Found
    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> manejarNotFound(ClienteNotFoundException ex) {
        log.warn("Cliente no encontrado: id={}", ex.getId());
        ApiErrorResponse respuesta = new ApiErrorResponse(
                HttpStatus.NOT_FOUND.value(), "Not Found", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(respuesta);
    }

    // 500 Internal Server Error (error controlado del servicio)
    @ExceptionHandler(ClienteServiceException.class)
    public ResponseEntity<ApiErrorResponse> manejarServiceException(ClienteServiceException ex) {
        log.error("Error en el servicio de clientes", ex);
        ApiErrorResponse respuesta = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                "Error interno del servidor. Por favor intenta más tarde.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }

    // 400 Bad Request (validaciones @Valid sin BindingResult)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> manejarValidacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String campo = (error instanceof FieldError fe) ? fe.getField() : error.getObjectName();
            errores.put(campo, error.getDefaultMessage());
        });
        log.debug("Errores de validación: {}", errores);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errores);
    }

    // 400 Bad Request (JSON mal formado)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiErrorResponse> manejarJsonInvalido(HttpMessageNotReadableException ex) {
        log.debug("JSON inválido en la petición: {}", ex.getMessage());
        ApiErrorResponse respuesta = new ApiErrorResponse(
                HttpStatus.BAD_REQUEST.value(), "Bad Request",
                "El cuerpo de la petición no es un JSON válido.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
    }

    // 500 genérico: red de seguridad
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> manejarExcepcionGenerica(Exception ex) {
        log.error("Excepción no manejada", ex);
        ApiErrorResponse respuesta = new ApiErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error",
                "Error inesperado. Por favor contacta al administrador.");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(respuesta);
    }
}
