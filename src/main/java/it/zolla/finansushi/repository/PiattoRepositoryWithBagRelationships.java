package it.zolla.finansushi.repository;

import it.zolla.finansushi.domain.Piatto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface PiattoRepositoryWithBagRelationships {
    Optional<Piatto> fetchBagRelationships(Optional<Piatto> piatto);

    List<Piatto> fetchBagRelationships(List<Piatto> piattos);

    Page<Piatto> fetchBagRelationships(Page<Piatto> piattos);
}
