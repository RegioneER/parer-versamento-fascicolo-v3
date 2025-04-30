/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto.base;

import java.util.Map;

/**
 *
 * @author Fioravanti_F
 */
public class RispostaControlliAttSpec extends RispostaControlli {

    /**
     *
     */
    private static final long serialVersionUID = -5018344233965022152L;
    private long idRecXsdDatiSpec;
    private Map<String, DatoSpecifico> datiSpecifici;

    public long getIdRecXsdDatiSpec() {
        return idRecXsdDatiSpec;
    }

    public void setIdRecXsdDatiSpec(long idRecXsdDatiSpec) {
        this.idRecXsdDatiSpec = idRecXsdDatiSpec;
    }

    public Map<String, DatoSpecifico> getDatiSpecifici() {
        return datiSpecifici;
    }

    public void setDatiSpecifici(Map<String, DatoSpecifico> datiSpecifici) {
        this.datiSpecifici = datiSpecifici;
    }
}
