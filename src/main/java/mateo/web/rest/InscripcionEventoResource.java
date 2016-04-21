package mateo.web.rest;

import com.codahale.metrics.annotation.Timed;
import mateo.domain.InscripcionEvento;
import mateo.repository.InscripcionEventoRepository;
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
 * REST controller for managing InscripcionEvento.
 */
@RestController
@RequestMapping("/api")
public class InscripcionEventoResource {

    private final Logger log = LoggerFactory.getLogger(InscripcionEventoResource.class);
        
    @Inject
    private InscripcionEventoRepository inscripcionEventoRepository;
    
    /**
     * POST  /inscripcion-eventos : Create a new inscripcionEvento.
     *
     * @param inscripcionEvento the inscripcionEvento to create
     * @return the ResponseEntity with status 201 (Created) and with body the new inscripcionEvento, or with status 400 (Bad Request) if the inscripcionEvento has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inscripcion-eventos",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InscripcionEvento> createInscripcionEvento(@RequestBody InscripcionEvento inscripcionEvento) throws URISyntaxException {
        log.debug("REST request to save InscripcionEvento : {}", inscripcionEvento);
        if (inscripcionEvento.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("inscripcionEvento", "idexists", "A new inscripcionEvento cannot already have an ID")).body(null);
        }
        InscripcionEvento result = inscripcionEventoRepository.save(inscripcionEvento);
        return ResponseEntity.created(new URI("/api/inscripcion-eventos/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("inscripcionEvento", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /inscripcion-eventos : Updates an existing inscripcionEvento.
     *
     * @param inscripcionEvento the inscripcionEvento to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated inscripcionEvento,
     * or with status 400 (Bad Request) if the inscripcionEvento is not valid,
     * or with status 500 (Internal Server Error) if the inscripcionEvento couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/inscripcion-eventos",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InscripcionEvento> updateInscripcionEvento(@RequestBody InscripcionEvento inscripcionEvento) throws URISyntaxException {
        log.debug("REST request to update InscripcionEvento : {}", inscripcionEvento);
        if (inscripcionEvento.getId() == null) {
            return createInscripcionEvento(inscripcionEvento);
        }
        InscripcionEvento result = inscripcionEventoRepository.save(inscripcionEvento);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("inscripcionEvento", inscripcionEvento.getId().toString()))
            .body(result);
    }

    /**
     * GET  /inscripcion-eventos : get all the inscripcionEventos.
     *
     * @return the ResponseEntity with status 200 (OK) and the list of inscripcionEventos in body
     */
    @RequestMapping(value = "/inscripcion-eventos",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<InscripcionEvento> getAllInscripcionEventos() {
        log.debug("REST request to get all InscripcionEventos");
        List<InscripcionEvento> inscripcionEventos = inscripcionEventoRepository.findAll();
        return inscripcionEventos;
    }

    /**
     * GET  /inscripcion-eventos/:id : get the "id" inscripcionEvento.
     *
     * @param id the id of the inscripcionEvento to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the inscripcionEvento, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/inscripcion-eventos/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<InscripcionEvento> getInscripcionEvento(@PathVariable Long id) {
        log.debug("REST request to get InscripcionEvento : {}", id);
        InscripcionEvento inscripcionEvento = inscripcionEventoRepository.findOne(id);
        return Optional.ofNullable(inscripcionEvento)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /inscripcion-eventos/:id : delete the "id" inscripcionEvento.
     *
     * @param id the id of the inscripcionEvento to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/inscripcion-eventos/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteInscripcionEvento(@PathVariable Long id) {
        log.debug("REST request to delete InscripcionEvento : {}", id);
        inscripcionEventoRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("inscripcionEvento", id.toString())).build();
    }

}
