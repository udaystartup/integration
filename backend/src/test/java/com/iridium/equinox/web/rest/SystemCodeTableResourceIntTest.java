package com.iridium.equinox.web.rest;

import com.iridium.equinox.CoreengineApp;

import com.iridium.equinox.domain.SystemCodeTable;
import com.iridium.equinox.repository.SystemCodeTableRepository;
import com.iridium.equinox.repository.search.SystemCodeTableSearchRepository;
import com.iridium.equinox.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the SystemCodeTableResource REST controller.
 *
 * @see SystemCodeTableResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreengineApp.class)
public class SystemCodeTableResourceIntTest {

    private static final String DEFAULT_CODE_TABLE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CODE_TABLE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_TABLE_DESC = "AAAAAAAAAA";
    private static final String UPDATED_CODE_TABLE_DESC = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_TABLE_ENTRY = "AAAAAAAAAA";
    private static final String UPDATED_CODE_TABLE_ENTRY = "BBBBBBBBBB";

    private static final String DEFAULT_CODE_TABLE_ENTRY_DESC = "AAAAAAAAAA";
    private static final String UPDATED_CODE_TABLE_ENTRY_DESC = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ACTIVE_INDICATOR = false;
    private static final Boolean UPDATED_ACTIVE_INDICATOR = true;

    private static final String DEFAULT_TENANT_ID = "AAAAAAAAAA";
    private static final String UPDATED_TENANT_ID = "BBBBBBBBBB";

    @Autowired
    private SystemCodeTableRepository systemCodeTableRepository;

    @Autowired
    private SystemCodeTableSearchRepository systemCodeTableSearchRepository;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    private MockMvc restSystemCodeTableMockMvc;

