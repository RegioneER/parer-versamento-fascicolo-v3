/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto.base;

import java.util.Set;

import it.eng.parer.fascicolo.beans.utils.Costanti;

public interface IRestWSBase {

    String getDatiXml();

    void setDatiXml(String datiXml);

    public IWSDesc getDescrizione();

    public void setDescrizione(IWSDesc descrizione);

    public String getLoginName();

    public void setLoginName(String loginName);

    public String getVersioneWsChiamata();

    public void setVersioneWsChiamata(String versioneWsChiamata);

    public Set<Costanti.ModificatoriWS> getModificatoriWSCalc();
}
