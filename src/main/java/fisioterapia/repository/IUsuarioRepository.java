package fisioterapia.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import fisioterapia.modelo.entities.Usuario;

public interface IUsuarioRepository extends JpaRepository<Usuario, String> {

    List<Usuario> findByEmail(String email);

    @Query("SELECT u FROM Usuario u JOIN u.perfiles p WHERE p.nombre = :roleName")
    List<Usuario> findByRole(@Param("roleName") String roleName);

}
