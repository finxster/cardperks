package com.finxsoft.repository;

import com.finxsoft.domain.Store;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Store entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {}
