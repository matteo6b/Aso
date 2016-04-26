package mateo.web.rest;

import mateo.AsoApp;
import mateo.domain.Favorito;
import mateo.repository.FavoritoRepository;

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
 * Test class for the FavoritoResource REST controller.
 *
 * @see FavoritoResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = AsoApp.class)
@WebAppConfiguration
@IntegrationTest
public class FavoritoResourceIntTest {


    private static final Boolean DEFAULT_LIKE = false;
    private static final Boolean UPDATED_LIKE = true;

    private static final Double DEFAULT_LIKES = 1D;
    private static final Double UPDATED_LIKES = 2D;

    @Inject
    private FavoritoRepository favoritoRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restFavoritoMockMvc;

    private Favorito favorito;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        FavoritoResource favoritoResource = new FavoritoResource();
        ReflectionTestUtils.setField(favoritoResource, "favoritoRepository", favoritoRepository);
        this.restFavoritoMockMvc = MockMvcBuilders.standaloneSetup(favoritoResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        favorito = new Favorito();
        favorito.setLike(DEFAULT_LIKE);
        favorito.setLikes(DEFAULT_LIKES);
    }

    @Test
    @Transactional
    public void createFavorito() throws Exception {
        int databaseSizeBeforeCreate = favoritoRepository.findAll().size();

        // Create the Favorito

        restFavoritoMockMvc.perform(post("/api/favoritos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(favorito)))
                .andExpect(status().isCreated());

        // Validate the Favorito in the database
        List<Favorito> favoritos = favoritoRepository.findAll();
        assertThat(favoritos).hasSize(databaseSizeBeforeCreate + 1);
        Favorito testFavorito = favoritos.get(favoritos.size() - 1);
        assertThat(testFavorito.isLike()).isEqualTo(DEFAULT_LIKE);
        assertThat(testFavorito.getLikes()).isEqualTo(DEFAULT_LIKES);
    }

    @Test
    @Transactional
    public void getAllFavoritos() throws Exception {
        // Initialize the database
        favoritoRepository.saveAndFlush(favorito);

        // Get all the favoritos
        restFavoritoMockMvc.perform(get("/api/favoritos?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(favorito.getId().intValue())))
                .andExpect(jsonPath("$.[*].like").value(hasItem(DEFAULT_LIKE.booleanValue())))
                .andExpect(jsonPath("$.[*].likes").value(hasItem(DEFAULT_LIKES.doubleValue())));
    }

    @Test
    @Transactional
    public void getFavorito() throws Exception {
        // Initialize the database
        favoritoRepository.saveAndFlush(favorito);

        // Get the favorito
        restFavoritoMockMvc.perform(get("/api/favoritos/{id}", favorito.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(favorito.getId().intValue()))
            .andExpect(jsonPath("$.like").value(DEFAULT_LIKE.booleanValue()))
            .andExpect(jsonPath("$.likes").value(DEFAULT_LIKES.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingFavorito() throws Exception {
        // Get the favorito
        restFavoritoMockMvc.perform(get("/api/favoritos/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateFavorito() throws Exception {
        // Initialize the database
        favoritoRepository.saveAndFlush(favorito);
        int databaseSizeBeforeUpdate = favoritoRepository.findAll().size();

        // Update the favorito
        Favorito updatedFavorito = new Favorito();
        updatedFavorito.setId(favorito.getId());
        updatedFavorito.setLike(UPDATED_LIKE);
        updatedFavorito.setLikes(UPDATED_LIKES);

        restFavoritoMockMvc.perform(put("/api/favoritos")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedFavorito)))
                .andExpect(status().isOk());

        // Validate the Favorito in the database
        List<Favorito> favoritos = favoritoRepository.findAll();
        assertThat(favoritos).hasSize(databaseSizeBeforeUpdate);
        Favorito testFavorito = favoritos.get(favoritos.size() - 1);
        assertThat(testFavorito.isLike()).isEqualTo(UPDATED_LIKE);
        assertThat(testFavorito.getLikes()).isEqualTo(UPDATED_LIKES);
    }

    @Test
    @Transactional
    public void deleteFavorito() throws Exception {
        // Initialize the database
        favoritoRepository.saveAndFlush(favorito);
        int databaseSizeBeforeDelete = favoritoRepository.findAll().size();

        // Get the favorito
        restFavoritoMockMvc.perform(delete("/api/favoritos/{id}", favorito.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Favorito> favoritos = favoritoRepository.findAll();
        assertThat(favoritos).hasSize(databaseSizeBeforeDelete - 1);
    }
}
