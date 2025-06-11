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

package it.eng.parer;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import org.apache.commons.io.IOUtils;

import io.quarkus.arc.Arc;
import it.eng.parer.fascicolo.beans.MessaggiWSCache;
import it.eng.parer.fascicolo.jpa.entity.AplParamApplic;
import it.eng.parer.fascicolo.jpa.entity.DecErrSacer;
import it.eng.parer.fascicolo.jpa.viewEntity.AplVGetvalParamByAatifasc;
import it.eng.parer.fascicolo.jpa.viewEntity.AplVGetvalParamByApl;
import it.eng.parer.fascicolo.jpa.viewEntity.AplVGetvalParamByTiud;
import jakarta.enterprise.context.Dependent;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;

@Dependent
@Transactional(Transactional.TxType.MANDATORY)
public class DatabaseInit {

    private static final String SCRIPT_PATH = "/h2/";

    public static final long ID_DEC_TIPO_FASCICOLO = 1L;
    public static final Long ID_DEC_AA_TIPO_FASCICOLO = 1L;
    public static final long ID_DEC_MODELLO_XSD_PROFILO_GENERALE = 1L;
    public static final long ID_DEC_MODELLO_XSD_PROFILO_ARCHIVISTICO = 2L;
    public static final long ID_DEC_MODELLO_XSD_PROFILO_NORMATIVO = 3L;
    public static final long ID_DEC_MODELLO_XSD_DATI_SPEC = 4L;
    public static final long ID_FAS_FASCICOLO = 1L;
    public static final long ID_ORG_STRUT = 3323L;
    public static final long ID_VRS_SES_FASCICOLO_KO = 63L;
    public static final long ID_VRS_FASCICOLO_KO = 47L;
    public static final long ID_VRS_SES_FASCICOLO_ERR = 55L;
    public static final long ID_VRS_V_UPD_FASCICOLO_KO = 47L;

    @Inject
    private EntityManager em;

    public void insertIamUser() {
	exceInsertScript("iam_user.sql");
    }

    public void insertDecTitolo() {
	exceInsertScript("dec_titol.sql");
    }

    public void insertDecLivelloTitolo() {
	insertDecTitolo();
	exceInsertScript("dec_livello_titolo.sql");
    }

    public void insertDecVoceTitolo() {
	insertDecLivelloTitolo();
	exceInsertScript("dec_voce_titol.sql");
    }

    public void insertVrsFascicoloKo() {
	exceInsertScript("dec_error_sacer.sql");
	exceInsertScript("vrs_fascicolo_ko.sql");
    }

    public void insertFasFasciclo() {
	insertIamUser();
	insertDecVoceTitolo();
	insertDecTipoFascicolo();
	insertDecCriterioRaggrFasc();
	insertElvElencoVersFasc();
	exceInsertScript("fas_fascicolo.sql");
    }

    public void insertDecCriterioRaggrFasc() {
	exceInsertScript("dec_criterio_raggr_fasc.sql");
    }

    public void insertElvElencoVersFasc() {
	exceInsertScript("elv_elenco_vers_fasc.sql");
    }

    public void insertOrgStrut() {
	exceInsertScript("org_ambiente.sql");
	exceInsertScript("org_ente.sql");
	exceInsertScript("org_strut.sql");
    }

    public void insertDecAATipoFasciclo() {
	insertDecTipoFascicolo();
	exceInsertScript("dec_aa_tipo_fascicolo.sql");
    }

    public void insertDecTipoFascicolo() {
	exceInsertScript("dec_tipo_fascicolo.sql");
    }

    public void insertDecModelloXsd() {
	exceInsertScript("dec_modello_xsd.sql");
    }

    private int exceInsertScript(String filename) {
	try {
	    final String sql = IOUtils.toString(
		    this.getClass().getResourceAsStream(SCRIPT_PATH + filename),
		    StandardCharsets.UTF_8);
	    Query query = em.createNativeQuery(sql);
	    return query.executeUpdate();
	} catch (IOException e) {
	    throw new RuntimeException(e);
	}
    }

    public void insertVrsSesFascicoloErr() {
	exceInsertScript("dec_error_sacer.sql");
	exceInsertScript("vrs_ses_fascicolo_err.sql");
    }

