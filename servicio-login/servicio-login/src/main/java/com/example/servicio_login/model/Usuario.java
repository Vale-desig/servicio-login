package com.example.servicio_login.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/*
* Esta clase representa los usuarios registrados
* en el servicio web.
*/
@Entity
@Table(name = "usuarios")
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Nombre del usuario.
    @Column(nullable = false, length = 50)
    private String nombre;
    
    // Uno o dos apellidos.
    @Column(nullable = false, length = 80)
    private String apellido;
    
    /*
    * Elcorreo se utiliza para recuperar la contraseña
    * y no puede repetirse entre los usuarios.
    */
    @Column(nullable = false, unique = true, length = 100)
    private String correo;
    
    /*
     La contraseña se almacenará en forma de hash.
     Nunca se debe guardar la contraseña directamente.
    */
    @Column(nullable = false, length = 100)
    private String contrasena;
    
    // Código temporal utilizado para recuperar la contraseña.
    @Column(length = 10)
    private String codigoRecuperacion;
    
    // Fecha y hora en la que vence el código.
    private java.time.LocalDateTime expiracionCodigo;
    
    // Constructor vacío necesario para JPA.
    public Usuario(){
    }
    
    // Constructor para crear un usuario.
    public Usuario(String nombre, String apellido, String correo,
                   String contrasena){
        this.nombre = nombre;
        this.apellido = apellido;
        this.correo = correo;
        this.contrasena = contrasena;
    }
    
    public Long getId(){
        return id;
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
    
    public String getCodigoRecuperacion(){
        return codigoRecuperacion;
    }
    
    public void setCodigoRecuperacion(String codigoRecuperacion){
        this.codigoRecuperacion = codigoRecuperacion;
    }
    
    public java.time.LocalDateTime getExpiracionCodigo(){
        return expiracionCodigo;
    }
    
    public void setExpiracionCodigo(java.time.LocalDateTime expiracionCodigo){
        this.expiracionCodigo = expiracionCodigo;
    }
}