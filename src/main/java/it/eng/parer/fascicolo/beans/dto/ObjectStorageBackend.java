package it.eng.parer.fascicolo.beans.dto;

import java.net.URI;

/**
 * Backend di tipo <strong>Object Storage</strong>
 *
 * @author Snidero_L
 */
public interface ObjectStorageBackend extends BackendStorage {

    /**
     * Indirizzo dell'object storage
     *
     * @return URI dell'object storage
     */
    URI getAddress();

    String getBucket();

    String getAccessKeyId();

    String getSecretKey();

    @Override
    default STORAGE_TYPE getType() {
        return STORAGE_TYPE.OS;
    }

}
