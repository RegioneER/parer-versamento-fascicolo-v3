package it.eng.parer.fascicolo.beans.utils.converter;

import it.eng.parer.fascicolo.beans.utils.CostantiDB;

/**
 *
 * @author sinatti_s
 */
public class FlagConverter {

    private FlagConverter() {
        throw new IllegalStateException("Utility class");
    }

    // true / false -> 1 / 0
    public static String fromBoolToFl(String value) {
        return Boolean.parseBoolean(value) ? CostantiDB.Flag.TRUE : CostantiDB.Flag.FALSE;//
    }

    // 1 / 0 -> true / false
    public static String fromFlToBool(String value) {
        return value.equals(CostantiDB.Flag.TRUE) ? Boolean.TRUE.toString() : Boolean.FALSE.toString();//
    }

}
