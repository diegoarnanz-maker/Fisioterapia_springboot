package fisioterapia.modelo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import fisioterapia.modelo.entities.Usuario;
import fisioterapia.repository.IUsuarioRepository;

@Repository
public class UsuarioDaoImplJpaMy8 implements IUsuarioDao {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Override
    public Usuario create(Usuario entity) {
        try{
            return usuarioRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error al crear el usuario", e);
        }
    }

    @Override
    public Optional<Usuario> read(String id) {
        try{
            return usuarioRepository.findById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al leer el usuario", e);
        }
    }

    @Override
    public Usuario update(Usuario entity) {
        try{
            return usuarioRepository.save(entity);
        } catch (Exception e) {
            throw new RuntimeException("Error al actualizar el usuario", e);
        }
    }

    @Override
    public void delete(String id) {
        try{
            usuarioRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Error al eliminar el usuario", e);
        }
    }

    @Override
    public List<Usuario> findAll() {
        try{
            return usuarioRepository.findAll();
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar todos los usuarios", e);
        }
    }

    @Override
    public List<Usuario> findByEmail(String email) {
        try{
            return usuarioRepository.findByEmail(email);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el usuario por email", e);
        }
    }

    @Override
    public List<Usuario> findByRole(String role) {
        try{
            return usuarioRepository.findByRole(role);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el usuario por rol", e);
        }
    }

    @Override
    public Usuario findByUsername(String name) {
        try{
            return usuarioRepository.findByUsername(name).orElse(null);
        } catch (Exception e) {
            throw new RuntimeException("Error al buscar el usuario por nombre de usuario", e);
        }
    }

}
