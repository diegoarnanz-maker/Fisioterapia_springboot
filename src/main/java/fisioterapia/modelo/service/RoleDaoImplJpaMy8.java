package fisioterapia.modelo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fisioterapia.modelo.entities.Role;
import fisioterapia.repository.IRoleRepository;

@Repository
public class RoleDaoImplJpaMy8 implements IRoleDao {

    @Autowired
    private IRoleRepository roleRepository;

    @Override
    public Role create(Role entity) {
        try {
            return roleRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el rol");
        }
    }

    @Override
    public Optional<Role> read(Long id) {
        try {
            return roleRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el rol");
        }
    }

    @Override
    public Role update(Role entity) {
        try {
            return roleRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el rol");
        }
    }

    @Override
    public void delete(Long id) {
        try {
            roleRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el rol");
        }
    }

    @Override
    public List<Role> findAll() {
        try {
            return roleRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar todos los roles");
        }
    }

    @Override
    public Optional<Role> findByName(String nombre) {
        try {
            return roleRepository.findByNombre(nombre);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el rol por nombre");
        }
    }

}
