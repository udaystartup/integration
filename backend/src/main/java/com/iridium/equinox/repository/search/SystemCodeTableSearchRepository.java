package com.iridium.equinox.repository.search;

import com.iridium.equinox.domain.SystemCodeTable;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data Elasticsearch repository for the SystemCodeTable entity.
 */
public interface SystemCodeTableSearchRepository extends ElasticsearchRepository<SystemCodeTable, Long> {
}
