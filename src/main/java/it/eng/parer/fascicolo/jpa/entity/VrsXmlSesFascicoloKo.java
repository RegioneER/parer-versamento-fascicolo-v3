package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the VRS_XML_SES_FASCICOLO_KO database table.
 *
 */
@Entity
@Table(name = "VRS_XML_SES_FASCICOLO_KO")
@NamedQuery(name = "VrsXmlSesFascicoloKo.findAll", query = "SELECT v FROM VrsXmlSesFascicoloKo v")
public class VrsXmlSesFascicoloKo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idXmlSesFascicoloKo;
    private String blXml;
    private String cdVersioneXml;
    private LocalDate dtRegXmlSesKo;
    private BigDecimal idStrut;
    private String tiXml;
    private VrsSesFascicoloKo vrsSesFascicoloKo;

    public VrsXmlSesFascicoloKo() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "VRS_XML_SES_FASCICOLO_KO_IDXMLSESFASCICOLOKO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SVRS_XML_SES_FASCICOLO_KO"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VRS_XML_SES_FASCICOLO_KO_IDXMLSESFASCICOLOKO_GENERATOR")
    @Column(name = "ID_XML_SES_FASCICOLO_KO")
    public Long getIdXmlSesFascicoloKo() {
        return this.idXmlSesFascicoloKo;
    }

    public void setIdXmlSesFascicoloKo(Long idXmlSesFascicoloKo) {
        this.idXmlSesFascicoloKo = idXmlSesFascicoloKo;
    }

    @Lob
    @Column(name = "BL_XML")
    public String getBlXml() {
        return this.blXml;
    }

    public void setBlXml(String blXml) {
        this.blXml = blXml;
    }

    @Column(name = "CD_VERSIONE_XML")
    public String getCdVersioneXml() {
        return this.cdVersioneXml;
    }

    public void setCdVersioneXml(String cdVersioneXml) {
        this.cdVersioneXml = cdVersioneXml;
    }

    @Column(name = "DT_REG_XML_SES_KO")
    public LocalDate getDtRegXmlSesKo() {
        return this.dtRegXmlSesKo;
    }

    public void setDtRegXmlSesKo(LocalDate dtRegXmlSesKo) {
        this.dtRegXmlSesKo = dtRegXmlSesKo;
    }

    @Column(name = "ID_STRUT")
    public BigDecimal getIdStrut() {
        return this.idStrut;
    }

    public void setIdStrut(BigDecimal idStrut) {
        this.idStrut = idStrut;
    }

    @Column(name = "TI_XML")
    public String getTiXml() {
        return this.tiXml;
    }

    public void setTiXml(String tiXml) {
        this.tiXml = tiXml;
    }

    // bi-directional many-to-one association to VrsSesFascicoloKo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SES_FASCICOLO_KO")
    public VrsSesFascicoloKo getVrsSesFascicoloKo() {
        return this.vrsSesFascicoloKo;
    }

    public void setVrsSesFascicoloKo(VrsSesFascicoloKo vrsSesFascicoloKo) {
        this.vrsSesFascicoloKo = vrsSesFascicoloKo;
    }

}
