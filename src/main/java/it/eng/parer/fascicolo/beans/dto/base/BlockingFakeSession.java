/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto.base;

import java.time.ZonedDateTime;

import static it.eng.parer.fascicolo.beans.utils.Costanti.CLCR_REGEXP;
import it.eng.parer.ws.xml.versfascicoloV3.IndiceSIPFascicolo;

public class BlockingFakeSession implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 8890894113138332183L;
    private boolean salvaSessione = true;
    private String tipoDatiSessioneVers;
    private String ipChiamante;
    private String loginName;
    private String password;
    private long idUser = 0;
    private String versioneWS;
    //
    private String datiIndiceSipXml;
    private String urnIndiceSipXml;
    private String hashIndiceSipXml;
    private String datiDaSalvareIndiceSip;
    //
    private String datiPackInfoSipXml;
    private String urnPackInfoSipXml;
    private String hashPackInfoSipXml;
    private String datiC14NPackInfoSipXml;
    //
    private String urnEsitoVersamento;
    //
    private String datiRapportoVersamento;
    private String urnRapportoVersamento;
    private String hashRapportoVersamento;
    //
    private boolean xmlOk;
    private ZonedDateTime tmApertura;
    private ZonedDateTime tmChiusura;
    //
    private transient IndiceSIPFascicolo indiceSIPFascicolo;

    /**
     * Costruttore
     */
    public BlockingFakeSession() {
	xmlOk = false;
    }

    /*
     *
     */
    /**
     * @return the salvaSessione
     */
    public boolean isSalvaSessione() {
	return salvaSessione;
    }

    /**
     * @param salvaSessione the salvaSessione to set
     */
    public void setSalvaSessione(boolean salvaSessione) {
	this.salvaSessione = salvaSessione;
    }

    /**
     * @return the tipoDatiSessioneVers
     */
    public String getTipoDatiSessioneVers() {
	return tipoDatiSessioneVers;
    }

    /**
     * @param tipoDatiSessioneVers the tipoDatiSessioneVers to set
     */
    public void setTipoDatiSessioneVers(String tipoDatiSessioneVers) {
	this.tipoDatiSessioneVers = tipoDatiSessioneVers;
    }

    public String getIpChiamante() {
	return ipChiamante;
    }

    public void setIpChiamante(String ipChiamante) {
	this.ipChiamante = ipChiamante;
    }

    /**
     * @return the loginName
     */
    public String getLoginName() {
	return loginName;
    }

    /**
     * Nota: la variabile inserita viene pulita di tutti gli eventuali caratteri di controllo
     *
     * @param loginName the loginName to set
     */
    public void setLoginName(String loginName) {
	this.loginName = loginName.replaceAll(CLCR_REGEXP, "");
    }

    /**
     * @return the password
     */
    public String getPassword() {
	return password;
    }

    /**
     * Nota: la variabile inserita viene pulita di tutti gli eventuali caratteri di controllo
     *
     * @param password the password to set
     */
    public void setPassword(String password) {
	this.password = password.replaceAll(CLCR_REGEXP, "");
    }

    /**
     * @return the idUser
     */
    public long getIdUser() {
	return idUser;
    }

    /**
     * @param idUser the idUser to set
     */
    public void setIdUser(long idUser) {
	this.idUser = idUser;
    }

    /**
     * @return the versioneWS
     */
    public String getVersioneWS() {
	return versioneWS;
    }

    /**
     * Nota: la variabile inserita viene pulita di tutti gli eventuali caratteri di controllo
     *
     * @param versioneWS the versioneWS to set
     */
    public void setVersioneWS(String versioneWS) {
	this.versioneWS = versioneWS.replaceAll(CLCR_REGEXP, "");
    }

    /**
     * @return the datiIndiceSipXml
     */
    public String getDatiIndiceSipXml() {
	return datiIndiceSipXml;
    }

    /**
     * Nota: la variabile inserita viene pulita di tutti gli eventuali caratteri di controllo
     *
     * @param datiIndiceSipXml the datiIndiceSipXml to set
     */
    public void setDatiIndiceSipXml(String datiIndiceSipXml) {
	this.datiIndiceSipXml = datiIndiceSipXml.replaceAll(CLCR_REGEXP, "");
    }

    public String getUrnIndiceSipXml() {
	return urnIndiceSipXml;
    }

    public void setUrnIndiceSipXml(String urnIndiceSipXml) {
	this.urnIndiceSipXml = urnIndiceSipXml;
    }

    public String getHashIndiceSipXml() {
	return hashIndiceSipXml;
    }

    public void setHashIndiceSipXml(String hashIndiceSipXml) {
	this.hashIndiceSipXml = hashIndiceSipXml;
    }

    public String getDatiDaSalvareIndiceSip() {
	return datiDaSalvareIndiceSip;
    }

    public void setDatiDaSalvareIndiceSip(String datiDaSalvareIndiceSip) {
	this.datiDaSalvareIndiceSip = datiDaSalvareIndiceSip;
    }

    public String getDatiPackInfoSipXml() {
	return datiPackInfoSipXml;
    }

    /**
     * Nota: la variabile inserita viene pulita di tutti gli eventuali caratteri di controllo
     *
     * @param datiPackInfoSipXml the datiIndiceSipXml to set
     */
    public void setDatiPackInfoSipXml(String datiPackInfoSipXml) {
	this.datiPackInfoSipXml = datiPackInfoSipXml.replaceAll(CLCR_REGEXP, "");
    }

    public String getUrnPackInfoSipXml() {
	return urnPackInfoSipXml;
    }

    public void setUrnPackInfoSipXml(String urnPackInfoSipXml) {
	this.urnPackInfoSipXml = urnPackInfoSipXml;
    }

    public String getHashPackInfoSipXml() {
	return hashPackInfoSipXml;
    }

    public void setHashPackInfoSipXml(String hashPackInfoSipXml) {
	this.hashPackInfoSipXml = hashPackInfoSipXml;
    }

    public String getUrnEsitoVersamento() {
	return urnEsitoVersamento;
    }

    public void setUrnEsitoVersamento(String urnEsitoVersamento) {
	this.urnEsitoVersamento = urnEsitoVersamento;
    }

    public String getDatiRapportoVersamento() {
	return datiRapportoVersamento;
    }

    public void setDatiRapportoVersamento(String datiRapportoVersamento) {
	this.datiRapportoVersamento = datiRapportoVersamento;
    }

    public String getUrnRapportoVersamento() {
	return urnRapportoVersamento;
    }

    public void setUrnRapportoVersamento(String urnRapportoVersamento) {
	this.urnRapportoVersamento = urnRapportoVersamento;
    }

    public String getHashRapportoVersamento() {
	return hashRapportoVersamento;
    }

    public void setHashRapportoVersamento(String hashRapportoVersamento) {
	this.hashRapportoVersamento = hashRapportoVersamento;
    }

    public boolean isXmlOk() {
	return xmlOk;
    }

    public void setXmlOk(boolean xmlOk) {
	this.xmlOk = xmlOk;
    }

    public ZonedDateTime getTmApertura() {
	return tmApertura;
    }

    public void setTmApertura(ZonedDateTime tmApertura) {
	this.tmApertura = tmApertura;
    }

    public ZonedDateTime getTmChiusura() {
	return tmChiusura;
    }

    public void setTmChiusura(ZonedDateTime tmChiusura) {
	this.tmChiusura = tmChiusura;
    }

    public String getDatiC14NPackInfoSipXml() {
	return datiC14NPackInfoSipXml;
    }

    public void setDatiC14NPackInfoSipXml(String datiC14NPackInfoSipXml) {
	this.datiC14NPackInfoSipXml = datiC14NPackInfoSipXml;
    }

    public IndiceSIPFascicolo getIndiceSIPFascicolo() {
	return indiceSIPFascicolo;
    }

    public void setIndiceSIPFascicolo(IndiceSIPFascicolo indiceSIPFascicolo) {
	this.indiceSIPFascicolo = indiceSIPFascicolo;
    }
}
