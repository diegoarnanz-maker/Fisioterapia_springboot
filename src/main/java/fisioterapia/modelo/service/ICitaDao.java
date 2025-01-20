package fisioterapia.modelo.service;

import fisioterapia.modelo.entities.Cita;
import fisioterapia.modelo.entities.EstadoCita;
import fisioterapia.modelo.entities.Usuario;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ICitaDao extends ICrudGenerico<Cita, Long> {

    List<Cita> findByUsuarioUsername(String username);

    List<Cita> findByFisioterapeutaUsername(String fisioterapeutaUsername);

    List<Cita> findByEstado(EstadoCita estado);

    List<Cita> findByFechaCitaBetween(Date startDate, Date endDate);

    List<Cita> findByUsuarioUsernameAndEstado(String username, EstadoCita estado);

    List<Cita> findByFisioterapeutaUsernameAndFechaCita(String fisioterapeutaUsername, Date fechaCita);

    boolean existsByUsuarioUsernameAndFisioterapeutaUsernameAndFechaCita(String usuarioUsername, String fisioterapeutaUsername, Date fechaCita);

    Optional<Cita> findByUsuarioAndFisioterapeutaAndFechaCita(Usuario usuario, Usuario fisioterapeuta, Date fechaCita);
}
