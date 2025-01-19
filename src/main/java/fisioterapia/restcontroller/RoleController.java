package fisioterapia.restcontroller;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import fisioterapia.modelo.service.IRoleDao;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private IRoleDao roleDao;

    @GetMapping
    public List<Role> listar() {
        return roleDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable long id) {
        Optional<Role> role = roleDao.read(id);
        if (role.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(role.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Usuario no encontrado" + id));
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Role role, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        roleDao.create(role);
        return ResponseEntity.status(HttpStatus.CREATED).body(role);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable long id, @Valid @RequestBody Role role, BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }
        Optional<Role> roleOptional = roleDao.read(id);
        if (roleOptional.isPresent()) {
            role.setIdRole(id);
            roleDao.update(role);
            return ResponseEntity.status(HttpStatus.OK).body(role);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Role no encontrado" + id));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable long id) {
        Optional<Role> role = roleDao.read(id);
        if (role.isPresent()) {
            roleDao.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Role no encontrado" + id));
        }
    }

}
