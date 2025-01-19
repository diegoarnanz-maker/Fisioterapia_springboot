package fisioterapia.modelo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fisioterapia.modelo.entities.Servicio;
import fisioterapia.repository.IServicioRepository;

@Repository
public class ServicioDaoImplJpaMy8 implements IServicioDao {

    @Autowired
    private IServicioRepository servicioRepository;

    @Override
    public List<Servicio> findAll() {
        try {
            return servicioRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al listar los servicios", e);
        }
    }

    @Override
    public Servicio create(Servicio entity) {
        try {
            return servicioRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el servicio", e);
        }
    }

    @Override
    public Optional<Servicio> read(Long id) {
        try {
            return servicioRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el servicio por id", e);
        }
    }

    @Override
    public Servicio update(Servicio entity) {
        try {
            return servicioRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el servicio", e);
        }
    }

    @Override
    public void delete(Long id) {
        try {
            servicioRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el servicio", e);
        }
    }

    @Override
    public Optional<Servicio> findByName(String name) {
        try {
            return servicioRepository.findByNombre(name);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el servicio por nombre", e);
        }
    }

}
