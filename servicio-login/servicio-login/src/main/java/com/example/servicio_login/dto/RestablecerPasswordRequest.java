package com.example.servicio_login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/*
* Esta clase recibe los datos necesarios para
* cambiar una contraseña mediante un código temporal.
*/
public class RestablecerPasswordRequest {
    
    // Correo asociaado a la cuenta.
    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Ingrese un correo electrónico válido.")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres.")
    private String correo;
    
    // Código temporal enviado para recuperar la cuenta.
    @NotBlank(message = "El código de recuperación es obligatorio.")
    private String codigo;
    
    /*
    * La nueva contraseña debe cumplir las mismas reglas
    * establecidas durante el registro.
    */
    @NotBlank(message = "La nueva contraseña es obligatoria.")
    @Size(max = 30, message = "La contraseña no puede superar los 30 caracteres.")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^A-Za-z0-9\\s]).{8,30}$",
        message = "La contraseña debe tener mínimo 8 caracteres, al menos una mayúscula, una minúscula, un número y un carácter especial."
    )
    private String nuevaContrasena;
    
    // Confirmación de la nueva contraseña.
    @NotBlank(message = "Debe confirmar la nueva contraseña.")
    private String confirmarContrasena;
    
    // Constructor vacía necesario para recibir el JSON.
    public RestablecerPasswordRequest(){
    }
    
    public String getCorreo(){
        return correo;
    }
    
    public void setCorreo(String correo){
        this.correo = correo;
    }
    
    public String getCodigo(){
        return codigo;
    }
    
    public void setCodigo(String codigo){
        this.codigo = codigo;
    }
    
    public String getNuevaContrasena(){
        return nuevaContrasena;
    }
    
    public void setNuevacontrasena(String nuevaContrasena){
        this.nuevaContrasena = nuevaContrasena;
    }
    
    public String getConfirmarContrasena(){
        return confirmarContrasena;
    }
    public void setConfirmarContrasena(String confirmarContrasena){
        this.confirmarContrasena = confirmarContrasena;
    }
}