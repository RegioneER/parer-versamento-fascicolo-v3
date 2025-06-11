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

package it.eng.parer.fascicolo.beans.impl;

import static it.eng.parer.fascicolo.beans.utils.Costanti.AwsConstants.TAG_KEY_VRSOBJ_TYPE;
import static it.eng.parer.fascicolo.beans.utils.Costanti.AwsConstants.TAG_VALUE_VRSOBJ_METADATI_FASC_ERR_KO;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.collections4.SetUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import it.eng.parer.fascicolo.beans.IObjectStorageService;
import it.eng.parer.fascicolo.beans.ISalvataggioBackendDao;
import it.eng.parer.fascicolo.beans.dto.base.IWSDesc;
import it.eng.parer.fascicolo.beans.utils.Costanti.ErrorCategory;
import it.eng.parer.fascicolo.beans.utils.CRC32CChecksum;
import it.eng.parer.fascicolo.beans.utils.CostantiDB;
import it.eng.parer.fascicolo.beans.dto.BackendStorage;
import it.eng.parer.fascicolo.beans.dto.ObjectStorageBackend;
import it.eng.parer.fascicolo.beans.dto.ObjectStorageResource;
import it.eng.parer.fascicolo.beans.exceptions.AppGenericRuntimeException;
import it.eng.parer.fascicolo.beans.exceptions.ObjectStorageException;
import it.eng.parer.fascicolo.beans.utils.UUIDMdcLogUtil;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasXmlFascicolo.TiModXsdFasXmlFascicolo;
import software.amazon.awssdk.services.s3.model.Tag;
import software.amazon.awssdk.utils.StringUtils;

@ApplicationScoped
public class ObjectStorageService implements IObjectStorageService {

    private final Logger log = LoggerFactory.getLogger(ObjectStorageService.class);

    private static final String METADATI_FASCICOLI = "WRITE_FASCICOLI";
    private static final String SESSIONI_FASC_ERR_KO = "WRITE_SESSIONI_FASC_ERR_KO";
    private static final String PATTERN_FORMAT = "yyyyMMdd/HH/mm";
    //
    private static final int BUFFER_SIZE = 10 * 1024 * 1024;
    //
    private static final FileAttribute<Set<PosixFilePermission>> attr = PosixFilePermissions
	    .asFileAttribute(PosixFilePermissions.fromString("rw-------"));

    @Inject
    private ISalvataggioBackendDao salvataggioBackendDao;

    /**
     * Effettua la lookup per stabilire come sia configurato il backend per le sessioni
     * errate/fallite del versamento fascicolo
     *
     * @return tipologia di backend. Al momento sono supportate
     *         {@link BackendStorage.STORAGE_TYPE#BLOB} e {@link BackendStorage.STORAGE_TYPE#OS}
     */
    public BackendStorage lookupBackendVrsSessioniErrKo() throws ObjectStorageException {
	try {
	    String tipoBackend = salvataggioBackendDao.getBackendSessioniErrKo();

	    // tipo backend
	    return salvataggioBackendDao.getBackend(tipoBackend);

	} catch (Exception e) {
	    throw ObjectStorageException.builder().cause(e).build();
	}
    }

    /**
     * Effettua il lookup per stabilire come sia configurato il backend per ogni tipo di servizio
     *
     * @param idAaTipoFascicolo id tipologia anno fascicolo
     * @param nomeWs            nome del servizio (vedere {@link IWSDesc})
     *
     * @return tipologia di backend. Al momento sono supportate
     *         {@link BackendStorage.STORAGE_TYPE#BLOB} e {@link BackendStorage.STORAGE_TYPE#OS}
     *
     * @throws ObjectStorageException in caso di errore.
     */
    public BackendStorage lookupBackendByServiceName(long idAaTipoFascicolo, String nomeWs)
	    throws ObjectStorageException {
	try {

	    String tipoBackend = salvataggioBackendDao.getBackendByServiceName(idAaTipoFascicolo,
		    nomeWs);
	    return salvataggioBackendDao.getBackend(tipoBackend);

	} catch (Exception e) {
	    throw ObjectStorageException.builder().cause(e).build();
	}
    }

