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

package it.eng.parer.fascicolo.beans.dao;

import static it.eng.parer.fascicolo.beans.utils.Costanti.WS_VERS_FASCICOLO_NOME;
import static it.eng.parer.fascicolo.beans.utils.Costanti.AwsConstants.MEATADATA_INGEST_NODE;
import static it.eng.parer.fascicolo.beans.utils.Costanti.AwsConstants.MEATADATA_INGEST_TIME;

import java.io.InputStream;
import java.math.BigDecimal;
import java.net.URI;
import java.net.URL;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NonUniqueResultException;
import jakarta.persistence.TypedQuery;

import it.eng.parer.fascicolo.jpa.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.eng.parer.fascicolo.beans.IConfigurationDao;
import it.eng.parer.fascicolo.beans.ISalvataggioBackendDao;
import it.eng.parer.fascicolo.beans.utils.ParametroApplDB;
import it.eng.parer.fascicolo.beans.dto.BackendStorage;
import it.eng.parer.fascicolo.beans.dto.ObjectStorageBackend;
import it.eng.parer.fascicolo.beans.dto.ObjectStorageResource;
import it.eng.parer.fascicolo.beans.AwsClientService;
import it.eng.parer.fascicolo.beans.AwsPresignerService;
import it.eng.parer.fascicolo.beans.exceptions.ObjectStorageException;
import it.eng.parer.fascicolo.beans.exceptions.ParamApplicNotFoundException;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest.Builder;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.services.s3.model.Tag;
import software.amazon.awssdk.services.s3.model.Tagging;

@ApplicationScoped
public class SalvataggioBackendDao implements ISalvataggioBackendDao {

    private final Logger log = LoggerFactory.getLogger(SalvataggioBackendDao.class);

    private static final String METADATI_FASCICOLI = "WRITE_FASCICOLI";
    private static final String SESSIONI_FASC_ERR_KO = "WRITE_SESSIONI_FASC_ERR_KO";
    private static final String NO_PARAMETER = "Impossibile ottenere il parametro {0}";
    private static final String LOG_MESSAGE_NO_SAVED = "Impossibile salvare il link dell'oggetto su DB";

    @Inject
    protected IConfigurationDao configurationDao;

    @Inject
    protected AwsPresignerService presigner;

    @Inject
    protected AwsClientService s3Clients;

    @Inject
    private EntityManager entityManager;

    @ConfigProperty(name = "fascicoli.w.accesskeyid")
    Optional<String> accessKeyIdMetaFasc;

    @ConfigProperty(name = "fascicoli.w.secretkey")
    Optional<String> secretKeyMetaFasc;

    @ConfigProperty(name = "sessioni.fasc.err.ko.w.accesskeyid")
    Optional<String> accessKeyIdSesErrKoFasc;

    @ConfigProperty(name = "sessioni.fasc.err.ko.w.secretkey")
    Optional<String> secretKeySesErrKoFasc;

    @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.access-key-id")
    Optional<String> staticProviderAccessKeyId;

    @ConfigProperty(name = "quarkus.s3.aws.credentials.static-provider.secret-access-key")
    Optional<String> staticProviderSecretAccessKey;

    @ConfigProperty(name = "bucket.name.metadata.fasc")
    Optional<String> bucketNameMetadataFasc;

    @ConfigProperty(name = "bucket.name.ses.err.ko.fasc")
    Optional<String> bucketNameSesErrKoFasc;

    @ConfigProperty(name = "quarkus.uuid")
    String instanceUUID;

    public enum BACKEND_VERSAMENTO {
	DATABASE, OBJECT_STORAGE
    }

    /**
     * Ottieni la configurazione applicativa relativa alla tipologia di Backend per il salvataggio
     * delle sessioni errate/fallite del versamento fascicolo
     *
     * @return configurazione del backend. Può essere, per esempio OBJECT_STORAGE_STAGING oppure
     *         DATABASE_PRIMARIO
     *
     * @throws ObjectStorageException in caso di errore di recupero del parametro
     */
    public String getBackendSessioniErrKo() throws ObjectStorageException {
	try {
	    return configurationDao
		    .getValoreParamApplicByApplic(ParametroApplDB.BACKEND_XML_SES_FASC_ERR_KO);

	} catch (ParamApplicNotFoundException | IllegalArgumentException e) {
	    throw ObjectStorageException.builder()
		    .message(NO_PARAMETER, ParametroApplDB.BACKEND_XML_SES_FASC_ERR_KO).cause(e)
		    .build();
	}
    }

