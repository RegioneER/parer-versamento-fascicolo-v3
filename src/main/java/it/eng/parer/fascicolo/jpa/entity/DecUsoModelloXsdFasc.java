package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

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
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the DEC_USO_MODELLO_XSD_FASC database table.
 *
 */
@Entity
@Table(name = "DEC_USO_MODELLO_XSD_FASC")
@NamedQuery(name = "DecUsoModelloXsdFasc.findAll", query = "SELECT d FROM DecUsoModelloXsdFasc d")
public class DecUsoModelloXsdFasc implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idUsoModelloXsdFasc;
    private String flStandard;
    private DecAaTipoFascicolo decAaTipoFascicolo;
    private DecModelloXsdFascicolo decModelloXsdFascicolo;
    private LocalDateTime dtIstituz;
    private LocalDateTime dtSoppres;

    public DecUsoModelloXsdFasc() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "DEC_USO_MODELLO_XSD_FASC_IDUSOMODELLOXSDFASC_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SDEC_USO_MODELLO_XSD_FASC"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEC_USO_MODELLO_XSD_FASC_IDUSOMODELLOXSDFASC_GENERATOR")
    @Column(name = "ID_USO_MODELLO_XSD_FASC")
    public Long getIdUsoModelloXsdFasc() {
        return this.idUsoModelloXsdFasc;
    }

    public void setIdUsoModelloXsdFasc(Long idUsoModelloXsdFasc) {
        this.idUsoModelloXsdFasc = idUsoModelloXsdFasc;
    }

    @Column(name = "FL_STANDARD", columnDefinition = "CHAR")
    public String getFlStandard() {
        return this.flStandard;
    }

    public void setFlStandard(String flStandard) {
        this.flStandard = flStandard;
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

    // bi-directional many-to-one association to DecModelloXsdFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MODELLO_XSD_FASCICOLO")
    public DecModelloXsdFascicolo getDecModelloXsdFascicolo() {
        return this.decModelloXsdFascicolo;
    }

    public void setDecModelloXsdFascicolo(DecModelloXsdFascicolo decModelloXsdFascicolo) {
        this.decModelloXsdFascicolo = decModelloXsdFascicolo;
    }

    @Column(name = "DT_ISTITUZ")
    public LocalDateTime getDtIstituz() {
        return this.dtIstituz;
    }

    public void setDtIstituz(LocalDateTime dtIstituz) {
        this.dtIstituz = dtIstituz;
    }

    @Column(name = "DT_SOPPRES")
    public LocalDateTime getDtSoppres() {
        return this.dtSoppres;
    }

    public void setDtSoppres(LocalDateTime dtSoppres) {
        this.dtSoppres = dtSoppres;
    }

}
