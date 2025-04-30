package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequenceGenerator;
import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The persistent class for the ORG_AMBIENTE database table.
 *
 */
@Entity
@Cacheable(true)
@Table(name = "ORG_AMBIENTE")
public class OrgAmbiente implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idAmbiente;
    private String dsAmbiente;
    private String nmAmbiente;
    private String dsNote;
    private LocalDateTime dtFinVal;
    private LocalDateTime dtIniVal;
    private BigDecimal idEnteConserv;
    private BigDecimal idEnteGestore;
    private List<OrgEnte> orgEntes = new ArrayList<>();
    private List<AplValoreParamApplic> aplValoreParamApplics = new ArrayList<>();
    private List<AplValParamApplicMulti> aplValParamApplicMultis = new ArrayList<>();

    public OrgAmbiente() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "ORG_AMBIENTE_IDAMBIENTE_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SORG_AMBIENTE"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORG_AMBIENTE_IDAMBIENTE_GENERATOR")
    @Column(name = "ID_AMBIENTE")
    public Long getIdAmbiente() {
        return this.idAmbiente;
    }

    public void setIdAmbiente(Long idAmbiente) {
        this.idAmbiente = idAmbiente;
    }

    @Column(name = "DS_AMBIENTE")
    public String getDsAmbiente() {
        return this.dsAmbiente;
    }

    public void setDsAmbiente(String dsAmbiente) {
        this.dsAmbiente = dsAmbiente;
    }

    @Column(name = "NM_AMBIENTE")
    public String getNmAmbiente() {
        return this.nmAmbiente;
    }

    public void setNmAmbiente(String nmAmbiente) {
        this.nmAmbiente = nmAmbiente;
    }

    // bi-directional many-to-one association to OrgEnte
    @OneToMany(mappedBy = "orgAmbiente")
    public List<OrgEnte> getOrgEntes() {
        return this.orgEntes;
    }

    public void setOrgEntes(List<OrgEnte> orgEntes) {
        this.orgEntes = orgEntes;
    }

    public OrgEnte addOrgEnte(OrgEnte orgEnte) {
        getOrgEntes().add(orgEnte);
        orgEnte.setOrgAmbiente(this);

        return orgEnte;
    }

    public OrgEnte removeOrgEnte(OrgEnte orgEnte) {
        getOrgEntes().remove(orgEnte);
        orgEnte.setOrgAmbiente(null);

        return orgEnte;
    }

    @Column(name = "DS_NOTE")
    public String getDsNote() {
        return this.dsNote;
    }

    public void setDsNote(String dsNote) {
        this.dsNote = dsNote;
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

    @Column(name = "ID_ENTE_CONSERV")
    public java.math.BigDecimal getIdEnteConserv() {
        return this.idEnteConserv;
    }

    public void setIdEnteConserv(java.math.BigDecimal idEnteConserv) {
        this.idEnteConserv = idEnteConserv;
    }

    @Column(name = "ID_ENTE_GESTORE")
    public java.math.BigDecimal getIdEnteGestore() {
        return this.idEnteGestore;
    }

    public void setIdEnteGestore(java.math.BigDecimal idEnteGestore) {
        this.idEnteGestore = idEnteGestore;
    }

    // bi-directional many-to-one association to AplValoreParamApplic
    @OneToMany(mappedBy = "orgAmbiente", cascade = { CascadeType.REMOVE })
    public List<AplValoreParamApplic> getAplValoreParamApplics() {
        return this.aplValoreParamApplics;
    }

    public void setAplValoreParamApplics(List<AplValoreParamApplic> aplValoreParamApplics) {
        this.aplValoreParamApplics = aplValoreParamApplics;
    }

    public AplValoreParamApplic addAplValoreParamApplic(AplValoreParamApplic aplValoreParamApplic) {
        getAplValoreParamApplics().add(aplValoreParamApplic);
        aplValoreParamApplic.setOrgAmbiente(this);

        return aplValoreParamApplic;
    }

    public AplValoreParamApplic removeAplValoreParamApplic(AplValoreParamApplic aplValoreParamApplic) {
        getAplValoreParamApplics().remove(aplValoreParamApplic);
        aplValoreParamApplic.setOrgAmbiente(null);

        return aplValoreParamApplic;
    }

    // bi-directional many-to-one association to AplValParamApplicMulti
    @OneToMany(mappedBy = "orgAmbiente", cascade = { CascadeType.REMOVE })
    public List<AplValParamApplicMulti> getAplValParamApplicMultis() {
        return this.aplValParamApplicMultis;
    }

    public void setAplValParamApplicMultis(List<AplValParamApplicMulti> aplValParamApplicMultis) {
        this.aplValParamApplicMultis = aplValParamApplicMultis;
    }

    public AplValParamApplicMulti addAplValParamApplicMulti(AplValParamApplicMulti aplValParamApplicMulti) {
        getAplValParamApplicMultis().add(aplValParamApplicMulti);
        aplValParamApplicMulti.setOrgAmbiente(this);

        return aplValParamApplicMulti;
    }

    public AplValParamApplicMulti removeAplValParamApplicMulti(AplValParamApplicMulti aplValParamApplicMulti) {
        getAplValParamApplicMultis().remove(aplValParamApplicMulti);
        aplValParamApplicMulti.setOrgAmbiente(null);

        return aplValParamApplicMulti;
    }

}
