package it.zolla.finansushi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Utente.
 */
@Entity
@Table(name = "utenti")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Utente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "utente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "piatto", "locale", "utente" }, allowSetters = true)
    private Set<Ordine> ordines = new HashSet<>();

    @OneToMany(mappedBy = "utente")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "piatto", "utente", "locale" }, allowSetters = true)
    private Set<StoricoOrdini> storicoOrdinis = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Utente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Ordine> getOrdines() {
        return this.ordines;
    }

    public void setOrdines(Set<Ordine> ordines) {
        if (this.ordines != null) {
            this.ordines.forEach(i -> i.setUtente(null));
        }
        if (ordines != null) {
            ordines.forEach(i -> i.setUtente(this));
        }
        this.ordines = ordines;
    }

    public Utente ordines(Set<Ordine> ordines) {
        this.setOrdines(ordines);
        return this;
    }

    public Utente addOrdine(Ordine ordine) {
        this.ordines.add(ordine);
        ordine.setUtente(this);
        return this;
    }

    public Utente removeOrdine(Ordine ordine) {
        this.ordines.remove(ordine);
        ordine.setUtente(null);
        return this;
    }

    public Set<StoricoOrdini> getStoricoOrdinis() {
        return this.storicoOrdinis;
    }

    public void setStoricoOrdinis(Set<StoricoOrdini> storicoOrdinis) {
        if (this.storicoOrdinis != null) {
            this.storicoOrdinis.forEach(i -> i.setUtente(null));
        }
        if (storicoOrdinis != null) {
            storicoOrdinis.forEach(i -> i.setUtente(this));
        }
        this.storicoOrdinis = storicoOrdinis;
    }

    public Utente storicoOrdinis(Set<StoricoOrdini> storicoOrdinis) {
        this.setStoricoOrdinis(storicoOrdinis);
        return this;
    }

    public Utente addStoricoOrdini(StoricoOrdini storicoOrdini) {
        this.storicoOrdinis.add(storicoOrdini);
        storicoOrdini.setUtente(this);
        return this;
    }

    public Utente removeStoricoOrdini(StoricoOrdini storicoOrdini) {
        this.storicoOrdinis.remove(storicoOrdini);
        storicoOrdini.setUtente(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Utente)) {
            return false;
        }
        return id != null && id.equals(((Utente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Utente{" +
            "id=" + getId() +
            "}";
    }
}
