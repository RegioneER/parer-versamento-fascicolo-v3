/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import org.apache.commons.text.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.runtime.Startup;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericRuntimeException;
import it.eng.parer.fascicolo.beans.utils.Costanti.ErrorCategory;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSHelper;
import it.eng.parer.fascicolo.jpa.entity.DecErrSacer;
import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@Singleton
@Startup
public class MessaggiWSCache {

    private static Logger log = LoggerFactory.getLogger(MessaggiWSCache.class);

    @Inject
    MessaggiWSHelper messaggiWSHelper;

    Map<String, String> errorMap;

    @PostConstruct
    public void initSingleton() {
        log.info("Inizializzazione singleton MessaggiWSCache...");
        try {

            List<DecErrSacer> list = messaggiWSHelper.caricaListaErrori();
            errorMap = new HashMap<>();
            for (DecErrSacer err : list) {
                errorMap.put(err.getCdErr(), StringEscapeUtils.unescapeJava(err.getDsErr())); // Unescape java-like
                // every message
            }
        } catch (RuntimeException ex) {
            throw new AppGenericRuntimeException(ex, ErrorCategory.INTERNAL_ERROR);
        }
        log.info("Inizializzazione singleton MessaggiWSCache... completata.");
    }

    @Transactional(value = TxType.NOT_SUPPORTED)
    public String getString(String key) {
        return errorMap.get(key);
    }

    @Transactional(value = TxType.NOT_SUPPORTED)
    public String getString(String key, Object... params) {
        return MessageFormat.format(errorMap.get(key), cleanTextContent(params));
    }

    /*
     * Clean up placeholder (from XML)
     */
    private Object[] cleanTextContent(Object... params) {
        List<Object> cleanedTxtParams = new ArrayList<>();
        Stream.of(params).filter(Objects::nonNull).forEach(param -> {
            String sparam = param.toString(); // force toString()
            // erases all the ASCII control characters
            sparam = sparam.replaceAll("[\\p{Cntrl}&&[^\r\n\t]]", "?");
            // removes non-printable characters from Unicode
            sparam = sparam.replaceAll("\\p{C}", "?");
            // unescape XML of sparam
            sparam = StringEscapeUtils.unescapeXml(sparam);
            // final result
            cleanedTxtParams.add(sparam);
        });
        return cleanedTxtParams.toArray(new Object[0]);
    }

}
