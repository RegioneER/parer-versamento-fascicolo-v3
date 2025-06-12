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
package it.eng.parer.fascicolo.beans.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

import it.eng.parer.fascicolo.beans.IControlliCollFascicoloService;
import it.eng.parer.fascicolo.beans.IControlliFascicoliService;
import it.eng.parer.fascicolo.beans.IControlliSemanticiService;
import it.eng.parer.fascicolo.beans.dto.FascicoloLink;
import it.eng.parer.fascicolo.beans.dto.StrutturaVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.UnitaDocLink;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.CSChiave;
import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.dto.profile.arch.DXPAFascicoloCollegato;
import it.eng.parer.fascicolo.beans.utils.Costanti;
import it.eng.parer.fascicolo.beans.utils.Costanti.TipiGestioneUDAnnullate;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSFormat;
import it.eng.parer.fascicolo.beans.utils.xml.XmlDateUtility;
import it.eng.parer.ws.xml.versfascicoloV3.IndiceSIPFascicolo;
import it.eng.parer.ws.xml.versfascicoloV3.UnitaDocumentariaType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECEsitoContenutoFascicoloType;
import it.eng.parer.ws.xml.versfascicolorespV3.ECFascicoloType;
import it.eng.parer.ws.xml.versfascicolorespV3.SCChiaveUDType;
import it.eng.parer.ws.xml.versfascicolorespV3.SCContenutoType;
import it.eng.parer.ws.xml.versfascicolorespV3.SCUDTypeNonPresenti;
import it.eng.parer.ws.xml.versfascicolorespV3.SCUDTypePresenti;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class ControlliCollFascicoloService implements IControlliCollFascicoloService {

    @Inject
    EntityManager entityManager;

    @Inject
    IControlliSemanticiService controlliSemanticiService;

    @Inject
    IControlliFascicoliService controlliFascicoliService;

    @Override
    public boolean verificaUdFascicolo(VersFascicoloExt versamento, ECFascicoloType fascicoloResp,
	    BlockingFakeSession syncFakeSession) {
	//
	boolean result = false;

	IndiceSIPFascicolo parsedIndiceFasc = syncFakeSession.getIndiceSIPFascicolo();
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();

	int numeroUd = parsedIndiceFasc.getContenuto().getUnitaDocumentarie()
		.getNumeroUnitaDocumentarie();
	// popolo il tag di esito ContenutoSintetico
	fascicoloResp.setContenuto(new SCContenutoType());
	fascicoloResp.getContenuto().setNumeroUnitaDocumentarie(numeroUd);

	// verifico che il numero dichiarato e il numero di tag UnitaDocumentaria
	// coincidano
	if (parsedIndiceFasc.getContenuto().getUnitaDocumentarie().getDettaglioUnitaDocumentarie()
		.getUnitaDocumentaria().size() != numeroUd) {
	    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
		    MessaggiWSBundle.FASC_003_001, svf.getUrnPartChiaveFascicolo(), numeroUd);
	    return false;
	}

	// verifica univocità unita documenterie all'interno della lista
	Set<Entry<List<Object>, List<UnitaDocumentariaType>>> udsKeysNonUnique = udsSameKey(
		parsedIndiceFasc.getContenuto().getUnitaDocumentarie()
			.getDettaglioUnitaDocumentarie().getUnitaDocumentaria());
	if (!udsKeysNonUnique.isEmpty()) {
	    StringJoiner keysNonUnique = new StringJoiner(" , ");
	    for (Iterator<Entry<List<Object>, List<UnitaDocumentariaType>>> it = udsKeysNonUnique
		    .iterator(); it.hasNext();) {
		Entry<List<Object>, List<UnitaDocumentariaType>> entry = it.next();
		keysNonUnique.add(StringUtils.join(entry.getKey(), "-"));
	    }
	    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
		    MessaggiWSBundle.FASC_003_005, svf.getUrnPartChiaveFascicolo(),
		    keysNonUnique.toString());
	    return false;
	}

	// verifico che la posizione non sia ripetuta
	List<UnitaDocumentariaType> udsPosNonUnique = udsSamePosition(
		parsedIndiceFasc.getContenuto().getUnitaDocumentarie()
			.getDettaglioUnitaDocumentarie().getUnitaDocumentaria());
	if (!udsPosNonUnique.isEmpty()) {
	    StringJoiner posNonUnique = new StringJoiner(" , ");
	    for (UnitaDocumentariaType ud : udsPosNonUnique) {
		posNonUnique.add(StringUtils
			.join(Arrays.asList(ud.getRegistro(), ud.getAnno(), ud.getNumero()), "-"));
	    }
	    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
		    MessaggiWSBundle.FASC_003_004, svf.getUrnPartChiaveFascicolo(),
		    posNonUnique.toString());
	    return false;
	}

	// verifico ogni singolo collegamento
	List<UnitaDocLink> unitaDocs = new ArrayList<>();
	boolean trovatiErrori = false;
	// prima preparo il tag ControlliContenutoFascicolo
	ECEsitoContenutoFascicoloType ececft = new ECEsitoContenutoFascicoloType();
	SCUDTypePresenti presenti = new SCUDTypePresenti();
	SCUDTypeNonPresenti nonPresenti = new SCUDTypeNonPresenti();
	ececft.setUnitaDocumentariePresenti(presenti);
	ececft.setUnitaDocumentarieNonPresenti(nonPresenti);
	fascicoloResp.setControlliContenutoFascicolo(ececft);

	for (UnitaDocumentariaType ud : parsedIndiceFasc.getContenuto().getUnitaDocumentarie()
		.getDettaglioUnitaDocumentarie().getUnitaDocumentaria()) {
	    // dto
	    UnitaDocLink udColl = new UnitaDocLink();
	    // ud key
	    CSChiave tmpChiaveUd = new CSChiave();
	    tmpChiaveUd.setTipoRegistro(ud.getRegistro());
	    tmpChiaveUd.setAnno(Long.valueOf(ud.getAnno()));
	    tmpChiaveUd.setNumero(ud.getNumero());
	    String descChiaveUdColl = MessaggiWSFormat.formattaUrnPartUnitaDoc(tmpChiaveUd);
	    // preparo la chiave UD da rendere nell'esito di ControlliContenutoFascicolo
	    SCChiaveUDType sCChiaveUDType = new SCChiaveUDType();
	    sCChiaveUDType.setRegistro(ud.getRegistro());
	    sCChiaveUDType.setAnno(ud.getAnno());
	    sCChiaveUDType.setNumero(ud.getNumero());

	    RispostaControlli rcCheckChiave = controlliSemanticiService.checkChiave(tmpChiaveUd,
		    versamento.getStrutturaComponenti().getIdStruttura(),
		    TipiGestioneUDAnnullate.CONSIDERA_ASSENTE);
	    if (rcCheckChiave.getrLong() == -1
		    || rcCheckChiave.getCodErr().equals(MessaggiWSBundle.UD_012_002)) { // se
		// non
		// ha
		// trovato
		// la
		// chiave...oppure esiste
		// ma ANNULLATA
		trovatiErrori = true;
		if (rcCheckChiave.isrBoolean()) { // se in ogni caso la query è andata a buon fine
		    // creo il nuovo errore da aggiungere alla lista
		    versamento.listErrAddError(svf.getUrnPartChiaveFascicolo(),
			    MessaggiWSBundle.FASC_003_003, svf.getUrnPartChiaveFascicolo(),
			    descChiaveUdColl);
		    // inoltre la aggiungo alla lista di esito delle UD non presenti
		    nonPresenti.getUnitaDocumentaria().add(sCChiaveUDType);
		} else {
		    // allora è un errore di database..
		    // non ha trovato la chiave ma non ha impostato a true
		    // il bool di rispostacontrolli
		    versamento.addError(svf.getUrnPartChiaveFascicolo(), rcCheckChiave.getCodErr(),
			    rcCheckChiave.getDsErr());
		    break;
		}
	    } else {
		// on dto
		udColl.setIdLinkUnitaDoc(rcCheckChiave.getrLong());
		udColl.setCsChiave(tmpChiaveUd);
		// non mandatory
		udColl.setPosizione(ud.getPosizione());
		// non mandatory
		udColl.setDataInserimentoFas(XmlDateUtility
			.xmlGregorianCalendarToDateOrNull(ud.getDataInserimentoFascicolo()));
		// se l'ha trovata, la aggiunge alla lista delle UD trovate
		unitaDocs.add(udColl);
		presenti.getUnitaDocumentaria().add(sCChiaveUDType);
	    }
	}
	//
	presenti.setNumeroUnitaDocumentariePresenti(
		BigInteger.valueOf(presenti.getUnitaDocumentaria().size()));
	nonPresenti.setNumeroUnitaDocumentarieNonPresenti(
		BigInteger.valueOf(nonPresenti.getUnitaDocumentaria().size()));
	//
	if (!trovatiErrori) {
	    result = true;
	    svf.setUnitaDocElencate(unitaDocs);
	}
	return result;
    }

    /*
     * Restituisce una lista (nel peggiore dei casi vuota) di fascicoli da inserire (non
     * controllati)
     */
    @Override
    public RispostaControlli buildCollegamentiFascicolo(VersFascicoloExt versamento,
	    ECFascicoloType.EsitoControlliFascicolo myControlliFascicolo) {

	RispostaControlli rcCollegamentiFasc = new RispostaControlli();
	rcCollegamentiFasc.setrBoolean(false);

	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	// composizione dei fascicoli da collegare non verificati (no fk su id
	// fascicolo)
	List<FascicoloLink> fascicoliToBeLinked = new ArrayList<>(0);

	/*
	 * vengono costruite due liste: duplicated -> chiavi duplicate notDuplicated -> chiavi non
	 * duplicate
	 *
	 * obiettivo: costrutire un'unica lista da processare successivamente ossia
	 * fascicoliToBeLinked
	 */

	Set<CSChiaveFasc> setUniqueCsFKeys = new HashSet<>(0); // utile per verificare i duplicati
	Set<CSChiaveFasc> setDuplicateCsFKeys = new HashSet<>(0); // utile per verificare i
								  // duplicati

	// build dei duplicati
	for (DXPAFascicoloCollegato el : svf.getDatiXmlProfiloArchivistico().getFascCollegati()) {
	    CSChiaveFasc csKey = el.getCsChiaveFasc();
	    if (!setUniqueCsFKeys.add(csKey)) { // se già presente in lista
		// add on duplicates
		setDuplicateCsFKeys.add(csKey);
	    }
	}

	// sui duplicati si ottiene una descrizione completa (con separatore ";")
	for (Iterator<CSChiaveFasc> it = setDuplicateCsFKeys.iterator(); it.hasNext();) {
	    CSChiaveFasc csKey = it.next();
	    StringBuilder sb = new StringBuilder(0);
	    Set<String> setUniqueDesc = new HashSet<>(0);
	    for (DXPAFascicoloCollegato coll : svf.getDatiXmlProfiloArchivistico()
		    .getFascCollegati()) {
		if (coll.getCsChiaveFasc().equals(csKey)
			&& StringUtils.isNotBlank(coll.getDescCollegamento())
			&& setUniqueDesc.add(coll.getDescCollegamento())) {
		    // tutte le descrizioni diverse (a parità di chiave) vengono inserite con il
		    // carattere ";"
		    sb.append(coll.getDescCollegamento());
		    sb.append(Costanti.COLLEGAMENTO_DESC_SEP);
		}
	    }
	    // se ha inserito qualcosa come descrizione finale per quella chiave
	    if (sb.length() > 0) {
		// rimuove ultimo separatore ";" se esiste
		int ind = sb.lastIndexOf(Costanti.COLLEGAMENTO_DESC_SEP);
		if (ind >= 0) {
		    sb = sb.deleteCharAt(ind);
		}
	    }
	    // finally .... create a new one and add on list
	    FascicoloLink coll = new FascicoloLink();
	    coll.setCsChiaveFasc(csKey);
	    coll.setDescCollegamento(sb.toString());

	    // verifica lunghezza massima descrizione
	    if (coll.getDescCollegamento().length() > Costanti.COLLEGAMENTO_DESC_MAX_SIZE) {
		rcCollegamentiFasc.setCodErr(MessaggiWSBundle.FASC_006_002);
	    }

	    // aggiunge in lista
	    fascicoliToBeLinked.add(coll);

	    // remove from unique
	    if (setUniqueCsFKeys.contains(csKey)) {
		setUniqueCsFKeys.remove(csKey);
	    }
	}

	// inserisce i collegamenti con chiave univoca (nessun problema sulla
	// descrizione "doppia") "ripescandoli" dalla
	// lista dei collegamenti
	for (Iterator<CSChiaveFasc> it = setUniqueCsFKeys.iterator(); it.hasNext();) {
	    CSChiaveFasc csKeyUnique = it.next();
	    for (DXPAFascicoloCollegato coll : svf.getDatiXmlProfiloArchivistico()
		    .getFascCollegati()) {
		if (coll.getCsChiaveFasc().equals(csKeyUnique)) {
		    // aggiunge in lista
		    FascicoloLink link = new FascicoloLink();
		    link.setCsChiaveFasc(coll.getCsChiaveFasc());
		    link.setDescCollegamento(coll.getDescCollegamento());
		    fascicoliToBeLinked.add(link);

		    // verifica lunghezza massima descrizione
		    if (coll.getDescCollegamento().length() > Costanti.COLLEGAMENTO_DESC_MAX_SIZE) {
			rcCollegamentiFasc.setCodErr(MessaggiWSBundle.FASC_006_002);
		    }
		}
	    }
	}

	rcCollegamentiFasc.setrObject(fascicoliToBeLinked);// lo setto in ogni caso
	if (!fascicoliToBeLinked.isEmpty()) {
	    rcCollegamentiFasc.setrBoolean(true);
	}

	return rcCollegamentiFasc;
    }

    private List<UnitaDocumentariaType> udsSamePosition(List<UnitaDocumentariaType> uds) {
	return uds.stream().filter(u -> u.getPosizione() != null)
		.collect(Collectors.groupingBy(UnitaDocumentariaType::getPosizione)).entrySet()
		.stream().filter(u -> u.getValue().size() > 1).flatMap(e -> e.getValue().stream())
		.collect(Collectors.toList());
    }

    // non unique registro / anno / numero
    private Set<Entry<List<Object>, List<UnitaDocumentariaType>>> udsSameKey(
	    List<UnitaDocumentariaType> uds) {
	Function<UnitaDocumentariaType, List<Object>> compositeKey = udRecord -> Arrays
		.<Object>asList(udRecord.getRegistro(), udRecord.getAnno(), udRecord.getNumero());
	return uds.stream().collect(Collectors.groupingBy(compositeKey, Collectors.toList()))
		.entrySet().stream().filter(u -> u.getValue().size() > 1)
		.collect(Collectors.toSet());

    }

}
