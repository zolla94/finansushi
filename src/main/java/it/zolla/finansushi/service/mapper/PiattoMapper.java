package it.zolla.finansushi.service.mapper;

import it.zolla.finansushi.domain.Locale;
import it.zolla.finansushi.domain.Ordine;
import it.zolla.finansushi.domain.Piatto;
import it.zolla.finansushi.domain.StoricoOrdini;
import it.zolla.finansushi.service.dto.LocaleDTO;
import it.zolla.finansushi.service.dto.OrdineDTO;
import it.zolla.finansushi.service.dto.PiattoDTO;
import it.zolla.finansushi.service.dto.StoricoOrdiniDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Piatto} and its DTO {@link PiattoDTO}.
 */
@Mapper(componentModel = "spring")
public interface PiattoMapper extends EntityMapper<PiattoDTO, Piatto> {
    @Mapping(target = "ordine", source = "ordine", qualifiedByName = "ordineId")
    @Mapping(target = "storicoOrdini", source = "storicoOrdini", qualifiedByName = "storicoOrdiniId")
    @Mapping(target = "locale", source = "locale", qualifiedByName = "localeId")
    PiattoDTO toDto(Piatto s);

    @Named("ordineId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    OrdineDTO toDtoOrdineId(Ordine ordine);

    @Named("storicoOrdiniId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    StoricoOrdiniDTO toDtoStoricoOrdiniId(StoricoOrdini storicoOrdini);

    @Named("localeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    LocaleDTO toDtoLocaleId(Locale locale);
}
