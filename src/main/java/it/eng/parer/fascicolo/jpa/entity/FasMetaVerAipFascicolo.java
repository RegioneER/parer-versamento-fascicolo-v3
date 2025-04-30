package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * The persistent class for the FAS_META_VER_AIP_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_META_VER_AIP_FASCICOLO")
public class FasMetaVerAipFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idMetaVerAipFascicolo;
    private String nmMeta;
    private String tiMeta;
    private String tiFormatoMeta;
    private String dsHashFile;
    private String dsAlgoHashFile;
    private String cdEncodingHashFile;
    private FasVerAipFascicolo fasVerAipFascicolo;
    private List<FasFileMetaVerAipFasc> fasFileMetaVerAipFascs = new ArrayList<>();
    private List<FasXsdMetaVerAipFasc> fasXsdMetaVerAipFascs = new ArrayList<>();
    private String dsUrnMetaFascicolo;
    private String dsUrnNormalizMetaFascicolo;

    public FasMetaVerAipFascicolo() {
        // hibernate constructor
    }

    @Id
    @SequenceGenerator(name = "FAS_META_VER_AIP_FASCICOLO_IDMETAVERAIPFASCICOLO_GENERATOR", allocationSize = 1, sequenceName = "SFAS_META_VER_AIP_FASCICOLO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_META_VER_AIP_FASCICOLO_IDMETAVERAIPFASCICOLO_GENERATOR")
    @Column(name = "ID_META_VER_AIP_FASCICOLO")
    public Long getIdMetaVerAipFascicolo() {
        return this.idMetaVerAipFascicolo;
    }

    public void setIdMetaVerAipFascicolo(Long idMetaVerAipFascicolo) {
        this.idMetaVerAipFascicolo = idMetaVerAipFascicolo;
    }

    @Column(name = "NM_META")
    public String getNmMeta() {
        return this.nmMeta;
    }

    public void setNmMeta(String nmMeta) {
        this.nmMeta = nmMeta;
    }

    @Column(name = "TI_META")
    public String getTiMeta() {
        return this.tiMeta;
    }

    public void setTiMeta(String tiMeta) {
        this.tiMeta = tiMeta;
    }

    @Column(name = "TI_FORMATO_META")
    public String getTiFormatoMeta() {
        return this.tiFormatoMeta;
    }

    public void setTiFormatoMeta(String tiFormatoMeta) {
        this.tiFormatoMeta = tiFormatoMeta;
    }

    @Column(name = "DS_HASH_FILE")
    public String getDsHashFile() {
        return this.dsHashFile;
    }

    public void setDsHashFile(String dsHashFile) {
        this.dsHashFile = dsHashFile;
    }

    @Column(name = "DS_ALGO_HASH_FILE")
    public String getDsAlgoHashFile() {
        return this.dsAlgoHashFile;
    }

    public void setDsAlgoHashFile(String dsAlgoHashFile) {
        this.dsAlgoHashFile = dsAlgoHashFile;
    }

    @Column(name = "CD_ENCODING_HASH_FILE")
    public String getCdEncodingHashFile() {
        return this.cdEncodingHashFile;
    }

    public void setCdEncodingHashFile(String cdEncodingHashFile) {
        this.cdEncodingHashFile = cdEncodingHashFile;
    }

    @Column(name = "DS_URN_META_FASCICOLO")
    public String getDsUrnMetaFascicolo() {
        return this.dsUrnMetaFascicolo;
    }

    public void setDsUrnMetaFascicolo(String dsUrnMetaFascicolo) {
        this.dsUrnMetaFascicolo = dsUrnMetaFascicolo;
    }

    @Column(name = "DS_URN_NORMALIZ_META_FASCICOLO")
    public String getDsUrnNormalizMetaFascicolo() {
        return this.dsUrnNormalizMetaFascicolo;
    }

    public void setDsUrnNormalizMetaFascicolo(String dsUrnNormalizMetaFascicolo) {
        this.dsUrnNormalizMetaFascicolo = dsUrnNormalizMetaFascicolo;
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

    // bi-directional many-to-one association to FasFileMetaVerAipFasc
    @OneToMany(mappedBy = "fasMetaVerAipFascicolo")
    public List<FasFileMetaVerAipFasc> getFasFileMetaVerAipFascs() {
        return this.fasFileMetaVerAipFascs;
    }

    public void setFasFileMetaVerAipFascs(List<FasFileMetaVerAipFasc> fasFileMetaVerAipFascs) {
        this.fasFileMetaVerAipFascs = fasFileMetaVerAipFascs;
    }

    // bi-directional many-to-one association to FasXsdMetaVerAipFasc
    @OneToMany(mappedBy = "fasMetaVerAipFascicolo")
    public List<FasXsdMetaVerAipFasc> getFasXsdMetaVerAipFascs() {
        return this.fasXsdMetaVerAipFascs;
    }

    public void setFasXsdMetaVerAipFascs(List<FasXsdMetaVerAipFasc> fasXsdMetaVerAipFascs) {
        this.fasXsdMetaVerAipFascs = fasXsdMetaVerAipFascs;
    }

}