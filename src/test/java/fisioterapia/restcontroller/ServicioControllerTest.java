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

import com.jayway.jsonpath.JsonPath;

import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ServicioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testListarServicios() throws Exception {

        mockMvc.perform(get("/api/servicios")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    public void testBuscarServicio() throws Exception {

        mockMvc.perform(get("/api/servicios/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idServicio").value(1));
    }

    @Test
    public void testBuscarServicioNoEncontrado() throws Exception {

        mockMvc.perform(get("/api/servicios/9999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCrearServicio() throws Exception {

        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        String nuevoServicio = "{"
                + "\"nombre\": \"Servicio de Masaje\","
                + "\"descripcion\": \"Masaje relajante de 60 minutos\","
                + "\"duracion\": 60,"
                + "\"precio\": 30.00"
                + "}";

        mockMvc.perform(post("/api/servicios")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(nuevoServicio))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nombre").value("Servicio de Masaje"))
                .andExpect(jsonPath("$.precio").value(30.00));
    }

    @Test
    public void testActualizarServicio() throws Exception {

        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        String servicioActualizado = "{"
                + "\"nombre\": \"Servicio modificado\","
                + "\"descripcion\": \"Masaje modificado de alta intensidad\","
                + "\"duracion\": 75,"
                + "\"precio\": 50.00"
                + "}";

        mockMvc.perform(put("/api/servicios/1")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(servicioActualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre").value("Servicio modificado"))
                .andExpect(jsonPath("$.precio").value(50.00));
    }

    @Test
    public void testActualizarServicioNoExistente() throws Exception {

        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        String servicioActualizado = "{"
                + "\"nombre\": \"Nuevo Servicio con id irreal\","
                + "\"descripcion\": \"Nuevo masaje\","
                + "\"duracion\": 60,"
                + "\"precio\": 40.00"
                + "}";

        mockMvc.perform(put("/api/servicios/999")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(servicioActualizado))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Servicio no encontrado con id 999"));
    }

    @Test
    public void testEliminarServicio() throws Exception {

        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        mockMvc.perform(delete("/api/servicios/1")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    public void testEliminarServicioNoExistente() throws Exception {

        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        mockMvc.perform(delete("/api/servicios/999")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Servicio no encontrado999"));
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
