package com.example.servicio_login.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/*
* Esta clase recibe el correo electrónico
* utilizado para solicitar la recuperación
* de una contraseña.
*/
public class RecuperacionRequest {
    
    // El correo es obligatorio y debe tener un formato válido.
    @NotBlank(message = "El correo es obligatorio.")
    @Email(message = "Ingrese un correo electrónico válido.")
    @Size(max = 100, message = "El correo no puede superar los 100 caracteres.")
    private String correo;
    
    // Constructor vacío necesario para recibir el JSON.
    public RecuperacionRequest(){
    }
    
    public String getCorreo(){
        return correo;
    }
    
    public void setCorreo(String correo){
        this.correo = correo;
    }
}