    /**
     * Ottieni la tipologia di backend per servizio
     *
     * @param idAaTipoFascicolo id della tipologia dell'anno del fascicolo
     * @param nomeWs            nome del servizio
     *
     * @return configurazione del backend. Può essere, per esempio OBJECT_STORAGE_STAGING oppure
     *         DATABASE_PRIMARIO
     *
     * @throws ObjectStorageException in caso di errore
     */
    public String getBackendByServiceName(long idAaTipoFascicolo, String nomeWs)
	    throws ObjectStorageException {
	try {

	    final String backendDatiVersamento;
	    switch (nomeWs) {
	    case WS_VERS_FASCICOLO_NOME:
		backendDatiVersamento = ParametroApplDB.BACKEND_VERSAMENTO_FASCICOLO;
		break;
	    default:
		throw new IllegalArgumentException("Tipo creazione documento non supportato");
	    }

	    return getParameter(backendDatiVersamento, idAaTipoFascicolo);

	} catch (ParamApplicNotFoundException | IllegalArgumentException e) {
	    throw ObjectStorageException.builder().message(
		    "Impossibile ottenere il parametro per id tipo anno fascicolo {0} e nome servizio {1}",
		    idAaTipoFascicolo, nomeWs).cause(e).build();
	}
    }

    private String getParameter(String parameterName, long idAaTipoFascicolo) {
	DecAaTipoFascicolo aaTipoFascicolo = entityManager.find(DecAaTipoFascicolo.class,
		idAaTipoFascicolo);
	long idStrut = aaTipoFascicolo.getDecTipoFascicolo().getOrgStrut().getIdStrut();

	long idAmbiente = aaTipoFascicolo.getDecTipoFascicolo().getOrgStrut().getOrgEnte()
		.getOrgAmbiente().getIdAmbiente();

	return configurationDao.getValoreParamApplicByAaTipoFasc(parameterName, idStrut, idAmbiente,
		idAaTipoFascicolo);
    }

    /**
     * Salva lo stream di dati sull'object storage della configurazione identificandolo con la
     * chiave passata come parametro.
     *
     * @param blob          stream di dati
     * @param blobLength    dimensione dello stream di dati
     * @param key           chiave dell'oggetto
     * @param configuration configurazione dell'object storage in cui aggiungere l'oggetto
     *
     * @return riferimento alla risorsa appena inserita
     *
     * @throws ObjectStorageException in caso di errore
     */
    public ObjectStorageResource putObject(InputStream blob, long blobLength, final String key,
	    ObjectStorageBackend configuration) throws ObjectStorageException {
	checkConfiguration(configuration);
	try {
	    return putObject(blob, blobLength, key, configuration, Optional.empty(),
		    Optional.empty(), Optional.empty());
	} catch (Exception e) {
	    throw ObjectStorageException.builder()
		    .message("Impossibile salvare oggetto {0} sul bucket {1}", key,
			    configuration.getBucket())
		    .cause(e).build();
	}

    }

