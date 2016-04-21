package mateo.web.rest;

import mateo.AsoApp;
import mateo.domain.InscripcionAso;
import mateo.repository.InscripcionAsoRepository;

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
 * Test class for the InscripcionAsoResource REST controller.
 *
 * @see InscripcionAsoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AsoApp.class)
@WebAppConfiguration
@IntegrationTest
public class InscripcionAsoResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));


    private static final Boolean DEFAULT_VALIDAR = false;
    private static final Boolean UPDATED_VALIDAR = true;

    private static final ZonedDateTime DEFAULT_FECHA_INSCRITO = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHA_INSCRITO = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHA_INSCRITO_STR = dateTimeFormatter.format(DEFAULT_FECHA_INSCRITO);

    @Inject
    private InscripcionAsoRepository inscripcionAsoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restInscripcionAsoMockMvc;

    private InscripcionAso inscripcionAso;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        InscripcionAsoResource inscripcionAsoResource = new InscripcionAsoResource();
        ReflectionTestUtils.setField(inscripcionAsoResource, "inscripcionAsoRepository", inscripcionAsoRepository);
        this.restInscripcionAsoMockMvc = MockMvcBuilders.standaloneSetup(inscripcionAsoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        inscripcionAso = new InscripcionAso();
        inscripcionAso.setValidar(DEFAULT_VALIDAR);
        inscripcionAso.setFechaInscrito(DEFAULT_FECHA_INSCRITO);
    }

    @Test
    @Transactional
    public void createInscripcionAso() throws Exception {
        int databaseSizeBeforeCreate = inscripcionAsoRepository.findAll().size();

        // Create the InscripcionAso

        restInscripcionAsoMockMvc.perform(post("/api/inscripcion-asos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(inscripcionAso)))
                .andExpect(status().isCreated());

        // Validate the InscripcionAso in the database
        List<InscripcionAso> inscripcionAsos = inscripcionAsoRepository.findAll();
        assertThat(inscripcionAsos).hasSize(databaseSizeBeforeCreate + 1);
        InscripcionAso testInscripcionAso = inscripcionAsos.get(inscripcionAsos.size() - 1);
        assertThat(testInscripcionAso.isValidar()).isEqualTo(DEFAULT_VALIDAR);
        assertThat(testInscripcionAso.getFechaInscrito()).isEqualTo(DEFAULT_FECHA_INSCRITO);
    }

    @Test
    @Transactional
    public void getAllInscripcionAsos() throws Exception {
        // Initialize the database
        inscripcionAsoRepository.saveAndFlush(inscripcionAso);

        // Get all the inscripcionAsos
        restInscripcionAsoMockMvc.perform(get("/api/inscripcion-asos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(inscripcionAso.getId().intValue())))
                .andExpect(jsonPath("$.[*].validar").value(hasItem(DEFAULT_VALIDAR.booleanValue())))
                .andExpect(jsonPath("$.[*].fechaInscrito").value(hasItem(DEFAULT_FECHA_INSCRITO_STR)));
    }

    @Test
    @Transactional
    public void getInscripcionAso() throws Exception {
        // Initialize the database
        inscripcionAsoRepository.saveAndFlush(inscripcionAso);

        // Get the inscripcionAso
        restInscripcionAsoMockMvc.perform(get("/api/inscripcion-asos/{id}", inscripcionAso.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(inscripcionAso.getId().intValue()))
            .andExpect(jsonPath("$.validar").value(DEFAULT_VALIDAR.booleanValue()))
            .andExpect(jsonPath("$.fechaInscrito").value(DEFAULT_FECHA_INSCRITO_STR));
    }

    @Test
    @Transactional
    public void getNonExistingInscripcionAso() throws Exception {
        // Get the inscripcionAso
        restInscripcionAsoMockMvc.perform(get("/api/inscripcion-asos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateInscripcionAso() throws Exception {
        // Initialize the database
        inscripcionAsoRepository.saveAndFlush(inscripcionAso);
        int databaseSizeBeforeUpdate = inscripcionAsoRepository.findAll().size();

        // Update the inscripcionAso
        InscripcionAso updatedInscripcionAso = new InscripcionAso();
        updatedInscripcionAso.setId(inscripcionAso.getId());
        updatedInscripcionAso.setValidar(UPDATED_VALIDAR);
        updatedInscripcionAso.setFechaInscrito(UPDATED_FECHA_INSCRITO);

        restInscripcionAsoMockMvc.perform(put("/api/inscripcion-asos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedInscripcionAso)))
                .andExpect(status().isOk());

        // Validate the InscripcionAso in the database
        List<InscripcionAso> inscripcionAsos = inscripcionAsoRepository.findAll();
        assertThat(inscripcionAsos).hasSize(databaseSizeBeforeUpdate);
        InscripcionAso testInscripcionAso = inscripcionAsos.get(inscripcionAsos.size() - 1);
        assertThat(testInscripcionAso.isValidar()).isEqualTo(UPDATED_VALIDAR);
        assertThat(testInscripcionAso.getFechaInscrito()).isEqualTo(UPDATED_FECHA_INSCRITO);
    }

    @Test
    @Transactional
    public void deleteInscripcionAso() throws Exception {
        // Initialize the database
        inscripcionAsoRepository.saveAndFlush(inscripcionAso);
        int databaseSizeBeforeDelete = inscripcionAsoRepository.findAll().size();

        // Get the inscripcionAso
        restInscripcionAsoMockMvc.perform(delete("/api/inscripcion-asos/{id}", inscripcionAso.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<InscripcionAso> inscripcionAsos = inscripcionAsoRepository.findAll();
        assertThat(inscripcionAsos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
