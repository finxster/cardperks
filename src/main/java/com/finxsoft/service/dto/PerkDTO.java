package com.finxsoft.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.finxsoft.domain.Perk} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PerkDTO implements Serializable {

    private Long id;

    private String name;

    private String description;

    private Instant expirationDate;

    private Boolean active;

    private Boolean expired;

    private CardDTO card;

    private String cardName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(Instant expirationDate) {
        this.expirationDate = expirationDate;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }

    public CardDTO getCard() {
        return card;
    }

    public void setCard(CardDTO card) {
        this.card = card;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PerkDTO)) {
            return false;
        }

        PerkDTO perkDTO = (PerkDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, perkDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore

    @Override
    public String toString() {
        return "PerkDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", description='" + description + '\'' +
            ", expirationDate=" + expirationDate +
            ", active=" + active +
            ", expired=" + expired +
            ", card=" + card +
            ", cardName='" + cardName + '\'' +
            '}';
    }
}
