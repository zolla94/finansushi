package it.zolla.finansushi.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link it.zolla.finansushi.domain.Piatto} entity.
 */
public class PiattoDTO implements Serializable {

    private Long id;

    private String codice;

    private String descrizione;

    private String url;

    private Boolean spicy;

    private Boolean vegan;

    private Boolean limiteOrdine;

    private OrdineDTO ordine;

    private StoricoOrdiniDTO storicoOrdini;

    private LocaleDTO locale;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return codice;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getSpicy() {
        return spicy;
    }

    public void setSpicy(Boolean spicy) {
        this.spicy = spicy;
    }

    public Boolean getVegan() {
        return vegan;
    }

    public void setVegan(Boolean vegan) {
        this.vegan = vegan;
    }

    public Boolean getLimiteOrdine() {
        return limiteOrdine;
    }

    public void setLimiteOrdine(Boolean limiteOrdine) {
        this.limiteOrdine = limiteOrdine;
    }

    public OrdineDTO getOrdine() {
        return ordine;
    }

    public void setOrdine(OrdineDTO ordine) {
        this.ordine = ordine;
    }

    public StoricoOrdiniDTO getStoricoOrdini() {
        return storicoOrdini;
    }

    public void setStoricoOrdini(StoricoOrdiniDTO storicoOrdini) {
        this.storicoOrdini = storicoOrdini;
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
        if (!(o instanceof PiattoDTO)) {
            return false;
        }

        PiattoDTO piattoDTO = (PiattoDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, piattoDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PiattoDTO{" +
            "id=" + getId() +
            ", codice='" + getCodice() + "'" +
            ", descrizione='" + getDescrizione() + "'" +
            ", url='" + getUrl() + "'" +
            ", spicy='" + getSpicy() + "'" +
            ", vegan='" + getVegan() + "'" +
            ", limiteOrdine='" + getLimiteOrdine() + "'" +
            ", ordine=" + getOrdine() +
            ", storicoOrdini=" + getStoricoOrdini() +
            ", locale=" + getLocale() +
            "}";
    }
}
