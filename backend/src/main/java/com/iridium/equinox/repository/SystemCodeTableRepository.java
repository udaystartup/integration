package com.iridium.equinox.repository;

import com.iridium.equinox.domain.SystemCodeTable;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SystemCodeTable entity.
 */
@SuppressWarnings("unused")
public interface SystemCodeTableRepository extends JpaRepository<SystemCodeTable,Long> {

}
