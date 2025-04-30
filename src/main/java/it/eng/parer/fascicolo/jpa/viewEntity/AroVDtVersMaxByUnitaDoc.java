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
 * The persistent class for the ARO_V_DT_VERS_MAX_BY_UNITA_DOC database table.
 *
 */
@Entity
@Table(name = "ARO_V_DT_VERS_MAX_BY_UNITA_DOC")
@NamedQuery(name = "AroVDtVersMaxByUnitaDoc.findAll", query = "SELECT a FROM AroVDtVersMaxByUnitaDoc a")
public class AroVDtVersMaxByUnitaDoc implements Serializable {

    private static final long serialVersionUID = 1L;
    private BigDecimal idUnitaDoc;
    private LocalDateTime dtVersMax;

    public AroVDtVersMaxByUnitaDoc(BigDecimal idUnitaDoc, LocalDateTime dtVersMax) {
        this.idUnitaDoc = idUnitaDoc;
        this.dtVersMax = dtVersMax;
    }

    public AroVDtVersMaxByUnitaDoc() {
    }

    @Id
    @Column(name = "ID_UNITA_DOC")
    public BigDecimal getIdUnitaDoc() {
        return this.idUnitaDoc;
    }

    public void setIdUnitaDoc(BigDecimal idUnitaDoc) {
        this.idUnitaDoc = idUnitaDoc;
    }

    @Column(name = "DT_VERS_MAX")
    public LocalDateTime getDtVersMax() {
        return this.dtVersMax;
    }

    public void setDtVersMax(LocalDateTime dtVersMax) {
        this.dtVersMax = dtVersMax;
    }

}
