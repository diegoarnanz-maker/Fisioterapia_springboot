package fisioterapia.restcontroller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fisioterapia.modelo.entities.Perfil;
import fisioterapia.modelo.entities.Usuario;
import fisioterapia.modelo.service.IPerfilDao;
import fisioterapia.modelo.service.IUsuarioDao;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ClaimsBuilder;
import io.jsonwebtoken.ClaimsMutator;
import io.jsonwebtoken.Jwts;
import fisioterapia.auth.TokenJwtConfig; // Asegúrate de que la clase TokenJwtConfig esté correctamente definida

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private IPerfilDao perfilDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

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

        if (usuario.getPerfiles() == null) {
            usuario.setPerfiles(new ArrayList<>());
        }

        Perfil perfilAdmin = new Perfil();
        perfilAdmin.setNombre("ROLE_ADMON");

        perfilDao.create(perfilAdmin);

        usuario.getPerfiles().add(perfilAdmin);
        usuario.setFechaRegistro(new Date());

        usuarioDao.create(usuario);

        usuarioDao.update(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

}
