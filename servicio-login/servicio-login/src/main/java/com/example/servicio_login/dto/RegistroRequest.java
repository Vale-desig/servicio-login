package com.example.servicio_login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/*
* Esta clase recibe y valida los datos que una persona
* envía cuando quiere registrarse.
*/
public class RegistroRequest {
    
    // Nombre obligatorio y solamente con letras y espacios.
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
    
    // Correo obligatorio y con formato válido.
    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Ingrese un correo electrónico válido.")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres.")
    private String correo;
    
    /*
    * La contraseña debe tener mínimo 8 caracteres,
    * una mayúscula, una minúscula, un número
    * y un carácter especial.
    */
    @NotBlank(message = "La contraseña es obligatoria.")
    @Size(max = 30, message = "La contraseña no puede superar los 30 caracteres.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s]).{8,30}$",
        message = "La contraseña debe tener mínimo 8 caracteres, al menos una mayúscula, una minúscula, un número y un carácter especial."
    )
    private String contrasena;
    
    // Debe coincidir con la contraseña anterior.
    @NotBlank(message = "Debe confirmar la contraseña.")
    private String confirmarContrasena;
    
    public RegistroRequest(){
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
    
    public String getCorreo(){
        return correo;
    }
    
    public void setCorreo(String correo){
        this.correo = correo;
    }
    
    public String getContrasena(){
        return contrasena;
    }
    
    public void setContrasena(String contrasena){
        this.contrasena = contrasena;
    }
    
    public String getConfirmarContrasena(){
        return confirmarContrasena;
    }
    
    public void setConfirmarContrasena(String confirmarContrasena){
        this.confirmarContrasena = confirmarContrasena;
    }
}