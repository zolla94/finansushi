package it.zolla.finansushi.service.mapper;

import it.zolla.finansushi.domain.Locale;
import it.zolla.finansushi.service.dto.LocaleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Locale} and its DTO {@link LocaleDTO}.
 */
@Mapper(componentModel = "spring")
public interface LocaleMapper extends EntityMapper<LocaleDTO, Locale> {}
