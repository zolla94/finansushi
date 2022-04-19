package it.zolla.finansushi.service.mapper;

import it.zolla.finansushi.domain.Utente;
import it.zolla.finansushi.service.dto.UtenteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Utente} and its DTO {@link UtenteDTO}.
 */
@Mapper(componentModel = "spring")
public interface UtenteMapper extends EntityMapper<UtenteDTO, Utente> {}
