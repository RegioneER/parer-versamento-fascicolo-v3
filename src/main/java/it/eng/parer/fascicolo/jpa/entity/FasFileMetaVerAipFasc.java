package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * The persistent class for the FAS_FILE_META_VER_AIP_FASC database table.
 *
 */
@Entity
@Table(name = "FAS_FILE_META_VER_AIP_FASC")
public class FasFileMetaVerAipFasc implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idFileMetaVerAipFasc;
    private String blFileVerIndiceAip;
    private OrgStrut orgStrut;
    private LocalDateTime dtCreazione;
    private FasMetaVerAipFascicolo fasMetaVerAipFascicolo;

    public FasFileMetaVerAipFasc() {
        // hibernate constructor
    }

    @Id
    @SequenceGenerator(name = "FAS_FILE_META_VER_AIP_FASC_IDFILEMETAVERAIPFASC_GENERATOR", allocationSize = 1, sequenceName = "SFAS_FILE_META_VER_AIP_FASC")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_FILE_META_VER_AIP_FASC_IDFILEMETAVERAIPFASC_GENERATOR")
    @Column(name = "ID_FILE_META_VER_AIP_FASC")
    public Long getIdFileMetaVerAipFasc() {
        return this.idFileMetaVerAipFasc;
    }

    public void setIdFileMetaVerAipFasc(Long idFileMetaVerAipFasc) {
        this.idFileMetaVerAipFasc = idFileMetaVerAipFasc;
    }

    @Lob
    @Column(name = "BL_FILE_VER_INDICE_AIP")
    public String getBlFileVerIndiceAip() {
        return this.blFileVerIndiceAip;
    }

    public void setBlFileVerIndiceAip(String blFileVerIndiceAip) {
        this.blFileVerIndiceAip = blFileVerIndiceAip;
    }

    // bi-directional many-to-one association to OrgStrut
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_STRUT")
    public OrgStrut getOrgStrut() {
        return this.orgStrut;
    }

    public void setOrgStrut(OrgStrut orgStrut) {
        this.orgStrut = orgStrut;
    }

    @Column(name = "DT_CREAZIONE")
    public LocalDateTime getDtCreazione() {
        return this.dtCreazione;
    }

    public void setDtCreazione(LocalDateTime dtCreazione) {
        this.dtCreazione = dtCreazione;
    }

    // bi-directional many-to-one association to FasMetaVerAipFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_META_VER_AIP_FASCICOLO")
    public FasMetaVerAipFascicolo getFasMetaVerAipFascicolo() {
        return this.fasMetaVerAipFascicolo;
    }

    public void setFasMetaVerAipFascicolo(FasMetaVerAipFascicolo fasMetaVerAipFascicolo) {
        this.fasMetaVerAipFascicolo = fasMetaVerAipFascicolo;
    }

}