/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto;

import java.util.EnumSet;
import java.util.Set;

import it.eng.parer.fascicolo.beans.dto.base.IWSDesc;
import it.eng.parer.fascicolo.beans.utils.Costanti;

/**
 *
 * @author fioravanti_f
 */
public class VersFascicoloExt extends AbsVersFascicoloExt {

    private static final long serialVersionUID = 5261426459498072293L;
    private String datiXml;
    private boolean simulaScrittura;

    private transient IWSDesc descrizione;
    private Set<Costanti.ModificatoriWS> modificatoriWS = EnumSet.noneOf(Costanti.ModificatoriWS.class);

    @Override
    public void setDatiXml(String datiXml) {
        this.datiXml = datiXml;
    }

    @Override
    public String getDatiXml() {
        return datiXml;
    }

    @Override
    public boolean isSimulaScrittura() {
        return simulaScrittura;
    }

    @Override
    public void setSimulaScrittura(boolean simulaScrittura) {
        this.simulaScrittura = simulaScrittura;
    }

    @Override
    public IWSDesc getDescrizione() {
        return descrizione;
    }

    @Override
    public void setDescrizione(IWSDesc descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public Set<Costanti.ModificatoriWS> getModificatoriWSCalc() {
        return this.modificatoriWS;
    }

    public Set<Costanti.ModificatoriWS> getModificatoriWS() {
        return modificatoriWS;
    }

    public void setModificatoriWS(Set<Costanti.ModificatoriWS> modificatoriWS) {
        this.modificatoriWS = modificatoriWS;
    }
}
