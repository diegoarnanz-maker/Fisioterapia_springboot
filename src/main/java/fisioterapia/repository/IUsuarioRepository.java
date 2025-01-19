package fisioterapia.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fisioterapia.modelo.entities.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, String> {

    List<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u JOIN u.roles r WHERE r.nombre = :roleName")
    List<Usuario> findByRole(@Param("roleName") String roleName);

    Optional<Usuario> findByUsername(String name);

}
