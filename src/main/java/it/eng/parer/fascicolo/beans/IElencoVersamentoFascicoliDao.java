/**
 *
 */
package it.eng.parer.fascicolo.beans;

import it.eng.parer.fascicolo.jpa.entity.ElvFascDaElabElenco;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasStatoConservFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasStatoFascicoloElenco;
import it.eng.parer.fascicolo.jpa.entity.constraint.ElvFascDaElabElenco.TiStatoFascDaElab;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoConservFascicolo.TiStatoConservazione;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoFascicoloElenco.TiStatoFascElenco;
import jakarta.validation.constraints.NotNull;

public interface IElencoVersamentoFascicoliDao {

    ElvFascDaElabElenco insertFascicoloOnCodaDaElab(
            @NotNull(message = "IElencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab: fascicolo non inizializzato") FasFascicolo fascicolo,
            long idTipoFasc,
            @NotNull(message = "IElencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab: status non inizializzato") TiStatoFascDaElab status);

    FasStatoConservFascicolo insertFascicoloOnStatoCons(
            @NotNull(message = "IElencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab: fascicolo non inizializzato") FasFascicolo fascicolo,
            @NotNull(message = "IElencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab: fascicolo non inizializzato") TiStatoConservazione status);

    FasStatoFascicoloElenco insertFascicoloOnStatoElenco(
            @NotNull(message = "IElencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab: fascicolo non inizializzato") FasFascicolo fascicolo,
            @NotNull(message = "IElencoVersamentoFascicoliDao.insertFascicoloOnCodaDaElab: fascicolo non inizializzato") TiStatoFascElenco status);

}
