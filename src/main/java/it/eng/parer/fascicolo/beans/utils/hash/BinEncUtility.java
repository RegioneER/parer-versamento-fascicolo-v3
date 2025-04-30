/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.utils.hash;

import java.io.UnsupportedEncodingException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.fascicolo.beans.exceptions.AppGenericRuntimeException;
import it.eng.parer.fascicolo.beans.utils.Costanti.ErrorCategory;

/**
 *
 * @author fioravanti_f
 */
public class BinEncUtility {

    private static final Logger log = LoggerFactory.getLogger(BinEncUtility.class);

    private BinEncUtility() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isBase64String(String utf8str) {
        return Base64.isBase64(utf8str);
    }

    public static String encodeUTF8Base64String(byte[] barray) {
        if (barray != null && barray.length > 0) {
            try {
                return new String(Base64.encodeBase64(barray), "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                log.atError().log("Encoding UTF-8 non supportato");
                throw new AppGenericRuntimeException(ex.getMessage(), ErrorCategory.INTERNAL_ERROR);
            }
        } else {
            return "";
        }
    }

    public static byte[] decodeUTF8Base64String(String utf8str) {
        try {
            return Base64.decodeBase64(utf8str.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            log.atError().log("Encoding UTF-8 non supportato");
            throw new AppGenericRuntimeException(ex.getMessage(), ErrorCategory.INTERNAL_ERROR);
        }
    }

    public static boolean isHexString(String utf8str) {
        return (utf8str.length() > 1 && utf8str.length() % 2 == 0) && utf8str.matches("^[0-9a-fA-F]+$");
    }

    public static String encodeUTF8HexString(byte[] barray) {
        if (barray != null && barray.length > 0) {
            return Hex.encodeHexString(barray);
        } else {
            return "";
        }
    }

    public static byte[] decodeUTF8HexString(String hexstr) {
        try {
            return Hex.decodeHex(hexstr.toCharArray());
        } catch (DecoderException ex) {
            log.atError().log("La stringa non rappresenta un numero in base 16");
            throw new AppGenericRuntimeException(ex.getMessage(), ErrorCategory.INTERNAL_ERROR);
        }
    }

}
