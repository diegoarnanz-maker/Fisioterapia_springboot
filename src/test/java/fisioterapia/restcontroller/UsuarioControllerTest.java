package fisioterapia.restcontroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import fisioterapia.modelo.service.IRoleDao;
import fisioterapia.modelo.service.IUsuarioDao;
import fisioterapia.modelo.entities.Role;
import fisioterapia.modelo.entities.Usuario;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UsuarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private IRoleDao roleDao;

    // Solo ROLE_ADMON gestiona usuarios
    @Test
    public void testListarUsuarios() throws Exception {
        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        mockMvc.perform(get("/api/usuarios")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testBuscarUsuario() throws Exception {
        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        mockMvc.perform(get("/api/usuarios/{username}", "admin")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"));
    }

    @Test
    public void testCrearUsuarioConRoleCliente() throws Exception {
        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername("nuevoUsuario");
        nuevoUsuario.setPassword("12345678");
        nuevoUsuario.setEmail("nuevoUsuario@example.com");
        nuevoUsuario.setNombre("Nuevo Usuario");
        nuevoUsuario.setApellidos("Apellido Usuario");
        nuevoUsuario.setDireccion("nuevoUsuario Direccion");
        nuevoUsuario.setFechaRegistro(new Date());
        nuevoUsuario.setEnabled(true);

        Role roleCliente = roleDao.findByName("ROLE_CLIENTE").orElseThrow(() -> new Exception("Role no encontrado"));
        nuevoUsuario.setRoles(new ArrayList<>(Arrays.asList(roleCliente)));

        String jsonUsuario = objectMapper.writeValueAsString(nuevoUsuario);

        mockMvc.perform(post("/api/usuarios")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUsuario))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("nuevoUsuario"))
                .andExpect(jsonPath("$.email").value("nuevoUsuario@example.com"))
                .andExpect(jsonPath("$.roles[0].nombre").value("ROLE_CLIENTE"));
    }

    @Test
    public void testCrearUsuarioConRoleAdmon() throws Exception {
        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername("nuevoUsuarioAdmon");
        nuevoUsuario.setPassword("12345678");
        nuevoUsuario.setEmail("nuevoUsuarioAdmon@example.com");
        nuevoUsuario.setNombre("Nuevo Usuario Admon");
        nuevoUsuario.setApellidos("Apellido Usuario Admon");
        nuevoUsuario.setDireccion("nuevoUsuarioAdmon Direccion");
        nuevoUsuario.setFechaRegistro(new Date());
        nuevoUsuario.setEnabled(true);

        Role roleAdmon = roleDao.findByName("ROLE_ADMON").orElseThrow(() -> new Exception("Role no encontrado"));
        nuevoUsuario.setRoles(new ArrayList<>(Arrays.asList(roleAdmon)));

        String jsonUsuario = objectMapper.writeValueAsString(nuevoUsuario);

        mockMvc.perform(post("/api/usuarios")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUsuario))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("nuevoUsuarioAdmon"))
                .andExpect(jsonPath("$.email").value("nuevoUsuarioAdmon@example.com"))
                .andExpect(jsonPath("$.roles[0].nombre").value("ROLE_ADMON"));
    }

    @Test
    public void testCrearUsuarioConRoleAdmonSinPermiso() throws Exception {
        String username = "cliente";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername("nuevoUsuario");
        nuevoUsuario.setPassword("12345678");
        nuevoUsuario.setEmail("nuevoUsuario@example.com");
        nuevoUsuario.setNombre("Nuevo Usuario");
        nuevoUsuario.setApellidos("Apellido Usuario");
        nuevoUsuario.setDireccion("nuevoUsuario Direccion");
        nuevoUsuario.setFechaRegistro(new Date());
        nuevoUsuario.setEnabled(true);

        Role roleAdmon = roleDao.findByName("ROLE_ADMON").orElseThrow(() -> new Exception("Role no encontrado"));
        nuevoUsuario.setRoles(new ArrayList<>(Arrays.asList(roleAdmon)));

        String jsonUsuario = objectMapper.writeValueAsString(nuevoUsuario);

        mockMvc.perform(post("/api/usuarios")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUsuario))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testCrearUsuarioConRoleFisioSinPermiso() throws Exception {
        String username = "fisioterapeuta";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsername("nuevoUsuario");
        nuevoUsuario.setPassword("12345678");
        nuevoUsuario.setEmail("nuevoUsuario@example.com");
        nuevoUsuario.setNombre("Nuevo Usuario");
        nuevoUsuario.setApellidos("Apellido Usuario");
        nuevoUsuario.setDireccion("nuevoUsuario Direccion");
        nuevoUsuario.setFechaRegistro(new Date());
        nuevoUsuario.setEnabled(true);

        Role roleFisio = roleDao.findByName("ROLE_FISIOTERAPEUTA")
                .orElseThrow(() -> new Exception("Role no encontrado"));
        nuevoUsuario.setRoles(new ArrayList<>(Arrays.asList(roleFisio)));

        String jsonUsuario = objectMapper.writeValueAsString(nuevoUsuario);

        mockMvc.perform(post("/api/usuarios")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUsuario))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testActualizarUsuario() throws Exception {
        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        Usuario usuarioExistente = usuarioDao.findByUsername("admin");
        if (usuarioExistente == null) {
            usuarioExistente = new Usuario();
            usuarioExistente.setUsername("admin");
            usuarioExistente.setPassword("12345678");
            usuarioExistente.setEmail("admin@example.com");
            usuarioExistente.setNombre("Admin");
            usuarioExistente.setApellidos("Administrador");
            usuarioExistente.setDireccion("Direccion Admin");
            usuarioExistente.setFechaRegistro(new Date());
            usuarioExistente.setEnabled(true);

            Role role = roleDao.findByName("ROLE_ADMIN").orElseThrow(() -> new Exception("Role no encontrado"));
            usuarioExistente.setRoles(new ArrayList<>(Arrays.asList(role)));

            usuarioDao.create(usuarioExistente);
        }

        usuarioExistente.setNombre("Admin Actualizado");
        usuarioExistente.setApellidos("Administrador Actualizado");

        String jsonUsuario = objectMapper.writeValueAsString(usuarioExistente);

        mockMvc.perform(put("/api/usuarios/{username}", "admin")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonUsuario))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Admin Actualizado"))
                .andExpect(jsonPath("$.apellidos").value("Administrador Actualizado"));
    }

    @Test
    public void testEliminarUsuario() throws Exception {
        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        mockMvc.perform(delete("/api/usuarios/{username}", "admin")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    // Método para generar el token JWT
    private String obtenerTokenJwt(String username, String password) throws Exception {

        String loginPayload = "{ \"username\": \"" + username + "\", \"password\": \"" + password + "\" }";

        MvcResult result = mockMvc.perform(post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(loginPayload))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        String token = JsonPath.read(responseBody, "$.token");

        return token;
    }
}
