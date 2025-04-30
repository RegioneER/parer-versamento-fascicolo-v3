/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto;

import it.eng.parer.fascicolo.beans.dto.base.IWSDesc;
import it.eng.parer.fascicolo.beans.utils.Costanti;

/**
 *
 * @author fioravanti_f
 */
public class WSDescVersFascicolo implements IWSDesc {

    @Override
    public String getNomeWs() {
        return Costanti.WS_VERS_FASCICOLO_NOME;
    }

}
