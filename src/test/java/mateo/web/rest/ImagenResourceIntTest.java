package mateo.web.rest;

import mateo.AsoApp;
import mateo.domain.Imagen;
import mateo.repository.ImagenRepository;

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
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the ImagenResource REST controller.
 *
 * @see ImagenResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AsoApp.class)
@WebAppConfiguration
@IntegrationTest
public class ImagenResourceIntTest {

    private static final String DEFAULT_URL = "AAAAA";
    private static final String UPDATED_URL = "BBBBB";

    @Inject
    private ImagenRepository imagenRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restImagenMockMvc;

    private Imagen imagen;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ImagenResource imagenResource = new ImagenResource();
        ReflectionTestUtils.setField(imagenResource, "imagenRepository", imagenRepository);
        this.restImagenMockMvc = MockMvcBuilders.standaloneSetup(imagenResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        imagen = new Imagen();
        imagen.setUrl(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void createImagen() throws Exception {
        int databaseSizeBeforeCreate = imagenRepository.findAll().size();

        // Create the Imagen

        restImagenMockMvc.perform(post("/api/imagens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(imagen)))
                .andExpect(status().isCreated());

        // Validate the Imagen in the database
        List<Imagen> imagens = imagenRepository.findAll();
        assertThat(imagens).hasSize(databaseSizeBeforeCreate + 1);
        Imagen testImagen = imagens.get(imagens.size() - 1);
        assertThat(testImagen.getUrl()).isEqualTo(DEFAULT_URL);
    }

    @Test
    @Transactional
    public void getAllImagens() throws Exception {
        // Initialize the database
        imagenRepository.saveAndFlush(imagen);

        // Get all the imagens
        restImagenMockMvc.perform(get("/api/imagens?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(imagen.getId().intValue())))
                .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL.toString())));
    }

    @Test
    @Transactional
    public void getImagen() throws Exception {
        // Initialize the database
        imagenRepository.saveAndFlush(imagen);

        // Get the imagen
        restImagenMockMvc.perform(get("/api/imagens/{id}", imagen.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(imagen.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingImagen() throws Exception {
        // Get the imagen
        restImagenMockMvc.perform(get("/api/imagens/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateImagen() throws Exception {
        // Initialize the database
        imagenRepository.saveAndFlush(imagen);
        int databaseSizeBeforeUpdate = imagenRepository.findAll().size();

        // Update the imagen
        Imagen updatedImagen = new Imagen();
        updatedImagen.setId(imagen.getId());
        updatedImagen.setUrl(UPDATED_URL);

        restImagenMockMvc.perform(put("/api/imagens")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedImagen)))
                .andExpect(status().isOk());

        // Validate the Imagen in the database
        List<Imagen> imagens = imagenRepository.findAll();
        assertThat(imagens).hasSize(databaseSizeBeforeUpdate);
        Imagen testImagen = imagens.get(imagens.size() - 1);
        assertThat(testImagen.getUrl()).isEqualTo(UPDATED_URL);
    }

    @Test
    @Transactional
    public void deleteImagen() throws Exception {
        // Initialize the database
        imagenRepository.saveAndFlush(imagen);
        int databaseSizeBeforeDelete = imagenRepository.findAll().size();

        // Get the imagen
        restImagenMockMvc.perform(delete("/api/imagens/{id}", imagen.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Imagen> imagens = imagenRepository.findAll();
        assertThat(imagens).hasSize(databaseSizeBeforeDelete - 1);
    }
}
