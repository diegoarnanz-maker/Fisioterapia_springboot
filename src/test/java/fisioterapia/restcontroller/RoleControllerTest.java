package fisioterapia.restcontroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import fisioterapia.modelo.entities.Role;
import fisioterapia.modelo.service.IRoleDao;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IRoleDao roleDao;

    // Solo ROLE_ADMON gestiona roles

    @Test
    public void testListar() throws Exception {
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
    public void testListarConRoleCliente() throws Exception {
        String username = "cliente";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        mockMvc.perform(get("/api/usuarios")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testBuscarRole() throws Exception {
        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        mockMvc.perform(get("/api/roles/1")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idRole").value(1))
                .andExpect(jsonPath("$.nombre").value("ROLE_ADMON"));
    }

    @Test
    public void testBuscarRoleNoExiste() throws Exception {
        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        mockMvc.perform(get("/api/roles/100")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCrearRole() throws Exception {
        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        Role role = new Role();
        role.setNombre("ROLE_TEST");

        mockMvc.perform(post("/api/roles")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("ROLE_TEST"));
    }

    @Test
    public void testCrearRoleSinPermiso() throws Exception {
        String username = "cliente";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        Role role = new Role();
        role.setNombre("ROLE_TEST");

        mockMvc.perform(post("/api/roles")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(role)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void testActualizarRole() throws Exception {
        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        Role roleToUpdate = roleDao.read(1L).orElseThrow();

        roleToUpdate.setNombre("ROLE_NUEVO");

        mockMvc.perform(put("/api/roles/1")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roleToUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("ROLE_NUEVO"));
    }

    @Test
    public void testEliminarRole() throws Exception {
        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        mockMvc.perform(delete("/api/roles/1")
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
