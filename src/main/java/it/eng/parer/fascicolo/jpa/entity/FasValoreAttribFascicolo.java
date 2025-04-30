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
 * The persistent class for the FAS_VALORE_ATTRIB_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_VALORE_ATTRIB_FASCICOLO")
public class FasValoreAttribFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idValoreAttribFascicolo;
    private DecUsoModelloXsdFasc decUsoModelloXsdFasc;
    private DecAttribFascicolo decAttribFascicolo;
    private String dlValore;
    private DecAaTipoFascicolo decAaTipoFascicolo;
    private FasFascicolo fasFascicolo;

    public FasValoreAttribFascicolo() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_VALORE_ATTRIB_FASCICOLO_IDVALOREATTRIBFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_VALORE_ATTRIB_FASCICOLO"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_VALORE_ATTRIB_FASCICOLO_IDVALOREATTRIBFASCICOLO_GENERATOR")
    @Column(name = "ID_VALORE_ATTRIB_FASCICOLO")
    public Long getIdValoreAttribFascicolo() {
        return this.idValoreAttribFascicolo;
    }

    public void setIdValoreAttribFascicolo(Long idValoreAttribFascicolo) {
        this.idValoreAttribFascicolo = idValoreAttribFascicolo;
    }

    // bi-directional many-to-one association to DecUsoModelloXsdFasc
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USO_MODELLO_XSD_FASC")
    public DecUsoModelloXsdFasc getDecUsoModelloXsdFasc() {
        return this.decUsoModelloXsdFasc;
    }

    public void setDecUsoModelloXsdFasc(DecUsoModelloXsdFasc decUsoModelloXsdFasc) {
        this.decUsoModelloXsdFasc = decUsoModelloXsdFasc;
    }

    // bi-directional many-to-one association to DecAttribFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ATTRIB_FASCICOLO")
    public DecAttribFascicolo getDecAttribFascicolo() {
        return this.decAttribFascicolo;
    }

    public void setDecAttribFascicolo(DecAttribFascicolo decAttribFascicolo) {
        this.decAttribFascicolo = decAttribFascicolo;
    }

    @Column(name = "DL_VALORE")
    public String getDlValore() {
        return this.dlValore;
    }

    public void setDlValore(String dlValore) {
        this.dlValore = dlValore;
    }

    // bi-directional many-to-one association to DecAaTipoFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AA_TIPO_FASCICOLO")
    public DecAaTipoFascicolo getDecAaTipoFascicolo() {
        return this.decAaTipoFascicolo;
    }

    public void setDecAaTipoFascicolo(DecAaTipoFascicolo decAaTipoFascicolo) {
        this.decAaTipoFascicolo = decAaTipoFascicolo;
    }

    // bi-directional many-to-one association to DecAaTipoFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FASCICOLO")
    public FasFascicolo getFasFascicolo() {
        return this.fasFascicolo;
    }

    public void setFasFascicolo(FasFascicolo fasFascicolo) {
        this.fasFascicolo = fasFascicolo;
    }
}
