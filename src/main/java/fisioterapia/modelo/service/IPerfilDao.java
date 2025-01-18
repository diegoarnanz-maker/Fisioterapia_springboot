package fisioterapia.modelo.service;

import java.util.Optional;

import fisioterapia.modelo.entities.Perfil;

public interface IPerfilDao extends ICrudGenerico<Perfil, Long> {

public Optional<Perfil> findByName(String nombre);
}