    private SystemCodeTable systemCodeTable;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SystemCodeTableResource systemCodeTableResource = new SystemCodeTableResource(systemCodeTableRepository, systemCodeTableSearchRepository);
        this.restSystemCodeTableMockMvc = MockMvcBuilders.standaloneSetup(systemCodeTableResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemCodeTable createEntity(EntityManager em) {
        SystemCodeTable systemCodeTable = new SystemCodeTable()
            .codeTableName(DEFAULT_CODE_TABLE_NAME)
            .codeTableDesc(DEFAULT_CODE_TABLE_DESC)
            .codeTableEntry(DEFAULT_CODE_TABLE_ENTRY)
            .codeTableEntryDesc(DEFAULT_CODE_TABLE_ENTRY_DESC)
            .activeIndicator(DEFAULT_ACTIVE_INDICATOR)
            .tenantID(DEFAULT_TENANT_ID);
        return systemCodeTable;
    }

    @Before
    public void initTest() {
        systemCodeTableSearchRepository.deleteAll();
        systemCodeTable = createEntity(em);
    }

    @Test
    @Transactional
    public void createSystemCodeTable() throws Exception {
        int databaseSizeBeforeCreate = systemCodeTableRepository.findAll().size();

        // Create the SystemCodeTable
        restSystemCodeTableMockMvc.perform(post("/api/system-code-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemCodeTable)))
            .andExpect(status().isCreated());

        // Validate the SystemCodeTable in the database
        List<SystemCodeTable> systemCodeTableList = systemCodeTableRepository.findAll();
        assertThat(systemCodeTableList).hasSize(databaseSizeBeforeCreate + 1);
        SystemCodeTable testSystemCodeTable = systemCodeTableList.get(systemCodeTableList.size() - 1);
        assertThat(testSystemCodeTable.getCodeTableName()).isEqualTo(DEFAULT_CODE_TABLE_NAME);
        assertThat(testSystemCodeTable.getCodeTableDesc()).isEqualTo(DEFAULT_CODE_TABLE_DESC);
        assertThat(testSystemCodeTable.getCodeTableEntry()).isEqualTo(DEFAULT_CODE_TABLE_ENTRY);
        assertThat(testSystemCodeTable.getCodeTableEntryDesc()).isEqualTo(DEFAULT_CODE_TABLE_ENTRY_DESC);
        assertThat(testSystemCodeTable.isActiveIndicator()).isEqualTo(DEFAULT_ACTIVE_INDICATOR);
        assertThat(testSystemCodeTable.getTenantID()).isEqualTo(DEFAULT_TENANT_ID);

        // Validate the SystemCodeTable in Elasticsearch
        SystemCodeTable systemCodeTableEs = systemCodeTableSearchRepository.findOne(testSystemCodeTable.getId());
        assertThat(systemCodeTableEs).isEqualToComparingFieldByField(testSystemCodeTable);
    }

    @Test
    @Transactional
    public void createSystemCodeTableWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = systemCodeTableRepository.findAll().size();

        // Create the SystemCodeTable with an existing ID
        systemCodeTable.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemCodeTableMockMvc.perform(post("/api/system-code-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemCodeTable)))
            .andExpect(status().isBadRequest());

        // Validate the Alice in the database
        List<SystemCodeTable> systemCodeTableList = systemCodeTableRepository.findAll();
        assertThat(systemCodeTableList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    public void checkCodeTableNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemCodeTableRepository.findAll().size();
        // set the field null
        systemCodeTable.setCodeTableName(null);

        // Create the SystemCodeTable, which fails.

        restSystemCodeTableMockMvc.perform(post("/api/system-code-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemCodeTable)))
            .andExpect(status().isBadRequest());

        List<SystemCodeTable> systemCodeTableList = systemCodeTableRepository.findAll();
        assertThat(systemCodeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeTableDescIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemCodeTableRepository.findAll().size();
        // set the field null
        systemCodeTable.setCodeTableDesc(null);

        // Create the SystemCodeTable, which fails.

        restSystemCodeTableMockMvc.perform(post("/api/system-code-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemCodeTable)))
            .andExpect(status().isBadRequest());

        List<SystemCodeTable> systemCodeTableList = systemCodeTableRepository.findAll();
        assertThat(systemCodeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeTableEntryIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemCodeTableRepository.findAll().size();
        // set the field null
        systemCodeTable.setCodeTableEntry(null);

        // Create the SystemCodeTable, which fails.

        restSystemCodeTableMockMvc.perform(post("/api/system-code-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemCodeTable)))
            .andExpect(status().isBadRequest());

        List<SystemCodeTable> systemCodeTableList = systemCodeTableRepository.findAll();
        assertThat(systemCodeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkCodeTableEntryDescIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemCodeTableRepository.findAll().size();
        // set the field null
        systemCodeTable.setCodeTableEntryDesc(null);

        // Create the SystemCodeTable, which fails.

        restSystemCodeTableMockMvc.perform(post("/api/system-code-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemCodeTable)))
            .andExpect(status().isBadRequest());

        List<SystemCodeTable> systemCodeTableList = systemCodeTableRepository.findAll();
        assertThat(systemCodeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkActiveIndicatorIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemCodeTableRepository.findAll().size();
        // set the field null
        systemCodeTable.setActiveIndicator(null);

        // Create the SystemCodeTable, which fails.

        restSystemCodeTableMockMvc.perform(post("/api/system-code-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemCodeTable)))
            .andExpect(status().isBadRequest());

        List<SystemCodeTable> systemCodeTableList = systemCodeTableRepository.findAll();
        assertThat(systemCodeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTenantIDIsRequired() throws Exception {
        int databaseSizeBeforeTest = systemCodeTableRepository.findAll().size();
        // set the field null
        systemCodeTable.setTenantID(null);

        // Create the SystemCodeTable, which fails.

        restSystemCodeTableMockMvc.perform(post("/api/system-code-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemCodeTable)))
            .andExpect(status().isBadRequest());

        List<SystemCodeTable> systemCodeTableList = systemCodeTableRepository.findAll();
        assertThat(systemCodeTableList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSystemCodeTables() throws Exception {
        // Initialize the database
        systemCodeTableRepository.saveAndFlush(systemCodeTable);

        // Get all the systemCodeTableList
        restSystemCodeTableMockMvc.perform(get("/api/system-code-tables?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemCodeTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeTableName").value(hasItem(DEFAULT_CODE_TABLE_NAME.toString())))
            .andExpect(jsonPath("$.[*].codeTableDesc").value(hasItem(DEFAULT_CODE_TABLE_DESC.toString())))
            .andExpect(jsonPath("$.[*].codeTableEntry").value(hasItem(DEFAULT_CODE_TABLE_ENTRY.toString())))
            .andExpect(jsonPath("$.[*].codeTableEntryDesc").value(hasItem(DEFAULT_CODE_TABLE_ENTRY_DESC.toString())))
            .andExpect(jsonPath("$.[*].activeIndicator").value(hasItem(DEFAULT_ACTIVE_INDICATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].tenantID").value(hasItem(DEFAULT_TENANT_ID.toString())));
    }

    @Test
    @Transactional
    public void getSystemCodeTable() throws Exception {
        // Initialize the database
        systemCodeTableRepository.saveAndFlush(systemCodeTable);

        // Get the systemCodeTable
        restSystemCodeTableMockMvc.perform(get("/api/system-code-tables/{id}", systemCodeTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(systemCodeTable.getId().intValue()))
            .andExpect(jsonPath("$.codeTableName").value(DEFAULT_CODE_TABLE_NAME.toString()))
            .andExpect(jsonPath("$.codeTableDesc").value(DEFAULT_CODE_TABLE_DESC.toString()))
            .andExpect(jsonPath("$.codeTableEntry").value(DEFAULT_CODE_TABLE_ENTRY.toString()))
            .andExpect(jsonPath("$.codeTableEntryDesc").value(DEFAULT_CODE_TABLE_ENTRY_DESC.toString()))
            .andExpect(jsonPath("$.activeIndicator").value(DEFAULT_ACTIVE_INDICATOR.booleanValue()))
            .andExpect(jsonPath("$.tenantID").value(DEFAULT_TENANT_ID.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSystemCodeTable() throws Exception {
        // Get the systemCodeTable
        restSystemCodeTableMockMvc.perform(get("/api/system-code-tables/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSystemCodeTable() throws Exception {
        // Initialize the database
        systemCodeTableRepository.saveAndFlush(systemCodeTable);
        systemCodeTableSearchRepository.save(systemCodeTable);
        int databaseSizeBeforeUpdate = systemCodeTableRepository.findAll().size();

        // Update the systemCodeTable
        SystemCodeTable updatedSystemCodeTable = systemCodeTableRepository.findOne(systemCodeTable.getId());
        updatedSystemCodeTable
            .codeTableName(UPDATED_CODE_TABLE_NAME)
            .codeTableDesc(UPDATED_CODE_TABLE_DESC)
            .codeTableEntry(UPDATED_CODE_TABLE_ENTRY)
            .codeTableEntryDesc(UPDATED_CODE_TABLE_ENTRY_DESC)
            .activeIndicator(UPDATED_ACTIVE_INDICATOR)
            .tenantID(UPDATED_TENANT_ID);

        restSystemCodeTableMockMvc.perform(put("/api/system-code-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(updatedSystemCodeTable)))
            .andExpect(status().isOk());

        // Validate the SystemCodeTable in the database
        List<SystemCodeTable> systemCodeTableList = systemCodeTableRepository.findAll();
        assertThat(systemCodeTableList).hasSize(databaseSizeBeforeUpdate);
        SystemCodeTable testSystemCodeTable = systemCodeTableList.get(systemCodeTableList.size() - 1);
        assertThat(testSystemCodeTable.getCodeTableName()).isEqualTo(UPDATED_CODE_TABLE_NAME);
        assertThat(testSystemCodeTable.getCodeTableDesc()).isEqualTo(UPDATED_CODE_TABLE_DESC);
        assertThat(testSystemCodeTable.getCodeTableEntry()).isEqualTo(UPDATED_CODE_TABLE_ENTRY);
        assertThat(testSystemCodeTable.getCodeTableEntryDesc()).isEqualTo(UPDATED_CODE_TABLE_ENTRY_DESC);
        assertThat(testSystemCodeTable.isActiveIndicator()).isEqualTo(UPDATED_ACTIVE_INDICATOR);
        assertThat(testSystemCodeTable.getTenantID()).isEqualTo(UPDATED_TENANT_ID);

        // Validate the SystemCodeTable in Elasticsearch
        SystemCodeTable systemCodeTableEs = systemCodeTableSearchRepository.findOne(testSystemCodeTable.getId());
        assertThat(systemCodeTableEs).isEqualToComparingFieldByField(testSystemCodeTable);
    }

    @Test
    @Transactional
    public void updateNonExistingSystemCodeTable() throws Exception {
        int databaseSizeBeforeUpdate = systemCodeTableRepository.findAll().size();

        // Create the SystemCodeTable

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restSystemCodeTableMockMvc.perform(put("/api/system-code-tables")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(systemCodeTable)))
            .andExpect(status().isCreated());

        // Validate the SystemCodeTable in the database
        List<SystemCodeTable> systemCodeTableList = systemCodeTableRepository.findAll();
        assertThat(systemCodeTableList).hasSize(databaseSizeBeforeUpdate + 1);
    }

    @Test
    @Transactional
    public void deleteSystemCodeTable() throws Exception {
        // Initialize the database
        systemCodeTableRepository.saveAndFlush(systemCodeTable);
        systemCodeTableSearchRepository.save(systemCodeTable);
        int databaseSizeBeforeDelete = systemCodeTableRepository.findAll().size();

        // Get the systemCodeTable
        restSystemCodeTableMockMvc.perform(delete("/api/system-code-tables/{id}", systemCodeTable.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate Elasticsearch is empty
        boolean systemCodeTableExistsInEs = systemCodeTableSearchRepository.exists(systemCodeTable.getId());
        assertThat(systemCodeTableExistsInEs).isFalse();

        // Validate the database is empty
        List<SystemCodeTable> systemCodeTableList = systemCodeTableRepository.findAll();
        assertThat(systemCodeTableList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void searchSystemCodeTable() throws Exception {
        // Initialize the database
        systemCodeTableRepository.saveAndFlush(systemCodeTable);
        systemCodeTableSearchRepository.save(systemCodeTable);

        // Search the systemCodeTable
        restSystemCodeTableMockMvc.perform(get("/api/_search/system-code-tables?query=id:" + systemCodeTable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemCodeTable.getId().intValue())))
            .andExpect(jsonPath("$.[*].codeTableName").value(hasItem(DEFAULT_CODE_TABLE_NAME.toString())))
            .andExpect(jsonPath("$.[*].codeTableDesc").value(hasItem(DEFAULT_CODE_TABLE_DESC.toString())))
            .andExpect(jsonPath("$.[*].codeTableEntry").value(hasItem(DEFAULT_CODE_TABLE_ENTRY.toString())))
            .andExpect(jsonPath("$.[*].codeTableEntryDesc").value(hasItem(DEFAULT_CODE_TABLE_ENTRY_DESC.toString())))
            .andExpect(jsonPath("$.[*].activeIndicator").value(hasItem(DEFAULT_ACTIVE_INDICATOR.booleanValue())))
            .andExpect(jsonPath("$.[*].tenantID").value(hasItem(DEFAULT_TENANT_ID.toString())));
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SystemCodeTable.class);
    }
}
