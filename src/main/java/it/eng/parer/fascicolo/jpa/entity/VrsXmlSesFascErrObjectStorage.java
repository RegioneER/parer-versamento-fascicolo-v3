package it.eng.parer.fascicolo.jpa.entity;

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "VRS_XML_SES_FASC_ERR_OBJECT_STORAGE")
public class VrsXmlSesFascErrObjectStorage implements Serializable {
    private static final long serialVersionUID = 1L;

    public VrsXmlSesFascErrObjectStorage() {
        // hibernate constructor
    }

    private Long idXmlSesFascErrObjectStorage;
    private DecBackend decBackend;
    private VrsSesFascicoloErr vrsSesFascicoloErr;
    private String nmTenant;
    private String nmBucket;
    private String nmKeyFile;
    private BigDecimal idStrut;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_XML_SES_FASC_ERR_OBJECT_STORAGE")
    public Long getIdXmlSesFascErrObjectStorage() {
        return idXmlSesFascErrObjectStorage;
    }

    public void setIdXmlSesFascErrObjectStorage(Long idXmlSesFascErrObjectStorage) {
        this.idXmlSesFascErrObjectStorage = idXmlSesFascErrObjectStorage;
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
    @JoinColumn(name = "ID_SES_FASCICOLO_ERR")
    public VrsSesFascicoloErr getVrsSesFascicoloErr() {
        return vrsSesFascicoloErr;
    }

    public void setVrsSesFascicoloErr(VrsSesFascicoloErr vrsSesFascicoloErr) {
        this.vrsSesFascicoloErr = vrsSesFascicoloErr;
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
