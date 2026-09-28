package com.example.servicio_login.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
* Esta clase permite controlar los errores de validación
* y mostrar mensajes más claros cuando los datos enviados
* por el usuario no cumplen las condiciones establecidas.
*/
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    /*
    * Captura los errores producidos cuando un dato enviado
    * no cumple las validaciones de RegistroRequest o LoginRequest.
    */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> manejarErrorValidacion(
            MethodArgumentNotValidException exception){
        
        /*
        * Obtiene el primer mensaje de error encontrado
        * para mostrarlo de forma sencilla.
        */
        String mensaje = exception.getBindingResult()
                .getFieldErrors()
                .get(0)
                .getDefaultMessage();
        
        return ResponseEntity.badRequest().body(mensaje);
    }
}