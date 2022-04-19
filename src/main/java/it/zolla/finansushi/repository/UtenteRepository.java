package it.zolla.finansushi.repository;

import it.zolla.finansushi.domain.Utente;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Utente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {}
