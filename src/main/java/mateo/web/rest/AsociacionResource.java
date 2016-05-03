package mateo.web.rest;

import com.codahale.metrics.annotation.Timed;
import mateo.domain.Asociacion;
import mateo.domain.User;
import mateo.repository.AsociacionRepository;
import mateo.repository.UserRepository;
import mateo.security.AuthoritiesConstants;
import mateo.security.SecurityUtils;
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
 * REST controller for managing Asociacion.
 */
@RestController
@RequestMapping("/api")
public class AsociacionResource {

    private final Logger log = LoggerFactory.getLogger(AsociacionResource.class);

    @Inject
    private AsociacionRepository asociacionRepository;

    @Inject

    private UserRepository userRepository;

    /**
     * POST  /asociacions : Create a new asociacion.
     *
     * @param asociacion the asociacion to create
     * @return the ResponseEntity with status 201 (Created) and with body the new asociacion, or with status 400 (Bad Request) if the asociacion has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/asociacions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Asociacion> createAsociacion(@RequestBody Asociacion asociacion) throws URISyntaxException {
        log.debug("REST request to save Asociacion : {}", asociacion);
        if (asociacion.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asociacion", "idexists", "A new asociacion cannot already have an ID")).body(null);
        }
        Asociacion result;
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.USER)){

            User user = userRepository.findOneByLogin(SecurityUtils.getCurrentUserLogin()).get();
            asociacion.setUser(user);
            result = asociacionRepository.save(asociacion);

        }
        else{

            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asociacion", "incorrect", "inorrect")).body(null);

        }


        return ResponseEntity.created(new URI("/api/asociacions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("asociacion", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /asociacions : Updates an existing asociacion.
     *
     * @param asociacion the asociacion to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated asociacion,
     * or with status 400 (Bad Request) if the asociacion is not valid,
     * or with status 500 (Internal Server Error) if the asociacion couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/asociacions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Asociacion> updateAsociacion(@RequestBody Asociacion asociacion) throws URISyntaxException {
        log.debug("REST request to update Asociacion : {}", asociacion);
        if (asociacion.getId() == null) {
            return createAsociacion(asociacion);
        }
        Asociacion result;
        if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.USER)){
            result = asociacionRepository.save(asociacion);

        }
        else{

            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("asociacion", "incorrect", "inorrect")).body(null);

        }


        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("asociacion", asociacion.getId().toString()))
            .body(result);
    }

    /**
     * GET  /asociacions : get all the asociacions.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of asociacions in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/asociacions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Asociacion>> getAllAsociacions(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Asociacions");
        Page<Asociacion> page = asociacionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/asociacions");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /asociacions/:id : get the "id" asociacion.
     *
     * @param id the id of the asociacion to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the asociacion, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/asociacions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Asociacion> getAsociacion(@PathVariable Long id) {
        log.debug("REST request to get Asociacion : {}", id);
        Asociacion asociacion = asociacionRepository.findOne(id);
        return Optional.ofNullable(asociacion)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /asociacions/:id : delete the "id" asociacion.
     *
     * @param id the id of the asociacion to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/asociacions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteAsociacion(@PathVariable Long id) {
        log.debug("REST request to delete Asociacion : {}", id);
        asociacionRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("asociacion", id.toString())).build();
    }

}
