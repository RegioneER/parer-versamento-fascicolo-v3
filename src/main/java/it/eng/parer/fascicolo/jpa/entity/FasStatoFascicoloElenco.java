package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoFascicoloElenco.TiStatoFascElenco;
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
 * The persistent class for the FAS_STATO_FASCICOLO_ELENCO database table.
 *
 */
@Entity
@Table(name = "FAS_STATO_FASCICOLO_ELENCO")
@NamedQuery(name = "FasStatoFascicoloElenco.findAll", query = "SELECT e FROM FasStatoFascicoloElenco e")
@NamedQuery(name = "FasStatoFascicoloElenco.findByFasFascicolo", query = "SELECT e FROM FasStatoFascicoloElenco e WHERE e.fasFascicolo = :fasFascicolo")
public class FasStatoFascicoloElenco implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idStatoFascicoloElenco;
    private IamUser iamUser;
    private TiStatoFascElenco tiStatoFascElencoVers;
    private LocalDateTime tsStato;
    private FasFascicolo fasFascicolo;

    public FasStatoFascicoloElenco() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_STATO_FASCICOLO_ELENCO_IDSTATOFASCICOLOELENCO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_STATO_FASCICOLO_ELENCO"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_STATO_FASCICOLO_ELENCO_IDSTATOFASCICOLOELENCO_GENERATOR")
    @Column(name = "ID_STATO_FASCICOLO_ELENCO")
    public Long getIdStatoFascicoloElenco() {
        return this.idStatoFascicoloElenco;
    }

    public void setIdStatoFascicoloElenco(Long idStatoFascicoloElenco) {
        this.idStatoFascicoloElenco = idStatoFascicoloElenco;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TI_STATO_FASC_ELENCO_VERS")
    public TiStatoFascElenco getTiStatoFascElencoVers() {
        return this.tiStatoFascElencoVers;
    }

    public void setTiStatoFascElencoVers(TiStatoFascElenco tiStatoFascElencoVers) {
        this.tiStatoFascElencoVers = tiStatoFascElencoVers;
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

    @Column(name = "TS_STATO")
    public LocalDateTime getTsStato() {
        return this.tsStato;
    }

    public void setTsStato(LocalDateTime tsStato) {
        this.tsStato = tsStato;
    }

    // bi-directional many-to-one association to FasFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FASCICOLO")
    public FasFascicolo getFasFascicolo() {
        return this.fasFascicolo;
    }

    public void setFasFascicolo(FasFascicolo fasFascicolo) {
        this.fasFascicolo = fasFascicolo;
    }
}