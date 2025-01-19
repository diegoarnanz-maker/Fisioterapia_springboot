package fisioterapia.modelo.service;

import java.util.Optional;

import fisioterapia.modelo.entities.Servicio;

public interface IServicioDao extends ICrudGenerico<Servicio, Long> {

    Optional<Servicio> findByName(String name);

}