    public void insertVrsVUpdFascicoloKo() {
	exceInsertScript("vrs_v_upd_fascicolo_ko.sql");
    }

    public void insertVrsSesFascicoloKo() {
	exceInsertScript("dec_error_sacer.sql");
	exceInsertScript("vrs_ses_fascicolo_ko.sql");
    }

    public void initErrors(String... errorCodes) {
	for (String errCode : errorCodes) {
	    DecErrSacer decErrSacer = new DecErrSacer();
	    decErrSacer.setCdErr(errCode);
	    decErrSacer.setDsErr("Errore test " + errCode);
	    em.persist(decErrSacer);
	}
	final MessaggiWSCache messaggiWSCache = Arc.container().instance(MessaggiWSCache.class)
		.get();
	messaggiWSCache.initSingleton();
    }

    public void insertParametroApplicazione(String parametro, String valore) {
	insertParametroApplicazione(parametro, valore, null);
    }

    public void insertParametroApplicazione(String parametro, String valore, String tiParamApplic) {
	try {
	    AplParamApplic aplParamApplic = new AplParamApplic();
	    aplParamApplic.setFlAppartApplic("1");
	    aplParamApplic.setNmParamApplic(parametro);
	    aplParamApplic.setTiParamApplic(tiParamApplic);
	    em.persist(aplParamApplic);
	    SecureRandom rand = SecureRandom.getInstance("NativePRNGNonBlocking");
	    AplVGetvalParamByApl aplVGetvalParamByApl = new AplVGetvalParamByApl();
	    aplVGetvalParamByApl.setIdValoreParamApplic(BigDecimal.valueOf(rand.nextInt()));
	    aplVGetvalParamByApl.setNmParamApplic(parametro);
	    aplVGetvalParamByApl.setDsValoreParamApplic(valore);
	    em.persist(aplVGetvalParamByApl);
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	}
    }

    public void insertParametroTipoUD(String parametro, String valore, long idTipoUnitaDoc) {
	try {
	    AplParamApplic aplParamApplic = new AplParamApplic();
	    aplParamApplic.setFlAppartTipoUnitaDoc("1");
	    aplParamApplic.setNmParamApplic(parametro);
	    em.persist(aplParamApplic);
	    SecureRandom rand = SecureRandom.getInstance("NativePRNGNonBlocking");
	    AplVGetvalParamByTiud aplVGetvalParamByTiud = new AplVGetvalParamByTiud();
	    aplVGetvalParamByTiud.setIdValoreParamApplic(BigDecimal.valueOf(rand.nextInt()));
	    aplVGetvalParamByTiud.setNmParamApplic(parametro);
	    aplVGetvalParamByTiud.setIdTipoUnitaDoc(BigDecimal.valueOf(idTipoUnitaDoc));
	    aplVGetvalParamByTiud.setDsValoreParamApplic(valore);
	    em.persist(aplVGetvalParamByTiud);
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	}
    }

    public void insertParametroAATipoFascicolo(String parametro, String valore,
	    long idAATipoFascicolo) {
	try {
	    AplParamApplic aplParamApplic = new AplParamApplic();
	    aplParamApplic.setFlAppartAaTipoFascicolo("1");
	    aplParamApplic.setNmParamApplic(parametro);
	    em.persist(aplParamApplic);
	    SecureRandom rand = SecureRandom.getInstance("NativePRNGNonBlocking");
	    AplVGetvalParamByAatifasc aplVGetvalParamByAatifasc = new AplVGetvalParamByAatifasc();
	    aplVGetvalParamByAatifasc.setIdValoreParamApplic(BigDecimal.valueOf(rand.nextInt()));
	    aplVGetvalParamByAatifasc.setNmParamApplic(parametro);
	    aplVGetvalParamByAatifasc.setIdAaTipoFascicolo(BigDecimal.valueOf(idAATipoFascicolo));
	    aplVGetvalParamByAatifasc.setDsValoreParamApplic(valore);
	    em.persist(aplVGetvalParamByAatifasc);
	} catch (NoSuchAlgorithmException e) {
	    throw new RuntimeException(e);
	}
    }
}
