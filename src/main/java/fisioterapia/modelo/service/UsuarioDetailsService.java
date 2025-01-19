package fisioterapia.modelo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fisioterapia.modelo.entities.Role;
import fisioterapia.modelo.entities.Usuario;
import fisioterapia.repository.IUsuarioRepository;

@Service
public class UsuarioDetailsService implements UserDetailsService {

    @Autowired
    private IUsuarioRepository usuarioRepository;

    @Transactional(readOnly = true)
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<Usuario> usuarioOptional = usuarioRepository.findByUsername(username);
        if (usuarioOptional.isEmpty()) {
            throw new UsernameNotFoundException(username + " not found");
        }

        Usuario usuario = usuarioOptional.get();

        List<GrantedAuthority> authorities = usuario.getRoles().stream()
                .map(Role::getNombre)
                .map(role -> {
                    System.out.println("Role: " + role); // Verifica qué roles se están cargando
                    return new SimpleGrantedAuthority(role);
                })
                .collect(Collectors.toList());

        return new User(usuario.getUsername(), usuario.getPassword(), true, true, true, true, authorities);

    }
}
