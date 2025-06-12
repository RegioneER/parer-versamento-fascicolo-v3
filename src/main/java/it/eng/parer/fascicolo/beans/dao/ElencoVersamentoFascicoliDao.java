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

import java.math.BigDecimal;
import java.util.ArrayList;

import it.eng.parer.fascicolo.beans.IElencoVersamentoFascicoliDao;
import it.eng.parer.fascicolo.jpa.entity.ElvFascDaElabElenco;
import it.eng.parer.fascicolo.jpa.entity.FasFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasStatoConservFascicolo;
import it.eng.parer.fascicolo.jpa.entity.FasStatoFascicoloElenco;
import it.eng.parer.fascicolo.jpa.entity.constraint.ElvFascDaElabElenco.TiStatoFascDaElab;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoConservFascicolo.TiStatoConservazione;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasStatoFascicoloElenco.TiStatoFascElenco;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

/**
 *
 * @author sinatti_S
 */
@ApplicationScoped
public class ElencoVersamentoFascicoliDao implements IElencoVersamentoFascicoliDao {

    @Inject
    EntityManager entityManager;

    @Override
    public ElvFascDaElabElenco insertFascicoloOnCodaDaElab(FasFascicolo fascicolo, long idTipoFasc,
	    TiStatoFascDaElab status) {
	// init
	fascicolo.setElvFascDaElabElencos(new ArrayList<>());

	ElvFascDaElabElenco fascVersDaElab = new ElvFascDaElabElenco();
	fascVersDaElab.setFasFascicolo(fascicolo);
	fascVersDaElab.setIdTipoFascicolo(new BigDecimal(idTipoFasc));
	fascVersDaElab.setTiStatoFascDaElab(status);
	fascVersDaElab.setIdStrut(new BigDecimal(fascicolo.getOrgStrut().getIdStrut()));
	fascVersDaElab.setAaFascicolo(fascicolo.getAaFascicolo());
	fascVersDaElab.setTsVersFascicolo(fascicolo.getTsFineSes());

	fascicolo.getElvFascDaElabElencos().add(fascVersDaElab);
	entityManager.persist(fascVersDaElab);
	entityManager.flush();
	return fascVersDaElab;
    }

    @Override
    public FasStatoConservFascicolo insertFascicoloOnStatoCons(FasFascicolo fascicolo,
	    TiStatoConservazione status) {
	// init
	fascicolo.setFasStatoConservFascicoloElencos(new ArrayList<>());

	FasStatoConservFascicolo statoConservFascicolo = new FasStatoConservFascicolo();
	statoConservFascicolo.setFasFascicolo(fascicolo);
	statoConservFascicolo.setIamUser(fascicolo.getIamUser());
	statoConservFascicolo.setTiStatoConservazione(status);
	statoConservFascicolo.setTsStato(fascicolo.getTsFineSes());

	fascicolo.getFasStatoConservFascicoloElencos().add(statoConservFascicolo);
	entityManager.persist(statoConservFascicolo);
	entityManager.flush();
	return statoConservFascicolo;
    }

    @Override
    public FasStatoFascicoloElenco insertFascicoloOnStatoElenco(FasFascicolo fascicolo,
	    TiStatoFascElenco status) {
	// init
	fascicolo.setFasStatoFascicoloElencos(new ArrayList<>());

	FasStatoFascicoloElenco statoFascicoloElenco = new FasStatoFascicoloElenco();
	statoFascicoloElenco.setFasFascicolo(fascicolo);
	statoFascicoloElenco.setIamUser(fascicolo.getIamUser());
	statoFascicoloElenco.setTiStatoFascElencoVers(status);
	statoFascicoloElenco.setTsStato(fascicolo.getTsFineSes());

	fascicolo.getFasStatoFascicoloElencos().add(statoFascicoloElenco);
	entityManager.persist(statoFascicoloElenco);
	entityManager.flush();
	return statoFascicoloElenco;
    }

}
