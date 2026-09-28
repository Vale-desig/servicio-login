package com.example.servicio_login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
* Esta clase recibe los datos necesarios para
* confirmar la eliminación de una cuenta.
*/
public class EliminarCuentaRequest {
    
    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Ingrese un correo electrónico válido.")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres.")
    private String correo;
    
    @NotBlank(message = "La contraseña es obligatoria.")
    private String contrasena;
    
    public EliminarCuentaRequest(){
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
}

