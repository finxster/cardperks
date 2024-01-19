package com.finxsoft.service.mapper;

import com.finxsoft.domain.Card;
import com.finxsoft.service.dto.CardDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Card} and its DTO {@link CardDTO}.
 */
@Mapper(componentModel = "spring")
public interface CardMapper extends EntityMapper<CardDTO, Card> {}
