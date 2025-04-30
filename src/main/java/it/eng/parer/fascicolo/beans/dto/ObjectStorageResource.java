package it.eng.parer.fascicolo.beans.dto;

import java.net.URI;

import it.eng.parer.fascicolo.beans.utils.ParametroApplDB;

/**
 * Informazioni per la risorsa salvata sull'object storage.
 *
 * @author Snidero_L
 */
public interface ObjectStorageResource {

    /**
     * Questo metodo esiste per ragioni storiche. Tecnicamente non serve a nulla
     *
     * @return il nome del tenant come indicato nel parametro applicativo {@link ParametroApplDB#TENANT_OBJECT_STORAGE}
     */
    String getTenant();

    String getBucket();

    String getKey();

    String getETag();

    /**
     * Scadenza della risorsa (nel caso ci siano lifecycle policy configurate sul bucket)
     *
     * @return scadenza della risorsa
     */
    String getExpiration();

    /**
     * Ottiene una URI utilizzabile per scaricare l'oggetto <strong>senza</strong> autenticazione alcuna per un lasso di
     * tempo limitato.
     *
     * @return URI da cui effettuare una HTTP GET
     */
    URI getPresignedURL();
}
