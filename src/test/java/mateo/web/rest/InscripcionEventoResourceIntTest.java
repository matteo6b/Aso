package mateo.web.rest;

import mateo.AsoApp;
import mateo.domain.InscripcionEvento;
import mateo.repository.InscripcionEventoRepository;

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
 * Test class for the InscripcionEventoResource REST controller.
 *
 * @see InscripcionEventoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AsoApp.class)
@WebAppConfiguration
@IntegrationTest
public class InscripcionEventoResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Double DEFAULT_ENTRADA = 1D;
    private static final Double UPDATED_ENTRADA = 2D;

    private static final ZonedDateTime DEFAULT_FECHAINS = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHAINS = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHAINS_STR = dateTimeFormatter.format(DEFAULT_FECHAINS);

    @Inject
    private InscripcionEventoRepository inscripcionEventoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInscripcionEventoMockMvc;

    private InscripcionEvento inscripcionEvento;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InscripcionEventoResource inscripcionEventoResource = new InscripcionEventoResource();
        ReflectionTestUtils.setField(inscripcionEventoResource, "inscripcionEventoRepository", inscripcionEventoRepository);
        this.restInscripcionEventoMockMvc = MockMvcBuilders.standaloneSetup(inscripcionEventoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        inscripcionEvento = new InscripcionEvento();
        inscripcionEvento.setEntrada(DEFAULT_ENTRADA);
        inscripcionEvento.setFechains(DEFAULT_FECHAINS);
    }

    @Test
    @Transactional
    public void createInscripcionEvento() throws Exception {
        int databaseSizeBeforeCreate = inscripcionEventoRepository.findAll().size();

        // Create the InscripcionEvento

        restInscripcionEventoMockMvc.perform(post("/api/inscripcion-eventos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inscripcionEvento)))
                .andExpect(status().isCreated());

        // Validate the InscripcionEvento in the database
        List<InscripcionEvento> inscripcionEventos = inscripcionEventoRepository.findAll();
        assertThat(inscripcionEventos).hasSize(databaseSizeBeforeCreate + 1);
        InscripcionEvento testInscripcionEvento = inscripcionEventos.get(inscripcionEventos.size() - 1);
        assertThat(testInscripcionEvento.getEntrada()).isEqualTo(DEFAULT_ENTRADA);
        assertThat(testInscripcionEvento.getFechains()).isEqualTo(DEFAULT_FECHAINS);
    }

    @Test
    @Transactional
    public void getAllInscripcionEventos() throws Exception {
        // Initialize the database
        inscripcionEventoRepository.saveAndFlush(inscripcionEvento);

        // Get all the inscripcionEventos
        restInscripcionEventoMockMvc.perform(get("/api/inscripcion-eventos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(inscripcionEvento.getId().intValue())))
                .andExpect(jsonPath("$.[*].entrada").value(hasItem(DEFAULT_ENTRADA.doubleValue())))
                .andExpect(jsonPath("$.[*].fechains").value(hasItem(DEFAULT_FECHAINS_STR)));
    }

    @Test
    @Transactional
    public void getInscripcionEvento() throws Exception {
        // Initialize the database
        inscripcionEventoRepository.saveAndFlush(inscripcionEvento);

        // Get the inscripcionEvento
        restInscripcionEventoMockMvc.perform(get("/api/inscripcion-eventos/{id}", inscripcionEvento.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(inscripcionEvento.getId().intValue()))
            .andExpect(jsonPath("$.entrada").value(DEFAULT_ENTRADA.doubleValue()))
            .andExpect(jsonPath("$.fechains").value(DEFAULT_FECHAINS_STR));
    }

    @Test
    @Transactional
    public void getNonExistingInscripcionEvento() throws Exception {
        // Get the inscripcionEvento
        restInscripcionEventoMockMvc.perform(get("/api/inscripcion-eventos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInscripcionEvento() throws Exception {
        // Initialize the database
        inscripcionEventoRepository.saveAndFlush(inscripcionEvento);
        int databaseSizeBeforeUpdate = inscripcionEventoRepository.findAll().size();

        // Update the inscripcionEvento
        InscripcionEvento updatedInscripcionEvento = new InscripcionEvento();
        updatedInscripcionEvento.setId(inscripcionEvento.getId());
        updatedInscripcionEvento.setEntrada(UPDATED_ENTRADA);
        updatedInscripcionEvento.setFechains(UPDATED_FECHAINS);

        restInscripcionEventoMockMvc.perform(put("/api/inscripcion-eventos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInscripcionEvento)))
                .andExpect(status().isOk());

        // Validate the InscripcionEvento in the database
        List<InscripcionEvento> inscripcionEventos = inscripcionEventoRepository.findAll();
        assertThat(inscripcionEventos).hasSize(databaseSizeBeforeUpdate);
        InscripcionEvento testInscripcionEvento = inscripcionEventos.get(inscripcionEventos.size() - 1);
        assertThat(testInscripcionEvento.getEntrada()).isEqualTo(UPDATED_ENTRADA);
        assertThat(testInscripcionEvento.getFechains()).isEqualTo(UPDATED_FECHAINS);
    }

    @Test
    @Transactional
    public void deleteInscripcionEvento() throws Exception {
        // Initialize the database
        inscripcionEventoRepository.saveAndFlush(inscripcionEvento);
        int databaseSizeBeforeDelete = inscripcionEventoRepository.findAll().size();

        // Get the inscripcionEvento
        restInscripcionEventoMockMvc.perform(delete("/api/inscripcion-eventos/{id}", inscripcionEvento.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<InscripcionEvento> inscripcionEventos = inscripcionEventoRepository.findAll();
        assertThat(inscripcionEventos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
