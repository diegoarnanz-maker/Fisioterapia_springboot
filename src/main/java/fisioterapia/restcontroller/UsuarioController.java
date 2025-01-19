package fisioterapia.restcontroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fisioterapia.modelo.entities.Role;
import fisioterapia.modelo.entities.Usuario;
import fisioterapia.modelo.service.IRoleDao;
import fisioterapia.modelo.service.IUsuarioDao;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private IRoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public List<Usuario> listar() {
        return usuarioDao.findAll();
    }

    @GetMapping("{username}")
    public ResponseEntity<?> buscar(@PathVariable String username) {
        Optional<Usuario> usuario = usuarioDao.read(username);
        if (usuario.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(usuario.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Usuario no encontrado" + username));
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        if (usuarioDao.existsByEmail(usuario.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El correo electrónico " + usuario.getEmail() + " ya está registrado.");
        }

        String encodedPassword = passwordEncoder.encode(usuario.getPassword());
        usuario.setPassword(encodedPassword);

        usuario.setEnabled(true);
        usuario.setFechaRegistro(new java.util.Date());

        // asociar roles al usuario
        if (usuario.getRoles() != null) {
            List<Role> rolesList = new ArrayList<>(usuario.getRoles());

            for (Role role : rolesList) {
                Role existingRole = roleDao.findByName(role.getNombre()).orElse(null);
                if (existingRole == null) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("El rol " + role.getNombre() + " no es válido.");
                }
            }
        }

        usuarioDao.create(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @PutMapping("{username}")
    public ResponseEntity<?> actualizar(@PathVariable String username, @Valid @RequestBody Usuario usuario,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        Optional<Usuario> usuarioExistente = usuarioDao.read(username);
        if (usuarioExistente.isPresent()) {

            if (!usuario.getEmail().equals(usuarioExistente.get().getEmail())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(Collections.singletonMap("error", "El correo electrónico no puede ser modificado."));
            }

            if (!usuario.getPassword().equals(usuarioExistente.get().getPassword())) {
                if (usuario.getPassword().length() < 8) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(Collections.singletonMap("error", "La contraseña debe tener al menos 8 caracteres."));
                }

                String encodedPassword = passwordEncoder.encode(usuario.getPassword());
                usuario.setPassword(encodedPassword);
            }

            usuarioDao.update(usuario);
            return ResponseEntity.status(HttpStatus.OK).body(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Usuario no encontrado " + username));
        }
    }

    @DeleteMapping("{username}")
    public ResponseEntity<?> eliminar(@PathVariable String username) {
        Optional<Usuario> usuario = usuarioDao.read(username);
        if (usuario.isPresent()) {
            usuarioDao.delete(username);
            return ResponseEntity.status(HttpStatus.NO_CONTENT)
                    .body(Collections.singletonMap("success", "Usuario eliminado"));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Usuario no encontrado" + username));
        }
    }

}
