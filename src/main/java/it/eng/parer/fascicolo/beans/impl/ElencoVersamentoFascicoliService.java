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
            elencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab(fascicolo, svf.getIdTipoFascicolo(),
                    TiStatoFascDaElab.IN_ATTESA_SCHED);
        } catch (Exception e) {
            throw new AppGenericPersistenceException(e,
                    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
                            "Errore interno nella fase di salvataggio coda da elaborare: "
                                    + ExceptionUtils.getRootCauseMessage(e)));
        }
    }

    @Override
    @Transactional
    public void scriviStatoConservFascicolo(RispostaWSFascicolo rispostaWs, VersFascicoloExt versamento,
            BlockingFakeSession sessione, FasFascicolo fascicolo) throws AppGenericPersistenceException {
        try {
            elencoVersamentoFascicoliDao.insertFascicoloOnStatoCons(fascicolo, TiStatoConservazione.PRESA_IN_CARICO);
        } catch (Exception e) {
            throw new AppGenericPersistenceException(e,
                    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
                            "Errore interno nella fase di salvataggio stato conservazione fascicolo: "
                                    + ExceptionUtils.getRootCauseMessage(e)));
        }
    }

    @Override
    @Transactional
    public void scriviStatoFascicoloElenco(RispostaWSFascicolo rispostaWs, VersFascicoloExt versamento,
            BlockingFakeSession sessione, FasFascicolo fascicolo) throws AppGenericPersistenceException {
        try {
            elencoVersamentoFascicoliDao.insertFascicoloOnStatoElenco(fascicolo, TiStatoFascElenco.IN_ATTESA_SCHED);
        } catch (Exception e) {
            throw new AppGenericPersistenceException(e,
                    MessaggiWSBundle.getString(MessaggiWSBundle.ERR_666P,
                            "Errore interno nella fase di salvataggio stato fascicolo elenco: "
                                    + ExceptionUtils.getRootCauseMessage(e)));
        }
    }

}
