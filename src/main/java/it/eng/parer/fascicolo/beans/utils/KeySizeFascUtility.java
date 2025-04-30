/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.utils;

import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;
import it.eng.parer.fascicolo.beans.dto.base.CSVersatore;
import it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSFormat;

/**
 *
 * @author sinatti_s
 */
public class KeySizeFascUtility {

    public static final int MAX_LEN_NUMERO_IN_CHIAVEORD = 75;
    //
    public static final int MAX_LEN_CHIAVEORD = 100;
    public static final int MAX_LEN_URN = 254;
    public static final int MAX_LEN_FILENAME_ARK = 254;
    //
    private int maxLenNumero = 0;
    private int lenURN = 0;
    private int lenPath = 0;

    public KeySizeFascUtility(CSVersatore csv, CSChiaveFasc csc, String sistema) {

        int numKeyOrd = this.calcolaMaxLenNumeroKeyOrd(csc);
        int numUrn = this.calcolaMaxLenNumeroURN(csv, csc, sistema);
        maxLenNumero = numKeyOrd < numUrn ? numKeyOrd : numUrn;

        lenURN = this.calcolaURN(csv, csc, sistema).length();

    }

    public int getMaxLenNumero() {
        return maxLenNumero;
    }

    public int getLenURN() {
        return lenURN;
    }

    public int getLenPath() {
        return lenPath;
    }

    private int calcolaMaxLenNumeroKeyOrd(CSChiaveFasc chiave) {
        String tmpChiaveOrd;
        if (chiave.getNumero().length() <= MAX_LEN_NUMERO_IN_CHIAVEORD) {
            tmpChiaveOrd = +chiave.getAnno() + "-" + chiave.getNumero();
        } else {
            /*
             * prova a rifare il controllo troncando il <codice fascicolo> a 75 caratteri. codice fascicolo???
             */
            tmpChiaveOrd = chiave.getAnno() + "-" + chiave.getNumero().substring(0, MAX_LEN_NUMERO_IN_CHIAVEORD);
        }

        return MAX_LEN_CHIAVEORD - tmpChiaveOrd.length();
    }

    private int calcolaMaxLenNumeroURN(CSVersatore versatore, CSChiaveFasc chiave, String sistema) {
        return MAX_LEN_URN - this.calcolaURN(versatore, chiave, sistema).length();
    }

    private String calcolaURN(CSVersatore versatore, CSChiaveFasc chiave, String sistema) {
        String chiaveComp = MessaggiWSFormat.formattaChiaveFascicolo(versatore, chiave, sistema);
        return MessaggiWSFormat.formattaUrnIndiceSipFasc(chiaveComp, Costanti.UrnFormatter.URN_INDICE_SIP_V2);
    }

}