    /**
     * Salva i metadati nel bucket delle sessioni fallite.
     *
     * @param nomeBackend      backend configurato (per esempio OBJECT_STORAGE_PRIMARIO)
     * @param xmlFiles         mappa con i metadati da salvare
     * @param idSesFascicoloKo id sessione fallita
     * @param idStrut          id della struttura versante
     *
     * @return risorsa su OS che identifica il file caricato
     */
    public ObjectStorageResource createSipInSessioniKo(String nomeBackend,
	    Map<String, String> xmlFiles, long idSesFascicoloKo, BigDecimal idStrut) {
	try {
	    ObjectStorageBackend configuration = salvataggioBackendDao
		    .getObjectStorageConfiguration(nomeBackend, SESSIONI_FASC_ERR_KO);
	    // generate std tag
	    Set<Tag> tags = new HashSet<>();
	    tags.add(Tag.builder().key(TAG_KEY_VRSOBJ_TYPE)
		    .value(TAG_VALUE_VRSOBJ_METADATI_FASC_ERR_KO).build());
	    // punt on O.S.
	    ObjectStorageResource savedFile = createMetaXmlMapAndPutOnBucket(null, xmlFiles,
		    configuration, tags);
	    // link
	    salvataggioBackendDao.saveObjectStorageLinkSipSessioneKo(savedFile, nomeBackend,
		    idSesFascicoloKo, idStrut);
	    return savedFile;
	} catch (ObjectStorageException | IOException | NoSuchAlgorithmException ex) {
	    throw new AppGenericRuntimeException(ex, ErrorCategory.INTERNAL_ERROR);
	}
    }

    /**
     * Salva i metadati nel bucket delle sessioni errate.
     *
     * @param nomeBackend       backend configurato (per esempio OBJECT_STORAGE_PRIMARIO)
     * @param xmlFiles          mappa con i metadati da salvare
     * @param idSesFascicoloErr id sessione errata
     * @param idStrut           id della struttura versante
     *
     * @return risorsa su OS che identifica il file caricato
     */
    public ObjectStorageResource createSipInSessioniErr(String nomeBackend,
	    Map<String, String> xmlFiles, long idSesFascicoloErr, BigDecimal idStrut) {
	try {
	    ObjectStorageBackend configuration = salvataggioBackendDao
		    .getObjectStorageConfiguration(nomeBackend, SESSIONI_FASC_ERR_KO);
	    // generate std tag
	    Set<Tag> tags = new HashSet<>();
	    tags.add(Tag.builder().key(TAG_KEY_VRSOBJ_TYPE)
		    .value(TAG_VALUE_VRSOBJ_METADATI_FASC_ERR_KO).build());
	    // punt on O.S.
	    ObjectStorageResource savedFile = createMetaXmlMapAndPutOnBucket(null, xmlFiles,
		    configuration, tags);
	    // link
	    salvataggioBackendDao.saveObjectStorageLinkSipSessioneErr(savedFile, nomeBackend,
		    idSesFascicoloErr, idStrut);
	    return savedFile;
	} catch (ObjectStorageException | IOException | NoSuchAlgorithmException ex) {
	    throw new AppGenericRuntimeException(ex, ErrorCategory.INTERNAL_ERROR);
	}
    }

    /**
     * Salva il file nel bucket dei SIP per il fascicolo
     *
     * @param nomeBackend backend configurato (per esempio OBJECT_STORAGE_PRIMARIO)
     * @param xmlFiles    file Xml da salvare (previa creazione file zip)
     * @param idFascicolo id fascicolo
     * @param idStrut     id della struttura versante
     *
     * @return risorsa su OS che identifica il file caricato
     */
    public ObjectStorageResource createResourcesInSipFascicoli(final String urn, String nomeBackend,
	    Map<String, String> xmlFiles, long idFascicolo, BigDecimal idStrut) {
	try {
	    ObjectStorageBackend configuration = salvataggioBackendDao
		    .getObjectStorageConfiguration(nomeBackend, METADATI_FASCICOLI);
	    // put on O.S.
	    ObjectStorageResource savedFile = createMetaXmlMapAndPutOnBucket(urn, xmlFiles,
		    configuration, SetUtils.emptySet());
	    // link
	    salvataggioBackendDao.saveObjectStorageLinkSipFasc(savedFile, nomeBackend, idFascicolo,
		    idStrut);
	    return savedFile;
	} catch (ObjectStorageException | IOException | NoSuchAlgorithmException ex) {
	    throw new AppGenericRuntimeException(ex, ErrorCategory.INTERNAL_ERROR);
	}

    }

    /**
     * Salva il file nel bucket dei metadati profilo per il fascicolo
     *
     * @param nomeBackend backend configurato (per esempio OBJECT_STORAGE_PRIMARIO)
     * @param xmlFiles    file Xml da salvare (previa creazione file zip)
     * @param idFascicolo id fascicolo
     * @param idStrut     id della struttura versante
     *
     * @return risorsa su OS che identifica il file caricato
     */
    public ObjectStorageResource createResourcesInMetaProfFascicoli(final String urn,
	    String nomeBackend, Map<String, String> xmlFiles, long idFascicolo,
	    BigDecimal idStrut) {
	try {
	    ObjectStorageBackend configuration = salvataggioBackendDao
		    .getObjectStorageConfiguration(nomeBackend, METADATI_FASCICOLI);
	    // put on O.S.
	    ObjectStorageResource savedFile = createMetaXmlMapAndPutOnBucket(urn, xmlFiles,
		    configuration, SetUtils.emptySet());
	    // link
	    salvataggioBackendDao.saveObjectStorageLinkMetaProfFasc(savedFile, nomeBackend,
		    idFascicolo, idStrut);
	    return savedFile;
	} catch (ObjectStorageException | IOException | NoSuchAlgorithmException ex) {
	    throw new AppGenericRuntimeException(ex, ErrorCategory.INTERNAL_ERROR);
	}

    }

