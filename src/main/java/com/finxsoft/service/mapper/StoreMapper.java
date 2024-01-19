package com.finxsoft.service.mapper;

import com.finxsoft.domain.Perk;
import com.finxsoft.domain.Store;
import com.finxsoft.service.dto.PerkDTO;
import com.finxsoft.service.dto.StoreDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Store} and its DTO {@link StoreDTO}.
 */
@Mapper(componentModel = "spring")
public interface StoreMapper extends EntityMapper<StoreDTO, Store> {
    @Mapping(target = "perk", source = "perk", qualifiedByName = "perkId")
    StoreDTO toDto(Store s);

    @Named("perkId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    @Mapping(target = "name", source = "name")
    PerkDTO toDtoPerkId(Perk perk);
}