    /**
     * Salva lo stream di dati sull'object storage della configurazione identificandolo con la
     * chiave passata come parametro.
     *
     * @param blob          stream di dati
     * @param blobLength    dimensione dello stream di dati
     * @param key           chiave dell'oggetto
     * @param configuration configurazione dell'object storage in cui aggiungere l'oggetto
     * @param metadata      eventuali metadati (nel caso non vengano passati vengono utilizzati
     *                      quelli predefiniti)
     * @param tags          eventuali tag (nel caso non vengano passati non vengnono apposti)
     * @param base64crc32   eventuale base64-encoded CRC32C del file per data integrity check
     *
     * @return riferimento alla risorsa appena inserita
     *
     * @throws ObjectStorageException in caso di errore
     */
    public ObjectStorageResource putObject(InputStream blob, long blobLength, final String key,
	    ObjectStorageBackend configuration, Optional<Map<String, String>> metadata,
	    Optional<Set<Tag>> tags, Optional<String> base64crc32) throws ObjectStorageException {

	checkConfiguration(configuration);

	final URI storageAddress = configuration.getAddress();
	final String accessKeyId = configuration.getAccessKeyId();
	final String secretKey = configuration.getSecretKey();
	final String bucket = configuration.getBucket();

	log.debug("Sto per inserire nell'os {} la chiave {} sul bucket {}", storageAddress, key,
		bucket);

	try {
	    S3Client s3Client = (s3Clients.isClientProvided()) ? s3Clients.getStaticProviderClient()
		    : s3Clients.getClient(storageAddress, accessKeyId, secretKey);

	    Builder putObjectBuilder = PutObjectRequest.builder().bucket(bucket).key(key);

	    if (metadata.isPresent()) {
		putObjectBuilder.metadata(metadata.get());
	    } else {
		putObjectBuilder.metadata(defaultMetadata());
	    }
	    if (tags.isPresent()) {
		putObjectBuilder.tagging(Tagging.builder().tagSet(tags.get()).build());
	    }
	    if (base64crc32.isPresent()) {
		putObjectBuilder.checksumCRC32C(base64crc32.get());
	    }

	    PutObjectRequest objectRequest = putObjectBuilder.build();
	    final long start = System.currentTimeMillis();
	    PutObjectResponse response = s3Client.putObject(objectRequest,
		    RequestBody.fromInputStream(blob, blobLength));

	    final long end = System.currentTimeMillis() - start;
	    if (log.isDebugEnabled()) {
		log.debug("Salvato oggetto {} di {} byte sul bucket {} con ETag {} in {} ms", key,
			blobLength, bucket, response.eTag(), end);
	    }
	    final URL presignedUrl = presigner.getPresignedUrl(configuration, key);
	    //
	    final URI presignedURLasURI = presignedUrl.toURI();

	    final String tenant = getDefaultTenant();

	    return new ObjectStorageResource() {
		@Override
		public String getBucket() {
		    return bucket;
		}

		@Override
		public String getKey() {
		    return key;
		}

		@Override
		public String getETag() {
		    return response.eTag();
		}

		@Override
		public String getExpiration() {
		    return response.expiration();
		}

		@Override
		public URI getPresignedURL() {
		    return presignedURLasURI;
		}

		@Override
		public String getTenant() {
		    return tenant;
		}
	    };

	} catch (Exception e) {
	    throw ObjectStorageException.builder()
		    .message("{0}: impossibile salvare oggetto {1} sul bucket {2}",
			    configuration.getBackendName(), key, configuration.getBucket())
		    .cause(e).build();
	}
    }

    private Map<String, String> defaultMetadata() {

	Map<String, String> defaultMetadata = new HashMap<>();
	defaultMetadata.put(MEATADATA_INGEST_NODE, "versamento-fascicolo-v3-" + instanceUUID);
	defaultMetadata.put(MEATADATA_INGEST_TIME,
		ZonedDateTime.now().format(DateTimeFormatter.ISO_INSTANT));
	return defaultMetadata;
    }

    /**
     * Effettua il salvataggio del collegamento tra i sip del fascicolo e la chiave sull'object
     * storage
     *
     * @param object      informazioni dell'oggetto salvato
     * @param nmBackend   nome del backend (di tipo OS) su cui è stato salvato
     * @param idFascicolo id fascicolo
     *
     * @throws ObjectStorageException in caso di errore
     */
    public void saveObjectStorageLinkSipFasc(ObjectStorageResource object, String nmBackend,
	    long idFascicolo, BigDecimal idStrut) throws ObjectStorageException {
	try {
	    FasFascicolo fasFascicolo = entityManager.find(FasFascicolo.class, idFascicolo);

	    FasXmlVersFascObjectStorage osLink = new FasXmlVersFascObjectStorage();
	    osLink.setFasFascicolo(fasFascicolo);

	    osLink.setCdKeyFile(object.getKey());
	    osLink.setNmBucket(object.getBucket());
	    osLink.setNmTenant(object.getTenant());
	    osLink.setDecBackend(getBakendEntity(nmBackend));
	    osLink.setIdStrut(idStrut);

	    entityManager.persist(osLink);

	} catch (Exception e) {
	    throw ObjectStorageException.builder().message(LOG_MESSAGE_NO_SAVED).cause(e).build();
	}
    }

