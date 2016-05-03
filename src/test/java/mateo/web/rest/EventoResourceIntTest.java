package mateo.web.rest;

import mateo.AsoApp;
import mateo.domain.Evento;
import mateo.repository.EventoRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the EventoResource REST controller.
 *
 * @see EventoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AsoApp.class)
@WebAppConfiguration
@IntegrationTest
public class EventoResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_FECHA = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHA = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHA_STR = dateTimeFormatter.format(DEFAULT_FECHA);
    private static final String DEFAULT_STREET_ADRESS = "AAAAA";
    private static final String UPDATED_STREET_ADRESS = "BBBBB";

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;

    private static final Double DEFAULT_LNG = 1D;
    private static final Double UPDATED_LNG = 2D;

    private static final Integer DEFAULT_CAPACIDAD = 1;
    private static final Integer UPDATED_CAPACIDAD = 2;

    private static final Integer DEFAULT_ENTRADA = 1;
    private static final Integer UPDATED_ENTRADA = 2;
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";

    @Inject
    private EventoRepository eventoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restEventoMockMvc;

    private Evento evento;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        EventoResource eventoResource = new EventoResource();
        ReflectionTestUtils.setField(eventoResource, "eventoRepository", eventoRepository);
        this.restEventoMockMvc = MockMvcBuilders.standaloneSetup(eventoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        evento = new Evento();
        evento.setName(DEFAULT_NAME);
        evento.setFecha(DEFAULT_FECHA);
        evento.setStreetAdress(DEFAULT_STREET_ADRESS);
        evento.setLat(DEFAULT_LAT);
        evento.setLng(DEFAULT_LNG);
        evento.setCapacidad(DEFAULT_CAPACIDAD);
        evento.setEntrada(DEFAULT_ENTRADA);
        evento.setDescripcion(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void createEvento() throws Exception {
        int databaseSizeBeforeCreate = eventoRepository.findAll().size();

        // Create the Evento

        restEventoMockMvc.perform(post("/api/eventos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(evento)))
                .andExpect(status().isCreated());

        // Validate the Evento in the database
        List<Evento> eventos = eventoRepository.findAll();
        assertThat(eventos).hasSize(databaseSizeBeforeCreate + 1);
        Evento testEvento = eventos.get(eventos.size() - 1);
        assertThat(testEvento.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testEvento.getFecha()).isEqualTo(DEFAULT_FECHA);
        assertThat(testEvento.getStreetAdress()).isEqualTo(DEFAULT_STREET_ADRESS);
        assertThat(testEvento.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testEvento.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testEvento.getCapacidad()).isEqualTo(DEFAULT_CAPACIDAD);
        assertThat(testEvento.getEntrada()).isEqualTo(DEFAULT_ENTRADA);
        assertThat(testEvento.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
    }

    @Test
    @Transactional
    public void getAllEventos() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get all the eventos
        restEventoMockMvc.perform(get("/api/eventos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(evento.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].fecha").value(hasItem(DEFAULT_FECHA_STR)))
                .andExpect(jsonPath("$.[*].streetAdress").value(hasItem(DEFAULT_STREET_ADRESS.toString())))
                .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
                .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())))
                .andExpect(jsonPath("$.[*].capacidad").value(hasItem(DEFAULT_CAPACIDAD)))
                .andExpect(jsonPath("$.[*].entrada").value(hasItem(DEFAULT_ENTRADA)))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())));
    }

    @Test
    @Transactional
    public void getEvento() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);

        // Get the evento
        restEventoMockMvc.perform(get("/api/eventos/{id}", evento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(evento.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.fecha").value(DEFAULT_FECHA_STR))
            .andExpect(jsonPath("$.streetAdress").value(DEFAULT_STREET_ADRESS.toString()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.doubleValue()))
            .andExpect(jsonPath("$.capacidad").value(DEFAULT_CAPACIDAD))
            .andExpect(jsonPath("$.entrada").value(DEFAULT_ENTRADA))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingEvento() throws Exception {
        // Get the evento
        restEventoMockMvc.perform(get("/api/eventos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateEvento() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);
        int databaseSizeBeforeUpdate = eventoRepository.findAll().size();

        // Update the evento
        Evento updatedEvento = new Evento();
        updatedEvento.setId(evento.getId());
        updatedEvento.setName(UPDATED_NAME);
        updatedEvento.setFecha(UPDATED_FECHA);
        updatedEvento.setStreetAdress(UPDATED_STREET_ADRESS);
        updatedEvento.setLat(UPDATED_LAT);
        updatedEvento.setLng(UPDATED_LNG);
        updatedEvento.setCapacidad(UPDATED_CAPACIDAD);
        updatedEvento.setEntrada(UPDATED_ENTRADA);
        updatedEvento.setDescripcion(UPDATED_DESCRIPCION);

        restEventoMockMvc.perform(put("/api/eventos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedEvento)))
                .andExpect(status().isOk());

        // Validate the Evento in the database
        List<Evento> eventos = eventoRepository.findAll();
        assertThat(eventos).hasSize(databaseSizeBeforeUpdate);
        Evento testEvento = eventos.get(eventos.size() - 1);
        assertThat(testEvento.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testEvento.getFecha()).isEqualTo(UPDATED_FECHA);
        assertThat(testEvento.getStreetAdress()).isEqualTo(UPDATED_STREET_ADRESS);
        assertThat(testEvento.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testEvento.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testEvento.getCapacidad()).isEqualTo(UPDATED_CAPACIDAD);
        assertThat(testEvento.getEntrada()).isEqualTo(UPDATED_ENTRADA);
        assertThat(testEvento.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
    }

    @Test
    @Transactional
    public void deleteEvento() throws Exception {
        // Initialize the database
        eventoRepository.saveAndFlush(evento);
        int databaseSizeBeforeDelete = eventoRepository.findAll().size();

        // Get the evento
        restEventoMockMvc.perform(delete("/api/eventos/{id}", evento.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Evento> eventos = eventoRepository.findAll();
        assertThat(eventos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
