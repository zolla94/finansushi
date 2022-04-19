package it.zolla.finansushi.service.mapper;

import it.zolla.finansushi.domain.Locale;
import it.zolla.finansushi.domain.StoricoOrdini;
import it.zolla.finansushi.domain.Utente;
import it.zolla.finansushi.service.dto.LocaleDTO;
import it.zolla.finansushi.service.dto.StoricoOrdiniDTO;
import it.zolla.finansushi.service.dto.UtenteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link StoricoOrdini} and its DTO {@link StoricoOrdiniDTO}.
 */
@Mapper(componentModel = "spring")
public interface StoricoOrdiniMapper extends EntityMapper<StoricoOrdiniDTO, StoricoOrdini> {
    @Mapping(target = "utente", source = "utente", qualifiedByName = "utenteId")
    @Mapping(target = "locale", source = "locale", qualifiedByName = "localeId")
    StoricoOrdiniDTO toDto(StoricoOrdini s);

    @Named("utenteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtenteDTO toDtoUtenteId(Utente utente);

    @Named("localeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocaleDTO toDtoLocaleId(Locale locale);
}
