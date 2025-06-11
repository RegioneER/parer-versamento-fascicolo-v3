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

import static it.eng.parer.fascicolo.beans.utils.converter.DateUtilsConverter.convert;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.StringEscapeUtils;

import it.eng.parer.fascicolo.beans.IConfigurationDao;
import it.eng.parer.fascicolo.beans.IControlliFascicoliService;
import it.eng.parer.fascicolo.beans.dto.ConfigNumFasc;
import it.eng.parer.fascicolo.beans.dto.FlControlliFasc;
import it.eng.parer.fascicolo.beans.dto.StrutturaVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.dto.profile.arch.DXPAVoceClassificazione;
import it.eng.parer.fascicolo.beans.dto.profile.arch.DatiXmlProfiloArchivistico;
import it.eng.parer.fascicolo.beans.utils.Costanti;
import it.eng.parer.fascicolo.beans.utils.Costanti.TipiGestioneFascAnnullati;
import it.eng.parer.fascicolo.beans.utils.CostantiDB;
import it.eng.parer.fascicolo.beans.utils.KeyOrdFascUtility.TipiCalcolo;
import it.eng.parer.fascicolo.beans.utils.ParametroApplDB.ParametroApplFl;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.fascicolo.jpa.entity.DecAaTipoFascicolo;
import it.eng.parer.fascicolo.jpa.entity.DecLivelloTitol;
import it.eng.parer.fascicolo.jpa.entity.DecParteNumeroFascicolo;
import it.eng.parer.fascicolo.jpa.entity.DecTipoFascicolo;
import it.eng.parer.fascicolo.jpa.entity.DecTitol;
import it.eng.parer.fascicolo.jpa.entity.DecValVoceTitol;
import it.eng.parer.fascicolo.jpa.entity.DecVoceTitol;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasXmlVersFascicolo;
import it.eng.parer.fascicolo.jpa.entity.IamAbilTipoDato;
import it.eng.parer.fascicolo.jpa.entity.OrgStrut;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasFascicolo.TiStatoConservazione;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@SuppressWarnings("unchecked")
@ApplicationScoped
public class ControlliFascicoliService implements IControlliFascicoliService {

    @Inject
    EntityManager entityManager;

    @Inject
    IConfigurationDao configurationDao;

    @Inject
    IControlliFascicoliService me;

