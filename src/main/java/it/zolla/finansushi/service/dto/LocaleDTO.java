package it.zolla.finansushi.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link it.zolla.finansushi.domain.Locale} entity.
 */
public class LocaleDTO implements Serializable {

    private Long id;

    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocaleDTO)) {
            return false;
        }

        LocaleDTO localeDTO = (LocaleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, localeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocaleDTO{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
