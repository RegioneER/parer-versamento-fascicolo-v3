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
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the APL_VAL_PARAM_APPLIC_MULTI database table.
 *
 */
@Entity
@Table(name = "APL_VAL_PARAM_APPLIC_MULTI")
@NamedQuery(name = "AplValParamApplicMulti.findAll", query = "SELECT a FROM AplValParamApplicMulti a")
public class AplValParamApplicMulti implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idValParamApplicMulti;
    private String dsValoreParamApplic;
    private AplParamApplic aplParamApplic;
    private OrgAmbiente orgAmbiente;

    public AplValParamApplicMulti() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "APL_VAL_PARAM_APPLIC_MULTI_IDVALPARAMAPPLICMULTI_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SAPL_VAL_PARAM_APPLIC_MULTI"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APL_VAL_PARAM_APPLIC_MULTI_IDVALPARAMAPPLICMULTI_GENERATOR")
    @Column(name = "ID_VAL_PARAM_APPLIC_MULTI")
    public Long getIdValParamApplicMulti() {
        return this.idValParamApplicMulti;
    }

    public void setIdValParamApplicMulti(Long idValParamApplicMulti) {
        this.idValParamApplicMulti = idValParamApplicMulti;
    }

    @Column(name = "DS_VALORE_PARAM_APPLIC")
    public String getDsValoreParamApplic() {
        return this.dsValoreParamApplic;
    }

    public void setDsValoreParamApplic(String dsValoreParamApplic) {
        this.dsValoreParamApplic = dsValoreParamApplic;
    }

    // bi-directional many-to-one association to AplParamApplic
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_PARAM_APPLIC")
    public AplParamApplic getAplParamApplic() {
        return this.aplParamApplic;
    }

    public void setAplParamApplic(AplParamApplic aplParamApplic) {
        this.aplParamApplic = aplParamApplic;
    }

    // bi-directional many-to-one association to OrgAmbiente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AMBIENTE")
    public OrgAmbiente getOrgAmbiente() {
        return this.orgAmbiente;
    }

    public void setOrgAmbiente(OrgAmbiente orgAmbiente) {
        this.orgAmbiente = orgAmbiente;
    }

}