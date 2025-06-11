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

/**
 *
 */
package it.eng.parer.fascicolo.beans;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import it.eng.parer.fascicolo.beans.dto.BackendStorage;
import it.eng.parer.fascicolo.beans.dto.ObjectStorageBackend;
import it.eng.parer.fascicolo.beans.dto.ObjectStorageResource;
import it.eng.parer.fascicolo.beans.exceptions.ObjectStorageException;
import jakarta.validation.constraints.NotNull;
import software.amazon.awssdk.services.s3.model.Tag;

public interface ISalvataggioBackendDao {

    String getBackendSessioniErrKo() throws ObjectStorageException;

    String getBackendByServiceName(
	    @NotNull(message = "ISalvataggioBackendDao.getBackendByServiceName: idAaTipoFascicolo non inizializzato") long idAaTipoFascicolo,
	    @NotNull(message = "ISalvataggioBackendDao.getBackendByServiceName: nomeWs non inizializzato") String nomeWs)
	    throws ObjectStorageException;

    ObjectStorageResource putObject(
	    @NotNull(message = "ISalvataggioBackendDao.putObject: blob non inizializzato") InputStream blob,
	    @NotNull(message = "ISalvataggioBackendDao.putObject: blobLength non inizializzato") long blobLength,
	    @NotNull(message = "ISalvataggioBackendDao.putObject: key non inizializzato") final String key,
	    @NotNull(message = "ISalvataggioBackendDao.putObject: configuration non inizializzato") ObjectStorageBackend configuration)
	    throws ObjectStorageException;

    ObjectStorageResource putObject(
	    @NotNull(message = "ISalvataggioBackendDao.putObject: blob non inizializzato") InputStream blob,
	    @NotNull(message = "ISalvataggioBackendDao.putObject: blobLength non inizializzato") long blobLength,
	    @NotNull(message = "ISalvataggioBackendDao.putObject: key non inizializzato") final String key,
	    @NotNull(message = "ISalvataggioBackendDao.putObject: configuration non inizializzato") ObjectStorageBackend configuration,
	    @NotNull(message = "ISalvataggioBackendDao.putObject: metadata non inizializzato") Optional<Map<String, String>> metadata,
	    @NotNull(message = "ISalvataggioBackendDao.putObject: tags non inizializzato") Optional<Set<Tag>> tags,
	    @NotNull(message = "ISalvataggioBackendDao.putObject: base64md5 non inizializzato") Optional<String> base64md5)
	    throws ObjectStorageException;

    void saveObjectStorageLinkSipFasc(
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkSipFasc: object non inizializzato") ObjectStorageResource object,
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkSipFasc: nmBackend non inizializzato") String nmBackend,
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkSipFasc: idFascicolo non inizializzato") long idFascicolo,
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkSipFasc: idStrut non inizializzato") BigDecimal idStrut)
	    throws ObjectStorageException;

    void saveObjectStorageLinkMetaProfFasc(
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkMetaProfFasc: object non inizializzato") ObjectStorageResource object,
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkMetaProfFasc: nmBackend non inizializzato") String nmBackend,
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkMetaProfFasc: idFascicolo non inizializzato") long idFascicolo,
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkMetaProfFasc: idStrut non inizializzato") BigDecimal idStrut)
	    throws ObjectStorageException;

    void saveObjectStorageLinkSipSessioneKo(
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkSipSessioneKo: object non inizializzato") ObjectStorageResource object,
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkSipSessioneKo: nmBackend non inizializzato") String nmBackend,
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkSipSessioneKo: idSesFascicoloKo non inizializzato") long idSesFascicoloKo,
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkSipSessioneKo: idStrut non inizializzato") BigDecimal idStrut)
	    throws ObjectStorageException;

    void saveObjectStorageLinkSipSessioneErr(
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkSipSessioneErr: object non inizializzato") ObjectStorageResource object,
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkSipSessioneErr: nmBackend non inizializzato") String nmBackend,
	    @NotNull(message = "ISalvataggioBackendDao.saveObjectStorageLinkSipSessioneErr: idSesFascicoloErr non inizializzato") long idSesFascicoloErr,
	    BigDecimal idStrut) throws ObjectStorageException;

    BackendStorage getBackend(
	    @NotNull(message = "ISalvataggioBackendDao.getBackend: nomeBackend non inizializzato") String nomeBackend)
	    throws ObjectStorageException;

    ObjectStorageBackend getObjectStorageConfiguration(
	    @NotNull(message = "ISalvataggioBackendDao.getObjectStorageConfiguration: nomeBackend non inizializzato") final String nomeBackend,
	    @NotNull(message = "ISalvataggioBackendDao.getObjectStorageConfiguration: tipoUsoOs non inizializzato") final String tipoUsoOs)
	    throws ObjectStorageException;

    String generateKeyFascicolo(
	    @NotNull(message = "ISalvataggioBackendDao.generateKeyFascicolo: idFascicolo non inizializzato") long idFascicolo)
	    throws ObjectStorageException;

}
