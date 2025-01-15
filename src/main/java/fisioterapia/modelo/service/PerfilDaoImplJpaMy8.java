package fisioterapia.modelo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fisioterapia.modelo.entities.Perfil;
import fisioterapia.repository.IPerfilRepository;

@Repository
public class PerfilDaoImplJpaMy8 implements IPerfilDao {

    @Autowired
    private IPerfilRepository perfilRepository;

    @Override
    public Perfil create(Perfil entity) {
        try {
            return perfilRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el perfil");
        }
    }

    @Override
    public Optional<Perfil> read(Long id) {
        try {
            return perfilRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el perfil");
        }
    }

    @Override
    public Perfil update(Perfil entity) {
        try {
            return perfilRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el perfil");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            perfilRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el perfil");
        }
    }

    @Override
    public List<Perfil> findAll() {
        try {
            return perfilRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar todos los perfiles");
        }
    }

}
