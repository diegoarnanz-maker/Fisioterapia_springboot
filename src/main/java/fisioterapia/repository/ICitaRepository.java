package fisioterapia.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fisioterapia.modelo.entities.Cita;
import fisioterapia.modelo.entities.EstadoCita;
import fisioterapia.modelo.entities.Usuario;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ICitaRepository extends JpaRepository<Cita, Long> {

    List<Cita> findByUsuarioUsername(String username);

    List<Cita> findByFisioterapeutaUsername(String fisioterapeutaUsername);

    List<Cita> findByEstado(EstadoCita estado);

    List<Cita> findByFechaCitaBetween(Date startDate, Date endDate);

    List<Cita> findByUsuarioUsernameAndEstado(String username, EstadoCita estado);

    List<Cita> findByFisioterapeutaUsernameAndFechaCita(String fisioterapeutaUsername, Date fechaCita);

    boolean existsByUsuarioUsernameAndFisioterapeutaUsernameAndFechaCita(String usuarioUsername,
            String fisioterapeutaUsername, Date fechaCita);

    @Query("SELECT c FROM Cita c WHERE c.usuario = :usuario AND c.fisioterapeuta = :fisioterapeuta AND c.fechaCita = :fechaCita")
    Optional<Cita> findByUsuarioAndFisioterapeutaAndFechaCita(@Param("usuario") Usuario usuario,
            @Param("fisioterapeuta") Usuario fisioterapeuta, @Param("fechaCita") Date fechaCita);
}
