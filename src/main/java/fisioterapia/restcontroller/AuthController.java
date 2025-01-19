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

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private IRoleDao perfilDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/crearPrimerUsuario")
    public ResponseEntity<?> crearPrimerUsuario(@RequestBody Usuario usuario,
            @RequestHeader("Secret-Key") String secretKey) {

        if (!"fisioterapia".equals(secretKey)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("error", "Acceso denegado"));
        }

        if (usuarioDao.findByRole("ROLE_ADMON").size() > 0) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Collections.singletonMap("error", "Ya existe un usuario administrador"));
        }

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        if (usuario.getRoles() == null) {
            usuario.setRoles(new ArrayList<>());
        }

        Optional<Role> perfilAdminOptional = perfilDao.findByName("ROLE_ADMON");
        if (perfilAdminOptional.isEmpty()) {
            Role perfilAdmin = new Role();
            perfilAdmin.setNombre("ROLE_ADMON");
            perfilDao.create(perfilAdmin);
            usuario.getRoles().add(perfilAdmin);
        } else {
            usuario.getRoles().add(perfilAdminOptional.get());
        }

        usuario.setFechaRegistro(new Date());

        usuarioDao.create(usuario);
        usuarioDao.update(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

}
