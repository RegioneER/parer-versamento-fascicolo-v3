package it.eng.parer.fascicolo.jpa.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "VRS_XML_SES_FASC_KO_OBJECT_STORAGE")
public class VrsXmlSesFascKoObjectStorage implements Serializable {
    private static final long serialVersionUID = 1L;

    public VrsXmlSesFascKoObjectStorage() {
        // hibernate constructor
    }

    private Long idXmlSesFascKoObjectStorage;
    private DecBackend decBackend;
    private VrsSesFascicoloKo vrsSesFascicoloKo;
    private String nmTenant;
    private String nmBucket;
    private String nmKeyFile;
    private BigDecimal idStrut;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_XML_SES_FASC_KO_OBJECT_STORAGE")
    public Long getIdXmlSesFascKoObjectStorage() {
        return idXmlSesFascKoObjectStorage;
    }

    public void setIdXmlSesFascKoObjectStorage(Long idXmlSesFascKoObjectStorage) {
        this.idXmlSesFascKoObjectStorage = idXmlSesFascKoObjectStorage;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DEC_BACKEND")
    public DecBackend getDecBackend() {
        return decBackend;
    }

    public void setDecBackend(DecBackend decBackend) {
        this.decBackend = decBackend;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SES_FASCICOLO_KO")
    public VrsSesFascicoloKo getVrsSesFascicoloKo() {
        return vrsSesFascicoloKo;
    }

    public void setVrsSesFascicoloKo(VrsSesFascicoloKo vrsSesFascicoloKo) {
        this.vrsSesFascicoloKo = vrsSesFascicoloKo;
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
    public String getNmKeyFile() {
        return nmKeyFile;
    }

    public void setNmKeyFile(String nmKeyFile) {
        this.nmKeyFile = nmKeyFile;
    }

    @Column(name = "ID_STRUT")
    public BigDecimal getIdStrut() {
        return this.idStrut;
    }

    public void setIdStrut(BigDecimal idStrut) {
        this.idStrut = idStrut;
    }
}