    /**
     * Effettua il salvataggio del collegamento tra i metadati (profili) del fascicolo e la chiave
     * sull'object storage
     *
     * @param object      informazioni dell'oggetto salvato
     * @param nmBackend   nome del backend (di tipo OS) su cui è stato salvato
     * @param idFascicolo id fascicolo
     * @param idStrut     id della struttura versante
     *
     * @throws ObjectStorageException in caso di errore
     */
    public void saveObjectStorageLinkMetaProfFasc(ObjectStorageResource object, String nmBackend,
	    long idFascicolo, BigDecimal idStrut) throws ObjectStorageException {
	try {

	    FasFascicolo fasFascicolo = entityManager.find(FasFascicolo.class, idFascicolo);

	    FasXmlFascObjectStorage osLink = new FasXmlFascObjectStorage();
	    osLink.setFasFascicolo(fasFascicolo);

	    osLink.setCdKeyFile(object.getKey());
	    osLink.setNmBucket(object.getBucket());
	    osLink.setNmTenant(object.getTenant());
	    osLink.setDecBackend(getBakendEntity(nmBackend));
	    osLink.setIdStrut(idStrut);

	    entityManager.persist(osLink);

	} catch (Exception e) {
	    throw ObjectStorageException.builder().message(LOG_MESSAGE_NO_SAVED).cause(e).build();
	}
    }

    /**
     * Effettua il salvataggio del collegamento tra i versamenti falliti e la loro chiave
     * sull'object storage (nel bucket "sessioni-err-ko-fasc")
     *
     * @param object           informazioni dell'oggetto salvato
     * @param nmBackend        nome del backend (di tipo OS) su cui è stato salvato
     * @param idSesFascicoloKo id sessione fallita
     * @param idStrut          id della struttura versante
     *
     * @throws ObjectStorageException in caso di errore
     */
    public void saveObjectStorageLinkSipSessioneKo(ObjectStorageResource object, String nmBackend,
	    long idSesFascicoloKo, BigDecimal idStrut) throws ObjectStorageException {
	try {

	    VrsSesFascicoloKo sesFascicoloKo = entityManager.find(VrsSesFascicoloKo.class,
		    idSesFascicoloKo);

	    VrsXmlSesFascKoObjectStorage linkFalliti = new VrsXmlSesFascKoObjectStorage();
	    linkFalliti.setVrsSesFascicoloKo(sesFascicoloKo);
	    linkFalliti.setDecBackend(getBakendEntity(nmBackend));
	    linkFalliti.setNmTenant(object.getTenant());
	    linkFalliti.setNmBucket(object.getBucket());
	    linkFalliti.setNmKeyFile(object.getKey());
	    linkFalliti.setIdStrut(idStrut);
	    entityManager.persist(linkFalliti);

	} catch (Exception e) {
	    throw ObjectStorageException.builder().message(LOG_MESSAGE_NO_SAVED).cause(e).build();
	}
    }

