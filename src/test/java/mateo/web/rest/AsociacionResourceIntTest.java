package mateo.web.rest;

import mateo.AsoApp;
import mateo.domain.Asociacion;
import mateo.repository.AsociacionRepository;

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
 * Test class for the AsociacionResource REST controller.
 *
 * @see AsociacionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AsoApp.class)
@WebAppConfiguration
@IntegrationTest
public class AsociacionResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").withZone(ZoneId.of("Z"));

    private static final String DEFAULT_NAME = "A";
    private static final String UPDATED_NAME = "B";

    private static final ZonedDateTime DEFAULT_FECHA_CREACION = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_FECHA_CREACION = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_FECHA_CREACION_STR = dateTimeFormatter.format(DEFAULT_FECHA_CREACION);

    private static final Integer DEFAULT_CUOTA = 1;
    private static final Integer UPDATED_CUOTA = 2;
    private static final String DEFAULT_INSTRUCCIONES = "AAAAA";
    private static final String UPDATED_INSTRUCCIONES = "BBBBB";
    private static final String DEFAULT_DESCRIPCION = "AAAAA";
    private static final String UPDATED_DESCRIPCION = "BBBBB";
    private static final String DEFAULT_STREET_ADRESS = "AAAAA";
    private static final String UPDATED_STREET_ADRESS = "BBBBB";

    private static final Double DEFAULT_LAT = 1D;
    private static final Double UPDATED_LAT = 2D;

    private static final Double DEFAULT_LNG = 1D;
    private static final Double UPDATED_LNG = 2D;

    @Inject
    private AsociacionRepository asociacionRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restAsociacionMockMvc;

    private Asociacion asociacion;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        AsociacionResource asociacionResource = new AsociacionResource();
        ReflectionTestUtils.setField(asociacionResource, "asociacionRepository", asociacionRepository);
        this.restAsociacionMockMvc = MockMvcBuilders.standaloneSetup(asociacionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        asociacion = new Asociacion();
        asociacion.setName(DEFAULT_NAME);
        asociacion.setFechaCreacion(DEFAULT_FECHA_CREACION);
        asociacion.setCuota(DEFAULT_CUOTA);
        asociacion.setInstrucciones(DEFAULT_INSTRUCCIONES);
        asociacion.setDescripcion(DEFAULT_DESCRIPCION);
        asociacion.setStreetAdress(DEFAULT_STREET_ADRESS);
        asociacion.setLat(DEFAULT_LAT);
        asociacion.setLng(DEFAULT_LNG);
    }

    @Test
    @Transactional
    public void createAsociacion() throws Exception {
        int databaseSizeBeforeCreate = asociacionRepository.findAll().size();

        // Create the Asociacion

        restAsociacionMockMvc.perform(post("/api/asociacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(asociacion)))
                .andExpect(status().isCreated());

        // Validate the Asociacion in the database
        List<Asociacion> asociacions = asociacionRepository.findAll();
        assertThat(asociacions).hasSize(databaseSizeBeforeCreate + 1);
        Asociacion testAsociacion = asociacions.get(asociacions.size() - 1);
        assertThat(testAsociacion.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAsociacion.getFechaCreacion()).isEqualTo(DEFAULT_FECHA_CREACION);
        assertThat(testAsociacion.getCuota()).isEqualTo(DEFAULT_CUOTA);
        assertThat(testAsociacion.getInstrucciones()).isEqualTo(DEFAULT_INSTRUCCIONES);
        assertThat(testAsociacion.getDescripcion()).isEqualTo(DEFAULT_DESCRIPCION);
        assertThat(testAsociacion.getStreetAdress()).isEqualTo(DEFAULT_STREET_ADRESS);
        assertThat(testAsociacion.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testAsociacion.getLng()).isEqualTo(DEFAULT_LNG);
    }

    @Test
    @Transactional
    public void getAllAsociacions() throws Exception {
        // Initialize the database
        asociacionRepository.saveAndFlush(asociacion);

        // Get all the asociacions
        restAsociacionMockMvc.perform(get("/api/asociacions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(asociacion.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].fechaCreacion").value(hasItem(DEFAULT_FECHA_CREACION_STR)))
                .andExpect(jsonPath("$.[*].cuota").value(hasItem(DEFAULT_CUOTA)))
                .andExpect(jsonPath("$.[*].instrucciones").value(hasItem(DEFAULT_INSTRUCCIONES.toString())))
                .andExpect(jsonPath("$.[*].descripcion").value(hasItem(DEFAULT_DESCRIPCION.toString())))
                .andExpect(jsonPath("$.[*].streetAdress").value(hasItem(DEFAULT_STREET_ADRESS.toString())))
                .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT.doubleValue())))
                .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG.doubleValue())));
    }

    @Test
    @Transactional
    public void getAsociacion() throws Exception {
        // Initialize the database
        asociacionRepository.saveAndFlush(asociacion);

        // Get the asociacion
        restAsociacionMockMvc.perform(get("/api/asociacions/{id}", asociacion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(asociacion.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.fechaCreacion").value(DEFAULT_FECHA_CREACION_STR))
            .andExpect(jsonPath("$.cuota").value(DEFAULT_CUOTA))
            .andExpect(jsonPath("$.instrucciones").value(DEFAULT_INSTRUCCIONES.toString()))
            .andExpect(jsonPath("$.descripcion").value(DEFAULT_DESCRIPCION.toString()))
            .andExpect(jsonPath("$.streetAdress").value(DEFAULT_STREET_ADRESS.toString()))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT.doubleValue()))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingAsociacion() throws Exception {
        // Get the asociacion
        restAsociacionMockMvc.perform(get("/api/asociacions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAsociacion() throws Exception {
        // Initialize the database
        asociacionRepository.saveAndFlush(asociacion);
        int databaseSizeBeforeUpdate = asociacionRepository.findAll().size();

        // Update the asociacion
        Asociacion updatedAsociacion = new Asociacion();
        updatedAsociacion.setId(asociacion.getId());
        updatedAsociacion.setName(UPDATED_NAME);
        updatedAsociacion.setFechaCreacion(UPDATED_FECHA_CREACION);
        updatedAsociacion.setCuota(UPDATED_CUOTA);
        updatedAsociacion.setInstrucciones(UPDATED_INSTRUCCIONES);
        updatedAsociacion.setDescripcion(UPDATED_DESCRIPCION);
        updatedAsociacion.setStreetAdress(UPDATED_STREET_ADRESS);
        updatedAsociacion.setLat(UPDATED_LAT);
        updatedAsociacion.setLng(UPDATED_LNG);

        restAsociacionMockMvc.perform(put("/api/asociacions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedAsociacion)))
                .andExpect(status().isOk());

        // Validate the Asociacion in the database
        List<Asociacion> asociacions = asociacionRepository.findAll();
        assertThat(asociacions).hasSize(databaseSizeBeforeUpdate);
        Asociacion testAsociacion = asociacions.get(asociacions.size() - 1);
        assertThat(testAsociacion.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAsociacion.getFechaCreacion()).isEqualTo(UPDATED_FECHA_CREACION);
        assertThat(testAsociacion.getCuota()).isEqualTo(UPDATED_CUOTA);
        assertThat(testAsociacion.getInstrucciones()).isEqualTo(UPDATED_INSTRUCCIONES);
        assertThat(testAsociacion.getDescripcion()).isEqualTo(UPDATED_DESCRIPCION);
        assertThat(testAsociacion.getStreetAdress()).isEqualTo(UPDATED_STREET_ADRESS);
        assertThat(testAsociacion.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testAsociacion.getLng()).isEqualTo(UPDATED_LNG);
    }

    @Test
    @Transactional
    public void deleteAsociacion() throws Exception {
        // Initialize the database
        asociacionRepository.saveAndFlush(asociacion);
        int databaseSizeBeforeDelete = asociacionRepository.findAll().size();

        // Get the asociacion
        restAsociacionMockMvc.perform(delete("/api/asociacions/{id}", asociacion.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Asociacion> asociacions = asociacionRepository.findAll();
        assertThat(asociacions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
