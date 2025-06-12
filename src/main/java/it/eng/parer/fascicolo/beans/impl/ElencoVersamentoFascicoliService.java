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

package it.eng.parer.fascicolo.beans.impl;

import org.apache.commons.lang3.exception.ExceptionUtils;

import it.eng.parer.fascicolo.beans.IElencoVersamentoFascicoliDao;
import it.eng.parer.fascicolo.beans.IElencoVersamentoFascicoliService;
import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.StrutturaVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericPersistenceException;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.constraint.ElvFascDaElabElenco.TiStatoFascDaElab;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoConservFascicolo.TiStatoConservazione;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoFascicoloElenco.TiStatoFascElenco;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class ElencoVersamentoFascicoliService implements IElencoVersamentoFascicoliService {

    //
    @Inject
    IElencoVersamentoFascicoliDao elencoVersamentoFascicoliDao;

    @Override
    @Transactional
    public void scriviElvFascDaElabElenco(VersFascicoloExt versamento, FasFascicolo fascicolo)
	    throws AppGenericPersistenceException {
	StrutturaVersFascicolo svf = versamento.getStrutturaComponenti();
	try {
	    elencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab(fascicolo,
		    svf.getIdTipoFascicolo(), TiStatoFascDaElab.IN_ATTESA_SCHED);
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio coda da elaborare: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    @Override
    @Transactional
    public void scriviStatoConservFascicolo(RispostaWSFascicolo rispostaWs,
	    VersFascicoloExt versamento, BlockingFakeSession sessione, FasFascicolo fascicolo)
	    throws AppGenericPersistenceException {
	try {
	    elencoVersamentoFascicoliDao.insertFascicoloOnStatoCons(fascicolo,
		    TiStatoConservazione.PRESA_IN_CARICO);
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e, MessaggiWSBundle.getString(
		    MessaggiWSBundle.ERR_666P,
		    "Errore interno nella fase di salvataggio stato conservazione fascicolo: "
			    + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

    @Override
    @Transactional
    public void scriviStatoFascicoloElenco(RispostaWSFascicolo rispostaWs,
	    VersFascicoloExt versamento, BlockingFakeSession sessione, FasFascicolo fascicolo)
	    throws AppGenericPersistenceException {
	try {
	    elencoVersamentoFascicoliDao.insertFascicoloOnStatoElenco(fascicolo,
		    TiStatoFascElenco.IN_ATTESA_SCHED);
	} catch (Exception e) {
	    throw new AppGenericPersistenceException(e,
		    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
			    "Errore interno nella fase di salvataggio stato fascicolo elenco: "
				    + ExceptionUtils.getRootCauseMessage(e)));
	}
    }

}
