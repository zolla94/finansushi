package it.zolla.finansushi.repository;

import it.zolla.finansushi.domain.Piatto;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Piatto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PiattoRepository extends JpaRepository<Piatto, Long> {}
