package mateo.web.rest;

import com.codahale.metrics.annotation.Timed;
import mateo.domain.Favorito;
import mateo.repository.FavoritoRepository;
import mateo.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing Favorito.
 */
@RestController
@RequestMapping("/api")
public class FavoritoResource {

    private final Logger log = LoggerFactory.getLogger(FavoritoResource.class);
        
    @Inject
    private FavoritoRepository favoritoRepository;
    
    /**
     * POST  /favoritos : Create a new favorito.
     *
     * @param favorito the favorito to create
     * @return the ResponseEntity with status 201 (Created) and with body the new favorito, or with status 400 (Bad Request) if the favorito has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/favoritos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Favorito> createFavorito(@RequestBody Favorito favorito) throws URISyntaxException {
        log.debug("REST request to save Favorito : {}", favorito);
        if (favorito.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("favorito", "idexists", "A new favorito cannot already have an ID")).body(null);
        }
        Favorito result = favoritoRepository.save(favorito);
        return ResponseEntity.created(new URI("/api/favoritos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("favorito", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /favoritos : Updates an existing favorito.
     *
     * @param favorito the favorito to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated favorito,
     * or with status 400 (Bad Request) if the favorito is not valid,
     * or with status 500 (Internal Server Error) if the favorito couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/favoritos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Favorito> updateFavorito(@RequestBody Favorito favorito) throws URISyntaxException {
        log.debug("REST request to update Favorito : {}", favorito);
        if (favorito.getId() == null) {
            return createFavorito(favorito);
        }
        Favorito result = favoritoRepository.save(favorito);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("favorito", favorito.getId().toString()))
            .body(result);
    }

    /**
     * GET  /favoritos : get all the favoritos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of favoritos in body
     */
    @RequestMapping(value = "/favoritos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Favorito> getAllFavoritos() {
        log.debug("REST request to get all Favoritos");
        List<Favorito> favoritos = favoritoRepository.findAll();
        return favoritos;
    }

    /**
     * GET  /favoritos/:id : get the "id" favorito.
     *
     * @param id the id of the favorito to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the favorito, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/favoritos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Favorito> getFavorito(@PathVariable Long id) {
        log.debug("REST request to get Favorito : {}", id);
        Favorito favorito = favoritoRepository.findOne(id);
        return Optional.ofNullable(favorito)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /favoritos/:id : delete the "id" favorito.
     *
     * @param id the id of the favorito to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/favoritos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteFavorito(@PathVariable Long id) {
        log.debug("REST request to delete Favorito : {}", id);
        favoritoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("favorito", id.toString())).build();
    }

}
