package com.finxsoft.domain;

import static com.finxsoft.domain.CardTestSamples.*;
import static com.finxsoft.domain.PerkTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.finxsoft.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class CardTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Card.class);
        Card card1 = getCardSample1();
        Card card2 = new Card();
        assertThat(card1).isNotEqualTo(card2);

        card2.setId(card1.getId());
        assertThat(card1).isEqualTo(card2);

        card2 = getCardSample2();
        assertThat(card1).isNotEqualTo(card2);
    }

    @Test
    void perkTest() throws Exception {
        Card card = getCardRandomSampleGenerator();
        Perk perkBack = getPerkRandomSampleGenerator();

        card.addPerk(perkBack);
        assertThat(card.getPerks()).containsOnly(perkBack);
        assertThat(perkBack.getCard()).isEqualTo(card);

        card.removePerk(perkBack);
        assertThat(card.getPerks()).doesNotContain(perkBack);
        assertThat(perkBack.getCard()).isNull();

        card.perks(new HashSet<>(Set.of(perkBack)));
        assertThat(card.getPerks()).containsOnly(perkBack);
        assertThat(perkBack.getCard()).isEqualTo(card);

        card.setPerks(new HashSet<>());
        assertThat(card.getPerks()).doesNotContain(perkBack);
        assertThat(perkBack.getCard()).isNull();
    }
}
