/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.impl;

import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;

import javax.sql.DataSource;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.fascicolo.beans.AppServerInstance;
import it.eng.parer.fascicolo.beans.IControlliSemanticiService;
import it.eng.parer.fascicolo.beans.IWsIdpLoggerService;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericRuntimeException;
import it.eng.parer.fascicolo.beans.utils.Costanti.ErrorCategory;
import it.eng.parer.fascicolo.beans.utils.ParametroApplDB;
import it.eng.parer.idpjaas.logutils.IdpConfigLog;
import it.eng.parer.idpjaas.logutils.IdpLogger;
import it.eng.parer.idpjaas.logutils.LogDto;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@SuppressWarnings("unchecked")
@ApplicationScoped
public class WsIdpLoggerService implements IWsIdpLoggerService {

    private static final Logger log = LoggerFactory.getLogger(WsIdpLoggerService.class);

    @Inject
    IControlliSemanticiService controlliSemanticiService;

    @Inject
    AppServerInstance asi;

    @Inject
    EntityManager entityManager;

    @Inject
    DataSource dataSource;

    @Override
    @Transactional(value = TxType.REQUIRES_NEW, rollbackOn = Throwable.class)
    public void scriviLog(LogDto logDto) {
        HashMap<String, String> iamDefaults = null;

        RispostaControlli rcLoadDbDefault = controlliSemanticiService
                .caricaDefaultDaDB(ParametroApplDB.TipoParametroAppl.IAM);
        if (!rcLoadDbDefault.isrBoolean()) {
            throw new AppGenericRuntimeException("WsIdpLoggerService: Impossibile accedere alla tabella parametri",
                    ErrorCategory.INTERNAL_ERROR);
        } else {
            iamDefaults = (HashMap<String, String>) rcLoadDbDefault.getrObject();
        }
        if (iamDefaults.get(ParametroApplDB.IDP_QRY_REGISTRA_EVENTO_UTENTE) != null
                && !iamDefaults.get(ParametroApplDB.IDP_QRY_REGISTRA_EVENTO_UTENTE).isEmpty()
                && iamDefaults.get(ParametroApplDB.IDP_QRY_VERIFICA_DISATTIVAZIONE_UTENTE) != null
                && !iamDefaults.get(ParametroApplDB.IDP_QRY_VERIFICA_DISATTIVAZIONE_UTENTE).isEmpty()
                && iamDefaults.get(ParametroApplDB.IDP_QRY_DISABLE_USER) != null
                && !iamDefaults.get(ParametroApplDB.IDP_QRY_DISABLE_USER).isEmpty()
                && iamDefaults.get(ParametroApplDB.IDP_MAX_GIORNI) != null
                && !iamDefaults.get(ParametroApplDB.IDP_MAX_GIORNI).isEmpty()
                && iamDefaults.get(ParametroApplDB.IDP_MAX_TENTATIVI_FALLITI) != null
                && !iamDefaults.get(ParametroApplDB.IDP_MAX_TENTATIVI_FALLITI).isEmpty()) {
            try (Connection connection = dataSource.getConnection()) {
                IdpConfigLog icl = new IdpConfigLog();
                icl.setQryRegistraEventoUtente(iamDefaults.get(ParametroApplDB.IDP_QRY_REGISTRA_EVENTO_UTENTE));
                icl.setQryVerificaDisattivazioneUtente(
                        iamDefaults.get(ParametroApplDB.IDP_QRY_VERIFICA_DISATTIVAZIONE_UTENTE));
                icl.setQryDisabilitaUtente(iamDefaults.get(ParametroApplDB.IDP_QRY_DISABLE_USER));
                icl.setMaxTentativi(Integer.parseInt(iamDefaults.get(ParametroApplDB.IDP_MAX_TENTATIVI_FALLITI)));
                icl.setMaxGiorni(Integer.parseInt(iamDefaults.get(ParametroApplDB.IDP_MAX_GIORNI)));

                logDto.setServername(asi.getName());

                IdpLogger.EsitiLog risposta = (new IdpLogger(icl).scriviLog(logDto, connection));

                if (risposta == IdpLogger.EsitiLog.UTENTE_DISATTIVATO) {
                    final String queryStr = "update IamUser iu " + "set iu.flAttivo = :flAttivoIn "
                            + "where iu.nmUserid = :nmUseridIn ";

                    // l'operazione di log dell'evento BAD_PASS ha causato la disattivazione
                    // dell'utente nella tabella USR_USER di IAM; questa situazione verrà recepita
                    // tra circa 5 minuti, durante i qali l'utente risulta ancora attivo per SACER.
                    // Per accelerare la risposta del sistema, disattivo l'utente anche nella
                    // tabella locale. Tra 5 minuti il job di aggiornamento utenti ripeterà
                    // la stessa situazione.
                    Query query = entityManager.createQuery(queryStr);
                    query.setParameter("flAttivoIn", "0");
                    query.setParameter("nmUseridIn", logDto.getNmUser());
                    query.executeUpdate();

                    log.atWarn().log("ERRORE DI AUTENTICAZIONE WS. DISATTIVAZIONE UTENTE: {}", logDto.getNmUser());
                }

            } catch (UnknownHostException ex) {
                throw new AppGenericRuntimeException(
                        "WsIdpLoggerService: Errore nel determinare il nome host per il server: "
                                + ExceptionUtils.getRootCauseMessage(ex),
                        ErrorCategory.INTERNAL_ERROR);
            } catch (SQLException ex) {
                throw new AppGenericRuntimeException("WsIdpLoggerService: Errore nell'accesso ai dati di log: "
                        + ExceptionUtils.getRootCauseMessage(ex), ErrorCategory.INTERNAL_ERROR);
            } catch (Exception ex) {
                throw new AppGenericRuntimeException(
                        "WsIdpLoggerService: Errore: " + ExceptionUtils.getRootCauseMessage(ex),
                        ErrorCategory.INTERNAL_ERROR);
            }
        }

    }

}
