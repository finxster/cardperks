package com.finxsoft.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.finxsoft.domain.Store} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class StoreDTO implements Serializable {

    private Long id;

    private String name;

    private PerkDTO perk;
    private String perkName;

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

    public PerkDTO getPerk() {
        return perk;
    }

    public void setPerk(PerkDTO perk) {
        this.perk = perk;
    }

    public String getPerkName() {
        return perkName;
    }

    public void setPerkName(String perkName) {
        this.perkName = perkName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StoreDTO)) {
            return false;
        }

        StoreDTO storeDTO = (StoreDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, storeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoreDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", perk=" + perk +
            ", perkName='" + perkName + '\'' +
            '}';
    }
}
