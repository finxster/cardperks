package com.finxsoft.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.finxsoft.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PerkDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PerkDTO.class);
        PerkDTO perkDTO1 = new PerkDTO();
        perkDTO1.setId(1L);
        PerkDTO perkDTO2 = new PerkDTO();
        assertThat(perkDTO1).isNotEqualTo(perkDTO2);
        perkDTO2.setId(perkDTO1.getId());
        assertThat(perkDTO1).isEqualTo(perkDTO2);
        perkDTO2.setId(2L);
        assertThat(perkDTO1).isNotEqualTo(perkDTO2);
        perkDTO1.setId(null);
        assertThat(perkDTO1).isNotEqualTo(perkDTO2);
    }
}
