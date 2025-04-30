package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "FAS_XML_FASC_OBJECT_STORAGE")
public class FasXmlFascObjectStorage implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idFascicolo;
    private FasFascicolo fasFascicolo;
    private DecBackend decBackend;
    private String nmTenant;
    private String nmBucket;
    private String cdKeyFile;
    private BigDecimal idStrut;

    public FasXmlFascObjectStorage() {
        // hibernate constructor
    }

    @Id
    @Column(name = "ID_FASCICOLO")
    public Long getIdFascicolo() {
        return idFascicolo;
    }

    public void setIdFascicolo(Long idFascicolo) {
        this.idFascicolo = idFascicolo;
    }

    @MapsId
    @OneToOne(mappedBy = "fasXmlFascObjectStorage")
    @JoinColumn(name = "ID_FASCICOLO")
    public FasFascicolo getFasFascicolo() {
        return fasFascicolo;
    }

    public void setFasFascicolo(FasFascicolo fasFascicolo) {
        this.fasFascicolo = fasFascicolo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DEC_BACKEND")
    public DecBackend getDecBackend() {
        return decBackend;
    }

    public void setDecBackend(DecBackend decBackend) {
        this.decBackend = decBackend;
    }

    @Column(name = "NM_TENANT")
    public String getNmTenant() {
        return nmTenant;
    }

    public void setNmTenant(String nmTenant) {
        this.nmTenant = nmTenant;
    }

    @Column(name = "NM_BUCKET")
    public String getNmBucket() {
        return nmBucket;
    }

    public void setNmBucket(String nmBucket) {
        this.nmBucket = nmBucket;
    }

    @Column(name = "CD_KEY_FILE")
    public String getCdKeyFile() {
        return cdKeyFile;
    }

    public void setCdKeyFile(String cdKeyFile) {
        this.cdKeyFile = cdKeyFile;
    }

    @Column(name = "ID_STRUT")
    public BigDecimal getIdStrut() {
        return this.idStrut;
    }

    public void setIdStrut(BigDecimal idStrut) {
        this.idStrut = idStrut;
    }

}
