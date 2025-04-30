/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto.base;

import java.util.Map;

import it.eng.parer.fascicolo.beans.utils.VerificaVersione;

/**
 *
 * @author Fioravanti_F
 */
public interface IWSDesc {

    String getNomeWs();

    default String getVersione(Map<String, String> mapWsVersion) { // versione standard, senza modifiche indotte
                                                                   // dalla versione chiamata
        return VerificaVersione.latestVersion(getNomeWs(), mapWsVersion);
    }

    // public String[] getCompatibilitaWS(); // lista di versioni compatibili con il parser
}
