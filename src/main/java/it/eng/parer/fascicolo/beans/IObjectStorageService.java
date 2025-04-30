package it.eng.parer.fascicolo.beans;

import java.math.BigDecimal;

import java.util.Map;

import it.eng.parer.fascicolo.beans.dto.BackendStorage;
import it.eng.parer.fascicolo.beans.dto.ObjectStorageResource;
import it.eng.parer.fascicolo.beans.exceptions.ObjectStorageException;
import jakarta.validation.constraints.NotNull;

public interface IObjectStorageService {

    BackendStorage lookupBackendVrsSessioniErrKo() throws ObjectStorageException;

    BackendStorage lookupBackendByServiceName(
            @NotNull(message = "ISalvataggioBackendDao.lookupBackendByServiceName: idAaTipoFascicolo non inizializzato") long idAaTipoFascicolo,
            @NotNull(message = "ISalvataggioBackendDao.lookupBackendByServiceName: nomeWs non inizializzato") String nomeWs)
            throws ObjectStorageException;

    ObjectStorageResource createSipInSessioniKo(
            @NotNull(message = "ISalvataggioBackendDao.createSipInSessioniKo: nomeBackend non inizializzato") String nomeBackend,
            @NotNull(message = "ISalvataggioBackendDao.createSipInSessioniKo: xmlFiles non inizializzato") Map<String, String> xmlFiles,
            @NotNull(message = "ISalvataggioBackendDao.createSipInSessioniKo: idSesFascicoloKo non inizializzato") long idSesFascicoloKo,
            @NotNull(message = "ISalvataggioBackendDao.createSipInSessioniKo: idStrut non inizializzato") BigDecimal idStrut);

    ObjectStorageResource createSipInSessioniErr(
            @NotNull(message = "ISalvataggioBackendDao.createSipInSessioniErr: nomeBackend non inizializzato") String nomeBackend,
            @NotNull(message = "ISalvataggioBackendDao.createSipInSessioniErr: xmlFiles non inizializzato") Map<String, String> xmlFiles,
            @NotNull(message = "ISalvataggioBackendDao.createSipInSessioniErr: idSesFascicoloErr non inizializzato") long idSesFascicoloErr,
            BigDecimal idStrut);

    ObjectStorageResource createResourcesInSipFascicoli(
            @NotNull(message = "ISalvataggioBackendDao.createResourcesInSipFascicoli: urn non inizializzato") String urn,
            @NotNull(message = "ISalvataggioBackendDao.createResourcesInSipFascicoli: nomeBackend non inizializzato") String nomeBackend,
            @NotNull(message = "ISalvataggioBackendDao.createResourcesInSipFascicoli: xmlFiles non inizializzato") Map<String, String> xmlFiles,
            @NotNull(message = "ISalvataggioBackendDao.createResourcesInSipFascicoli: idFascicolo non inizializzato") long idFascicolo,
            @NotNull(message = "ISalvataggioBackendDao.createResourcesInSipFascicoli: idStrut non inizializzato") BigDecimal idStrut);

    ObjectStorageResource createResourcesInMetaProfFascicoli(
            @NotNull(message = "ISalvataggioBackendDao.createResourcesInMetaProfFascicoli: urn non inizializzato") String urn,
            @NotNull(message = "ISalvataggioBackendDao.createResourcesInMetaProfFascicoli: nomeBackend non inizializzato") String nomeBackend,
            @NotNull(message = "ISalvataggioBackendDao.createResourcesInMetaProfFascicoli: xmlFiles non inizializzato") Map<String, String> xmlFiles,
            @NotNull(message = "ISalvataggioBackendDao.createResourcesInMetaProfFascicoli: idFascicolo non inizializzato") long idFascicolo,
            @NotNull(message = "ISalvataggioBackendDao.createResourcesInMetaProfFascicoli: idStrut non inizializzato") BigDecimal idStrut);

    void saveLinkVrsSesKoFromObjectStorage(
            @NotNull(message = "ISalvataggioBackendDao.saveLinkVrsSesKoFromObjectStorage: object non inizializzato") ObjectStorageResource object,
            @NotNull(message = "ISalvataggioBackendDao.saveLinkVrsSesKoFromObjectStorage: nomeBackend non inizializzato") String nomeBackend,
            @NotNull(message = "ISalvataggioBackendDao.saveLinkVrsSesKoFromObjectStorage: idSesFascicoloKo non inizializzato") long idSesFascicoloKo,
            @NotNull(message = "ISalvataggioBackendDao.saveLinkVrsSesKoFromObjectStorage: idStrut non inizializzato") BigDecimal idStrut)
            throws ObjectStorageException;

    void saveLinkVrsSesErrFromObjectStorage(
            @NotNull(message = "ISalvataggioBackendDao.saveLinkVrsSesErrFromObjectStorage: object non inizializzato") ObjectStorageResource object,
            @NotNull(message = "ISalvataggioBackendDao.saveLinkVrsSesErrFromObjectStorage: nomeBackend non inizializzato") String nomeBackend,
            @NotNull(message = "ISalvataggioBackendDao.saveLinkVrsSesErrFromObjectStorage: idSesFascicoloErr non inizializzato") long idSesFascicoloErr,
            BigDecimal idStrut) throws ObjectStorageException;

}
