/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.utils;

import org.slf4j.Logger;

/**
 *
 * @author fioravanti_f
 */
public class LogSessioneUtils {

    //
    private static final int DS_ERR_MAX_LEN = 1024;

    private LogSessioneUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static String getDsErrAtMaxLen(String dsErrore) {
        String tmpErrMess;
        if (dsErrore.isEmpty()) {
            tmpErrMess = "(vuoto)";
        } else {
            tmpErrMess = getStringAtMaxLen(dsErrore, DS_ERR_MAX_LEN);
        }
        return tmpErrMess;
    }

    public static String getStringAtMaxLen(String string, int maxLen) {
        String tmpReturn = string;

        if (tmpReturn.length() > maxLen) {
            tmpReturn = tmpReturn.substring(0, maxLen);
        }
        return tmpReturn;
    }

    public static void logSimulazioni(String ambiente, String ente, String struttura, String registro, int anno,
            String numero, String user, Logger logger) {
        logger.info("Chiamata al WS con SimulaSalvataggioDatiInDB=true versatore=[{}-{}-{}] key=[{}-{}-{}] user=[{}] ",
                ambiente, ente, struttura, registro, anno, numero, user);
    }
}
