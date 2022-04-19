package it.zolla.finansushi.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link it.zolla.finansushi.domain.StoricoOrdini} entity.
 */
public class StoricoOrdiniDTO implements Serializable {

    private Long id;

    private String note;

    private UtenteDTO utente;

    private LocaleDTO locale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public UtenteDTO getUtente() {
        return utente;
    }

    public void setUtente(UtenteDTO utente) {
        this.utente = utente;
    }

    public LocaleDTO getLocale() {
        return locale;
    }

    public void setLocale(LocaleDTO locale) {
        this.locale = locale;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StoricoOrdiniDTO)) {
            return false;
        }

        StoricoOrdiniDTO storicoOrdiniDTO = (StoricoOrdiniDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, storicoOrdiniDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoricoOrdiniDTO{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            ", utente=" + getUtente() +
            ", locale=" + getLocale() +
            "}";
    }
}
