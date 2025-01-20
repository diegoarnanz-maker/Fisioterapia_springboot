package fisioterapia.restcontroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    // Publica - Header con secret-key (fisioterapia)
    @Test
    public void testCrearPrimerUsuario() throws Exception {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("username", "admin");
        usuario.put("password", "admin123");
        usuario.put("email", "admin@fisioterapia.com");
        usuario.put("nombre", "Administrador");
        usuario.put("apellidos", "Fisioterapia");
        usuario.put("direccion", "Calle Admin, 123");
        usuario.put("enabled", true);

        String usuarioPayload = new ObjectMapper().writeValueAsString(usuario);

        mockMvc.perform(post("/api/auth/crearPrimerUsuario")
                .header("Secret-Key", "fisioterapia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioPayload))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.roles[0].nombre").value("ROLE_ADMON"));
    }
    
    // Publica
    @Test
    public void testCrearPrimerUsuarioConSecretKeyIncorrecto() throws Exception {
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("username", "admin");
        usuario.put("password", "admin123");
        usuario.put("email", "admin@fisioterapia.com");
        usuario.put("nombre", "Administrador");
        usuario.put("apellidos", "Fisioterapia");
        usuario.put("direccion", "Calle Admin, 123");
        usuario.put("enabled", true);

        String usuarioPayload = new ObjectMapper().writeValueAsString(usuario);

        mockMvc.perform(post("/api/auth/crearPrimerUsuario")
                .header("Secret-Key", "wrongsecret")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioPayload))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Acceso denegado"));
    }
    
    // Publica
    @Test
    public void testCrearPrimerUsuarioCuandoYaExiste() throws Exception {
        Map<String, Object> usuarioExistente = new HashMap<>();
        usuarioExistente.put("username", "admin");
        usuarioExistente.put("password", "12345678");
        usuarioExistente.put("email", "admin@fisioterapia.com");
        usuarioExistente.put("nombre", "Administrador");
        usuarioExistente.put("apellidos", "Fisioterapia");
        usuarioExistente.put("direccion", "Calle Admin, 123");
        usuarioExistente.put("enabled", true);

        String usuarioExistentePayload = new ObjectMapper().writeValueAsString(usuarioExistente);

        mockMvc.perform(post("/api/auth/crearPrimerUsuario")
                .header("Secret-Key", "fisioterapia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioExistentePayload))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/auth/crearPrimerUsuario")
                .header("Secret-Key", "fisioterapia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioExistentePayload))
                .andExpect(status().isForbidden())
                .andExpect(jsonPath("$.error").value("Ya existe un usuario administrador"));
    }

    // Requiere header con jwt
    @Test
    public void testAccesoConTokenValido() throws Exception {
        String username = "admin";
        String password = "12345678";
        String token = obtenerTokenJwt(username, password);

        mockMvc.perform(get("/api/usuarios")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testAccesoSinToken() throws Exception {
        mockMvc.perform(get("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void testAccesoConTokenInvalido() throws Exception {
        String tokenInvalido = "invalid.token.here";

        mockMvc.perform(get("/api/usuarios")
                .header("Authorization", "Bearer " + tokenInvalido)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
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
