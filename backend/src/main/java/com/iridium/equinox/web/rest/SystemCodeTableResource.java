package com.iridium.equinox.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.iridium.equinox.domain.SystemCodeTable;

import com.iridium.equinox.repository.SystemCodeTableRepository;
import com.iridium.equinox.repository.search.SystemCodeTableSearchRepository;
import com.iridium.equinox.web.rest.util.HeaderUtil;
import com.iridium.equinox.web.rest.util.PaginationUtil;
import io.swagger.annotations.ApiParam;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing SystemCodeTable.
 */
@RestController
@RequestMapping("/api")
public class SystemCodeTableResource {

    private final Logger log = LoggerFactory.getLogger(SystemCodeTableResource.class);

    private static final String ENTITY_NAME = "systemCodeTable";
        
    private final SystemCodeTableRepository systemCodeTableRepository;

    private final SystemCodeTableSearchRepository systemCodeTableSearchRepository;

    public SystemCodeTableResource(SystemCodeTableRepository systemCodeTableRepository, SystemCodeTableSearchRepository systemCodeTableSearchRepository) {
        this.systemCodeTableRepository = systemCodeTableRepository;
        this.systemCodeTableSearchRepository = systemCodeTableSearchRepository;
    }

    /**
     * POST  /system-code-tables : Create a new systemCodeTable.
     *
     * @param systemCodeTable the systemCodeTable to create
     * @return the ResponseEntity with status 201 (Created) and with body the new systemCodeTable, or with status 400 (Bad Request) if the systemCodeTable has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PostMapping("/system-code-tables")
    @Timed
    public ResponseEntity<SystemCodeTable> createSystemCodeTable(@Valid @RequestBody SystemCodeTable systemCodeTable) throws URISyntaxException {
        log.debug("REST request to save SystemCodeTable : {}", systemCodeTable);
        if (systemCodeTable.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert(ENTITY_NAME, "idexists", "A new systemCodeTable cannot already have an ID")).body(null);
        }
        SystemCodeTable result = systemCodeTableRepository.save(systemCodeTable);
        systemCodeTableSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/system-code-tables/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /system-code-tables : Updates an existing systemCodeTable.
     *
     * @param systemCodeTable the systemCodeTable to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated systemCodeTable,
     * or with status 400 (Bad Request) if the systemCodeTable is not valid,
     * or with status 500 (Internal Server Error) if the systemCodeTable couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @PutMapping("/system-code-tables")
    @Timed
    public ResponseEntity<SystemCodeTable> updateSystemCodeTable(@Valid @RequestBody SystemCodeTable systemCodeTable) throws URISyntaxException {
        log.debug("REST request to update SystemCodeTable : {}", systemCodeTable);
        if (systemCodeTable.getId() == null) {
            return createSystemCodeTable(systemCodeTable);
        }
        SystemCodeTable result = systemCodeTableRepository.save(systemCodeTable);
        systemCodeTableSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(ENTITY_NAME, systemCodeTable.getId().toString()))
            .body(result);
    }

    /**
     * GET  /system-code-tables : get all the systemCodeTables.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of systemCodeTables in body
     */
    @GetMapping("/system-code-tables")
    @Timed
    public ResponseEntity<List<SystemCodeTable>> getAllSystemCodeTables(@ApiParam Pageable pageable) {
        log.debug("REST request to get a page of SystemCodeTables");
        Page<SystemCodeTable> page = systemCodeTableRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/system-code-tables");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /system-code-tables/:id : get the "id" systemCodeTable.
     *
     * @param id the id of the systemCodeTable to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the systemCodeTable, or with status 404 (Not Found)
     */
    @GetMapping("/system-code-tables/{id}")
    @Timed
    public ResponseEntity<SystemCodeTable> getSystemCodeTable(@PathVariable Long id) {
        log.debug("REST request to get SystemCodeTable : {}", id);
        SystemCodeTable systemCodeTable = systemCodeTableRepository.findOne(id);
        return ResponseUtil.wrapOrNotFound(Optional.ofNullable(systemCodeTable));
    }

    /**
     * DELETE  /system-code-tables/:id : delete the "id" systemCodeTable.
     *
     * @param id the id of the systemCodeTable to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @DeleteMapping("/system-code-tables/{id}")
    @Timed
    public ResponseEntity<Void> deleteSystemCodeTable(@PathVariable Long id) {
        log.debug("REST request to delete SystemCodeTable : {}", id);
        systemCodeTableRepository.delete(id);
        systemCodeTableSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert(ENTITY_NAME, id.toString())).build();
    }

    /**
     * SEARCH  /_search/system-code-tables?query=:query : search for the systemCodeTable corresponding
     * to the query.
     *
     * @param query the query of the systemCodeTable search 
     * @param pageable the pagination information
     * @return the result of the search
     */
    @GetMapping("/_search/system-code-tables")
    @Timed
    public ResponseEntity<List<SystemCodeTable>> searchSystemCodeTables(@RequestParam String query, @ApiParam Pageable pageable) {
        log.debug("REST request to search for a page of SystemCodeTables for query {}", query);
        Page<SystemCodeTable> page = systemCodeTableSearchRepository.search(queryStringQuery(query), pageable);
        HttpHeaders headers = PaginationUtil.generateSearchPaginationHttpHeaders(query, page, "/api/_search/system-code-tables");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }


}
