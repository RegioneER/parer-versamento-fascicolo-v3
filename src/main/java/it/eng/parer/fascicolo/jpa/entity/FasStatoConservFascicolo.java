package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoConservFascicolo.TiStatoConservazione;
import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequenceGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the FAS_STATO_CONSERV_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_STATO_CONSERV_FASCICOLO")
@NamedQuery(name = "FasStatoConservFascicolo.findAll", query = "SELECT e FROM FasStatoConservFascicolo e")
public class FasStatoConservFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idStatoConservFascicolo;
    private FasFascicolo fasFascicolo;
    private IamUser iamUser;
    private TiStatoConservazione tiStatoConservazione;
    private LocalDateTime tsStato;

    public FasStatoConservFascicolo() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_STATO_CONSERV_FASCICOLO_IDSTATOCONSERVFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_STATO_CONSERV_FASCICOLO"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_STATO_CONSERV_FASCICOLO_IDSTATOCONSERVFASCICOLO_GENERATOR")
    @Column(name = "ID_STATO_CONSERV_FASCICOLO")
    public Long getIdStatoConservFascicolo() {
        return this.idStatoConservFascicolo;
    }

    public void setIdStatoConservFascicolo(Long idStatoConservFascicolo) {
        this.idStatoConservFascicolo = idStatoConservFascicolo;
    }

    // bi-directional many-to-one association to IamUser
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FASCICOLO")
    public FasFascicolo getFasFascicolo() {
        return this.fasFascicolo;
    }

    public void setFasFascicolo(FasFascicolo fasFascicolo) {
        this.fasFascicolo = fasFascicolo;
    }

    // bi-directional many-to-one association to IamUser
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USER_IAM")
    public IamUser getIamUser() {
        return this.iamUser;
    }

    public void setIamUser(IamUser iamUser) {
        this.iamUser = iamUser;
    }

    /* XML non serializable */
    @Enumerated(EnumType.STRING)
    @Column(name = "TI_STATO_CONSERVAZIONE")
    public TiStatoConservazione getTiStatoConservazione() {
        return this.tiStatoConservazione;
    }

    public void setTiStatoConservazione(TiStatoConservazione tiStatoConservazione) {
        this.tiStatoConservazione = tiStatoConservazione;
    }

    @Column(name = "TS_STATO")
    public LocalDateTime getTsStato() {
        return this.tsStato;
    }

    public void setTsStato(LocalDateTime tsStato) {
        this.tsStato = tsStato;
    }

}