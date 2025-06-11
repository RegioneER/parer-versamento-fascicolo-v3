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

import it.eng.parer.fascicolo.beans.IRapportoVersBuilderService;
import it.eng.parer.fascicolo.beans.dto.base.BlockingFakeSession;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle;
import it.eng.parer.fascicolo.beans.utils.xml.XmlUtils;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RapportoVersBuilderService implements IRapportoVersBuilderService {

    /**
     * Canonicalizzazione dell'XML SIP recuperato da sessione.getDatiDaSalvareIndiceSip (viene
     * restituito l'xml secondo transformazione e secondo il "formato" comunicato dal client)
     *
     * @param sessione di tipo {@link BlockingFakeSession}
     *
     * @return RispostaControlli con indice sip canonicalizzato
     */
    @Override
    public RispostaControlli canonicalizzaDaSalvareIndiceSip(BlockingFakeSession sessione) {
	return canonicalizzaDaSalvareIndiceSip(sessione, false);
    }

    /**
     * Canonicalizzazione dell'XML SIP recuperato da sessione.getDatiDaSalvareIndiceSip (viene
     * restituito l'xml in formato prittyPrint se unPrettyPrint a true)
     *
     *
     * @param sessione      di tipo {@link BlockingFakeSession}
     * @param unPrettyPrint deprecato il valore true in quanto, l'xml restituito, deve essere il
     *                      prodotto della trasformazione attuata dalla canonicalizzazione
     *
     * @return RispostaControlli con indice sip canonicalizzato
     */
    private RispostaControlli canonicalizzaDaSalvareIndiceSip(BlockingFakeSession sessione,
	    boolean unPrettyPrint) {
	String xmlSip = sessione.getDatiDaSalvareIndiceSip();
	RispostaControlli rcCanonXml = new RispostaControlli();
	try {
	    // refactor
	    String xmlOut = XmlUtils.doCanonicalizzazioneXml(xmlSip, unPrettyPrint);
	    //
	    rcCanonXml.setrString(xmlOut);
	    rcCanonXml.setrBoolean(true);
	} catch (Exception e) {
	    rcCanonXml.setrBoolean(false);
	    rcCanonXml.setCodErr(MessaggiWSBundle.XSD_001_002);
	    rcCanonXml.setDsErr(MessaggiWSBundle.getString(MessaggiWSBundle.XSD_001_002,
		    ExceptionUtils.getRootCauseMessage(e)));
	}

	return rcCanonXml;
    }

}
