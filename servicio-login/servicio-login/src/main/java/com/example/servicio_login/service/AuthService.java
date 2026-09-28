package com.example.servicio_login.service;

import com.example.servicio_login.model.Usuario;
import com.example.servicio_login.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

/*
* Esta clase contiene la lógica principal
* del registro, inicio de sesión y recuperación
* de contraseña.
*/
@Service
public class AuthService {
    
    private final UsuarioRepository usuarioRepository;
    
    private final EmailService emailService;
    
    /*
    * BCrypt se utiliza para proteger las contraseñas
    * antes de guardarlas en la base de datos.
    */
    private final BCryptPasswordEncoder passwordEncoder;
    
    /*
    * Constructor utilizado por Spring para conectar
    * el servicio con el repositorio.
    */
    public AuthService(
            UsuarioRepository usuarioRepository,
            EmailService emailService){
        
        this.usuarioRepository = usuarioRepository;
        this.emailService = emailService;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }
    
    /*
    * Registra un nuevo usuario después de comprobar
    * que el correo no esté registrado y que las
    * contraseñas coincidan.
    */
    public String registrarUsuario(
            String nombre,
            String apellido,
            String correo,
            String contrasena,
            String confirmarContrasena){
        
        // Comprueba si el correo ya está registrado.
        if (usuarioRepository.existsByCorreo(correo)){
            return "El correo electrónico ya está registrado.";
        }
        
        // Comprueba que las dos contraseñas sean iguales.
        if (!contrasena.equals(confirmarContrasena)){
            return "Las contraseñas no coinciden.";
        }
        
        /*
        * La contraseña se convierte en un hash seguro
        * antes de guardarla en la base de datos.
        */
        String contrasenaSegura = passwordEncoder.encode(contrasena);
        
        // Crea el nuevo usuario.
        Usuario usuario = new Usuario(
                nombre,
                apellido,
                correo,
                contrasenaSegura
        );
        
        // Guarda el usuario en la base de datos.
        usuarioRepository.save(usuario);
        
        try {
            System.out.println("Intentando enviar correo de registro...");
            
            emailService.enviarCorreoRegistro(correo, nombre);
            
            System.out.println("Correo de registro enviado correctamente.");
        } catch (Exception e) {
            System.out.println("No se pudo enviar el correo de confirmación.");
            System.out.println("Motivo: " + e.getMessage());
        }
        
        return "Registro realizado correctamente.";
    }
    
        /*
     * Comprueba los datos enviados durante el inicio
     * de sesión y verifica la contraseña mediante BCrypt.
     */
    public String iniciarSesion(
            String nombre,
            String apellido,
            String contrasena) {

        /*
         * Busca los usuarios que coincidan con el nombre
         * y apellido proporcionados.
         */
        var usuarios = usuarioRepository
                .findAllByNombreAndApellido(nombre, apellido);

        /*
         * Recorre los usuarios encontrados y comprueba
         * la contraseña de cada uno.
         */
        for (Usuario usuario : usuarios) {

            if (passwordEncoder.matches(
                    contrasena,
                    usuario.getContrasena())) {

                return usuario.getCorreo();
            }
        }

        // Si ningún usuario coincide con la contraseña.
        return "Error de autenticación.";
    }
    
