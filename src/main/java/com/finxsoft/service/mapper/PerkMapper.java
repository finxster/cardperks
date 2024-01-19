package com.finxsoft.service.mapper;

import com.finxsoft.domain.Card;
import com.finxsoft.domain.Perk;
import com.finxsoft.service.dto.CardDTO;
import com.finxsoft.service.dto.PerkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Perk} and its DTO {@link PerkDTO}.
 */
@Mapper(componentModel = "spring")
public interface PerkMapper extends EntityMapper<PerkDTO, Perk> {
    @Mapping(target = "card", source = "card", qualifiedByName = "cardId")
    PerkDTO toDto(Perk s);

    @Named("cardId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    CardDTO toDtoCardId(Card card);
}
