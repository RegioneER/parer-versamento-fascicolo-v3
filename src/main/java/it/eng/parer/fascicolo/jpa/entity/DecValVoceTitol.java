package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
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
import jakarta.persistence.Table;

/**
 * The persistent class for the DEC_VAL_VOCE_TITOL database table.
 *
 */
@Entity
@Table(name = "DEC_VAL_VOCE_TITOL")
public class DecValVoceTitol implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idValVoceTitol;
    private String dlNote;
    private String dsVoceTitol;
    private LocalDateTime dtFinVal;
    private LocalDateTime dtIniVal;
    private String flUsoClassif;
    private BigDecimal niAnniConserv;
    private DecVoceTitol decVoceTitol;

    public DecValVoceTitol() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "DEC_VAL_VOCE_TITOL_IDVALVOCETITOL_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SDEC_VAL_VOCE_TITOL"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEC_VAL_VOCE_TITOL_IDVALVOCETITOL_GENERATOR")
    @Column(name = "ID_VAL_VOCE_TITOL")
    public Long getIdValVoceTitol() {
        return this.idValVoceTitol;
    }

    public void setIdValVoceTitol(Long idValVoceTitol) {
        this.idValVoceTitol = idValVoceTitol;
    }

    @Column(name = "DL_NOTE")
    public String getDlNote() {
        return this.dlNote;
    }

    public void setDlNote(String dlNote) {
        this.dlNote = dlNote;
    }

    @Column(name = "DS_VOCE_TITOL")
    public String getDsVoceTitol() {
        return this.dsVoceTitol;
    }

    public void setDsVoceTitol(String dsVoceTitol) {
        this.dsVoceTitol = dsVoceTitol;
    }

    @Column(name = "DT_FIN_VAL")
    public LocalDateTime getDtFinVal() {
        return this.dtFinVal;
    }

    public void setDtFinVal(LocalDateTime dtFinVal) {
        this.dtFinVal = dtFinVal;
    }

    @Column(name = "DT_INI_VAL")
    public LocalDateTime getDtIniVal() {
        return this.dtIniVal;
    }

    public void setDtIniVal(LocalDateTime dtIniVal) {
        this.dtIniVal = dtIniVal;
    }

    @Column(name = "FL_USO_CLASSIF", columnDefinition = "CHAR")
    public String getFlUsoClassif() {
        return this.flUsoClassif;
    }

    public void setFlUsoClassif(String flUsoClassif) {
        this.flUsoClassif = flUsoClassif;
    }

    @Column(name = "NI_ANNI_CONSERV")
    public BigDecimal getNiAnniConserv() {
        return this.niAnniConserv;
    }

    public void setNiAnniConserv(BigDecimal niAnniConserv) {
        this.niAnniConserv = niAnniConserv;
    }

    // bi-directional many-to-one association to DecVoceTitol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VOCE_TITOL")
    public DecVoceTitol getDecVoceTitol() {
        return this.decVoceTitol;
    }

    public void setDecVoceTitol(DecVoceTitol decVoceTitol) {
        this.decVoceTitol = decVoceTitol;
    }

}