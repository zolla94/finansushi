package it.zolla.finansushi.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link it.zolla.finansushi.domain.Utente} entity.
 */
public class UtenteDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UtenteDTO)) {
            return false;
        }

        UtenteDTO utenteDTO = (UtenteDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, utenteDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UtenteDTO{" +
            "id=" + getId() +
            "}";
    }
}
