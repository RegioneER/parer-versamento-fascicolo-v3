package it.eng.parer.fascicolo.jpa.viewEntity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the VRS_V_UPD_FASCICOLO_KO database table.
 *
 */
@Entity
@Table(name = "VRS_V_UPD_FASCICOLO_KO")
@NamedQuery(name = "VrsVUpdFascicoloKo.findByIdFascKo", query = "SELECT v FROM VrsVUpdFascicoloKo v WHERE v.idFascicoloKo=:idFascicoloKo")
public class VrsVUpdFascicoloKo implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long idFascicoloKo;
    private LocalDateTime tsIniFirstSes;
    private LocalDateTime tsIniLastSes;
    private BigDecimal idErrSacerPrinc;
    private String dsErrPrinc;
    private BigDecimal idTipoFascicoloLast;
    private String tiStatoFascicoloKo;
    private BigDecimal idSesFascicoloKoFirst;
    private BigDecimal idSesFascicoloKoLast;

    public VrsVUpdFascicoloKo() {
        // hibernate constructor
    }

    @Id
    @Column(name = "ID_FASCICOLO_KO")
    public Long getIdFascicoloKo() {
        return idFascicoloKo;
    }

    public void setIdFascicoloKo(Long idFascicoloKo) {
        this.idFascicoloKo = idFascicoloKo;
    }

    @Column(name = "TS_INI_FIRST_SES")
    public LocalDateTime getTsIniFirstSes() {
        return tsIniFirstSes;
    }

    public void setTsIniFirstSes(LocalDateTime tsIniFirstSes) {
        this.tsIniFirstSes = tsIniFirstSes;
    }

    @Column(name = "TS_INI_LAST_SES")
    public LocalDateTime getTsIniLastSes() {
        return tsIniLastSes;
    }

    public void setTsIniLastSes(LocalDateTime tsIniLastSes) {
        this.tsIniLastSes = tsIniLastSes;
    }

    @Column(name = "ID_ERR_SACER_PRINC")
    public BigDecimal getIdErrSacerPrinc() {
        return idErrSacerPrinc;
    }

    public void setIdErrSacerPrinc(BigDecimal idErrSacerPrinc) {
        this.idErrSacerPrinc = idErrSacerPrinc;
    }

    @Column(name = "DS_ERR_PRINC")
    public String getDsErrPrinc() {
        return dsErrPrinc;
    }

    public void setDsErrPrinc(String dsErrPrinc) {
        this.dsErrPrinc = dsErrPrinc;
    }

    @Column(name = "ID_TIPO_FASCICOLO_LAST")
    public BigDecimal getIdTipoFascicoloLast() {
        return idTipoFascicoloLast;
    }

    public void setIdTipoFascicoloLast(BigDecimal idTipoFascicoloLast) {
        this.idTipoFascicoloLast = idTipoFascicoloLast;
    }

    @Column(name = "TI_STATO_FASCICOLO_KO")
    public String getTiStatoFascicoloKo() {
        return tiStatoFascicoloKo;
    }

    public void setTiStatoFascicoloKo(String tiStatoFascicoloKo) {
        this.tiStatoFascicoloKo = tiStatoFascicoloKo;
    }

    @Column(name = "ID_SES_FASCICOLO_KO_FIRST")
    public BigDecimal getIdSesFascicoloKoFirst() {
        return idSesFascicoloKoFirst;
    }

    public void setIdSesFascicoloKoFirst(BigDecimal idSesFascicoloKoFirst) {
        this.idSesFascicoloKoFirst = idSesFascicoloKoFirst;
    }

    @Column(name = "ID_SES_FASCICOLO_KO_LAST")
    public BigDecimal getIdSesFascicoloKoLast() {
        return idSesFascicoloKoLast;
    }

    public void setIdSesFascicoloKoLast(BigDecimal idSesFascicoloKoLast) {
        this.idSesFascicoloKoLast = idSesFascicoloKoLast;
    }
}
