package it.zolla.finansushi.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link it.zolla.finansushi.domain.Ordine} entity.
 */
public class OrdineDTO implements Serializable {

    private Long id;

    private LocaleDTO locale;

    private UtenteDTO utente;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocaleDTO getLocale() {
        return locale;
    }

    public void setLocale(LocaleDTO locale) {
        this.locale = locale;
    }

    public UtenteDTO getUtente() {
        return utente;
    }

    public void setUtente(UtenteDTO utente) {
        this.utente = utente;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrdineDTO)) {
            return false;
        }

        OrdineDTO ordineDTO = (OrdineDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, ordineDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "OrdineDTO{" +
            "id=" + getId() +
            ", locale=" + getLocale() +
            ", utente=" + getUtente() +
            "}";
    }
}
