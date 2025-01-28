package fisioterapia.restcontroller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import fisioterapia.dto.AgendaDto;
import fisioterapia.modelo.entities.Agenda;
import fisioterapia.modelo.entities.Usuario;
import fisioterapia.modelo.service.IAgendaDao;
import fisioterapia.modelo.service.IUsuarioDao;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/agendas")
public class AgendaController {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private IAgendaDao agendaDao;

    @Autowired
    private IUsuarioDao usuarioDao;

    @GetMapping
    public List<Agenda> listar() {
        return agendaDao.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> buscar(@PathVariable long id) {
        Optional<Agenda> agenda = agendaDao.read(id);

        if (agenda.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(agenda.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Agenda no encontrada " + id));
        }
    }

    @GetMapping("/fisioterapeuta/{fisioterapeutaUsername}")
    public ResponseEntity<?> listarAgendasPorFisioterapeuta(@PathVariable String fisioterapeutaUsername) {
        List<Agenda> agendas = agendaDao.findByFisioterapeutaUsername(fisioterapeutaUsername);

        if (agendas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error",
                            "No se encontraron agendas para el fisioterapeuta " + fisioterapeutaUsername));
        }

        return ResponseEntity.status(HttpStatus.OK).body(agendas);
    }

    @GetMapping("/fisioterapeuta/{fisioterapeutaUsername}/fecha/{fecha}")
    public ResponseEntity<?> listarAgendasDisponibles(@PathVariable String fisioterapeutaUsername,
            @PathVariable Date fecha) {
        List<Agenda> agendas = agendaDao.findByFisioterapeutaUsernameAndFecha(fisioterapeutaUsername, fecha);

        if (agendas.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "No se encontraron agendas disponibles para esa fecha"));
        }

        return ResponseEntity.status(HttpStatus.OK).body(agendas);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody String rawJson) {
        System.out.println("JSON crudo recibido: " + rawJson);
        return ResponseEntity.ok().body("Procesado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(@PathVariable long id, @Valid @RequestBody Agenda agenda,
            BindingResult result) {
        if (result.hasErrors()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result.getAllErrors());
        }

        Optional<Agenda> agendaOptional = agendaDao.read(id);

        if (agendaOptional.isPresent()) {
            Agenda existingAgenda = agendaOptional.get();

            existingAgenda.setFecha(agenda.getFecha());
            existingAgenda.setHoraInicio(agenda.getHoraInicio());
            existingAgenda.setHoraFin(agenda.getHoraFin());
            existingAgenda.setDisponible(agenda.isDisponible());

            agendaDao.update(existingAgenda);

            return ResponseEntity.status(HttpStatus.OK).body(existingAgenda);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Agenda no encontrada " + id));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable long id) {
        Optional<Agenda> agenda = agendaDao.read(id);

        if (agenda.isPresent()) {
            agendaDao.delete(id);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("error", "Agenda no encontrada " + id));
        }
    }

    @PostMapping("/debug-json")
    public ResponseEntity<?> debugJson(HttpServletRequest request) {
        try {
            String body = new BufferedReader(new InputStreamReader(request.getInputStream()))
                    .lines()
                    .collect(Collectors.joining("\n"));
            System.out.println("JSON crudo recibido en el controlador: " + body);
            return ResponseEntity.ok("JSON procesado correctamente");
        } catch (IOException e) {
            System.out.println("Error al leer el cuerpo de la solicitud:");
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al procesar el JSON");
        }
    }

}
