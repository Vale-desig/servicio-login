package com.example.servicio_login.controller;

import com.example.servicio_login.dto.RestablecerPasswordRequest;
import com.example.servicio_login.dto.RecuperacionRequest;
import com.example.servicio_login.dto.LoginRequest;
import com.example.servicio_login.dto.EliminarCuentaRequest;
import com.example.servicio_login.dto.RegistroRequest;
import com.example.servicio_login.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CrossOrigin;

/*
* Este controlador recibe las solicitudes del servicio web
* relacionadas con el registro y el inicio de sesión.
*/
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {
    
    private final AuthService authService;
    
    /*
    * Constructor utilizado por Spring para conectar
    * el controlador con el servicio de autenticación.
    */
    public AuthController(AuthService authService){
        this.authService = authService;
    }
    
    /*
    * Endpoint para registrar un nuevo usuario.
    */
    @PostMapping("/registro")
    public ResponseEntity<String> registrar(
            @Valid @RequestBody RegistroRequest request){
        
        String resultado = authService.registrarUsuario(
                request.getNombre(), 
                request.getApellido(), 
                request.getCorreo(), 
                request.getContrasena(), 
                request.getConfirmarContrasena()
        );
        
        /*
        * Si el correo ya está registrado, se informa
        * mediante el código HTTP 409 (Conflict).
        */
        if (resultado.equals("El correo electrónico ya está registrado.")){
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(resultado);
        }
        
        /*
        * Si las contraseñas no coinciden, se informa
        * mediante el código HTTP 400 (Bad Request).
        */
        if (resultado.equals("Las contraseñas no coinciden.")){
            return ResponseEntity
                    .badRequest()
                    .body(resultado);
        }
        
        /*
        * Si ttodo fue correcto, se devuelve 201 (Created).
        */
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(resultado);
    }
    
    /*
    * endpoint para iniciar sesión.
    */
    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request){
        
        String resultado = authService.iniciarSesion(
                request.getNombre(), 
                request.getApellido(), 
                request.getContrasena()
        );
        
        /*
        * Si las credenciales son incorrectas,
        * se devuelve ell código HTTP 401.
        */
        if (resultado.equals("Error de autenticación.")){
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(resultado);
        }
        
        /*
        * Si las credenciales son correctas,
        * se devuelve 200 (OK).
        */
        return ResponseEntity.ok(
                java.util.Map.of(
                        "mensaje", "Autenticación satisfactoria.",
                        "correo", resultado
                )
        );
    }
    
    /*
    * Endpoint para solicitar un código de recuperación
    * mediante el correo electrónico.
    */
    @PostMapping("/recuperacion")
    public ResponseEntity<String> solicitarRecuperacion(
            @Valid @RequestBody RecuperacionRequest request){
        
        String resultado = authService.solicitarRecuperacion(
                request.getCorreo()
        );
        
        /*
        * Si el correo no está registrado, se devuelve
        * un error de recurso no encontrado.
        */
        if (resultado.equals(
                "No existe una cuenta asociada a ese correo.")){
            
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(resultado);
        }
        
        /*
        * Por ahora el código se devuelve en la respuesta
        * solamennte para poder probar la funcionalidad.
        */
        return ResponseEntity.ok(resultado);
    }
    
    /*
    * Endpoint para restablecer la contraseña utilizando
    * el código temporal de recuperación.
    */
    @PostMapping("/restablecer")
    public ResponseEntity<String> restablecerContrasena(
            @Valid @RequestBody RestablecerPasswordRequest request){
        
        String resultado = authService.restablecerContrasena(
                request.getCorreo(), 
                request.getCodigo(), 
                request.getNuevaContrasena(), 
                request.getConfirmarContrasena()
        );
        
        /*
        * Si la cuenta no existe, se devuelve 404.
        */
        if (resultado.equals(
                "No existe una cuenta asociada a ese correo.")){
            
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(resultado);
        }
        
        /*
        * Los errores relacionados con el código de recuperación
        * se consideran solicitudes incorrectas.
        */
        if (resultado.equals(
                "No existe un código de recuperación activo.")
                || resultado.equals(
                "El código dee recuperación es incorrecto.")
                || resultado.equals(
                "El código de recuperación ha expirado.")
                || resultado.equals(
                "Las contraseñas no coinciden.")){
            
            return ResponseEntity
                    .badRequest()
                    .body(resultado);
        }
        
        // Si todo es correcto, la contraseña fue actualizada.
        return ResponseEntity.ok(resultado);
    }
    
    /*
    * Endpoint para eliminar una cuenta.
    * Se requiere el correo y la contraseña para confirmar
    * que la solicitud corresponde al propietario de la cuenta.
    */
    @PostMapping("/eliminar-cuenta")
    public ResponseEntity<String> eliminarCuenta(
            @Valid @RequestBody EliminarCuentaRequest request){
        
        String resultado = authService.eliminarCuenta(
                request.getCorreo(), 
                request.getContrasena()
        );
        
        if (resultado.equals(
                "No existe una cuenta asociada a ese correo.")){
            
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(resultado);
        }
        
        if (resultado.equals("La contraseña es incorrecta.")){
            
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(resultado);
        }
        
        return ResponseEntity.ok(resultado);
    }
}