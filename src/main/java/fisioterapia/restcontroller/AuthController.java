package fisioterapia.restcontroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fisioterapia.modelo.entities.Role;
import fisioterapia.modelo.entities.Usuario;
import fisioterapia.modelo.service.IRoleDao;
import fisioterapia.modelo.service.IUsuarioDao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class); // Inicializa el logger

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private IRoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/crearPrimerUsuario")
    public ResponseEntity<?> crearPrimerUsuario(@RequestBody Usuario usuario,
            @RequestHeader("Secret-Key") String secretKey) {

        logger.info("Recibiendo solicitud para crear el primer usuario");

        if (!"fisioterapia".equals(secretKey)) {
            logger.warn("Acceso denegado: el Secret-Key es incorrecto");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("error", "Acceso denegado"));
        }

        if (usuarioDao.findByRole("ROLE_ADMON").size() > 0) {
            logger.warn("Ya existe un usuario administrador en la base de datos");
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("error", "Ya existe un usuario administrador"));
        }

        // Continuar con la creación del usuario
        logger.info("Creando usuario con nombre: {}", usuario.getUsername());

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        if (usuario.getRoles() == null) {
            usuario.setRoles(new ArrayList<>());
        }

        Optional<Role> roleAdminOptional = roleDao.findByName("ROLE_ADMON");
        if (roleAdminOptional.isEmpty()) {
            Role roleAdmin = new Role();
            roleAdmin.setNombre("ROLE_ADMON");
            roleDao.create(roleAdmin);
            usuario.getRoles().add(roleAdmin);
            logger.info("Rol ROLE_ADMON creado y asignado al usuario");
        } else {
            usuario.getRoles().add(roleAdminOptional.get());
            logger.info("Rol ROLE_ADMON encontrado y asignado al usuario");
        }

        usuario.setFechaRegistro(new Date());
        usuarioDao.create(usuario);
        usuarioDao.update(usuario);

        logger.info("Usuario creado exitosamente: {}", usuario.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

}
