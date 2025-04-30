package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.entity.constraint.FasCodIdeSog.TiCodSog;
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
 * The persistent class for the FAS_COD_IDE_SOG database table.
 *
 */
@Entity
@Table(name = "FAS_COD_IDE_SOG")
@NamedQuery(name = "FasCodIdeSog.find", query = "SELECT f FROM FasCodIdeSog f WHERE f.fasSogFascicolo.idSogFascicolo = :idSogFascicolo")
public class FasCodIdeSog implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idCodIdeSog;
    private TiCodSog tiCodSog;
    private String cdSog;
    private String nmCodSog;

    private FasSogFascicolo fasSogFascicolo;

    public FasCodIdeSog() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_COD_IDE_SOG_IDINDRIFSOG_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_COD_IDE_SOG"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_COD_IDE_SOG_IDINDRIFSOG_GENERATOR")
    @Column(name = "ID_COD_IDE_SOG")
    public Long getIdCodIdeSog() {
        return this.idCodIdeSog;
    }

    public void setIdCodIdeSog(Long idCodIdeSog) {
        this.idCodIdeSog = idCodIdeSog;
    }

    @Column(name = "CD_SOG")
    public String getCdSog() {
        return this.cdSog;
    }

    public void setCdSog(String cdSog) {
        this.cdSog = cdSog;
    }

    // bi-directional many-to-one association to FasFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SOG_FASCICOLO")
    public FasSogFascicolo getFasSogFascicolo() {
        return this.fasSogFascicolo;
    }

    public void setFasSogFascicolo(FasSogFascicolo fasSogFascicolo) {
        this.fasSogFascicolo = fasSogFascicolo;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TI_COD_SOG")
    public TiCodSog getTiCodSog() {
        return this.tiCodSog;
    }

    public void setTiCodSog(TiCodSog tiCodSog) {
        this.tiCodSog = tiCodSog;
    }

    @Column(name = "NM_COD_SOG")
    public String getNmCodSog() {
        return this.nmCodSog;
    }

    public void setNmCodSog(String nmCodSog) {
        this.nmCodSog = nmCodSog;
    }

}