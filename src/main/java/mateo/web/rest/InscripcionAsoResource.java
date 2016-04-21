package mateo.web.rest;

import com.codahale.metrics.annotation.Timed;
import mateo.domain.InscripcionAso;
import mateo.repository.InscripcionAsoRepository;
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
 * REST controller for managing InscripcionAso.
 */
@RestController
@RequestMapping("/api")
public class InscripcionAsoResource {

    private final Logger log = LoggerFactory.getLogger(InscripcionAsoResource.class);
        
    @Inject
    private InscripcionAsoRepository inscripcionAsoRepository;
    
    /**
     * POST  /inscripcion-asos : Create a new inscripcionAso.
     *
     * @param inscripcionAso the inscripcionAso to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inscripcionAso, or with status 400 (Bad Request) if the inscripcionAso has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inscripcion-asos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InscripcionAso> createInscripcionAso(@RequestBody InscripcionAso inscripcionAso) throws URISyntaxException {
        log.debug("REST request to save InscripcionAso : {}", inscripcionAso);
        if (inscripcionAso.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("inscripcionAso", "idexists", "A new inscripcionAso cannot already have an ID")).body(null);
        }
        InscripcionAso result = inscripcionAsoRepository.save(inscripcionAso);
        return ResponseEntity.created(new URI("/api/inscripcion-asos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("inscripcionAso", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inscripcion-asos : Updates an existing inscripcionAso.
     *
     * @param inscripcionAso the inscripcionAso to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inscripcionAso,
     * or with status 400 (Bad Request) if the inscripcionAso is not valid,
     * or with status 500 (Internal Server Error) if the inscripcionAso couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inscripcion-asos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InscripcionAso> updateInscripcionAso(@RequestBody InscripcionAso inscripcionAso) throws URISyntaxException {
        log.debug("REST request to update InscripcionAso : {}", inscripcionAso);
        if (inscripcionAso.getId() == null) {
            return createInscripcionAso(inscripcionAso);
        }
        InscripcionAso result = inscripcionAsoRepository.save(inscripcionAso);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("inscripcionAso", inscripcionAso.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inscripcion-asos : get all the inscripcionAsos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of inscripcionAsos in body
     */
    @RequestMapping(value = "/inscripcion-asos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InscripcionAso> getAllInscripcionAsos() {
        log.debug("REST request to get all InscripcionAsos");
        List<InscripcionAso> inscripcionAsos = inscripcionAsoRepository.findAll();
        return inscripcionAsos;
    }

    /**
     * GET  /inscripcion-asos/:id : get the "id" inscripcionAso.
     *
     * @param id the id of the inscripcionAso to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inscripcionAso, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/inscripcion-asos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InscripcionAso> getInscripcionAso(@PathVariable Long id) {
        log.debug("REST request to get InscripcionAso : {}", id);
        InscripcionAso inscripcionAso = inscripcionAsoRepository.findOne(id);
        return Optional.ofNullable(inscripcionAso)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /inscripcion-asos/:id : delete the "id" inscripcionAso.
     *
     * @param id the id of the inscripcionAso to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/inscripcion-asos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInscripcionAso(@PathVariable Long id) {
        log.debug("REST request to delete InscripcionAso : {}", id);
        inscripcionAsoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("inscripcionAso", id.toString())).build();
    }

}
