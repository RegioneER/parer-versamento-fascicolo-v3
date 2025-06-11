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
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;
import it.eng.parer.fascicolo.beans.dto.base.CSVersatore;
import it.eng.parer.fascicolo.beans.dto.base.DatoSpecifico;
import it.eng.parer.fascicolo.beans.dto.base.IDatiSpecEntity;
import it.eng.parer.fascicolo.beans.dto.profile.arch.DatiXmlProfiloArchivistico;
import it.eng.parer.fascicolo.beans.dto.profile.gen.DatiXmlProfiloGenerale;
import it.eng.parer.fascicolo.beans.dto.profile.norm.DatiXmlProfiloNormativo;

/**
 *
 * @author fioravanti_f
 */
public class StrutturaVersFascicolo implements java.io.Serializable, IDatiSpecEntity {

    /**
     *
     */
    private static final long serialVersionUID = -4174982575943362236L;

    private Date dataVersamento;

    private long idStruttura;
    private long idUser;
    private long idTipoFascicolo;
    private long idAATipoFasc;
    private long idIamAbilTipoDato;
    private long idOrgEnteConv;
    //
    private long idRecXsdProfiloArchivistico;
    private long idRecxsdProfiloGenerale;
    private long idRecXsdProfiloNormativo;
    private long idRecXsdDatiSpec;
    private long idRecXsdDatiSpecMigrazione;
    private long idRecXsdUsoProfiloSpec;

    private boolean versatoreVerificato = false;
    private boolean warningFormatoNumero;

    private String versioneIndiceSipNonVerificata = "N/A";
    private String tipoFascicoloNonverificato;
    private String keyOrdCalcolata;
    private String urnPartChiaveFascicolo;
    private String urnPartChiaveFascicoloNormalized;
    private String cdKeyFascicoloNormalized;

    private DatiXmlProfiloGenerale datiXmlProfiloGenerale;
    private DatiXmlProfiloArchivistico datiXmlProfiloArchivistico;
    private DatiXmlProfiloNormativo datiXmlProfiloNormativo;

    private Map<String, DatoSpecifico> datiSpecifici;
    private Map<String, DatoSpecifico> datiSpecificiMigrazione;

    private CSVersatore versatoreNonverificato;
    private CSChiaveFasc chiaveNonVerificata;
    private FlControlliFasc flControlliFasc;
    private ConfigNumFasc configNumFasc;

    private List<UnitaDocLink> unitaDocElencate;
    private List<FascicoloLink> fascicoliLinked;

    private Long progressivoCalcolato;
    private Long idVoceTitol;
    private Long niSottoFascicoli;

    public Date getDataVersamento() {
	return dataVersamento;
    }

    public void setDataVersamento(Date dataVersamento) {
	this.dataVersamento = dataVersamento;
    }

    public long getIdStruttura() {
	return idStruttura;
    }

    public void setIdStruttura(long idStruttura) {
	this.idStruttura = idStruttura;
    }

    public long getIdUser() {
	return idUser;
    }

    public void setIdUser(long idUser) {
	this.idUser = idUser;
    }

    public long getIdTipoFascicolo() {
	return idTipoFascicolo;
    }

    public void setIdTipoFascicolo(long idTipoFascicolo) {
	this.idTipoFascicolo = idTipoFascicolo;
    }

    public long getIdAATipoFasc() {
	return idAATipoFasc;
    }

    public void setIdAATipoFasc(long idAATipoFasc) {
	this.idAATipoFasc = idAATipoFasc;
    }

    public long getIdIamAbilTipoDato() {
	return idIamAbilTipoDato;
    }

    public void setIdIamAbilTipoDato(long idIamAbilTipoDato) {
	this.idIamAbilTipoDato = idIamAbilTipoDato;
    }

    public boolean isVersatoreVerificato() {
	return versatoreVerificato;
    }

    public void setVersatoreVerificato(boolean versatoreVerificato) {
	this.versatoreVerificato = versatoreVerificato;
    }

