package com.finxsoft.service.mapper;

import org.junit.jupiter.api.BeforeEach;

class CardMapperTest {

    private CardMapper cardMapper;

    @BeforeEach
    public void setUp() {
        cardMapper = new CardMapperImpl();
    }
}
