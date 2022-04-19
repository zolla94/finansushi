package it.zolla.finansushi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Piatto.
 */
@Entity
@Table(name = "piatti")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Piatto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "codice")
    private String codice;

    @Column(name = "descrizione")
    private String descrizione;

    @Column(name = "url")
    private String url;

    @Column(name = "spicy")
    private Boolean spicy;

    @Column(name = "vegan")
    private Boolean vegan;

    @Column(name = "limite_ordine")
    private Boolean limiteOrdine;

    @JsonIgnoreProperties(value = { "piatto", "locale", "utente" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private Ordine ordine;

    @JsonIgnoreProperties(value = { "piatto", "utente", "locale" }, allowSetters = true)
    @OneToOne
    @JoinColumn(unique = true)
    private StoricoOrdini storicoOrdini;

    @ManyToOne
    @JsonIgnoreProperties(value = { "ordines", "storicoOrdinis", "piattos" }, allowSetters = true)
    private Locale locale;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Piatto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodice() {
        return this.codice;
    }

    public Piatto codice(String codice) {
        this.setCodice(codice);
        return this;
    }

    public void setCodice(String codice) {
        this.codice = codice;
    }

    public String getDescrizione() {
        return this.descrizione;
    }

    public Piatto descrizione(String descrizione) {
        this.setDescrizione(descrizione);
        return this;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getUrl() {
        return this.url;
    }

    public Piatto url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getSpicy() {
        return this.spicy;
    }

    public Piatto spicy(Boolean spicy) {
        this.setSpicy(spicy);
        return this;
    }

    public void setSpicy(Boolean spicy) {
        this.spicy = spicy;
    }

    public Boolean getVegan() {
        return this.vegan;
    }

    public Piatto vegan(Boolean vegan) {
        this.setVegan(vegan);
        return this;
    }

    public void setVegan(Boolean vegan) {
        this.vegan = vegan;
    }

    public Boolean getLimiteOrdine() {
        return this.limiteOrdine;
    }

    public Piatto limiteOrdine(Boolean limiteOrdine) {
        this.setLimiteOrdine(limiteOrdine);
        return this;
    }

    public void setLimiteOrdine(Boolean limiteOrdine) {
        this.limiteOrdine = limiteOrdine;
    }

    public Ordine getOrdine() {
        return this.ordine;
    }

    public void setOrdine(Ordine ordine) {
        this.ordine = ordine;
    }

    public Piatto ordine(Ordine ordine) {
        this.setOrdine(ordine);
        return this;
    }

    public StoricoOrdini getStoricoOrdini() {
        return this.storicoOrdini;
    }

    public void setStoricoOrdini(StoricoOrdini storicoOrdini) {
        this.storicoOrdini = storicoOrdini;
    }

    public Piatto storicoOrdini(StoricoOrdini storicoOrdini) {
        this.setStoricoOrdini(storicoOrdini);
        return this;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Piatto locale(Locale locale) {
        this.setLocale(locale);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Piatto)) {
            return false;
        }
        return id != null && id.equals(((Piatto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Piatto{" +
            "id=" + getId() +
            ", codice='" + getCodice() + "'" +
            ", descrizione='" + getDescrizione() + "'" +
            ", url='" + getUrl() + "'" +
            ", spicy='" + getSpicy() + "'" +
            ", vegan='" + getVegan() + "'" +
            ", limiteOrdine='" + getLimiteOrdine() + "'" +
            "}";
    }
}
