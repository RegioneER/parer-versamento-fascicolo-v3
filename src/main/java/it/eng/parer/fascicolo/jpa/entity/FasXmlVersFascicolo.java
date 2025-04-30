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
import jakarta.persistence.CascadeType;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The persistent class for the FAS_XML_VERS_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_XML_VERS_FASCICOLO")
@NamedQuery(name = "FasXmlVersFascicolo.findAll", query = "SELECT f FROM FasXmlVersFascicolo f")
public class FasXmlVersFascicolo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idXmlVersFascicolo;
    private String blXmlVers;
    private String cdEncodingHashXmlVers;
    private String cdVersioneXml;
    private String dsAlgoHashXmlVers;
    private String dsHashXmlVers;
    private String dsUrnXmlVers;
    private String dsUrnNormalizXmlVers;
    private LocalDateTime dtVersFascicolo;
    private BigDecimal idStrut;
    private String tiXmlVers;
    private String flCanonicalized;
    private FasFascicolo fasFascicolo;
    private List<FasSipVerAipFascicolo> fasSipVerAipFascicoloRichs = new ArrayList<>();
    private List<FasSipVerAipFascicolo> fasSipVerAipFascicoloRisps = new ArrayList<>();

    public FasXmlVersFascicolo() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_XML_VERS_FASCICOLO_IDXMLVERSFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_XML_VERS_FASCICOLO"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_XML_VERS_FASCICOLO_IDXMLVERSFASCICOLO_GENERATOR")
    @Column(name = "ID_XML_VERS_FASCICOLO")
    public Long getIdXmlVersFascicolo() {
        return this.idXmlVersFascicolo;
    }

    public void setIdXmlVersFascicolo(Long idXmlVersFascicolo) {
        this.idXmlVersFascicolo = idXmlVersFascicolo;
    }

    @Lob
    @Column(name = "BL_XML_VERS")
    public String getBlXmlVers() {
        return this.blXmlVers;
    }

    public void setBlXmlVers(String blXmlVers) {
        this.blXmlVers = blXmlVers;
    }

    @Column(name = "CD_ENCODING_HASH_XML_VERS")
    public String getCdEncodingHashXmlVers() {
        return this.cdEncodingHashXmlVers;
    }

    public void setCdEncodingHashXmlVers(String cdEncodingHashXmlVers) {
        this.cdEncodingHashXmlVers = cdEncodingHashXmlVers;
    }

    @Column(name = "CD_VERSIONE_XML")
    public String getCdVersioneXml() {
        return this.cdVersioneXml;
    }

    public void setCdVersioneXml(String cdVersioneXml) {
        this.cdVersioneXml = cdVersioneXml;
    }

    @Column(name = "DS_ALGO_HASH_XML_VERS")
    public String getDsAlgoHashXmlVers() {
        return this.dsAlgoHashXmlVers;
    }

    public void setDsAlgoHashXmlVers(String dsAlgoHashXmlVers) {
        this.dsAlgoHashXmlVers = dsAlgoHashXmlVers;
    }

    @Column(name = "DS_HASH_XML_VERS")
    public String getDsHashXmlVers() {
        return this.dsHashXmlVers;
    }

    public void setDsHashXmlVers(String dsHashXmlVers) {
        this.dsHashXmlVers = dsHashXmlVers;
    }

    @Column(name = "DS_URN_XML_VERS")
    public String getDsUrnXmlVers() {
        return this.dsUrnXmlVers;
    }

    public void setDsUrnXmlVers(String dsUrnXmlVers) {
        this.dsUrnXmlVers = dsUrnXmlVers;
    }

    @Column(name = "DS_URN_NORMALIZ_XML_VERS")
    public String getDsUrnNormalizXmlVers() {
        return this.dsUrnNormalizXmlVers;
    }

    public void setDsUrnNormalizXmlVers(String dsUrnNormalizXmlVers) {
        this.dsUrnNormalizXmlVers = dsUrnNormalizXmlVers;
    }

    @Column(name = "DT_VERS_FASCICOLO")
    public LocalDateTime getDtVersFascicolo() {
        return this.dtVersFascicolo;
    }

    public void setDtVersFascicolo(LocalDateTime dtVersFascicolo) {
        this.dtVersFascicolo = dtVersFascicolo;
    }

    @Column(name = "ID_STRUT")
    public BigDecimal getIdStrut() {
        return this.idStrut;
    }

    public void setIdStrut(BigDecimal idStrut) {
        this.idStrut = idStrut;
    }

    @Column(name = "TI_XML_VERS")
    public String getTiXmlVers() {
        return this.tiXmlVers;
    }

    public void setTiXmlVers(String tiXmlVers) {
        this.tiXmlVers = tiXmlVers;
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

    @Column(name = "FL_CANONICALIZED", columnDefinition = "CHAR")
    public String getFlCanonicalized() {
        return this.flCanonicalized;
    }

    public void setFlCanonicalized(String flCanonicalized) {
        this.flCanonicalized = flCanonicalized;
    }

    // bi-directional many-to-one association to FasSipVerAipFascicolo
    @OneToMany(mappedBy = "fasXmlVersFascicoloRich", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH })
    public List<FasSipVerAipFascicolo> getFasSipVerAipFascicoloRichs() {
        return this.fasSipVerAipFascicoloRichs;
    }

    public void setFasSipVerAipFascicoloRichs(List<FasSipVerAipFascicolo> fasSipVerAipFascicoloRichs) {
        this.fasSipVerAipFascicoloRichs = fasSipVerAipFascicoloRichs;
    }

    // bi-directional many-to-one association to FasSipVerAipFascicolo
    @OneToMany(mappedBy = "fasXmlVersFascicoloRisp", cascade = { CascadeType.PERSIST, CascadeType.MERGE,
            CascadeType.REFRESH })
    public List<FasSipVerAipFascicolo> getFasSipVerAipFascicoloRisps() {
        return this.fasSipVerAipFascicoloRisps;
    }

    public void setFasSipVerAipFascicoloRisps(List<FasSipVerAipFascicolo> fasSipVerAipFascicoloRisps) {
        this.fasSipVerAipFascicoloRisps = fasSipVerAipFascicoloRisps;
    }

}