    private ObjectStorageResource createMetaXmlMapAndPutOnBucket(final String urn,
	    Map<String, String> xmlFiles, ObjectStorageBackend configuration, Set<Tag> tags)
	    throws IOException, ObjectStorageException, NoSuchAlgorithmException {
	ObjectStorageResource savedFile = null;
	Path tempZip = Files.createTempFile("metadati-", ".zip", attr);
	//
	try (InputStream is = Files.newInputStream(tempZip)) {
	    // create key
	    final String key;
	    if (StringUtils.isBlank(urn)) {
		key = createRandomKey() + ".zip";
	    } else {
		key = createRandomKey(urn) + ".zip";
	    }
	    // create zip file
	    createZipFile(xmlFiles, tempZip);
	    // put on O.S.
	    savedFile = salvataggioBackendDao.putObject(is, Files.size(tempZip), key, configuration,
		    Optional.empty(), Optional.of(tags),
		    Optional.of(calculateFileCRC32CBase64(tempZip)));
	    log.debug("Salvato file {}/{}", savedFile.getBucket(), savedFile.getKey());
	} finally {
	    if (tempZip != null) {
		Files.delete(tempZip);
	    }
	}

	return savedFile;
    }

    /**
     * Salva, nella tabella di raccordo <code>VRS_XML_SES_FASC_KO_OBJECT_STORAGE</code>, la chiave
     * del file presente nel bucket delle sessioni errate/fallite che rappresenta il versamento
     * fallito. Grazie alla policy di lifecycle dopo 5 anni verrà eliminato.
     *
     * @param object           risorsa salvata nel bucket delle sessioni errate/fallite
     * @param nomeBackend      nome del backend della suddetta risorsa
     * @param idSesFascicoloKo identificativo del file delle sessioni fallite
     * @param idStrut          id della struttura versante
     *
     * @throws ObjectStorageException in caso di errore.
     */
    public void saveLinkVrsSesKoFromObjectStorage(ObjectStorageResource object, String nomeBackend,
	    long idSesFascicoloKo, BigDecimal idStrut) throws ObjectStorageException {
	try {
	    salvataggioBackendDao.saveObjectStorageLinkSipSessioneKo(object, nomeBackend,
		    idSesFascicoloKo, idStrut);
	} catch (Exception e) {
	    throw ObjectStorageException.builder().cause(e).build();
	}
    }

    /**
     * Salva, nella tabella di raccordo <code>VRS_XML_SES_FASC_ERR_OBJECT_STORAGE</code>, la chiave
     * del file presente nel bucket delle sessioni errate/fallite che rappresenta il versamento
     * errato. Grazie alla policy di lifecycle dopo 5 anni verrà eliminato.
     *
     * @param object            risorsa salvata nel bucket delle sessioni errate/fallite
     * @param nomeBackend       nome del backend della suddetta risorsa
     * @param idSesFascicoloErr identificativo del file delle sessioni errate
     * @param idStrut           id della struttura versante
     *
     * @throws ObjectStorageException in caso di errore.
     */
    public void saveLinkVrsSesErrFromObjectStorage(ObjectStorageResource object, String nomeBackend,
	    long idSesFascicoloErr, BigDecimal idStrut) throws ObjectStorageException {
	try {
	    salvataggioBackendDao.saveObjectStorageLinkSipSessioneErr(object, nomeBackend,
		    idSesFascicoloErr, idStrut);
	} catch (Exception e) {
	    throw ObjectStorageException.builder().cause(e).build();
	}
    }

