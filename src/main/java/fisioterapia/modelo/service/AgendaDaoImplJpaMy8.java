package fisioterapia.modelo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fisioterapia.modelo.entities.Agenda;
import fisioterapia.repository.IAgendaRepository;

@Repository
public class AgendaDaoImplJpaMy8 implements IAgendaDao {

    @Autowired
    private IAgendaRepository agendaRepository;

    @Override
    public List<Agenda> findAll() {
        try {
            return agendaRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar las agendas", e);
        }
    }

    @Override
    public Agenda create(Agenda entity) {
        try {
            return agendaRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear la agenda", e);
        }
    }

    @Override
    public Optional<Agenda> read(Long id) {
        try {
            return agendaRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer la agenda", e);
        }
    }

    @Override
    public Agenda update(Agenda entity) {
        try {
            return agendaRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar la agenda", e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            agendaRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar la agenda", e);
        }
    }

    @Override
    public List<Agenda> findByFisioterapeutaUsername(String fisioterapeutaUsername) {
        try {
            return agendaRepository.findByFisioterapeutaUsername(fisioterapeutaUsername);
        } catch (Exception e) {
            throw new RuntimeException("Error al listar las agendas del fisioterapeuta", e);
        }
    }

    //TENER CUIDADO CON EL FORMATO DE LA FECHA
    @Override
    public List<Agenda> findByFisioterapeutaUsernameAndFecha(String fisioterapeutaUsername, Date fecha) {
        try {
            return agendaRepository.findByFisioterapeutaUsernameAndFecha(fisioterapeutaUsername, fecha);
        } catch (Exception e) {
            throw new RuntimeException("Error al listar las agendas del fisioterapeuta en la fecha", e);
        }
    }

    @Override
    public Optional<Agenda> findAvailableAgenda(String fisioterapeutaUsername, Date fecha, int horaInicio,
            int horaFin) {
        try {
            return agendaRepository.findAvailableAgenda(fisioterapeutaUsername, fecha, horaInicio, horaFin);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar la agenda disponible", e);
        }
    }

}
