package it.zolla.finansushi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StoricoOrdini.
 */
@Entity
@Table(name = "storico_ordini")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StoricoOrdini implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "note")
    private String note;

    @JsonIgnoreProperties(value = { "ordine", "storicoOrdini", "locale" }, allowSetters = true)
    @OneToOne(mappedBy = "storicoOrdini")
    private Piatto piatto;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ordines", "storicoOrdinis" }, allowSetters = true)
    private Utente utente;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ordines", "storicoOrdinis", "piattos" }, allowSetters = true)
    private Locale locale;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StoricoOrdini id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNote() {
        return this.note;
    }

    public StoricoOrdini note(String note) {
        this.setNote(note);
        return this;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Piatto getPiatto() {
        return this.piatto;
    }

    public void setPiatto(Piatto piatto) {
        if (this.piatto != null) {
            this.piatto.setStoricoOrdini(null);
        }
        if (piatto != null) {
            piatto.setStoricoOrdini(this);
        }
        this.piatto = piatto;
    }

    public StoricoOrdini piatto(Piatto piatto) {
        this.setPiatto(piatto);
        return this;
    }

    public Utente getUtente() {
        return this.utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }

    public StoricoOrdini utente(Utente utente) {
        this.setUtente(utente);
        return this;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public StoricoOrdini locale(Locale locale) {
        this.setLocale(locale);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StoricoOrdini)) {
            return false;
        }
        return id != null && id.equals(((StoricoOrdini) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StoricoOrdini{" +
            "id=" + getId() +
            ", note='" + getNote() + "'" +
            "}";
    }
}