    /**
     * Crea i file zip contenente i vari xml di versamento e di profilo. Possono essere di tipo:
     * <ul>
     * <li>{@link CostantiDB.TipiXmlDati#RICHIESTA}, obbligatorio è il sip di versamento</li>
     * <li>{@link CostantiDB.TipiXmlDati#RISPOSTA}, obbligatorio è la risposta del sip di
     * versamento</li>
     * </ul>
     * <ul>
     * <li>{@link TiModXsdFasXmlFascicolo.PROFILO_GENERALE_FASCICOLO}, obbligatorio è il profilo
     * generale</li>
     * <li>{@link TiModXsdFasXmlFascicolo.PROFILO_ARCHIVISTICO_FASCICOLO}, facoltativo è il profilo
     * archivistico</li>
     * <li>{@link TiModXsdFasXmlFascicolo.PROFILO_NORMATIVO_FASCICOLO}, facoltativo è il profilo
     * normativo</li>
     * <li>{@link TiModXsdFasXmlFascicolo.PROFILO_SPECIFICO_FASCICOLO}, facoltativo è il profilo
     * specifico</li>
     * </ul>
     *
     * @param xmlFiles mappa dei file delle tipologie indicate in descrizione.
     * @param zipFile  file zip su cui salvare tutto
     *
     * @throws IOException in caso di errore
     */
    private void createZipFile(Map<String, String> xmlFiles, Path zipFile) throws IOException {
	try (ZipOutputStream out = new ZipOutputStream(Files.newOutputStream(zipFile))) {
	    for (Entry<String, String> metaBlob : xmlFiles.entrySet()) {
		ZipEntry entry = new ZipEntry(metaBlob.getKey() + ".xml");
		out.putNextEntry(entry);
		out.write(metaBlob.getValue().getBytes());
		out.closeEntry();
	    }
	}
    }

    /**
     * Crea una chiave utilizzando i seguenti elementi separati dal carattere <code>/</code>:
     * <ul>
     * <li>data in formato anno mese giorno (per esempio <strong>20221124</strong>)</li>
     * <li>ora a due cifre (per esempio <strong>14</strong>)</li>
     * <li>minuto a due cifre (per esempio <strong>05</strong>)</li>
     * <li>UUID sessione di versamento <strong>550e8400-e29b-41d4-a716-446655440000</strong>)
     * recuperato dall'MDC ossia dal Mapped Diagnostic Context (se non esiste viene generato
     * comunque un UUID)</li>
     * <li>UUID generato runtime <strong>28fd282d-fbe6-4528-bd28-2dfbe685286f</strong>) per ogni
     * oggetto caricato</li>
     * </ul>
     *
     * Esempio di chiave completa:
     * <code>20221124/14/05/550e8400-e29b-41d4-a716-446655440000/28fd282d-fbe6-4528-bd28-2dfbe685286f</code>
     *
     * @return chiave dell'oggetto
     */
    private static String createRandomKey() {

	String when = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone(ZoneId.systemDefault())
		.format(Instant.now());

	return when + "/" + UUIDMdcLogUtil.getUuid() + "/" + UUID.randomUUID().toString();
    }

    /**
     * Crea una chiave utilizzando i seguenti elementi separati dal carattere <code>/</code>:
     * <ul>
     * <li>data in formato anno mese giorno (per esempio <strong>20221124</strong>)</li>
     * <li>ora a due cifre (per esempio <strong>14</strong>)</li>
     * <li>minuto a due cifre (per esempio <strong>05</strong>)</li>
     * <li>URN <strong>{0}:{1}:{2}:{3}-{4}</strong>) del fascicolo</li>
     * <li>UUID generato runtime <strong>28fd282d-fbe6-4528-bd28-2dfbe685286f</strong>) per ogni
     * oggetto caricato</li>
     * </ul>
     *
     * Esempio di chiave completa:
     * <code>20221124/14/05/550e8400-e29b-41d4-a716-446655440000/28fd282d-fbe6-4528-bd28-2dfbe685286f</code>
     *
     * @return chiave dell'oggetto
     */
    private static String createRandomKey(final String urn) {

	String when = DateTimeFormatter.ofPattern(PATTERN_FORMAT).withZone(ZoneId.systemDefault())
		.format(Instant.now());

	return when + "/" + urn + "/" + UUID.randomUUID().toString();
    }

    /**
     * Calcola il checksum CRC32C (base64 encoded) del file da inviare via S3.
     *
     * Nota: questa scelta deriva dal modello supportato dal vendor
     * (https://docs.aws.amazon.com/AmazonS3/latest/userguide/checking-object-integrity.html)
     *
     * @param resource file
     *
     * @return rappresentazione base64 del checksum CRC32C calcolato
     *
     * @throws IOException errore generico
     */
    private String calculateFileCRC32CBase64(Path resource) throws IOException {
	byte[] buffer = new byte[BUFFER_SIZE];
	int readed;
	CRC32CChecksum crc32c = new CRC32CChecksum();
	try (InputStream is = Files.newInputStream(resource)) {
	    while ((readed = is.read(buffer)) != -1) {
		crc32c.update(buffer, 0, readed);
	    }
	}
	return Base64.getEncoder().encodeToString(crc32c.getValueAsBytes());
    }
}
