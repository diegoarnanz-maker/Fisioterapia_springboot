package fisioterapia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fisioterapia.modelo.entities.Perfil;


public interface IPerfilRepository extends JpaRepository<Perfil
, Long> {

    Optional<Perfil> findByNombre(String nombre);

}