    /**
     * Effettua il salvataggio del collegamento tra i versamenti errati e la loro chiave sull'object
     * storage (nel bucket "sessioni-err-ko-fasc")
     *
     * @param object            informazioni dell'oggetto salvato
     * @param nmBackend         nome del backend (di tipo OS) su cui è stato salvato
     * @param idSesFascicoloErr id sessione errata
     * @param idStrut           id della struttura versante
     *
     * @throws ObjectStorageException in caso di errore
     */
    public void saveObjectStorageLinkSipSessioneErr(ObjectStorageResource object, String nmBackend,
	    long idSesFascicoloErr, BigDecimal idStrut) throws ObjectStorageException {
	try {

	    VrsSesFascicoloErr sesFascicoloErr = entityManager.find(VrsSesFascicoloErr.class,
		    idSesFascicoloErr);

	    VrsXmlSesFascErrObjectStorage linkErrati = new VrsXmlSesFascErrObjectStorage();
	    linkErrati.setVrsSesFascicoloErr(sesFascicoloErr);
	    linkErrati.setDecBackend(getBakendEntity(nmBackend));
	    linkErrati.setNmTenant(object.getTenant());
	    linkErrati.setNmBucket(object.getBucket());
	    linkErrati.setNmKeyFile(object.getKey());
	    linkErrati.setIdStrut(idStrut);
	    entityManager.persist(linkErrati);

	} catch (Exception e) {
	    throw ObjectStorageException.builder().message(LOG_MESSAGE_NO_SAVED).cause(e).build();
	}
    }

    private DecBackend getBakendEntity(String nomeBackend) {
	TypedQuery<DecBackend> query = entityManager.createQuery(
		"Select d from DecBackend d where d.nmBackend = :nomeBackend", DecBackend.class);
	query.setParameter("nomeBackend", nomeBackend);
	return query.getSingleResult();
    }

    /**
     * Ottieni la configurazione del backend a partire dal nome del backend
     *
     * @param nomeBackend per esempio "OBJECT_STORAGE_PRIMARIO"
     *
     * @return Informazioni sul Backend identificato
     *
     * @throws ObjectStorageException in caso di errore
     */
    public BackendStorage getBackend(String nomeBackend) throws ObjectStorageException {
	try {

	    DecBackend backend = getBakendEntity(nomeBackend);
	    final BackendStorage.STORAGE_TYPE type = BackendStorage.STORAGE_TYPE
		    .valueOf(backend.getNmTipoBackend());
	    final String backendName = backend.getNmBackend();

	    return new BackendStorage() {
		private static final long serialVersionUID = 1L;

		@Override
		public BackendStorage.STORAGE_TYPE getType() {
		    return type;
		}

		@Override
		public String getBackendName() {
		    return backendName;
		}
	    };

	} catch (IllegalArgumentException | NonUniqueResultException e) {
	    throw ObjectStorageException.builder()
		    .message("Impossibile ottenere le informazioni di backend").cause(e).build();
	}

    }

    private static final String BUCKET = "BUCKET";
    private static final String ACCESS_KEY_ID_SYS_PROP = "ACCESS_KEY_ID_SYS_PROP";
    private static final String SECRET_KEY_SYS_PROP = "SECRET_KEY_SYS_PROP";

    /**
     * Ottieni la configurazione per potersi collegare a quel bucket dell'Object Storage scelto.
     *
     * @param nomeBackend nome del backend <strong> di tipo DEC_BACKEND.NM_TIPO_BACKEND = 'OS'
     *                    </strong>come censito su DEC_BACKEND (per esempio OBJECT_STORAGE_PRIMARIO)
     * @param tipoUsoOs   ambito di utilizzo di questo backend (per esempio METADATI_FASCICOLI)
     *
     * @return Configurazione dell'Object Storage per quell'ambito
     *
     * @throws ObjectStorageException in caso di errore
     */
    public ObjectStorageBackend getObjectStorageConfiguration(final String nomeBackend,
	    final String tipoUsoOs) throws ObjectStorageException {

	if (s3Clients.isClientProvided()) {
	    return getStaticProviderConfiguration(tipoUsoOs);
	} else {
	    return getDynamicProviderConfiguration(nomeBackend, tipoUsoOs);
	}
    }

    private ObjectStorageBackend getStaticProviderConfiguration(String tipoUsoOs)
	    throws ObjectStorageException {
	Optional<String> bucket = getBucketByUsage(tipoUsoOs);

	if (bucket.isEmpty() || staticProviderAccessKeyId.isEmpty()
		|| staticProviderSecretAccessKey.isEmpty()) {
	    throw ObjectStorageException.builder()
		    .message("Configurazione object storage per tipologia {0} non corretta",
			    tipoUsoOs)
		    .build();
	}

	return createObjectStorageBackend(staticProviderAccessKeyId.get(),
		staticProviderSecretAccessKey.get(),
		s3Clients.getStaticProviderClient().serviceClientConfiguration().endpointOverride()
			.orElseThrow(() -> ObjectStorageException.builder()
				.message("Impossibile stabilire URI object storage per {0} statico",
					tipoUsoOs)
				.build()),
		bucket.get(), "StaticProvider");
    }

