package fisioterapia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fisioterapia.modelo.entities.Servicio;

public interface IServicioRepository extends JpaRepository<Servicio, Long> {

    Optional<Servicio> findByNombre(String nombre);

}
