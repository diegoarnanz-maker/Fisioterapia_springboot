package fisioterapia.modelo.service;

import java.util.List;

import fisioterapia.modelo.entities.Usuario;

public interface IUsuarioDao extends ICrudGenerico<Usuario, String> {

        List<Usuario> findByEmail(String email);

        List<Usuario> findByRole(String role);

        Usuario findByUsername(String name);

}
