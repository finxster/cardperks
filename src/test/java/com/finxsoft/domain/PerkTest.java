package com.finxsoft.domain;

import static com.finxsoft.domain.CardTestSamples.*;
import static com.finxsoft.domain.PerkTestSamples.*;
import static com.finxsoft.domain.StoreTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.finxsoft.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class PerkTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Perk.class);
        Perk perk1 = getPerkSample1();
        Perk perk2 = new Perk();
        assertThat(perk1).isNotEqualTo(perk2);

        perk2.setId(perk1.getId());
        assertThat(perk1).isEqualTo(perk2);

        perk2 = getPerkSample2();
        assertThat(perk1).isNotEqualTo(perk2);
    }

    @Test
    void storeTest() throws Exception {
        Perk perk = getPerkRandomSampleGenerator();
        Store storeBack = getStoreRandomSampleGenerator();

        perk.addStore(storeBack);
        assertThat(perk.getStores()).containsOnly(storeBack);
        assertThat(storeBack.getPerk()).isEqualTo(perk);

        perk.removeStore(storeBack);
        assertThat(perk.getStores()).doesNotContain(storeBack);
        assertThat(storeBack.getPerk()).isNull();

        perk.stores(new HashSet<>(Set.of(storeBack)));
        assertThat(perk.getStores()).containsOnly(storeBack);
        assertThat(storeBack.getPerk()).isEqualTo(perk);

        perk.setStores(new HashSet<>());
        assertThat(perk.getStores()).doesNotContain(storeBack);
        assertThat(storeBack.getPerk()).isNull();
    }

    @Test
    void cardTest() throws Exception {
        Perk perk = getPerkRandomSampleGenerator();
        Card cardBack = getCardRandomSampleGenerator();

        perk.setCard(cardBack);
        assertThat(perk.getCard()).isEqualTo(cardBack);

        perk.card(null);
        assertThat(perk.getCard()).isNull();
    }
}
