package it.eng.parer.fascicolo.beans.dto;

import java.io.Serializable;

/**
 * Backend su cui viene salvata la risorsa.
 *
 *
 * @author Snidero_L
 */
public interface BackendStorage extends Serializable {

    enum STORAGE_TYPE {
        OS, BLOB, FILE
    }

    STORAGE_TYPE getType();

    String getBackendName();

    default boolean isObjectStorage() {
        return STORAGE_TYPE.OS.equals(getType());
    }

    default boolean isDataBase() {
        return STORAGE_TYPE.BLOB.equals(getType());
    }

    default boolean isFile() {
        return STORAGE_TYPE.FILE.equals(getType());
    }

}
