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
public interface IDatiSpecEntity {

    public long getIdRecXsdDatiSpec();

    public void setIdRecXsdDatiSpec(long idRecXsdDatiSpec);

    public Map<String, DatoSpecifico> getDatiSpecifici();

    public void setDatiSpecifici(Map<String, DatoSpecifico> datiSpecifici);

    public long getIdRecXsdDatiSpecMigrazione();

    public void setIdRecXsdDatiSpecMigrazione(long idRecXsdDatiSpec);

    public Map<String, DatoSpecifico> getDatiSpecificiMigrazione();

    public void setDatiSpecificiMigrazione(Map<String, DatoSpecifico> datiSpecificiMigrazione);
}
