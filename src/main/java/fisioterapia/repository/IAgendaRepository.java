package fisioterapia.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fisioterapia.modelo.entities.Agenda;

public interface IAgendaRepository extends JpaRepository<Agenda, Long> {

        @Query("SELECT a FROM Agenda a WHERE a.fisioterapeuta.username = :fisioterapeutaUsername")
        List<Agenda> findByFisioterapeutaUsername(String fisioterapeutaUsername);

        @Query("SELECT a FROM Agenda a WHERE a.fisioterapeuta.username = :fisioterapeutaUsername " +
                        "AND a.fecha = :fecha")
        List<Agenda> findByFisioterapeutaUsernameAndFecha(
                        @Param("fisioterapeutaUsername") String fisioterapeutaUsername,
                        @Param("fecha") Date fecha);

        @Query("SELECT a FROM Agenda a WHERE a.fisioterapeuta.username = :fisioterapeutaUsername " +
                        "AND a.fecha = :fecha " +
                        "AND ((a.horaInicio < :horaFin AND a.horaFin > :horaInicio))")
        Optional<Agenda> findAvailableAgenda(String fisioterapeutaUsername, Date fecha, int horaInicio, int horaFin);

}
