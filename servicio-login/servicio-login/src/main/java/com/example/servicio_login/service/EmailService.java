package com.example.servicio_login.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/*
* Esta clase se encarga de enviar los correos electrónicos
* utilizados por el servicio web.
*/
@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    /*
    * Envía un correo de confirmación cuando
    * una persona completa su registro.
    */
    public void enviarCorreoRegistro(String correo, String nombre){
        
        SimpleMailMessage mensaje = new SimpleMailMessage();
        
        mensaje.setTo(correo);
        mensaje.setSubject("Registro realizado correctamente");
        mensaje.setText(
                "Hola " + nombre + ",\n\n"
                + "Tu registro en el servicio de inicio de sesión "
                + "se realizó correctamente. \n\n"
                + "Ya puedes iniciar sesión con tus datos de acceso. \n\n"
                + "Este correo es una confirmación automática."
        );
        
        mailSender.send(mensaje);
    }
    
    /*
    * Envía el código temporal utilizado para
    * recuperar la contraseña.
    */
    public void enviarCodigoRecuperacion(
            String correo,
            String nombre,
            String codigo){
        
        SimpleMailMessage mensaje = new SimpleMailMessage();
        
        mensaje.setTo(correo);
        mensaje.setSubject("Código para recuperar tu contraseña");
        mensaje.setText(
                "Hola " + nombre + ",\n\n"
                + "Recibimos una solicitud para recuperar "
                + "la contraseña de tu cuenta.\n\n"
                + "Tu código de recuperación es: " + codigo + "\n\n"
                + "Este código tiene una duración de 10 minutos "
                + "y solo puede utilizarse una vez.\n\n"
                + "Si no solicitaste este cambio, puedes ignorar "
                + "este mensaje"
        );
        
        mailSender.send(mensaje);
    }
    
    /*
    * Envía un correo informando que la cuenta
    * fue eliminada correctamente.
    */
    public void enviarCorreoEliminacion(
            String correo,
            String nombre){
        
        SimpleMailMessage mensaje = new SimpleMailMessage();
        
        mensaje.setTo(correo);
        mensaje.setSubject("Cuenta eliminada");
        mensaje.setText(
                "Hola " + nombre + ",\n\n"
                + "Tu cuenta ha sido eliminada correctamente.\n\n"
                + "Si realizaste esta acción, no necesitas hacer nada más.\n\n"
                + "Este correo es una notificación automática."
        );
        System.out.println("Intentando enviar correo de registro...");
        
        mailSender.send(mensaje);
        
        System.out.println("Correo de registro enviado correctamente.");
    }
}