    public String getVersioneIndiceSipNonVerificata() {
	return versioneIndiceSipNonVerificata;
    }

    public void setVersioneIndiceSipNonVerificata(String versioneIndiceSipNonVerificata) {
	this.versioneIndiceSipNonVerificata = versioneIndiceSipNonVerificata;
    }

    public String getTipoFascicoloNonverificato() {
	return tipoFascicoloNonverificato;
    }

    public void setTipoFascicoloNonverificato(String tipoFascicoloNonverificato) {
	this.tipoFascicoloNonverificato = tipoFascicoloNonverificato;
    }

    public CSVersatore getVersatoreNonverificato() {
	return versatoreNonverificato;
    }

    public void setVersatoreNonverificato(CSVersatore versatoreNonverificato) {
	this.versatoreNonverificato = versatoreNonverificato;
    }

    public CSChiaveFasc getChiaveNonVerificata() {
	return chiaveNonVerificata;
    }

    public void setChiaveNonVerificata(CSChiaveFasc chiaveNonVerificata) {
	this.chiaveNonVerificata = chiaveNonVerificata;
    }

    public String getUrnPartChiaveFascicolo() {
	return urnPartChiaveFascicolo;
    }

    public void setUrnPartChiaveFascicolo(String urnPartChiaveFascicolo) {
	this.urnPartChiaveFascicolo = urnPartChiaveFascicolo;
    }

    public List<UnitaDocLink> getUnitaDocElencate() {
	return unitaDocElencate;
    }

    public void setUnitaDocElencate(List<UnitaDocLink> unitaDocElencate) {
	this.unitaDocElencate = unitaDocElencate;
    }

    public long getIdRecXsdProfiloArchivistico() {
	return idRecXsdProfiloArchivistico;
    }

    public void setIdRecXsdProfiloArchivistico(long idRecXsdProfiloArchivistico) {
	this.idRecXsdProfiloArchivistico = idRecXsdProfiloArchivistico;
    }

    public long getIdRecxsdProfiloGenerale() {
	return idRecxsdProfiloGenerale;
    }

    public void setIdRecxsdProfiloGenerale(long idRecxsdProfiloGenerale) {
	this.idRecxsdProfiloGenerale = idRecxsdProfiloGenerale;
    }

    public DatiXmlProfiloGenerale getDatiXmlProfiloGenerale() {
	return datiXmlProfiloGenerale;
    }

    public void setDatiXmlProfiloGenerale(DatiXmlProfiloGenerale datiXmlProfiloGenerale) {
	this.datiXmlProfiloGenerale = datiXmlProfiloGenerale;
    }

    public DatiXmlProfiloArchivistico getDatiXmlProfiloArchivistico() {
	return datiXmlProfiloArchivistico;
    }

    public void setDatiXmlProfiloArchivistico(
	    DatiXmlProfiloArchivistico datiXmlProfiloArchivistico) {
	this.datiXmlProfiloArchivistico = datiXmlProfiloArchivistico;
    }

    @Override
    public Map<String, DatoSpecifico> getDatiSpecifici() {
	if (datiSpecifici == null) {
	    datiSpecifici = new HashMap<>();
	}
	return datiSpecifici;
    }

    @Override
    public void setDatiSpecifici(Map<String, DatoSpecifico> datiSpecifici) {
	this.datiSpecifici = datiSpecifici;
    }

    @Override
    public Map<String, DatoSpecifico> getDatiSpecificiMigrazione() {
	if (datiSpecificiMigrazione == null) {
	    datiSpecificiMigrazione = new HashMap<>();
	}
	return datiSpecificiMigrazione;
    }

    @Override
    public void setDatiSpecificiMigrazione(Map<String, DatoSpecifico> datiSpecificiMigrazione) {
	this.datiSpecificiMigrazione = datiSpecificiMigrazione;
    }

    @Override
    public long getIdRecXsdDatiSpec() {
	return idRecXsdDatiSpec;
    }

    @Override
    public long getIdRecXsdDatiSpecMigrazione() {
	return idRecXsdDatiSpecMigrazione;
    }

