package it.eng.parer.fascicolo.beans.dto;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;

import it.eng.parer.fascicolo.beans.dto.base.CSChiave;

public class UnitaDocLink implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3153568788306139252L;

    //
    Long idLinkUnitaDoc;
    CSChiave csChiave;
    Date dataInserimentoFas;
    BigInteger posizione;

    public Long getIdLinkUnitaDoc() {
        return idLinkUnitaDoc;
    }

    public void setIdLinkUnitaDoc(Long idLinkUnitaDoc) {
        this.idLinkUnitaDoc = idLinkUnitaDoc;
    }

    public CSChiave getCsChiave() {
        return csChiave;
    }

    public void setCsChiave(CSChiave csChiave) {
        this.csChiave = csChiave;
    }

    public Date getDataInserimentoFas() {
        return dataInserimentoFas;
    }

    public void setDataInserimentoFas(Date dataInserimentoFas) {
        this.dataInserimentoFas = dataInserimentoFas;
    }

    public BigInteger getPosizione() {
        return posizione;
    }

    public void setPosizione(BigInteger posizione) {
        this.posizione = posizione;
    }

    @Override
    public String toString() {
        return "FasUnitaDocColl [idLinkUnitaDoc - " + idLinkUnitaDoc + " , csChiave - " + csChiave
                + " , dataInserimentoFas - " + dataInserimentoFas + " , posizione - " + posizione + "]";
    }

}
