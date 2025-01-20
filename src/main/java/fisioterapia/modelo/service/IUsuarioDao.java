package fisioterapia.modelo.service;

import java.util.List;

import fisioterapia.modelo.entities.Usuario;

public interface IUsuarioDao extends ICrudGenerico<Usuario, String> {

        List<Usuario> findByEmail(String email);

        List<Usuario> findByRole(String role);

        // Buena practica habria sido poner Optional<Usuario> para acceder a los metodos de Optional como isPresent, get, etc
        Usuario findByUsername(String name);

        boolean existsByEmail(String email);

}
