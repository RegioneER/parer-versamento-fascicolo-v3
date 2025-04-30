/**
 *
 */
package it.eng.parer.fascicolo.beans;

import java.util.Date;

import it.eng.parer.fascicolo.beans.dto.StrutturaVersFascicolo;
import it.eng.parer.fascicolo.beans.dto.base.CSChiaveFasc;
import it.eng.parer.fascicolo.beans.dto.base.RispostaControlli;
import it.eng.parer.fascicolo.beans.utils.Costanti.TipiGestioneFascAnnullati;
import jakarta.validation.constraints.NotNull;

public interface IControlliFascicoliService {

    RispostaControlli checkTipoFascicolo(
            @NotNull(message = "IControlliFascicoliService.checkTipoFascicolo: nomeTipoFascicolo non inizializzato") String nomeTipoFascicolo,
            @NotNull(message = "IControlliFascicoliService.checkTipoFascicolo: descKey non inizializzato") String descKey,
            long idStruttura);

    RispostaControlli checkTipoFascicoloIamUserOrganizzazione(
            @NotNull(message = "IControlliFascicoliService.checkTipoFascicoloIamUserOrganizzazione: versamento non inizializzato") String descKey,
            @NotNull(message = "IControlliFascicoliService.checkTipoFascicoloIamUserOrganizzazione: versamento non inizializzato") String tipoFasc,
            long idStruttura, long idUser, long idTipoFasc);

    RispostaControlli checkTipoFascicoloAnno(
            @NotNull(message = "IControlliFascicoliService.checkTipoFascicoloAnno: descChiaveFasc non inizializzato") String descChiaveFasc,
            @NotNull(message = "IControlliFascicoliService.checkTipoFascicoloAnno: tipoFasc non inizializzato") String tipoFasc,
            @NotNull(message = "IControlliFascicoliService.checkTipoFascicoloAnno: anno non inizializzato") Integer anno,
            long idTipoFascicolo);

    RispostaControlli checkTipoFascicoloSconosciuto(long idStruttura);

    RispostaControlli checkChiave(
            @NotNull(message = "IControlliFascicoliService.checkChiave: key non inizializzato") CSChiaveFasc key,
            @NotNull(message = "IControlliFascicoliService.checkChiave: descKey non inizializzato") String descKey,
            long idStruttura,
            @NotNull(message = "IControlliFascicoliService.checkChiave: tgfa non inizializzato") TipiGestioneFascAnnullati tgfa);

    /*
     * ######## FASE II ########
     *
     */

    RispostaControlli verificaSIPTitolario(
            @NotNull(message = "IControlliFascicoliService.verificaSIPTitolario: svf non inizializzato") StrutturaVersFascicolo svf);

    RispostaControlli getDecAaTipoFascicolo(long idAaTipoFascicolo);

    RispostaControlli checkFlDecAaTipoFascicoloOrgStrutt(long idAaTipoFasc);

    RispostaControlli getPartiAANumero(
            @NotNull(message = "IControlliFascicoliService.getPartiAANumero: descChiaveFasc non inizializzato") String descChiaveFasc,
            long idAATipoFasc);

    RispostaControlli checkDecVoceTitolWithComp(
            @NotNull(message = "IControlliFascicoliService.checkDecVoceTitolWithComp: cdCompositoVoceTitol non inizializzato") String cdCompositoVoceTitol,
            long idStrutt, Date dtApertura);

    RispostaControlli checkDecVoceTitolWithCompAndVoce(String cdVoceTitol,
            @NotNull(message = "IControlliFascicoliService.checkDecVoceTitolWithCompAndVoce: cdCompositoVoceTitol non inizializzato") String cdCompositoVoceTitol,
            long idStrutt,
            @NotNull(message = "IControlliFascicoliService.checkDecVoceTitolWithCompAndVoce: dtApertura non inizializzato") Date dtApertura);

    RispostaControlli checkCdVoceDescDecValVoceTitol(
            @NotNull(message = "IControlliFascicoliService.checkCdVoceDescDecValVoceTitol: cdCompositoVoceTitol non inizializzato") String cdCompositoVoceTitol,
            @NotNull(message = "IControlliFascicoliService.checkCdVoceDescDecValVoceTitol: dsVoceTitol non inizializzato") String dsVoceTitol,
            @NotNull(message = "IControlliFascicoliService.checkCdVoceDescDecValVoceTitol: dtApertura non inizializzato") Date dtApertura,
            long idTitol);

    RispostaControlli checkCdVoceDescDecValVoceTitol(String cdVoceTitol,
            @NotNull(message = "IControlliFascicoliService.checkCdVoceDescDecValVoceTitol: cdCompositoVoceTitol non inizializzato") String cdCompositoVoceTitol,
            @NotNull(message = "IControlliFascicoliService.checkCdVoceDescDecValVoceTitol: dsVoceTitol non inizializzato") String dsVoceTitol,
            @NotNull(message = "IControlliFascicoliService.checkCdVoceDescDecValVoceTitol: dtApertura non inizializzato") Date dtApertura,
            long idTitol);

    RispostaControlli getDecLvlVoceTitolWithNiLivello(long niLivello, long idTitol);

    RispostaControlli getDecTitolStrutt(long idStruttura, Date dtApertura);

    RispostaControlli calcAndcheckCdKeyNormalized(
            @NotNull(message = "IControlliFascicoliService.calcAndcheckCdKeyNormalized: key non inizializzato") CSChiaveFasc key,
            long idStrut,
            @NotNull(message = "IControlliFascicoliService.calcAndcheckCdKeyNormalized: cdKeyNormalized non inizializzato") String cdKeyNormalized);

}