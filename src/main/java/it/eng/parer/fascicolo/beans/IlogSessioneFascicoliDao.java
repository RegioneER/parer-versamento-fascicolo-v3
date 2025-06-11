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

/**
 *
 */
package it.eng.parer.fascicolo.beans;

import java.util.Map;

import it.eng.parer.fascicolo.beans.dto.BackendStorage;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.VrsFascicoloKo;
import it.eng.parer.fascicolo.jpa.entity.VrsSesFascicoloErr;
import it.eng.parer.fascicolo.jpa.entity.VrsSesFascicoloKo;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.JAXBException;

public interface IlogSessioneFascicoliDao {

    RispostaControlli scriviFascicoloErr(
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviFascicoloErr: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviFascicoloErr: versamento non inizializzato") VersFascicoloExt versamento,
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviFascicoloErr: sessione non inizializzato") BlockingFakeSession sessione)
	    throws AppGenericPersistenceException;

    void scriviXmlFascicoloErr(
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviXmlFascicoloErr: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviXmlFascicoloErr: versamento non inizializzato") VersFascicoloExt versamento,
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviXmlFascicoloErr: fascicoloErr non inizializzato") VrsSesFascicoloErr fascicoloErr,
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviXmlFascicoloErr: backendMetadata non inizializzato") BackendStorage backendMetadata,
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviXmlFascicoloErr: sipBlob non inizializzato") Map<String, String> sipBlob)
	    throws AppGenericPersistenceException;

    RispostaControlli cercaFascicoloKo(
	    @NotNull(message = "IlogSessioneFascicoliDao.cercaFascicoloKo: versamento non inizializzato") VersFascicoloExt versamento)
	    throws AppGenericPersistenceException;

    RispostaControlli scriviFascicoloKo(
	    @NotNull(message = "IlogSessioneFascicoliDao.cercaFascicoloKo: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
	    @NotNull(message = "IlogSessioneFascicoliDao.cercaFascicoloKo: versamento non inizializzato") VersFascicoloExt versamento,
	    @NotNull(message = "IlogSessioneFascicoliDao.cercaFascicoloKo: sessione non inizializzato") BlockingFakeSession sessione)
	    throws AppGenericPersistenceException;

    RispostaControlli scriviSessioneFascicoloKo(
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviSessioneFascicoloKo: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
	    @NotNull(message = "IlogSessitestFactory.createVrsSesFascicoloKo()oneFascicoliDao.scriviSessioneFascicoloKo: versamento non inizializzato") VersFascicoloExt versamento,
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviSessioneFascicoloKo: sessione non inizializzato") BlockingFakeSession sessione,
	    VrsFascicoloKo vrsFascicoloKo, FasFascicolo fascicoloOk)
	    throws AppGenericPersistenceException;

    void scriviXmlFascicoloKo(
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviXmlFascicoloKo: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviXmlFascicoloKo: versamento non inizializzato") VersFascicoloExt versamento,
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviXmlFascicoloKo: sessione non inizializzato") BlockingFakeSession sessione,
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviXmlFascicoloKo: sessFascicoloKo non inizializzato") VrsSesFascicoloKo sessFascicoloKo,
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviXmlFascicoloKo: backendMetadata non inizializzato") BackendStorage backendMetadata,
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviXmlFascicoloKo: sipBlob non inizializzato") Map<String, String> sipBlob)
	    throws AppGenericPersistenceException;

    void scriviErroriFascicoloKo(
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviErroriFascicoloKo: versamento non inizializzato") VersFascicoloExt versamento,
	    @NotNull(message = "IlogSessioneFascicoliDao.scriviErroriFascicoloKo: sessFascicoloKo non inizializzato") VrsSesFascicoloKo sessFascicoloKo)
	    throws AppGenericPersistenceException;

    String generaRapportoVersamento(
	    @NotNull(message = "IlogSessioneFascicoliDao.generaRapportoVersamento: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs)
	    throws JAXBException;

}
