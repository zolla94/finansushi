package it.zolla.finansushi.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Locale.
 */
@Entity
@Table(name = "locali")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Locale implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "nome")
    private String nome;

    @OneToMany(mappedBy = "locale")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "piatto", "locale", "utente" }, allowSetters = true)
    private Set<Ordine> ordines = new HashSet<>();

    @OneToMany(mappedBy = "locale")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "piatto", "utente", "locale" }, allowSetters = true)
    private Set<StoricoOrdini> storicoOrdinis = new HashSet<>();

    @OneToMany(mappedBy = "locale")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "ordine", "storicoOrdini", "locale" }, allowSetters = true)
    private Set<Piatto> piattos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Locale id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return this.nome;
    }

    public Locale nome(String nome) {
        this.setNome(nome);
        return this;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Set<Ordine> getOrdines() {
        return this.ordines;
    }

    public void setOrdines(Set<Ordine> ordines) {
        if (this.ordines != null) {
            this.ordines.forEach(i -> i.setLocale(null));
        }
        if (ordines != null) {
            ordines.forEach(i -> i.setLocale(this));
        }
        this.ordines = ordines;
    }

    public Locale ordines(Set<Ordine> ordines) {
        this.setOrdines(ordines);
        return this;
    }

    public Locale addOrdine(Ordine ordine) {
        this.ordines.add(ordine);
        ordine.setLocale(this);
        return this;
    }

    public Locale removeOrdine(Ordine ordine) {
        this.ordines.remove(ordine);
        ordine.setLocale(null);
        return this;
    }

    public Set<StoricoOrdini> getStoricoOrdinis() {
        return this.storicoOrdinis;
    }

    public void setStoricoOrdinis(Set<StoricoOrdini> storicoOrdinis) {
        if (this.storicoOrdinis != null) {
            this.storicoOrdinis.forEach(i -> i.setLocale(null));
        }
        if (storicoOrdinis != null) {
            storicoOrdinis.forEach(i -> i.setLocale(this));
        }
        this.storicoOrdinis = storicoOrdinis;
    }

    public Locale storicoOrdinis(Set<StoricoOrdini> storicoOrdinis) {
        this.setStoricoOrdinis(storicoOrdinis);
        return this;
    }

    public Locale addStoricoOrdini(StoricoOrdini storicoOrdini) {
        this.storicoOrdinis.add(storicoOrdini);
        storicoOrdini.setLocale(this);
        return this;
    }

    public Locale removeStoricoOrdini(StoricoOrdini storicoOrdini) {
        this.storicoOrdinis.remove(storicoOrdini);
        storicoOrdini.setLocale(null);
        return this;
    }

    public Set<Piatto> getPiattos() {
        return this.piattos;
    }

    public void setPiattos(Set<Piatto> piattos) {
        if (this.piattos != null) {
            this.piattos.forEach(i -> i.setLocale(null));
        }
        if (piattos != null) {
            piattos.forEach(i -> i.setLocale(this));
        }
        this.piattos = piattos;
    }

    public Locale piattos(Set<Piatto> piattos) {
        this.setPiattos(piattos);
        return this;
    }

    public Locale addPiatto(Piatto piatto) {
        this.piattos.add(piatto);
        piatto.setLocale(this);
        return this;
    }

    public Locale removePiatto(Piatto piatto) {
        this.piattos.remove(piatto);
        piatto.setLocale(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Locale)) {
            return false;
        }
        return id != null && id.equals(((Locale) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Locale{" +
            "id=" + getId() +
            ", nome='" + getNome() + "'" +
            "}";
    }
}
