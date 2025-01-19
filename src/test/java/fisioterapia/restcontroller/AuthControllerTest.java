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

    @Test
    public void testCrearPrimerUsuario() throws Exception {
        // Crea un objeto Map con los datos del usuario
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("username", "admin");
        usuario.put("password", "admin123");
        usuario.put("email", "admin@fisioterapia.com");
        usuario.put("nombre", "Administrador");
        usuario.put("apellidos", "Fisioterapia");
        usuario.put("direccion", "Calle Admin, 123");
        usuario.put("enabled", true);

        // Convierte el Map a JSON String usando ObjectMapper
        String usuarioPayload = new ObjectMapper().writeValueAsString(usuario);

        // Llamada al endpoint para crear el primer usuario con la clave secreta
        mockMvc.perform(post("/api/auth/crearPrimerUsuario")
                .header("Secret-Key", "fisioterapia") // Proporciona la clave secreta
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioPayload))
                .andExpect(status().isCreated()) // Verifica que la respuesta es un HTTP 201 (creado)
                .andExpect(jsonPath("$.username").value("admin")) // Verifica que el usuario creado tiene el nombre
                                                                  // "admin"
                .andExpect(jsonPath("$.roles[0].nombre").value("ROLE_ADMON")); // Verifica que el rol asignado es
                                                                               // ROLE_ADMON
    }

    @Test
    public void testCrearPrimerUsuarioConSecretKeyIncorrecto() throws Exception {
        // Crea un objeto Map con los datos del usuario
        Map<String, Object> usuario = new HashMap<>();
        usuario.put("username", "admin");
        usuario.put("password", "admin123");
        usuario.put("email", "admin@fisioterapia.com");
        usuario.put("nombre", "Administrador");
        usuario.put("apellidos", "Fisioterapia");
        usuario.put("direccion", "Calle Admin, 123");
        usuario.put("enabled", true);

        // Convierte el Map a JSON String usando ObjectMapper
        String usuarioPayload = new ObjectMapper().writeValueAsString(usuario);

        // Llamada al endpoint para crear el primer usuario con la clave secreta
        // incorrecta
        mockMvc.perform(post("/api/auth/crearPrimerUsuario")
                .header("Secret-Key", "wrongsecret") // Clave incorrecta
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioPayload))
                .andExpect(status().isForbidden()) // Se espera que el código de estado sea 403 Forbidden
                .andExpect(jsonPath("$.error").value("Acceso denegado")); // Verifica el mensaje de error
    }

    @Test
    public void testCrearPrimerUsuarioCuandoYaExiste() throws Exception {
        // Crear un usuario administrador previamente para simular que ya existe
        Map<String, Object> usuarioExistente = new HashMap<>();
        usuarioExistente.put("username", "admin");
        usuarioExistente.put("password", "12345678");
        usuarioExistente.put("email", "admin@fisioterapia.com");
        usuarioExistente.put("nombre", "Administrador");
        usuarioExistente.put("apellidos", "Fisioterapia");
        usuarioExistente.put("direccion", "Calle Admin, 123");
        usuarioExistente.put("enabled", true);

        // Convierte el Map a JSON String usando ObjectMapper
        String usuarioExistentePayload = new ObjectMapper().writeValueAsString(usuarioExistente);

        // Crear el primer usuario administrador
        mockMvc.perform(post("/api/auth/crearPrimerUsuario")
                .header("Secret-Key", "fisioterapia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioExistentePayload))
                .andExpect(status().isCreated());

        // Intentar crear otro administrador
        mockMvc.perform(post("/api/auth/crearPrimerUsuario")
                .header("Secret-Key", "fisioterapia")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioExistentePayload))
                .andExpect(status().isForbidden()) // Espera que el código sea 403
                .andExpect(jsonPath("$.error").value("Ya existe un usuario administrador")); // Verifica el error
    }

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