    private ObjectStorageBackend getDynamicProviderConfiguration(final String nomeBackend,
	    final String tipoUsoOs) throws ObjectStorageException {

	List<DecConfigObjectStorage> resultList = fetchDynamicConfigurations(nomeBackend,
		tipoUsoOs);

	String bucket = null;
	String nomeSystemPropertyAccessKeyId = null;
	String nomeSystemPropertySecretKey = null;
	String storageAddress = null;

	for (DecConfigObjectStorage config : resultList) {
	    switch (config.getNmConfigObjectStorage()) {
	    case ACCESS_KEY_ID_SYS_PROP:
		nomeSystemPropertyAccessKeyId = config.getDsValoreConfigObjectStorage();
		break;
	    case BUCKET:
		bucket = config.getDsValoreConfigObjectStorage();
		break;
	    case SECRET_KEY_SYS_PROP:
		nomeSystemPropertySecretKey = config.getDsValoreConfigObjectStorage();
		break;
	    default:
		throw ObjectStorageException.builder().message(
			"Impossibile stabilire la tipologia {0} tra quelle previste per l'object storage",
			tipoUsoOs).build();
	    }
	    // identico per tutti perché definito nella tabella padre
	    storageAddress = config.getDecBackend().getDlBackendUri();
	}

	validateDynamicConfiguration(bucket, nomeSystemPropertyAccessKeyId,
		nomeSystemPropertySecretKey, storageAddress);

	Optional<String> accessKeyId = getEnvironmentVariableAccessKeyId(tipoUsoOs);
	Optional<String> secretKey = getEnvironmentVariableSecretKey(tipoUsoOs);

	if (accessKeyId.isEmpty() || secretKey.isEmpty()) {
	    throw ObjectStorageException.builder().message(
		    "Errore in fase di recupero dei valori di accessKeyId / secretKey per la tipologia {0} del parametro per l'object storage",
		    tipoUsoOs).build();
	}

	return createObjectStorageBackend(accessKeyId.get(), secretKey.get(),
		URI.create(storageAddress), bucket, nomeBackend);
    }

    private Optional<String> getBucketByUsage(String tipoUsoOs) throws ObjectStorageException {
	switch (tipoUsoOs) {
	case METADATI_FASCICOLI:
	    return bucketNameMetadataFasc;
	case SESSIONI_FASC_ERR_KO:
	    return bucketNameSesErrKoFasc;
	default:
	    throw ObjectStorageException.builder().message(
		    "Impossibile stabilire il bucket per la tipologia {0} di utilizzo per l'object storage",
		    tipoUsoOs).build();
	}
    }

    private List<DecConfigObjectStorage> fetchDynamicConfigurations(String nomeBackend,
	    String tipoUsoOs) {
	return entityManager.createQuery(
		"Select c from DecConfigObjectStorage c where c.tiUsoConfigObjectStorage = :tipoUsoOs and c.decBackend.nmBackend = :nomeBackend order by c.nmConfigObjectStorage",
		DecConfigObjectStorage.class).setParameter("tipoUsoOs", tipoUsoOs)
		.setParameter("nomeBackend", nomeBackend).getResultList();
    }

    private void validateDynamicConfiguration(String bucket, String accessKeyId, String secretKey,
	    String storageAddress) throws ObjectStorageException {
	if (StringUtils.isBlank(bucket) || StringUtils.isBlank(accessKeyId)
		|| StringUtils.isBlank(secretKey) || StringUtils.isBlank(storageAddress)) {
	    throw ObjectStorageException.builder().message(
		    "Configurazione object storage errata, mancante uno o più parametri di configurazione bucket={0}, accessKeyId={1}, secretKey={2}, storageAddress={3}")
		    .build();
	}
    }

