package it.zolla.finansushi.repository;

import it.zolla.finansushi.domain.StoricoOrdini;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the StoricoOrdini entity.
 */
@SuppressWarnings("unused")
@Repository
public interface StoricoOrdiniRepository extends JpaRepository<StoricoOrdini, Long> {}