    /*
    * Genera un código temporal para recuperar
    * la contraseña de un usuario.
    */
    public String solicitarRecuperacion(String correo){
        
        // Busca el usuario mediante su correo.
        var usuarioOptional = usuarioRepository.findByCorreo(correo);
        
        /*
        * Si el correo no está registrado, se informa
        * que no se encontró la cuenta.
        */
        if (usuarioOptional.isEmpty()){
            return "No existe una cuenta asociada a ese correo.";
        }
        
        Usuario usuario = usuarioOptional.get();
        
        /*
        * Genera un código aleatorio de 6 dígitos.
        */
        String codigo;
        
        do{
            codigo = String.format(
                    "%06d", 
                    new Random().nextInt(1000000)
            );
        } while (usuarioRepository
                .existsByCodigoRecuperacionAndExpiracionCodigoAfter(
                        codigo, 
                        LocalDateTime.now()));
        
        // Guarda el código temporral en el usuario.
        usuario.setCodigoRecuperacion(codigo);
        
        /*
        * El código tendrá una duración de 10 minutos.
        */
        usuario.setExpiracionCodigo(
                LocalDateTime.now().plusMinutes(10)
        );
        
        // Guarda los cambios en la base de datos.
        usuarioRepository.save(usuario);
        
        /*
        * Envía el código temporal al correo registrado.
        */
        emailService.enviarCodigoRecuperacion(
                usuario.getCorreo(), 
                usuario.getNombre(), 
                codigo
        );
        
        return "Código de recuperación enviado correctamente.";
    }
    
    /*
    * Comprueba el código de recuperación y permite
    * establecer una nueva contraseña.
    */
    public String restablecerContrasena(
            String correo,
            String codigo,
            String nuevaContrasena,
            String confirmarContrasena){
        
        // Busca la cuenta mediante el correo.
        var usuarioOptional = usuarioRepository.findByCorreo(correo);
        
        // Comprueba que la cuenta exista.
        if (usuarioOptional.isEmpty()){
            return "No existe una cuenta asociada a ese correo.";
        }
        
        Usuario usuario = usuarioOptional.get();
        
        /*
        * Comprueba que existe un código de recuperación
        * asociado a la cuenta.
        */
        if (usuario.getCodigoRecuperacion() == null
                || usuario.getExpiracionCodigo() == null){
            
            return "No existe un código de recuperación activo.";
        }
        
        /*
        * Comprueba que el código recibido sea igual
        * al código almacenado.
        */
        if (!usuario.getCodigoRecuperacion().equals(codigo)){
            return "El código de recuperación es incorrecto.";
        }
        
        /*
        * Comprueba que el código todavía esté vigente.
        */
        if (LocalDateTime.now().isAfter(
                usuario.getExpiracionCodigo())){
            
            return "El código de recuperación ha expirado.";
        }
        
        /*
        * Comprueba que la nueva contraseña y su 
        * confirmación sean iguales.
        */
        if (!nuevaContrasena.equals(confirmarContrasena)){
            return "Las contraseñas no coinciden";
        }
        
        /*
        * Protege la nueva contraseña mediante BCrypt
        * antes de almacenarla.
        */
        String nuevaContrasenaSegura =
                passwordEncoder.encode(nuevaContrasena);
        
        usuario.setContrasena(nuevaContrasenaSegura);
        
        /*
        * Elimina el código y su fecha de vencimiento
        * para impedir que vuelva a utilizarse.
        */
        usuario.setCodigoRecuperacion(null);
        usuario.setExpiracionCodigo(null);
        
        // Guarda los cambios en la base de datos.
        usuarioRepository.save(usuario);
        
        return "Contraseña restablecida correctamente.";
    }
    
    /*
   ¨* Verifica el correo y la coontraseña antes de eliminar
    * la cuenta del usuario.
    */
    public String eliminarCuenta(
            String correo,
            String contrasena){
        
        var usuarioOptional = usuarioRepository.findByCorreo(correo);
        
        if (usuarioOptional.isEmpty()){
            return "No existe una cuenta asociada a ese correo.";
        }
        
        Usuario usuario = usuarioOptional.get();
        
        /*
        * Comprueba la contraseña utilizando BCrypt.
        */
        if (!passwordEncoder.matches(
                contrasena,
                usuario.getContrasena())){
                
            return "La contraseña es incorrecta.";
        }
        
        usuarioRepository.delete(usuario);
        
        /*
        * Envía un correo notificando que la cuenta
        * fue eliminada correctamente.
        */
        emailService.enviarCorreoEliminacion(
                usuario.getCorreo(),
                usuario.getNombre()
        );
        
        return "Cuenta eliminada correctamente.";
    }
}