package fisioterapia.modelo.service;

import java.util.Optional;

import fisioterapia.modelo.entities.Role;

public interface IRoleDao extends ICrudGenerico<Role, Long> {

public Optional<Role> findByName(String nombre);
}
