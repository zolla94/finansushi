package it.zolla.finansushi.repository;

import it.zolla.finansushi.domain.Locale;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Locale entity.
 */
@SuppressWarnings("unused")
@Repository
public interface LocaleRepository extends JpaRepository<Locale, Long> {}
