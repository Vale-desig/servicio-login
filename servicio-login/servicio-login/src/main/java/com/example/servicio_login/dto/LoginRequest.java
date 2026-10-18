package com.example.servicio_login.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/*
* Esta clase recibe los datos que una persona
* envía cuando quiere iniciar sesión.
*/
public class LoginRequest {
    
    // Nombre utilizado para identificar al usuario.
    @NotBlank(message = "El nombre es obligatorio.")
    @Size(max = 50, message = "El nombre no puede superar los 50 caracteres.")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+(?:[ ]+[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+)*$",
        message = "El nombre solo puede contener letras y espacios."
    )
    private String nombre;
    
    // Uno o dos apellidos.
    @NotBlank(message = "El apellido es obligatorio.")
    @Size(max = 80, message = "El apellido no puede superar los 80 caracteres.")
    @Pattern(
        regexp = "^[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+(?:[ ]+[A-Za-zÁÉÍÓÚáéíóúÑñÜü]+)*$",
        message = "El apellido solo puede contener letras y espacios."
    )
    private String apellido;
    
    // Contraseña utilizada para iniciar sesión.
    @NotBlank(message = "La contraseña es obligatoria.")
    private String contrasena;
    
    public LoginRequest(){
    }
    
    public String getNombre(){
        return nombre;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }
    
    public String getApellido(){
        return apellido;
    }
    
    public void setApellido(String apellido){
        this.apellido = apellido;
    }
    
    public String getContrasena(){
        return contrasena;
    }
    
    public void setContrasena(String contrasena){
        this.contrasena = contrasena;
    }
}