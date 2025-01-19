package fisioterapia.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import fisioterapia.modelo.entities.Role;


public interface IRoleRepository extends JpaRepository<Role
, Long> {

    Optional<Role> findByNombre(String nombre);

}