    private Optional<String> getEnvironmentVariableAccessKeyId(String tipoUsoOs)
	    throws ObjectStorageException {
	switch (tipoUsoOs) {
	case METADATI_FASCICOLI:
	    return accessKeyIdMetaFasc;
	case SESSIONI_FASC_ERR_KO:
	    return accessKeyIdSesErrKoFasc;
	default:
	    throw ObjectStorageException.builder()
		    .message("Impossibile stabilire la tipologia di utilizzo per l'object storage")
		    .build();
	}
    }

    private Optional<String> getEnvironmentVariableSecretKey(String tipoUsoOs)
	    throws ObjectStorageException {
	switch (tipoUsoOs) {
	case METADATI_FASCICOLI:
	    return secretKeyMetaFasc;
	case SESSIONI_FASC_ERR_KO:
	    return secretKeySesErrKoFasc;
	default:
	    throw ObjectStorageException.builder()
		    .message("Impossibile stabilire la tipologia di utilizzo per l'object storage")
		    .build();
	}
    }

    private ObjectStorageBackend createObjectStorageBackend(String accessKeyId, String secretKey,
	    URI address, String bucket, String backendName) {
	return new ObjectStorageBackend() {
	    private static final long serialVersionUID = -7818781527374773374L;

	    @Override
	    public String getBackendName() {
		return backendName;
	    }

	    @Override
	    public URI getAddress() {
		return address;
	    }

	    @Override
	    public String getBucket() {
		return bucket;
	    }

	    @Override
	    public String getAccessKeyId() {
		return accessKeyId;
	    }

	    @Override
	    public String getSecretKey() {
		return secretKey;
	    }
	};
    }

    /**
     * Genera la chiave del fascicolo da salvare sull'object storage.
     *
     * @param idFascicolo identificativo del fascicolo di cui salvare il contenuto
     *
     * @return chiave che verrà utilizzata sul bucket fascicoli e sessioni-err-ko-fasc
     *
     * @throws ObjectStorageException in caso di errore.
     */
    public String generateKeyFascicolo(long idFascicolo) throws ObjectStorageException {
	try {

	    FasFascicolo fascicolo = entityManager.find(FasFascicolo.class, idFascicolo);

	    String nmStrutNorm = fascicolo.getOrgStrut().getCdStrutNormaliz();

	    String nmEnteNorm = fascicolo.getOrgStrut().getOrgEnte().getCdEnteNormaliz();

	    int anno = fascicolo.getAaFascicolo().intValue();

	    return createKeyFascicoli(nmEnteNorm, nmStrutNorm, anno, idFascicolo);

	} catch (Exception e) {
	    throw ObjectStorageException.builder()
		    .message("Impossibile generare la chiave del fascicolo").cause(e).build();
	}
    }

    private String getDefaultTenant() {
	return configurationDao.getValoreParamApplicByApplic(ParametroApplDB.TENANT_OBJECT_STORAGE);

    }

    private String createKeyFascicoli(String nmEnteNorm, String nmStrutNorm, int anno,
	    long idFascicolo) {
	// Non serve a nulla
	String nmTenant = getDefaultTenant();

	return nmTenant + "/" + nmEnteNorm + "/" + nmStrutNorm + "/" + anno + "/" + idFascicolo;
    }

    private static void checkConfiguration(ObjectStorageBackend configuration)
	    throws ObjectStorageException {
	List<String> errors = new ArrayList<>();
	if (configuration.getAddress() == null) {
	    errors.add("indirizzo object storage");
	}
	if (StringUtils.isBlank(configuration.getAccessKeyId())) {
	    errors.add("access key Id");
	}
	if (StringUtils.isBlank(configuration.getSecretKey())) {
	    errors.add("secret Key");
	}
	if (StringUtils.isBlank(configuration.getBucket())) {
	    errors.add("nome bucket");
	}
	if (!errors.isEmpty()) {
	    throw ObjectStorageException.builder()
		    .message("Parametri mancanti per il collegamento a object storage: {0}",
			    String.join(",", errors))
		    .build();
	}

    }
}
