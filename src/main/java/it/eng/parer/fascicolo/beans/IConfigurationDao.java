/**
 *
 */
package it.eng.parer.fascicolo.beans;

import java.util.List;
import java.util.Map;

import jakarta.validation.constraints.NotNull;

public interface IConfigurationDao {

    String getParamApplicValue(
            @NotNull(message = "IConfigurationDao.getParamApplicValue: nmParamApplic non inizializzato") String nmParamApplic);

    String getParamApplicValue(
            @NotNull(message = "IConfigurationDao.getParamApplicValue: nmParamApplic non inizializzato") String nmParamApplic,
            long idStrut, long idAmbiente, long idTipoUnitaDoc);

    String getParamApplicValue(
            @NotNull(message = "IConfigurationDao.getParamApplicValue: nmParamApplic non inizializzato") String nmParamApplic,
            long idStrut, long idAmbiente, long idTipoUnitaDoc, long idAaTipoFascicolo);

    Map<String, String> getValoreParamApplicByTiParamApplicAsMap(
            @NotNull(message = "IConfigurationDao.getValoreParamApplicByTiParamApplicAsMap: nmParamApplic non inizializzato") List<String> tiParamApplics);

    String getValoreParamApplicByApplic(
            @NotNull(message = "IConfigurationDao.getValoreParamApplicByApplic: nmParamApplic non inizializzato") String nmParamApplic);

    String getValoreParamApplicByAaTipoFasc(
            @NotNull(message = "IConfigurationDao.getValoreParamApplicByAaTipoFasc: nmParamApplic non inizializzato") String nmParamApplic,
            long idStrut, long idAmbiente, long idAaTipoFascicolo);

    /**
     *
     * Gestione FLAG true = 1, false = 0
     *
     * @param nmParamApplic
     *            nome parametro applicativo
     * @param idStrut
     *            id struttura
     * @param idAmbiente
     *            id ambiente
     * @param idTipoUnitaDoc
     *            id tipo unita doc
     * @param idAaTipoFascicolo
     *            id anno tipo fascicolo
     *
     * @return Valore del flag indicato secondo nome
     */
    String getParamApplicValueAsFl(
            @NotNull(message = "IConfigurationDao.nmParamApplic: nmParamApplic non inizializzato") String nmParamApplic,
            long idStrut, long idAmbiente, long idTipoUnitaDoc, long idAaTipoFascicolo);

}