    @Override
    public void setIdRecXsdDatiSpec(long idRecXsdDatiSpec) {
	this.idRecXsdDatiSpec = idRecXsdDatiSpec;
    }

    @Override
    public void setIdRecXsdDatiSpecMigrazione(long idRecXsdDatiSpec) {
	this.idRecXsdDatiSpecMigrazione = idRecXsdDatiSpec;
    }

    public FlControlliFasc getFlControlliFasc() {
	return flControlliFasc;
    }

    public void setFlControlliFasc(FlControlliFasc flControlliFasc) {
	this.flControlliFasc = flControlliFasc;
    }

    public ConfigNumFasc getConfigNumFasc() {
	return configNumFasc;
    }

    public void setConfigNumFasc(ConfigNumFasc configNumFasc) {
	this.configNumFasc = configNumFasc;
    }

    public Long getProgressivoCalcolato() {
	return progressivoCalcolato;
    }

    public void setProgressivoCalcolato(Long progressivoCalcolato) {
	this.progressivoCalcolato = progressivoCalcolato;
    }

    public boolean isWarningFormatoNumero() {
	return warningFormatoNumero;
    }

    public void setWarningFormatoNumero(boolean warningFormatoNumero) {
	this.warningFormatoNumero = warningFormatoNumero;
    }

    public String getKeyOrdCalcolata() {
	return keyOrdCalcolata;
    }

    public void setKeyOrdCalcolata(String keyOrdCalcolata) {
	this.keyOrdCalcolata = keyOrdCalcolata;
    }

    public List<FascicoloLink> getFascicoliLinked() {
	return fascicoliLinked;
    }

    public void setFascicoliLinked(List<FascicoloLink> fascicoliLinked) {
	this.fascicoliLinked = fascicoliLinked;
    }

    public long getIdOrgEnteConv() {
	return idOrgEnteConv;
    }

    public void setIdOrgEnteConv(long idOrgEnteConv) {
	this.idOrgEnteConv = idOrgEnteConv;
    }

    public Long getIdVoceTitol() {
	return idVoceTitol;
    }

    public void setIdVoceTitol(Long idVoceTitol) {
	this.idVoceTitol = idVoceTitol;
    }

    public String getUrnPartChiaveFascicoloNormalized() {
	return urnPartChiaveFascicoloNormalized;
    }

    public void setUrnPartChiaveFascicoloNormalized(String urnPartChiaveFascicoloNormalized) {
	this.urnPartChiaveFascicoloNormalized = urnPartChiaveFascicoloNormalized;
    }

    public Long getNiSottoFascicoli() {
	return niSottoFascicoli;
    }

    public void setNiSottoFascicoli(Long niSottoFascicoli) {
	this.niSottoFascicoli = niSottoFascicoli;
    }

    public long getIdRecXsdProfiloNormativo() {
	return idRecXsdProfiloNormativo;
    }

    public void setIdRecXsdProfiloNormativo(long idRecXsdProfiloNormativo) {
	this.idRecXsdProfiloNormativo = idRecXsdProfiloNormativo;
    }

    public DatiXmlProfiloNormativo getDatiXmlProfiloNormativo() {
	return datiXmlProfiloNormativo;
    }

    public void setDatiXmlProfiloNormativo(DatiXmlProfiloNormativo datiXmlProfiloNormativo) {
	this.datiXmlProfiloNormativo = datiXmlProfiloNormativo;
    }

    public String getCdKeyFascicoloNormalized() {
	return cdKeyFascicoloNormalized;
    }

    public void setCdKeyFascicoloNormalized(String cdKeyFascicoloNormalized) {
	this.cdKeyFascicoloNormalized = cdKeyFascicoloNormalized;
    }

    public long getIdRecXsdUsoProfiloSpec() {
	return idRecXsdUsoProfiloSpec;
    }

    public void setIdRecXsdUsoProfiloSpec(long idRecXsdUsoProfiloSpec) {
	this.idRecXsdUsoProfiloSpec = idRecXsdUsoProfiloSpec;
    }

}
