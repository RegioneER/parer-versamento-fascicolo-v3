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

import it.eng.parer.fascicolo.beans.dto.RispostaWSFascicolo;
import it.eng.parer.fascicolo.beans.dto.VersFascicoloExt;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import jakarta.validation.constraints.NotNull;

public interface IRecupSessDubbieFascService {

    /*
     * //Markdown:
     *
     * # casi di uso della sessione DUBBIA
     *
     * |Caso |Ho Id Utente |Ho XML parsed|Ho ID Struttura|Versatore OK|
     * |----------------------------------|:-----------:|:-----------:|:------------
     * -:|:----------:| |Errore di versione ws | | | | | |Errore di credenziali | | | | | |Errore di
     * parser |SI | | | | |Versatore diverso tra ws e xml | |SI | | | |Versione diversa tra ws e xml
     * | |SI | | | |Struttura template | |SI |SI | | |Utente non autorizzato a Struttura| |SI |SI |
     * | |Tipo fascicolo inesistente | |SI |SI |SI |
     *
     * La versione non ha molta importanza: anche se manca oppure è prova di senso posso sempre
     * tentare di scrivere qualcosa nella tabella sessione fallita
     *
     * se le credenziali sono errate, perché non è stato fornito l'utente o se la password è errata.
     * dovrò in ogni caso salvare l'utente come nullo
     *
     * La chiave non ha molta importanza, viene scritta come si trova anche nelle sessioni fallite
     *
     * in pratica se ho un'id struttura ed un id fascicolo, la sessione è per lo meno FALLITA
     *
     * in definitiva, lo scopo di questo modulo è quello di restituire queste due informazioni, così
     * da poterle registare nelle tabelle delle sessioni fallite.
     *
     * se ricavo questi dati devo in ogni caso verificare che la chiave non sia doppia (come nel
     * versamento)
     *
     * anche se la chiave non è doppia, devo verificare che per quella chiave non ci siano già
     * errori (questo è gestito dal procedimento ordinario)
     *
     */
    //
    void recuperaSessioneErrata(
	    @NotNull(message = "IRecupSessDubbieFascService.recuperaSessioneErrata: rispostaWs non inizializzato") RispostaWSFascicolo rispostaWs,
	    @NotNull(message = "IRecupSessDubbieFascService.recuperaSessioneErrata: versamento non inizializzato") VersFascicoloExt versamento,
	    @NotNull(message = "IRecupSessDubbieFascService.recuperaSessioneErrata: sessione non inizializzato") BlockingFakeSession sessione);

}
