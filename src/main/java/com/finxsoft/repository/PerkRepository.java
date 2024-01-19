package com.finxsoft.repository;

import com.finxsoft.domain.Perk;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Perk entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PerkRepository extends JpaRepository<Perk, Long> {}
