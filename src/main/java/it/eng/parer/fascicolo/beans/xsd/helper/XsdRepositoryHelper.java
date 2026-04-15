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

package it.eng.parer.fascicolo.beans.xsd.helper;

import it.eng.parer.fascicolo.beans.xsd.XsdBlob;
import it.eng.parer.fascicolo.jpa.entity.DecModelloXsdFascicolo;
import it.eng.parer.fascicolo.jpa.entity.constraint.DecModelloXsdFascRif.TiRiferimento;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;

import java.time.LocalDateTime;

/**
 * Helper per la gestione del repository XSD e delle dipendenze import/include.
 *
 * Fornisce metodi per recuperare gli XSD importati/inclusi da altri XSD, utilizzato dal
 * LSResourceResolver durante la validazione XML.
 */
@ApplicationScoped
public class XsdRepositoryHelper {

    @Inject
    EntityManager entityManager;

    /**
     * Trova un XSD importato/incluso in base ai parametri di risoluzione.
     *
     * Effettua lookup con strategia a cascata: 1. Match esatto su tipo + namespace + schemaLocation
     * 2. Match su tipo + namespace (solo per IMPORT) 3. Match su tipo + schemaLocation (solo per
     * INCLUDE)
     *
     * @param tipo           Tipo di riferimento (IMPORT/INCLUDE)
     * @param namespaceUri   Namespace URI (xs:import/@namespace)
     * @param schemaLocation Schema location (xs:import/@schemaLocation o
     *                       xs:include/@schemaLocation)
     * @return XsdBlob con codice e contenuto dell'XSD, null se non trovato
     */
    public XsdBlob findImportedXsd(String tipo, String namespaceUri, String schemaLocation) {
        // Tentativo 1: match stretto su namespace + schemaLocation
        XsdBlob xsd = findByTipoNamespaceAndLocation(tipo, namespaceUri, schemaLocation);
        if (xsd != null) {
            return xsd;
        }

        // Fallback 1: se IMPORT e schemaLocation non risolve, prova solo namespace
        if ("IMPORT".equalsIgnoreCase(tipo) && namespaceUri != null && !namespaceUri.isBlank()) {
            xsd = findByImportNamespaceOnly(namespaceUri);
            if (xsd != null) {
                return xsd;
            }
        }

        // Fallback 2: se INCLUDE, spesso namespaceUri è null; prova per schemaLocation soltanto
        if ("INCLUDE".equalsIgnoreCase(tipo) && schemaLocation != null
                && !schemaLocation.isBlank()) {
            xsd = findByIncludeLocationOnly(schemaLocation);
            if (xsd != null) {
                return xsd;
            }
        }

        return null;
    }

    /**
     * Match esatto: tipo + namespace + schemaLocation
     */
    private XsdBlob findByTipoNamespaceAndLocation(String tipo, String namespaceUri,
            String schemaLocation) {
        try {
            LocalDateTime now = LocalDateTime.now();
            String jpql = "SELECT NEW it.eng.parer.fascicolo.beans.xsd.XsdBlob(tgt.cdXsd, tgt.blXsd) "
                    + "FROM DecModelloXsdFascRif r " + "JOIN r.decModelloXsdFascicoloTarget tgt "
                    + "WHERE r.tiRiferimento = :tipo "
                    + "AND COALESCE(r.namespaceUri, '-') = COALESCE(:namespaceUri, '-') "
                    + "AND COALESCE(r.schemaLocation, '-') = COALESCE(:schemaLocation, '-') "
                    + "AND r.dtIstituz <= :now AND r.dtSoppres >= :now "
                    + "AND tgt.dtIstituz <= :now AND tgt.dtSoppres >= :now";

            Query query = entityManager.createQuery(jpql);
            query.setParameter("tipo", TiRiferimento.valueOf(tipo.toUpperCase()));
            query.setParameter("namespaceUri", namespaceUri);
            query.setParameter("schemaLocation", schemaLocation);
            query.setParameter("now", now);

            return (XsdBlob) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Fallback per IMPORT: match solo su namespace
     */
    private XsdBlob findByImportNamespaceOnly(String namespaceUri) {
        try {
            LocalDateTime now = LocalDateTime.now();
            String jpql = "SELECT NEW it.eng.parer.fascicolo.beans.xsd.XsdBlob(tgt.cdXsd, tgt.blXsd) "
                    + "FROM DecModelloXsdFascRif r " + "JOIN r.decModelloXsdFascicoloTarget tgt "
                    + "WHERE r.tiRiferimento = :tipo " + "AND r.namespaceUri = :namespaceUri "
                    + "AND r.dtIstituz <= :now AND r.dtSoppres >= :now "
                    + "AND tgt.dtIstituz <= :now AND tgt.dtSoppres >= :now "
                    + "ORDER BY r.idModelloXsdFascRif";

            Query query = entityManager.createQuery(jpql);
            query.setParameter("tipo", TiRiferimento.IMPORT);
            query.setParameter("namespaceUri", namespaceUri);
            query.setParameter("now", now);
            query.setMaxResults(1);

            return (XsdBlob) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Fallback per INCLUDE: match solo su schemaLocation
     */
    private XsdBlob findByIncludeLocationOnly(String schemaLocation) {
        try {
            LocalDateTime now = LocalDateTime.now();
            String jpql = "SELECT NEW it.eng.parer.fascicolo.beans.xsd.XsdBlob(tgt.cdXsd, tgt.blXsd) "
                    + "FROM DecModelloXsdFascRif r " + "JOIN r.decModelloXsdFascicoloTarget tgt "
                    + "WHERE r.tiRiferimento = :tipo " + "AND r.schemaLocation = :schemaLocation "
                    + "AND r.dtIstituz <= :now AND r.dtSoppres >= :now "
                    + "AND tgt.dtIstituz <= :now AND tgt.dtSoppres >= :now "
                    + "ORDER BY r.idModelloXsdFascRif";

            Query query = entityManager.createQuery(jpql);
            query.setParameter("tipo", TiRiferimento.INCLUDE);
            query.setParameter("schemaLocation", schemaLocation);
            query.setParameter("now", now);
            query.setMaxResults(1);

            return (XsdBlob) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * Recupera un modello XSD per ID, filtrando opzionalmente per validità temporale.
     *
     * @param idModelloXsdFascicolo ID del modello XSD
     * @param filterValid           Se true, verifica che sia valido alla data corrente
     * @return DecModelloXsdFascicolo se trovato, null altrimenti
     */
    public DecModelloXsdFascicolo getModelloXsdFascicolo(Long idModelloXsdFascicolo,
            boolean filterValid) {
        try {
            StringBuilder jpql = new StringBuilder(
                    "SELECT m FROM DecModelloXsdFascicolo m WHERE m.idModelloXsdFascicolo = :id");

            if (filterValid) {
                jpql.append(" AND m.dtIstituz <= :now AND m.dtSoppres >= :now");
            }

            Query query = entityManager.createQuery(jpql.toString());
            query.setParameter("id", idModelloXsdFascicolo);

            if (filterValid) {
                LocalDateTime now = LocalDateTime.now();
                query.setParameter("now", now);
            }

            return (DecModelloXsdFascicolo) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}
