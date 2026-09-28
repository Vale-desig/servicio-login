package com.example.servicio_login.repository;

import com.example.servicio_login.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

import java.time.LocalDateTime;

/*
* Este repositorio permite guardar, buscar y consultar
* los usuarios registrados en la base de datos.
*/
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{
    
    /*
    * Busca todos los usuarios que tengan el mismo nombre
    * y apellido.
    *
    * Se utiliza una lista porque dos personas pueden
    * tener el mismo nombre y apellido.
    */
    List<Usuario> findAllByNombreAndApellido(String nombre, String apellido);
    
    // Busca un usuario por correo.
    Optional<Usuario> findByCorreo(String correo);
    
    // Comprueba si ya existe un correo registrado.
    boolean existsByCorreo(String correo);
    
    /*
    * Comprueba si un código de recuperación ya está
    * siendo utilizado y todavía no ha vencido.
    */
    boolean existsByCodigoRecuperacionAndExpiracionCodigoAfter(
            String codigo,
            LocalDateTime fechaActual);
}   