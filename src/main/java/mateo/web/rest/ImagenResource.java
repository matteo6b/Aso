package mateo.web.rest;

import com.codahale.metrics.annotation.Timed;
import mateo.domain.Imagen;
import mateo.repository.ImagenRepository;
import mateo.web.rest.util.HeaderUtil;
import mateo.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
 * REST controller for managing Imagen.
 */
@RestController
@RequestMapping("/api")
public class ImagenResource {

    private final Logger log = LoggerFactory.getLogger(ImagenResource.class);
        
    @Inject
    private ImagenRepository imagenRepository;
    
    /**
     * POST  /imagens : Create a new imagen.
     *
     * @param imagen the imagen to create
     * @return the ResponseEntity with status 201 (Created) and with body the new imagen, or with status 400 (Bad Request) if the imagen has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/imagens",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Imagen> createImagen(@RequestBody Imagen imagen) throws URISyntaxException {
        log.debug("REST request to save Imagen : {}", imagen);
        if (imagen.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("imagen", "idexists", "A new imagen cannot already have an ID")).body(null);
        }
        Imagen result = imagenRepository.save(imagen);
        return ResponseEntity.created(new URI("/api/imagens/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("imagen", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /imagens : Updates an existing imagen.
     *
     * @param imagen the imagen to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated imagen,
     * or with status 400 (Bad Request) if the imagen is not valid,
     * or with status 500 (Internal Server Error) if the imagen couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/imagens",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Imagen> updateImagen(@RequestBody Imagen imagen) throws URISyntaxException {
        log.debug("REST request to update Imagen : {}", imagen);
        if (imagen.getId() == null) {
            return createImagen(imagen);
        }
        Imagen result = imagenRepository.save(imagen);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("imagen", imagen.getId().toString()))
            .body(result);
    }

    /**
     * GET  /imagens : get all the imagens.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of imagens in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/imagens",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Imagen>> getAllImagens(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Imagens");
        Page<Imagen> page = imagenRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/imagens");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /imagens/:id : get the "id" imagen.
     *
     * @param id the id of the imagen to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the imagen, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/imagens/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Imagen> getImagen(@PathVariable Long id) {
        log.debug("REST request to get Imagen : {}", id);
        Imagen imagen = imagenRepository.findOne(id);
        return Optional.ofNullable(imagen)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /imagens/:id : delete the "id" imagen.
     *
     * @param id the id of the imagen to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/imagens/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteImagen(@PathVariable Long id) {
        log.debug("REST request to delete Imagen : {}", id);
        imagenRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("imagen", id.toString())).build();
    }

}