    @Override
    @Transactional
    public RispostaControlli checkTipoFascicolo(String nomeTipoFascicolo, String descKey,
	    long idStruttura) {
	RispostaControlli rcCheckTipoFascicolo = new RispostaControlli();
	rcCheckTipoFascicolo.setrBoolean(false);

	List<DecTipoFascicolo> decTipoFascicolos = null;

	try {
	    final String queryStr = "select tf from DecTipoFascicolo tf " + "join tf.orgStrut org "
		    + "where org.idStrut = :idStrutIn "
		    + "and upper(tf.nmTipoFascicolo) = :nmTipoFascicolo "
		    + " and tf.dtIstituz <= :dataDiOggiIn " + " and tf.dtSoppres > :dataDiOggiIn "; // da
												    // notare
	    // STRETTAMENTE
	    // MAGGIORE della
	    // data
	    // di
	    Query query = entityManager.createQuery(queryStr, DecTipoFascicolo.class);
	    query.setParameter("idStrutIn", idStruttura);
	    query.setParameter("nmTipoFascicolo", nomeTipoFascicolo.toUpperCase());
	    query.setParameter("dataDiOggiIn", LocalDateTime.now());
	    decTipoFascicolos = query.getResultList();

	    if (decTipoFascicolos.size() == 1) {
		// se trovato, rendo l'id del tipo fascicolo
		rcCheckTipoFascicolo.setrLong(decTipoFascicolos.get(0).getIdTipoFascicolo());
		rcCheckTipoFascicolo.setrBoolean(true);
	    } else {
		rcCheckTipoFascicolo.setCodErr(MessaggiWSBundle.FASC_002_001);
		rcCheckTipoFascicolo.setDsErr(MessaggiWSBundle
			.getString(MessaggiWSBundle.FASC_002_001, descKey, nomeTipoFascicolo));
	    }
	} catch (Exception e) {
	    rcCheckTipoFascicolo.setCodErr(MessaggiWSBundle.ERR_666);
	    rcCheckTipoFascicolo.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliFascicoliService.checkTipoFascicolo: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return rcCheckTipoFascicolo;
    }

    @Override
    @Transactional
    public RispostaControlli checkTipoFascicoloIamUserOrganizzazione(String descKey,
	    String tipoFasc, long idStruttura, long idUser, long idTipoFasc) {
	RispostaControlli rcCheckTipoFascIamUserOrg = new RispostaControlli();
	rcCheckTipoFascIamUserOrg.setrBoolean(false);

	List<IamAbilTipoDato> iamAbilTipoDatos = null;

	try {
	    final String queryStr = "select td from IamAbilTipoDato td "
		    + "join td.iamAbilOrganiz org " + "join td.iamAbilOrganiz.iamUser iu "
		    + "where iu.idUserIam = :idUserIam "
		    + "and org.idOrganizApplic = :idOrganizApplic  "
		    + "and td.idTipoDatoApplic = :idTipoFasc  "
		    + "and td.nmClasseTipoDato = 'TIPO_FASCICOLO'  ";
	    Query query = entityManager.createQuery(queryStr, IamAbilTipoDato.class);
	    query.setParameter("idOrganizApplic", new BigDecimal(idStruttura));
	    query.setParameter("idUserIam", idUser);
	    query.setParameter("idTipoFasc", new BigDecimal(idTipoFasc));

	    iamAbilTipoDatos = query.getResultList();

	    // ottengo un risultato -> abilitato al tipo fascicolo
	    if (iamAbilTipoDatos.size() == 1) {
		rcCheckTipoFascIamUserOrg.setrLong(iamAbilTipoDatos.get(0).getIdAbilTipoDato());
		rcCheckTipoFascIamUserOrg.setrBoolean(true);
	    } else {
		rcCheckTipoFascIamUserOrg.setCodErr(MessaggiWSBundle.FASC_002_002);
		rcCheckTipoFascIamUserOrg.setDsErr(MessaggiWSBundle
			.getString(MessaggiWSBundle.FASC_002_002, descKey, tipoFasc));
	    }
	} catch (Exception e) {
	    rcCheckTipoFascIamUserOrg.setCodErr(MessaggiWSBundle.ERR_666);
	    rcCheckTipoFascIamUserOrg.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliFascicoliService.checkTipoFascicoloOrganizzazione: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return rcCheckTipoFascIamUserOrg;
    }

    @Override
    @Transactional
    public RispostaControlli checkTipoFascicoloAnno(String descChiaveFasc, String tipoFasc,
	    Integer anno, long idTipoFascicolo) {
	RispostaControlli rcCheckTipoFascicoloAa = new RispostaControlli();
	rcCheckTipoFascicoloAa.setrBoolean(false);

	List<DecAaTipoFascicolo> decAaTipoFascicolos = null;

	try {
	    final String queryStr = "select t from DecAaTipoFascicolo t "
		    + "where t.decTipoFascicolo.idTipoFascicolo = :idTipoFascicolo "
		    + "and t.aaIniTipoFascicolo <= :aaFasc  "
		    + "and t.aaFinTipoFascicolo >= :aaFasc  ";// gli anni NON
	    // si sovrappongono quindi esiste un risultato
	    // per la verifica su tipo fasc
	    Query query = entityManager.createQuery(queryStr, DecAaTipoFascicolo.class);
	    query.setParameter("idTipoFascicolo", idTipoFascicolo);
	    query.setParameter("aaFasc", new BigDecimal(anno));
	    decAaTipoFascicolos = query.getResultList();

	    // ne esiste almeno uno
	    if (decAaTipoFascicolos.size() == 1) {
		// se trovato, rendo l'id
		rcCheckTipoFascicoloAa.setrLong(decAaTipoFascicolos.get(0).getIdAaTipoFascicolo());
		rcCheckTipoFascicoloAa.setrBoolean(true);
	    } else {
		rcCheckTipoFascicoloAa.setCodErr(MessaggiWSBundle.FASC_002_003);
		rcCheckTipoFascicoloAa
			.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.FASC_002_003,
				descChiaveFasc, tipoFasc, String.valueOf(anno)));
	    }
	} catch (Exception e) {
	    rcCheckTipoFascicoloAa.setCodErr(MessaggiWSBundle.ERR_666);
	    rcCheckTipoFascicoloAa.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliFascicoliService.checkTipoFascicoloAnno: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return rcCheckTipoFascicoloAa;
    }

    @Override
    @Transactional
    public RispostaControlli checkTipoFascicoloSconosciuto(long idStruttura) {
	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrBoolean(false);
	List<DecTipoFascicolo> decTipoFascicolos = null;

	try {
	    final String queryStr = "select tf from DecTipoFascicolo tf " + "join tf.orgStrut org "
		    + "where org.idStrut = :idStrutIn "
		    + "and upper(tf.nmTipoFascicolo) = :nmTipoFascicolo ";
	    Query query = entityManager.createQuery(queryStr, DecTipoFascicolo.class);
	    query.setParameter("idStrutIn", idStruttura);
	    query.setParameter("nmTipoFascicolo",
		    Costanti.NOME_FASCICOLO_SCONOSCIUTO.toUpperCase());
	    decTipoFascicolos = query.getResultList();

	    if (decTipoFascicolos.size() == 1) {
		// se trovato, rendo l'id del tipo fascicolo
		rispostaControlli.setrLong(decTipoFascicolos.get(0).getIdTipoFascicolo());
		rispostaControlli.setrBoolean(true);
	    } else {
		rispostaControlli.setCodErr(MessaggiWSBundle.FASC_002_001);
		rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.FASC_002_001,
			"<NON INDICATO>", Costanti.NOME_FASCICOLO_SCONOSCIUTO));
	    }
	} catch (Exception e) {
	    rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666);
	    rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliFascicoliService.checkTipoFascicoloSconosciuto: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return rispostaControlli;
    }

    @Override
    @Transactional
    public RispostaControlli checkChiave(CSChiaveFasc key, String descKey, long idStruttura,
	    TipiGestioneFascAnnullati tgfa) {
	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrLong(-1);
	rispostaControlli.setrBoolean(false);

	List<FasFascicolo> fasFascicolos;

	try {
	    final String queryStr = "select ud " + "from FasFascicolo ud join ud.orgStrut org "
		    + "where org.idStrut = :idStrutIn "
		    + " and ud.cdKeyFascicolo = :cdKeyFascicolo "
		    + " and ud.aaFascicolo = :aaFascicolo " + " order by ud.tsFineSes desc";
	    Query query = entityManager.createQuery(queryStr, FasFascicolo.class);
	    query.setParameter("idStrutIn", idStruttura);
	    query.setParameter("cdKeyFascicolo", key.getNumero());
	    query.setParameter("aaFascicolo", new BigDecimal(key.getAnno()));
	    fasFascicolos = query.getResultList();
	    // chiave già presente (uno o più righe trovate, mi interessa solo l'ultima -
	    // più recente)
	    if (!fasFascicolos.isEmpty()) {
		TiStatoConservazione scud = fasFascicolos.get(0).getTiStatoConservazione();
		if (scud == TiStatoConservazione.ANNULLATO
			&& tgfa == TipiGestioneFascAnnullati.CONSIDERA_ASSENTE) {
		    // mi comporto come se non avesse trovato il fascicolo
		    // NOTA: quando questo metodo verrà usato in un ws di recupero, qui dovrà
		    // costruire
		    // un messaggio di errore, come in ControlliSemanticiService#checkChiave
		    rispostaControlli.setrBoolean(true);
		} else {
		    // gestione normale: ho trovato il fascicolo e non è annullato.
		    // Oppure è annullato e voglio caricarlo lo stesso (il solo caso è nel ws
		    // recupero stato fascicolo)
		    // intanto rendo l'errore di chiave già presente
		    rispostaControlli.setCodErr(MessaggiWSBundle.FASC_001_001);
		    rispostaControlli.setDsErr(
			    MessaggiWSBundle.getString(MessaggiWSBundle.FASC_001_001, descKey));
		    rispostaControlli.setrLong(fasFascicolos.get(0).getIdFascicolo());
		    // già che ho sotto mano il fascicolo, memorizzo qualche informazione utile,
		    // in questo caso, l'xml di rapporto di versamento originale
		    for (FasXmlVersFascicolo fxvf : fasFascicolos.get(0)
			    .getFasXmlVersFascicolos()) {
			if (fxvf.getTiXmlVers()
				.equals(CostantiDB.TipiXmlDati.RISPOSTA.toString())) {
			    rispostaControlli.setrString(fxvf.getBlXmlVers());
			}
		    }
		}
		return rispostaControlli;
	    }

	    // Chiave non trovata
	    // NOTA: quando questo metodo verrà usato in un ws di recupero, qui dovrà
	    // costruire
	    // un messaggio di errore, come in ControlliSemanticiService#checkChiave
	    rispostaControlli.setrBoolean(true);

	} catch (Exception e) {
	    rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666);
	    rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliFascicoliService.checkChiave: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return rispostaControlli;
    }

    /*
     * ######## FASE II ########
     *
     */

    @Override
    @Transactional
    public RispostaControlli verificaSIPTitolario(StrutturaVersFascicolo svf) {
	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrBoolean(false);

	DecTitol decTitol = null;
	List<DecVoceTitol> decVoceTitols = null;

	try {
	    RispostaControlli tmpControlli = me.getDecTitolStrutt(svf.getIdStruttura(),
		    svf.getDatiXmlProfiloGenerale().getDataApertura());

	    if (tmpControlli.getrLong() != -1) {
		decTitol = (DecTitol) tmpControlli.getrObject();

		DatiXmlProfiloArchivistico dxpa = svf.getDatiXmlProfiloArchivistico();
		if (dxpa != null && StringUtils.isNotBlank(dxpa.getIndiceClassificazione())) {

		    // verifica sulle singole (composito)
		    tmpControlli = this.checkVoceDescTitol(dxpa.getVociClassificazione(),
			    svf.getDatiXmlProfiloGenerale().getDataApertura(),
			    decTitol.getIdTitol());

		    if (!tmpControlli.isrBoolean()) {
			return tmpControlli;
		    }

		    // se tutto ok recupero il cd composito costruito in precedenza ...
		    String cdCompisitoVoceTitol = tmpControlli.getrString();

		    // check con indice classificazione su SIP
		    if (StringUtils.isNotBlank(cdCompisitoVoceTitol) && cdCompisitoVoceTitol.trim()
			    .equalsIgnoreCase(dxpa.getIndiceClassificazione().trim())) {

			// estraggo/verifico il dato (solo con il compisito) su DB
			final String queryStr = "select vt from DecVoceTitol vt "
				+ "join vt.decTitol ti" + "where ti.idTitol = :idTitol "
				+ " AND upper(vt.cdCompositoVoceTitol) = upper(:cdCompositoVoceTitol)  "
				+ " AND vt.dtIstituz <= :dtApertura "
				+ " AND vt.dtSoppres >= :dtApertura  ";
			Query query = entityManager.createQuery(queryStr, DecVoceTitol.class);
			query.setParameter("idTitol", decTitol.getIdTitol());
			// escaping (stessa gestione della fase di inserimento sulle voci vedi
			// StrutTitolariEjb.creaVoce)
			query.setParameter("cdCompositoVoceTitol",
				StringEscapeUtils.escapeEcmaScript(dxpa.getIndiceClassificazione()
					.toUpperCase().trim().toUpperCase()));// ripulisco
			// dagli
			// spazi
			query.setParameter("dtApertura",
				svf.getDatiXmlProfiloGenerale().getDataApertura());

			decVoceTitols = query.getResultList();

			// se esiste
			if (decVoceTitols.size() == 1) {
			    // se trovato, rendo l'id della voce del titolario
			    rispostaControlli.setrLong(decVoceTitols.get(0).getIdVoceTitol());
			    rispostaControlli.setrBoolean(true);
			} else {
			    rispostaControlli.setCodErr(MessaggiWSBundle.FASC_004_003);
			    rispostaControlli.setDsErr(
				    MessaggiWSBundle.getString(MessaggiWSBundle.FASC_004_003,
					    dxpa.getIndiceClassificazione()));
			}
		    } else {
			rispostaControlli.setCodErr(MessaggiWSBundle.FASC_004_002);
			rispostaControlli.setDsErr(MessaggiWSBundle.getString(
				MessaggiWSBundle.FASC_004_002, dxpa.getIndiceClassificazione()));
		    }

		} else {
		    rispostaControlli.setCodErr(MessaggiWSBundle.FASC_004_002);
		    rispostaControlli
			    .setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.FASC_004_002));
		}

	    } else {
		rispostaControlli.setCodErr(MessaggiWSBundle.FASC_004_001);
		rispostaControlli
			.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.FASC_004_001));
	    }

	} catch (Exception e) {
	    rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666);
	    rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliFascicoliService.verificaSIPTitolario: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return rispostaControlli;
    }

    private RispostaControlli checkVoceDescTitol(List<DXPAVoceClassificazione> vociClassificazione,
	    Date dtApertura, long idTitol) {

	Map<BigDecimal, DecValVoceTitol> decLvlVoceTitols = new HashMap<>();

	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrBoolean(false);

	StringBuilder cdCompositoVoceTitolsb = new StringBuilder(0);
	String cdCompositoVoceTitolLastSep = null;
	// per ogni voce si effettua la verifica
	for (DXPAVoceClassificazione voce : vociClassificazione) {
	    RispostaControlli tmpControlli = me.checkCdVoceDescDecValVoceTitol(voce.getCodiceVoce(),
		    voce.getDescrizioneVoce(), dtApertura, idTitol);
	    if (!tmpControlli.isrBoolean()) {
		tmpControlli.setCodErr(MessaggiWSBundle.FASC_004_004);
		tmpControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.FASC_004_004,
			voce.getCodiceVoce(), voce.getDescrizioneVoce()));
		return tmpControlli;
	    }
	    // serve per la gestione del composito da confrontare con l'indice di
	    // classificazione
	    DecValVoceTitol decValVoceTitol = (DecValVoceTitol) tmpControlli.getrObject();
	    decLvlVoceTitols.put(
		    decValVoceTitol.getDecVoceTitol().getDecLivelloTitol().getNiLivello(),
		    decValVoceTitol);
	}

	// ordino la mappa per chiave
	decLvlVoceTitols = decLvlVoceTitols.entrySet().stream()
		.sorted(Map.Entry.comparingByKey(Comparator.naturalOrder()))
		.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
			(oldValue, newValue) -> oldValue, LinkedHashMap::new));

	for (BigDecimal nextniLivello : decLvlVoceTitols.keySet().stream()
		.collect(Collectors.toList())) {
	    // recupero la voce dalla mappa
	    DecValVoceTitol decValVoceTitol = decLvlVoceTitols.get(nextniLivello);

	    // se esiste recupero il livello successivo per ottenere il separatore da usare
	    // alla prossima iterazione
	    RispostaControlli tmpControlli = me
		    .getDecLvlVoceTitolWithNiLivello(nextniLivello.longValue(), idTitol);

	    if (tmpControlli.isrBoolean()) {
		// recupero separatore ottenuto ....
		DecLivelloTitol decLivelloTitol = (DecLivelloTitol) tmpControlli.getrObject();
		if (StringUtils.isNotBlank(decLivelloTitol.getCdSepLivello())) {
		    cdCompositoVoceTitolsb.append(decLivelloTitol.getCdSepLivello());
		    cdCompositoVoceTitolLastSep = decLivelloTitol.getCdSepLivello();
		}

		cdCompositoVoceTitolsb.append(decValVoceTitol.getDecVoceTitol().getCdVoceTitol());
	    }
	}
	//
	// se l'ultimo carattere coincide con il separatore lo rimuovo (valido per la
	// verifica)
	if (cdCompositoVoceTitolsb.length() > 0
		&& StringUtils.isNotBlank(cdCompositoVoceTitolLastSep)
		&& cdCompositoVoceTitolsb.toString().endsWith(cdCompositoVoceTitolLastSep)) {
	    cdCompositoVoceTitolsb = cdCompositoVoceTitolsb
		    .deleteCharAt(cdCompositoVoceTitolsb.length() - 1);
	}

	//
	rispostaControlli.setrString(cdCompositoVoceTitolsb.toString());
	rispostaControlli.setrBoolean(true);
	return rispostaControlli;
    }

    @Override
    @Transactional
    public RispostaControlli getDecAaTipoFascicolo(long idAaTipoFascicolo) {

	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrLong(-1);
	DecAaTipoFascicolo aaTipoFasc = null;

	try {
	    aaTipoFasc = entityManager.find(DecAaTipoFascicolo.class, idAaTipoFascicolo);
	    rispostaControlli.setrLong(0);
	    rispostaControlli.setrObject(aaTipoFasc);
	} catch (Exception e) {
	    rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666);
	    rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliFascicoliService.getDecAaTipoFascicolo: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}
	return rispostaControlli;
    }

    @Override
    @Transactional
    public RispostaControlli checkFlDecAaTipoFascicoloOrgStrutt(long idAaTipoFasc) {
	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrBoolean(false);

	DecAaTipoFascicolo decAaTipoFascicolo = null;

	String flAbilitaContrClassif = null;
	String flAccettaContrClassifNeg = null;
	String flForzaContrClassif = null;
	String flAbilitaContrNum = null;
	String flAccettaContrNumNeg = null;
	String flForzaContrNum = null;
	String flAbilitaContrColl = null;
	String flAccettaContrCollNeg = null;
	String flForzaContrColl = null;

	rispostaControlli = me.getDecAaTipoFascicolo(idAaTipoFasc);
	if (rispostaControlli.getrLong() != -1) {
	    decAaTipoFascicolo = (DecAaTipoFascicolo) rispostaControlli.getrObject();

	    OrgStrut orgStrut = decAaTipoFascicolo.getDecTipoFascicolo().getOrgStrut();
	    if (orgStrut == null) {
		rispostaControlli.setrBoolean(false);
		rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666P);
		rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			"ControlliFascicoliService.checkFlDecAaTipoFascicoloOrgStrutt.getOrgStrut: Errore recupero della struttura"));
		return rispostaControlli;// errore non previsto !
	    }

	    // fl (possono essere nulli -> recepisco quelli dell'organizzazione)
	    flAbilitaContrClassif = configurationDao.getParamApplicValueAsFl(
		    ParametroApplFl.FL_ABILITA_CONTR_CLASSIF, orgStrut.getIdStrut(),
		    orgStrut.getOrgEnte().getOrgAmbiente().getIdAmbiente(), Integer.MIN_VALUE,
		    idAaTipoFasc);
	    flAccettaContrClassifNeg = configurationDao.getParamApplicValueAsFl(
		    ParametroApplFl.FL_ACCETTA_CONTR_CLASSIF_NEG, orgStrut.getIdStrut(),
		    orgStrut.getOrgEnte().getOrgAmbiente().getIdAmbiente(), Integer.MIN_VALUE,
		    idAaTipoFasc);
	    flForzaContrClassif = configurationDao.getParamApplicValueAsFl(
		    ParametroApplFl.FL_FORZA_CONTR_CLASSIF, orgStrut.getIdStrut(),
		    orgStrut.getOrgEnte().getOrgAmbiente().getIdAmbiente(), Integer.MIN_VALUE,
		    idAaTipoFasc);
	    //
	    flAbilitaContrNum = configurationDao.getParamApplicValueAsFl(
		    ParametroApplFl.FL_ABILITA_CONTR_NUMERO, orgStrut.getIdStrut(),
		    orgStrut.getOrgEnte().getOrgAmbiente().getIdAmbiente(), Integer.MIN_VALUE,
		    idAaTipoFasc);
	    flAccettaContrNumNeg = configurationDao.getParamApplicValueAsFl(
		    ParametroApplFl.FL_ACCETTA_CONTR_NUMERO_NEG, orgStrut.getIdStrut(),
		    orgStrut.getOrgEnte().getOrgAmbiente().getIdAmbiente(), Integer.MIN_VALUE,
		    idAaTipoFasc);
	    flForzaContrNum = configurationDao.getParamApplicValueAsFl(
		    ParametroApplFl.FL_FORZA_CONTR_NUMERO, orgStrut.getIdStrut(),
		    orgStrut.getOrgEnte().getOrgAmbiente().getIdAmbiente(), Integer.MIN_VALUE,
		    idAaTipoFasc);
	    //
	    flAbilitaContrColl = configurationDao.getParamApplicValueAsFl(
		    ParametroApplFl.FL_ABILITA_CONTR_COLLEG, orgStrut.getIdStrut(),
		    orgStrut.getOrgEnte().getOrgAmbiente().getIdAmbiente(), Integer.MIN_VALUE,
		    idAaTipoFasc);
	    flAccettaContrCollNeg = configurationDao.getParamApplicValueAsFl(
		    ParametroApplFl.FL_ACCETTA_CONTR_COLLEG_NEG_FAS, orgStrut.getIdStrut(),
		    orgStrut.getOrgEnte().getOrgAmbiente().getIdAmbiente(), Integer.MIN_VALUE,
		    idAaTipoFasc);
	    flForzaContrColl = configurationDao.getParamApplicValueAsFl(
		    ParametroApplFl.FL_FORZA_CONTR_COLLEG, orgStrut.getIdStrut(),
		    orgStrut.getOrgEnte().getOrgAmbiente().getIdAmbiente(), Integer.MIN_VALUE,
		    idAaTipoFasc);

	    FlControlliFasc flControlliFasc = new FlControlliFasc();

	    flControlliFasc.setFlAbilitaContrClassif("1".equalsIgnoreCase(flAbilitaContrClassif));
	    flControlliFasc
		    .setFlAccettaContrClassifNeg("1".equalsIgnoreCase(flAccettaContrClassifNeg));
	    flControlliFasc.setFlForzaContrFlassif("1".equalsIgnoreCase(flForzaContrClassif));
	    //
	    flControlliFasc.setFlAbilitaContrNumero("1".equalsIgnoreCase(flAbilitaContrNum));
	    flControlliFasc.setFlAccettaContrNumeroNeg("1".equalsIgnoreCase(flAccettaContrNumNeg));
	    flControlliFasc.setFlForzaContrNumero("1".equalsIgnoreCase(flForzaContrNum));
	    //
	    flControlliFasc.setFlAbilitaContrColleg("1".equalsIgnoreCase(flAbilitaContrColl));
	    flControlliFasc.setFlAccettaContrCollegNeg("1".equalsIgnoreCase(flAccettaContrCollNeg));
	    flControlliFasc.setFlForzaContrColleg("1".equalsIgnoreCase(flForzaContrColl));

	    rispostaControlli.setrObject(flControlliFasc);
	    rispostaControlli.setrBoolean(true);
	} else {
	    rispostaControlli.setrBoolean(false);
	    rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666P);
	    rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
		    "ControlliFascicoliService.checkFlDecAaTipoFascicoloOrgStrutt.getDecAaTipoFascicolo: Errore recupero periodo per fascicolo"));
	    return rispostaControlli;// non vado oltre
	}

	return rispostaControlli;
    }

    @Override
    @Transactional
    public RispostaControlli getPartiAANumero(String descChiaveFasc, long idAATipoFasc) {

	// risposta impostata successivamente nei vari controlli
	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrBoolean(false);

	rispostaControlli = me.getDecAaTipoFascicolo(idAATipoFasc);// già verificato in precedenza
								   // (esiste sempre)
	if (rispostaControlli.getrLong() != -1) {
	    DecAaTipoFascicolo decAaTipoFascicolo = (DecAaTipoFascicolo) rispostaControlli
		    .getrObject();
	    // recupero della configurazione
	    ConfigNumFasc tmpConfigNumFasc = this.caricaPartiAANumero(
		    decAaTipoFascicolo.getIdAaTipoFascicolo(),
		    decAaTipoFascicolo.getNiCharPadParteClassif().longValue());

	    if (!tmpConfigNumFasc.getParti().isEmpty()) {
		rispostaControlli.setrObject(tmpConfigNumFasc);
		rispostaControlli.setrBoolean(true);
	    } else {
		// in caso le parti non siano presenti
		rispostaControlli.setCodErr(MessaggiWSBundle.FASC_005_002);
		rispostaControlli.setDsErr(
			MessaggiWSBundle.getString(MessaggiWSBundle.FASC_005_002, descChiaveFasc));
	    }
	} else {
	    // non dovrebbe MAI esistere un fascicolo che non ha parti in un certo
	    // periodo (caso estremo cod 666P)
	    rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666P);
	    rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
		    "ControlliFascicoliService.getDecAaTipoFascicolo: non esiste periodo configurato per il fascicolo"));
	}

	return rispostaControlli;
    }

    // chiamata anche dal job, rende la configurazione per validare i numeri/chiave
    // in base all'anno del registro
    private ConfigNumFasc caricaPartiAANumero(long idAaNumeroFasc, long niCharPadParteClassif) {
	ConfigNumFasc tmpConfAnno = new ConfigNumFasc(idAaNumeroFasc);

	final String queryStr = "select pnf from DecParteNumeroFascicolo pnf "
		+ "join pnf.decAaTipoFascicolo aa "
		+ "where aa.idAaTipoFascicolo = :idAaTipoFascicolo " + "order by pnf.niParteNumero";
	Query query = entityManager.createQuery(queryStr);
	query.setParameter("idAaTipoFascicolo", idAaNumeroFasc);
	List<DecParteNumeroFascicolo> tmpLstP = query.getResultList();

	for (DecParteNumeroFascicolo tmpParte : tmpLstP) {
	    ConfigNumFasc.ParteNumero tmpPRanno = tmpConfAnno.aggiungiParte();
	    tmpPRanno.setNumParte(tmpParte.getNiParteNumero().intValue());
	    tmpPRanno.setNomeParte(tmpParte.getNmParteNumero());
	    tmpPRanno.setMaxLen(
		    tmpParte.getNiMaxCharParte() != null ? tmpParte.getNiMaxCharParte().longValue()
			    : -1);
	    tmpPRanno.setMinLen(
		    tmpParte.getNiMinCharParte() != null ? tmpParte.getNiMinCharParte().longValue()
			    : 0);

	    //
	    tmpPRanno.setSeparatore(
		    (tmpParte.getTiCharSep() != null && tmpParte.getTiCharSep().isEmpty()) ? " "
			    : tmpParte.getTiCharSep());
	    /*
	     * se il separatore è una stringa non-nulla ma vuota, il valore viene letto come uno
	     * spazio. nel DB è memorizzato come CHAR(1), pad-dato -al salvataggio- da Oracle, e che
	     * al momento della lettura viene trim-ato da eclipselink. Quindi con questo sistema
	     * ricostruisco il valore originale se questo era uno spazio
	     */
	    //
	    // Nota: nel DB la variabile tiParte ha tre valori mutualmente esclusivi.
	    // in questo caso, vengono gestiti come 4 flag separati perché i test relativi
	    // vengono effettuati in parti diverse del codice
	    tmpPRanno.setMatchAnnoChiave(tmpParte.getTiParte() != null
		    && tmpParte.getTiParte().equals(ConfigNumFasc.TiParte.ANNO.name()));
	    tmpPRanno.setMatchClassif(tmpParte.getTiParte() != null
		    && tmpParte.getTiParte().equals(ConfigNumFasc.TiParte.CLASSIF.name()));
	    tmpPRanno.setUsaComeProgressivo(tmpParte.getTiParte() != null
		    && tmpParte.getTiParte().equals(ConfigNumFasc.TiParte.PROGR.name()));
	    tmpPRanno.setUsaComeSottoProgressivo(tmpParte.getTiParte() != null
		    && tmpParte.getTiParte().equals(ConfigNumFasc.TiParte.PROGSUB.name()));
	    tmpPRanno.setTipoCalcolo(TipiCalcolo.valueOf(tmpParte.getTiCharParte()));
	    tmpPRanno.setTiPadding(tmpParte.getTiPadParte() != null
		    ? ConfigNumFasc.TipiPadding.valueOf(tmpParte.getTiPadParte())
		    : ConfigNumFasc.TipiPadding.NESSUNO);
	    tmpPRanno.setNiPadParteClassif(niCharPadParteClassif);// padding classif
	    ConfigNumFasc.impostaValoriAccettabili(tmpPRanno, tmpParte.getDlValoriParte());
	}
	tmpConfAnno.elaboraParti();
	return tmpConfAnno;
    }

    @Override
    @Transactional
    public RispostaControlli checkDecVoceTitolWithComp(String cdCompositoVoceTitol, long idStrutt,
	    Date dtApertura) {

	return checkDecVoceTitolWithCompAndVoce(null, cdCompositoVoceTitol, idStrutt, dtApertura);

    }

    @Override
    @Transactional
    public RispostaControlli checkDecVoceTitolWithCompAndVoce(String cdVoceTitol,
	    String cdCompositoVoceTitol, long idStrutt, Date dtApertura) {
	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrBoolean(false);

	DecTitol decTitol = null;
	List<DecVoceTitol> decVoceTitols = null;

	try {

	    rispostaControlli = me.getDecTitolStrutt(idStrutt, dtApertura);

	    if (rispostaControlli.getrLong() != -1) {
		decTitol = (DecTitol) rispostaControlli.getrObject();

		final String queryStr = "select vt from DecVoceTitol vt " + "join vt.decTitol ti "
			+ "where ti.idTitol = :idTitol "
			+ (StringUtils.isNotBlank(cdVoceTitol)
				? " AND upper(vt.cdVoceTitol) = upper(:cdVoceTitol) "
				: " ")
			+ " AND upper(vt.cdCompositoVoceTitol) = upper(:cdCompositoVoceTitol)  "
			+ " AND vt.dtIstituz <= :dtApertura " + " AND vt.dtSoppres >= :dtApertura ";
		Query query = entityManager.createQuery(queryStr, DecVoceTitol.class);
		query.setParameter("idTitol", decTitol.getIdTitol());
		if (StringUtils.isNotBlank(cdVoceTitol)) {
		    query.setParameter("cdVoceTitol",
			    StringEscapeUtils.escapeEcmaScript(cdVoceTitol.toUpperCase().trim()));// ripulisco
												  // dagli
		    // spazi
		}
		// escaping (stessa gestione della fase di inserimento sulle voci vedi
		// StrutTitolariEjb.creaVoce)
		query.setParameter("cdCompositoVoceTitol", StringEscapeUtils
			.escapeEcmaScript(cdCompositoVoceTitol.toUpperCase().trim()));// ripulisco
		// dagli spazi
		query.setParameter("dtApertura", dtApertura);

		decVoceTitols = query.getResultList();

		// se esiste verifico se ottenere un separatore per la parte successiva
		if (decVoceTitols.size() == 1) {
		    // restituisco l'oggetto
		    rispostaControlli.setrObject(decVoceTitols.get(0));
		    rispostaControlli.setrBoolean(true);
		} else {
		    return rispostaControlli;
		}

	    } else {
		return rispostaControlli;
	    }

	} catch (Exception e) {
	    rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666);
	    rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliFascicoliService.checkDecVoceTitolWithCompAndVoce: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return rispostaControlli;
    }

    @Override
    @Transactional
    public RispostaControlli checkCdVoceDescDecValVoceTitol(String cdCompositoVoceTitol,
	    String dsVoceTitol, Date dtApertura, long idTitol) {

	return checkCdVoceDescDecValVoceTitol(null, cdCompositoVoceTitol, dsVoceTitol, dtApertura,
		idTitol);

    }

    @Override
    @Transactional
    public RispostaControlli checkCdVoceDescDecValVoceTitol(String cdVoceTitol,
	    String cdCompositoVoceTitol, String dsVoceTitol, Date dtApertura, long idTitol) {

	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrBoolean(false);

	List<DecValVoceTitol> decValVoceTitols = null;

	try {

	    final String queryStr = "select val from DecValVoceTitol val "
		    + "join val.decVoceTitol voce " + "join voce.decTitol ti "
		    + "where ti.idTitol = :idTitol "
		    + (StringUtils.isNotBlank(cdVoceTitol)
			    ? " AND upper(voce.cdVoceTitol) = upper(:cdVoceTitol) "
			    : " ")
		    // + " AND upper(d.decVoceTitol.cdVoceTitol) = upper(:cdVoceTitol) "
		    + (StringUtils.isNotBlank(cdCompositoVoceTitol)
			    ? " AND upper(voce.cdCompositoVoceTitol) = upper(:cdCompositoVoceTitol) "
			    : " ")
		    + " AND upper(val.dsVoceTitol) = upper(:dsVoceTitol)  "
		    + " AND voce.dtIstituz <= :dtApertura " + " AND voce.dtSoppres >= :dtApertura ";
	    Query query = entityManager.createQuery(queryStr, DecValVoceTitol.class);
	    query.setParameter("idTitol", idTitol);
	    // escaping (stessa gestione della fase di inserimento sulle voci vedi
	    // StrutTitolariEjb.creaVoce)
	    if (StringUtils.isNotBlank(cdVoceTitol)) {
		query.setParameter("cdVoceTitol",
			StringEscapeUtils.escapeEcmaScript(cdVoceTitol.toUpperCase().trim()));// ripulisco
		// dagli
		// spazi
	    }
	    if (StringUtils.isNotBlank(cdCompositoVoceTitol)) {
		query.setParameter("cdCompositoVoceTitol", StringEscapeUtils
			.escapeEcmaScript(cdCompositoVoceTitol.toUpperCase().trim()));// ripulisco
		// dagli spazi
	    }
	    query.setParameter("dsVoceTitol",
		    StringEscapeUtils.escapeEcmaScript(dsVoceTitol.toUpperCase().trim()));// ripulisco
	    // dagli
	    // spazi
	    query.setParameter("dtApertura", dtApertura);

	    decValVoceTitols = query.getResultList();

	    // se esiste verifico se ottenere un separatore per la parte successiva
	    if (decValVoceTitols.size() == 1) {
		// restituisco l'oggetto
		rispostaControlli.setrObject(decValVoceTitols.get(0));
		rispostaControlli.setrBoolean(true);
	    } else {
		return rispostaControlli;
	    }

	} catch (Exception e) {
	    rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666);
	    rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliFascicoliService.checkCdVoceDescDecValVoceTitol: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return rispostaControlli;
    }

    @Override
    @Transactional
    public RispostaControlli getDecLvlVoceTitolWithNiLivello(long niLivello, long idTitol) {

	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrBoolean(false);

	List<DecLivelloTitol> decLivelloTitols = null;

	try {
	    final String queryStr = "select liv from DecLivelloTitol liv " + "join liv.decTitol ti "
		    + "where ti.idTitol = :idTitol " + "AND liv.niLivello = :niLivello ";
	    Query query = entityManager.createQuery(queryStr, DecLivelloTitol.class);
	    query.setParameter("idTitol", idTitol);
	    query.setParameter("niLivello", new BigDecimal(niLivello));// livello successivo

	    decLivelloTitols = query.getResultList();

	    // se esiste verifico se ottenere un separatore per la parte successiva
	    if (decLivelloTitols.size() == 1) {
		// livello attuale
		rispostaControlli.setrObject(decLivelloTitols.get(0));
		rispostaControlli.setrBoolean(true);
	    }

	} catch (Exception e) {
	    rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666);
	    rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliFascicoliService.getDecLvlVoceTitolWithNiLivello: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return rispostaControlli;
    }

    @Override
    @Transactional
    public RispostaControlli getDecTitolStrutt(long idStruttura, Date dtApertura) {
	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrLong(-1);
	List<DecTitol> decTitols = null;

	try {

	    final String queryStr = "select ti from DecTitol ti " + "join ti.orgStrut org "
		    + "where org.idStrut = :idStrut  " + " AND  ti.dtIstituz <= :dtApertura  "
		    + " AND  ti.dtSoppres >= :dtApertura ";
	    Query query = entityManager.createQuery(queryStr, DecTitol.class);
	    query.setParameter("idStrut", idStruttura);
	    query.setParameter("dtApertura", convert(dtApertura).toLocalDateTime());

	    decTitols = query.getResultList();

	    if (decTitols.size() == 1) {
		rispostaControlli.setrObject(decTitols.get(0));
		rispostaControlli.setrLong(0);
	    }

	} catch (Exception e) {
	    rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666);
	    rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliFascicoliService.getDecTitol: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}
	return rispostaControlli;
    }

    @Override
    @Transactional
    public RispostaControlli calcAndcheckCdKeyNormalized(CSChiaveFasc key, long idStrut,
	    String cdKeyNormalized) {
	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrBoolean(false);
	try {
	    // check esistenza
	    rispostaControlli = checkCdKeyNormalized(key, idStrut, cdKeyNormalized);
	    // 666 error
	    if (rispostaControlli.getCodErr() != null) {
		return rispostaControlli;
	    }
	    if (rispostaControlli.getrLong() == 0/* esiste */) {
		// aggiungere _ infondo e richiamare ricorsivamente lo stesso metodo fino a che
		// la condizione di uscita restituisce l'urn part normalizzato corretto
		cdKeyNormalized = cdKeyNormalized.concat("_");
		// chiamata su metodo ricorsiva
		rispostaControlli = calcAndcheckCdKeyNormalized(key, idStrut, cdKeyNormalized);
	    } else {
		// condizione di uscita da chiamata ricorsiva
		// setta la chiave calcolata per utilizzarla dal chiamante
		rispostaControlli.setrString(cdKeyNormalized);// default
		rispostaControlli.setrBoolean(true);
	    }
	} catch (Exception e) {
	    rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666);
	    rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliFascicoliService.calcAndcheckCdKeyNormalized: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return rispostaControlli;
    }

    private RispostaControlli checkCdKeyNormalized(CSChiaveFasc key, long idStrut,
	    String cdKeyNormalized) {
	RispostaControlli rispostaControlli;
	rispostaControlli = new RispostaControlli();
	rispostaControlli.setrLong(-1);
	rispostaControlli.setrBoolean(false);

	Long num;
	try {
	    final String queryStr = "select count(fas) from FasFascicolo fas "
		    + "where fas.aaFascicolo = :aaFascicolo "
		    + " and fas.orgStrut.idStrut = :idStrut "
		    + " and fas.cdKeyNormalizFascicolo = :cdKeyNormalized ";
	    Query query = entityManager.createQuery(queryStr);
	    query.setParameter("aaFascicolo", new BigDecimal(key.getAnno()));
	    query.setParameter("idStrut", idStrut);
	    query.setParameter("cdKeyNormalized", cdKeyNormalized);

	    num = (Long) query.getSingleResult();
	    if (num > 0) {
		// esiste chiave normalized
		rispostaControlli.setrLong(0);
	    }
	} catch (Exception e) {
	    rispostaControlli.setCodErr(MessaggiWSBundle.ERR_666);
	    rispostaControlli.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666,
		    "ControlliSemanticiService.checkCdKeyNormalized: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}

	return rispostaControlli;
    }

}
