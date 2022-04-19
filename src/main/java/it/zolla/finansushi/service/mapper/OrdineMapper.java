package it.zolla.finansushi.service.mapper;

import it.zolla.finansushi.domain.Locale;
import it.zolla.finansushi.domain.Ordine;
import it.zolla.finansushi.domain.Utente;
import it.zolla.finansushi.service.dto.LocaleDTO;
import it.zolla.finansushi.service.dto.OrdineDTO;
import it.zolla.finansushi.service.dto.UtenteDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Ordine} and its DTO {@link OrdineDTO}.
 */
@Mapper(componentModel = "spring")
public interface OrdineMapper extends EntityMapper<OrdineDTO, Ordine> {
    @Mapping(target = "locale", source = "locale", qualifiedByName = "localeId")
    @Mapping(target = "utente", source = "utente", qualifiedByName = "utenteId")
    OrdineDTO toDto(Ordine s);

    @Named("localeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocaleDTO toDtoLocaleId(Locale locale);

    @Named("utenteId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    UtenteDTO toDtoUtenteId(Utente utente);
}
