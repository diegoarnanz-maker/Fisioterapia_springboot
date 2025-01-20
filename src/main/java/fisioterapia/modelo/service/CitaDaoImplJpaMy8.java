package fisioterapia.modelo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fisioterapia.modelo.entities.Cita;
import fisioterapia.modelo.entities.EstadoCita;
import fisioterapia.modelo.entities.Usuario;
import fisioterapia.repository.ICitaRepository;

@Repository
public class CitaDaoImplJpaMy8 implements ICitaDao {

    @Autowired
    private ICitaRepository citaRepository;

    @Override
    public List<Cita> findAll() {
        try {
            return citaRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar las citas", e);
        }
    }

    @Override
    public Cita create(Cita entity) {
        try {
            return citaRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la cita", e);
        }
    }

    @Override
    public Optional<Cita> read(Long id) {
        try {
            return citaRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la cita por id", e);
        }
    }

    @Override
    public Cita update(Cita entity) {
        try {
            return citaRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la cita", e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            citaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la cita", e);
        }
    }

    @Override
    public List<Cita> findByUsuarioUsername(String username) {
        try {
            return citaRepository.findByUsuarioUsername(username);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la cita por usuario", e);
        }
    }

    @Override
    public List<Cita> findByFisioterapeutaUsername(String fisioterapeutaUsername) {
        try {
            return citaRepository.findByFisioterapeutaUsername(fisioterapeutaUsername);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la cita por fisioterapeuta", e);
        }
    }

    @Override
    public List<Cita> findByEstado(EstadoCita estado) {
        try {
            return citaRepository.findByEstado(estado);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la cita por estado", e);
        }
    }

    @Override
    public List<Cita> findByFechaCitaBetween(Date startDate, Date endDate) {
        try {
            return citaRepository.findByFechaCitaBetween(startDate, endDate);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la cita por fecha", e);
        }
    }

    @Override
    public List<Cita> findByUsuarioUsernameAndEstado(String username, EstadoCita estado) {
        try {
            return citaRepository.findByUsuarioUsernameAndEstado(username, estado);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la cita por usuario y estado", e);
        }
    }

    @Override
    public List<Cita> findByFisioterapeutaUsernameAndFechaCita(String fisioterapeutaUsername, Date fechaCita) {
        try {
            return citaRepository.findByFisioterapeutaUsernameAndFechaCita(fisioterapeutaUsername, fechaCita);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la cita por fisioterapeuta y fecha", e);
        }
    }

    @Override
    public boolean existsByUsuarioUsernameAndFisioterapeutaUsernameAndFechaCita(String usuarioUsername,
            String fisioterapeutaUsername, Date fechaCita) {
        try {
            return citaRepository.existsByUsuarioUsernameAndFisioterapeutaUsernameAndFechaCita(usuarioUsername, fisioterapeutaUsername, fechaCita);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la cita por usuario, fisioterapeuta y fecha", e);
        }
    }

    @Override
    public Optional<Cita> findByUsuarioAndFisioterapeutaAndFechaCita(Usuario usuario, Usuario fisioterapeuta,
            Date fechaCita) {
        try {
            return citaRepository.findByUsuarioAndFisioterapeutaAndFechaCita(usuario, fisioterapeuta, fechaCita);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la cita por usuario, fisioterapeuta y fecha", e);
        }
    }

}
