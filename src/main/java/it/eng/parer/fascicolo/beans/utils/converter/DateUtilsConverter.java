package it.eng.parer.fascicolo.beans.utils.converter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Classe di utilità per la generica conversione delle date.
 *
 *
 * @author Snidero_L
 */
public class DateUtilsConverter {

    private DateUtilsConverter() {
        throw new IllegalStateException(
                "Classe di utility per aiutarti con la conversione delle date dopo l'introduzione di java.time.*");
    }

    /**
     * Conversione della data espressa con il fuso orario in data locale.
     *
     * @param zdt
     *            data in input
     *
     * @return oggetto java.util.Date oppure null
     */
    public static Date convert(ZonedDateTime zdt) {
        if (zdt != null) {
            return Date.from(zdt.withZoneSameInstant(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    /**
     * Conversione della data locale ad una data con fuso orario.
     *
     * @param dt
     *            data in input
     *
     * @return oggetto java.time.ZonedDateTime oppure null
     */
    public static ZonedDateTime convert(Date dt) {
        if (dt != null) {
            return ZonedDateTime.ofInstant(dt.toInstant(), ZoneId.systemDefault());
        }
        return null;
    }

    /**
     * Conversione in stringa della data con il formato {@link DateTimeFormatter#ISO_OFFSET_DATE_TIME}. Questo formato è
     * completo e non contiene caratteri potenzialmente "pericolosi" per, ad esempio, i metadati dell'object storage.
     *
     * @param dt
     *            data in input
     *
     * @return Data correttamente formatta o null.
     */
    public static String format(ZonedDateTime dt) {
        if (dt != null) {
            return dt.format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        }
        return null;
    }

    /**
     * Conversione della data espressa con il datetime locale.
     *
     * @param ldt
     *            data in input
     *
     * @return oggetto java.util.Date oppure null
     */
    public static Date convert(LocalDateTime ldt) {
        if (ldt != null) {
            return Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

    /**
     * Conversione della data espressa con il localdate.
     *
     * @param ld
     *            data in input
     *
     * @return oggetto java.util.Date oppure null
     */
    public static Date convert(LocalDate ld) {
        if (ld != null) {
            return Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
        }
        return null;
    }

}
