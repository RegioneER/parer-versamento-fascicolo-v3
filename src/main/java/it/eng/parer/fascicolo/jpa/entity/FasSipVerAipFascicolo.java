package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequenceGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The persistent class for the FAS_SIP_VER_AIP_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_SIP_VER_AIP_FASCICOLO")
public class FasSipVerAipFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idSipVerAipFascicolo;
    private FasVerAipFascicolo fasVerAipFascicolo;
    private String nmSip;
    private String tiSip;
    private FasXmlVersFascicolo fasXmlVersFascicoloRich;
    private FasXmlVersFascicolo fasXmlVersFascicoloRisp;

    public FasSipVerAipFascicolo() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_SIP_VER_AIP_FASCICOLO_IDSIPVERAIPFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_SIP_VER_AIP_FASCICOLO"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_SIP_VER_AIP_FASCICOLO_IDSIPVERAIPFASCICOLO_GENERATOR")
    @Column(name = "ID_SIP_VER_AIP_FASCICOLO")
    public Long getIdSipVerAipFascicolo() {
        return this.idSipVerAipFascicolo;
    }

    public void setIdSipVerAipFascicolo(Long idSipVerAipFascicolo) {
        this.idSipVerAipFascicolo = idSipVerAipFascicolo;
    }

    // bi-directional many-to-one association to FasVerAipFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VER_AIP_FASCICOLO")
    public FasVerAipFascicolo getFasVerAipFascicolo() {
        return this.fasVerAipFascicolo;
    }

    public void setFasVerAipFascicolo(FasVerAipFascicolo fasVerAipFascicolo) {
        this.fasVerAipFascicolo = fasVerAipFascicolo;
    }

    @Column(name = "NM_SIP")
    public String getNmSip() {
        return this.nmSip;
    }

    public void setNmSip(String nmSip) {
        this.nmSip = nmSip;
    }

    @Column(name = "TI_SIP")
    public String getTiSip() {
        return this.tiSip;
    }

    public void setTiSip(String tiSip) {
        this.tiSip = tiSip;
    }

    // bi-directional many-to-one association to FasXmlVersFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_XML_VERS_FASCICOLO_RICH")
    public FasXmlVersFascicolo getFasXmlVersFascicoloRich() {
        return this.fasXmlVersFascicoloRich;
    }

    public void setFasXmlVersFascicoloRich(FasXmlVersFascicolo fasXmlVersFascicoloRich) {
        this.fasXmlVersFascicoloRich = fasXmlVersFascicoloRich;
    }

    // bi-directional many-to-one association to FasXmlVersFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_XML_VERS_FASCICOLO_RISP")
    public FasXmlVersFascicolo getFasXmlVersFascicoloRisp() {
        return this.fasXmlVersFascicoloRisp;
    }

    public void setFasXmlVersFascicoloRisp(FasXmlVersFascicolo fasXmlVersFascicoloRisp) {
        this.fasXmlVersFascicoloRisp = fasXmlVersFascicoloRisp;
    }
}
