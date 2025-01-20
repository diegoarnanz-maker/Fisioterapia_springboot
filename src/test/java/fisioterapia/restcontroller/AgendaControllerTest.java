package fisioterapia.restcontroller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

import fisioterapia.modelo.entities.Agenda;
import fisioterapia.modelo.entities.Role;
import fisioterapia.modelo.entities.Servicio;
import fisioterapia.modelo.entities.Usuario;
import fisioterapia.modelo.service.IAgendaDao;
import fisioterapia.modelo.service.IRoleDao;
import fisioterapia.modelo.service.IServicioDao;
import fisioterapia.modelo.service.IUsuarioDao;
import jakarta.transaction.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AgendaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IAgendaDao agendaDao;

    @Autowired
    private IUsuarioDao usuarioDao;

    @Autowired
    private IServicioDao servicioDao;

    @Autowired
    private IRoleDao roleDao;

    // Como no tengo registros lo hago asi, al ser transaccional se borra al
    // finalizar. Podria hacerlo con una base de datos h2 en memoria... Por probar
    // otra cosa
    @BeforeEach
    public void setUp() {
        // Crear ROLES
        Role roleFisioterapeuta = new Role();
        roleFisioterapeuta.setNombre("ROLE_FISIOTERAPEUTA");
        roleDao.create(roleFisioterapeuta);

        Role roleCliente = new Role();
        roleCliente.setNombre("ROLE_CLIENTE");
        roleDao.create(roleCliente);

        // Crear usuario FISIOTERAPEUTA
        Usuario fisioterapeuta = new Usuario();
        fisioterapeuta.setUsername("fisioterapeuta");
        fisioterapeuta.setPassword("12345678");
        fisioterapeuta.setNombre("Fisioterapeuta");
        fisioterapeuta.setApellidos("García");
        fisioterapeuta.setDireccion("Calle Fisioterapeuta");
        fisioterapeuta.setEnabled(true);
        fisioterapeuta.setFechaRegistro(new Date());
        fisioterapeuta.setEmail("fisioterapeuta@example.com");
        fisioterapeuta.setRoles(List.of(roleFisioterapeuta));
        usuarioDao.create(fisioterapeuta);

        // Crear usuario CLIENTE
        Usuario cliente = new Usuario();
        cliente.setUsername("cliente");
        cliente.setPassword("12345678");
        cliente.setNombre("Cliente");
        cliente.setApellidos("Pérez");
        cliente.setDireccion("Calle Cliente");
        cliente.setEnabled(true);
        cliente.setFechaRegistro(new Date());
        cliente.setEmail("cliente@example.com");
        cliente.setRoles(List.of(roleCliente));
        usuarioDao.create(cliente);

        // Crear SERVICIO
        Servicio servicio = new Servicio();
        servicio.setNombre("Masaje Terapéutico");
        servicio.setDescripcion("Masaje para relajación muscular y alivio de tensiones.");
        servicio.setDuracion(60);
        servicio.setPrecio(50.0);
        servicioDao.create(servicio);

        // Crear AGENDA
        Agenda agenda = new Agenda();
        agenda.setFisioterapeuta(fisioterapeuta);
        agenda.setFecha(new Date(System.currentTimeMillis() + 86400000));
        agenda.setHoraInicio(9);
        agenda.setHoraFin(12);
        agenda.setDisponible(true);
        agendaDao.create(agenda);

    }

    @Test
    public void testListarTodasLasAgendas() throws Exception {

        mockMvc.perform(get("/api/agendas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idAgenda").exists())
                .andExpect(jsonPath("$[0].fisioterapeutaUsername").exists())
                .andExpect(jsonPath("$[0].fecha").exists())
                .andExpect(jsonPath("$[0].horaInicio").exists())
                .andExpect(jsonPath("$[0].horaFin").exists())
                .andExpect(jsonPath("$[0].disponible").exists());
    }

    @Test
    public void testBuscarAgendas() throws Exception {

        // Para no tener que crear otra agenda y poder usar el id exacto
        List<Agenda> agendaYaEnBd = agendaDao.findByFisioterapeutaUsername("fisioterapeuta");

        Agenda agendaEnBd = agendaYaEnBd.get(0);

        mockMvc.perform(get("/api/agendas/{id}", agendaEnBd.getIdAgenda())
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idAgenda").value(agendaEnBd.getIdAgenda()))
                .andExpect(jsonPath("$.fisioterapeutaUsername").value("fisioterapeuta"))
                .andExpect(jsonPath("$.fecha").exists())
                .andExpect(jsonPath("$.horaInicio").value(9))
                .andExpect(jsonPath("$.horaFin").value(12))
                .andExpect(jsonPath("$.disponible").value(true));
    }

    @Test
    public void testListarAgendasPorFisioterapeuta() throws Exception {

        mockMvc.perform(get("/api/agendas/fisioterapeuta/{fisioterapeutaUsername}", "fisioterapeuta")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].idAgenda").exists())
                .andExpect(jsonPath("$[0].fisioterapeutaUsername").value("fisioterapeuta"))
                .andExpect(jsonPath("$[0].horaInicio").exists())
                .andExpect(jsonPath("$[0].horaFin").exists())
                .andExpect(jsonPath("$[0].disponible").exists());

        // Pruebo que no existan
        mockMvc.perform(get("/api/agendas/fisioterapeuta/{fisioterapeutaUsername}", "fisioterapeutaInexistente")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error")
                        .value("No se encontraron agendas para el fisioterapeuta fisioterapeutaInexistente"));
    }

    // NO FUNCIONA, TENGO QUE REVISARLO
    // @Test
    // public void testListarAgendasDisponiblesPorFecha() throws Exception {
    // List<Agenda> agendaYaEnBd =
    // agendaDao.findByFisioterapeutaUsername("fisioterapeuta");
    // Agenda agendaEnBd = agendaYaEnBd.get(0);

    // String fechaFormateada = new
    // SimpleDateFormat("yyyy-MM-dd").format(agendaEnBd.getFecha());

    // mockMvc.perform(get("/api/agendas/fisioterapeuta/{fisioterapeutaUsername}/fecha/{fecha}",
    // agendaEnBd.getFisioterapeuta().getUsername(), fechaFormateada)
    // .contentType(MediaType.APPLICATION_JSON))
    // .andExpect(status().isOk())
    // .andExpect(jsonPath("$").isArray())
    // .andExpect(jsonPath("$[0].idAgenda").exists())
    // .andExpect(jsonPath("$[0].fisioterapeutaUsername").value("fisioterapeuta"))
    // .andExpect(jsonPath("$[0].fecha").exists())
    // .andExpect(jsonPath("$[0].horaInicio").exists())
    // .andExpect(jsonPath("$[0].horaFin").exists())
    // .andExpect(jsonPath("$[0].disponible").exists());
    // }

    @Test
    public void testCrearAgendaExitosamente() throws Exception {
        String username = "admin";
        String password = "12345678";

        String token = obtenerTokenJwt(username, password);

        Usuario fisioterapeuta = new Usuario();
        fisioterapeuta.setUsername("fisioterapeuta");
        fisioterapeuta.setPassword("password");
        fisioterapeuta.setNombre("Fisioterapeuta");
        usuarioDao.create(fisioterapeuta);
        
        Agenda agenda = new Agenda();
        agenda.setFisioterapeuta(fisioterapeuta);
        agenda.setFecha(new SimpleDateFormat("yyyy-MM-dd").parse("2025-01-20"));
        agenda.setHoraInicio(9);
        agenda.setHoraFin(12);
        agenda.setDisponible(true);

        mockMvc.perform(post("/api/agendas")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(agenda)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.fisioterapeutaUsername").value("fisioterapeuta"))
                .andExpect(jsonPath("$.fecha").exists())
                .andExpect(jsonPath("$.horaInicio").value(9))
                .andExpect(jsonPath("$.horaFin").value(12))
                .andExpect(jsonPath("$.disponible").value(true));
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

        System.out.println("JWT Token: " + token);

        return token;
    }

}
