package fisioterapia.modelo.service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import fisioterapia.modelo.entities.Agenda;

public interface IAgendaDao extends ICrudGenerico<Agenda, Long> {

    List<Agenda> findByFisioterapeutaUsername(String fisioterapeutaUsername);

    List<Agenda> findByFisioterapeutaUsernameAndFecha(String fisioterapeutaUsername, Date fecha);

    Optional<Agenda> findAvailableAgenda(String fisioterapeutaUsername, Date fecha, int horaInicio, int horaFin);

}
