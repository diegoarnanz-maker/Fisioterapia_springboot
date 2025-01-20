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

import fisioterapia.modelo.entities.Servicio;
import fisioterapia.modelo.service.IServicioDao;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/servicios")
public class ServicioController {

    @Autowired
    private IServicioDao servicioDao;

    @GetMapping
    public List<Servicio> listar() {
        return servicioDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable long id) {
        Optional<Servicio> servicio = servicioDao.read(id);
        if (servicio.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(servicio.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Servicio no encontrado " + id));
        }
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Servicio servicio) {
        servicioDao.create(servicio);
        return ResponseEntity.status(HttpStatus.CREATED).body(servicio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable long id, @Valid @RequestBody Servicio servicio,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        Optional<Servicio> servicioOptional = servicioDao.read(id);
        if (servicioOptional.isPresent()) {
            Servicio existingServicio = servicioOptional.get();

            existingServicio.setNombre(servicio.getNombre());
            existingServicio.setDescripcion(servicio.getDescripcion());
            existingServicio.setDuracion(servicio.getDuracion());
            existingServicio.setPrecio(servicio.getPrecio());

            servicioDao.update(existingServicio);

            return ResponseEntity.status(HttpStatus.OK).body(existingServicio);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Servicio no encontrado con id " + id));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable long id) {
        Optional<Servicio> servicio = servicioDao.read(id);
        if (servicio.isPresent()) {
            servicioDao.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Servicio no encontrado" + id));
        }
    }

}
