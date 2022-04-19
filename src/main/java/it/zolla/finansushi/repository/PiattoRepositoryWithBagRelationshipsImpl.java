package it.zolla.finansushi.repository;

import it.zolla.finansushi.domain.Piatto;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.hibernate.annotations.QueryHints;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class PiattoRepositoryWithBagRelationshipsImpl implements PiattoRepositoryWithBagRelationships {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<Piatto> fetchBagRelationships(Optional<Piatto> piatto) {
        return piatto.map(this::fetchOrdines).map(this::fetchStoricoOrdinis);
    }

    @Override
    public Page<Piatto> fetchBagRelationships(Page<Piatto> piattos) {
        return new PageImpl<>(fetchBagRelationships(piattos.getContent()), piattos.getPageable(), piattos.getTotalElements());
    }

    @Override
    public List<Piatto> fetchBagRelationships(List<Piatto> piattos) {
        return Optional.of(piattos).map(this::fetchOrdines).map(this::fetchStoricoOrdinis).orElse(Collections.emptyList());
    }

    Piatto fetchOrdines(Piatto result) {
        return entityManager
            .createQuery("select piatto from Piatto piatto left join fetch piatto.ordines where piatto is :piatto", Piatto.class)
            .setParameter("piatto", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Piatto> fetchOrdines(List<Piatto> piattos) {
        return entityManager
            .createQuery("select distinct piatto from Piatto piatto left join fetch piatto.ordines where piatto in :piattos", Piatto.class)
            .setParameter("piattos", piattos)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }

    Piatto fetchStoricoOrdinis(Piatto result) {
        return entityManager
            .createQuery("select piatto from Piatto piatto left join fetch piatto.storicoOrdinis where piatto is :piatto", Piatto.class)
            .setParameter("piatto", result)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getSingleResult();
    }

    List<Piatto> fetchStoricoOrdinis(List<Piatto> piattos) {
        return entityManager
            .createQuery(
                "select distinct piatto from Piatto piatto left join fetch piatto.storicoOrdinis where piatto in :piattos",
                Piatto.class
            )
            .setParameter("piattos", piattos)
            .setHint(QueryHints.PASS_DISTINCT_THROUGH, false)
            .getResultList();
    }
}
