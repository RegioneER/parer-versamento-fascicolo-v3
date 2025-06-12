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

package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequenceGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the FAS_IND_RIF_SOG database table.
 *
 */
@Entity
@Table(name = "FAS_IND_RIF_SOG")
@NamedQuery(name = "FasRifIndSog.find", query = "SELECT f FROM FasRifIndSog f WHERE f.fasSogFascicolo.idSogFascicolo = :idSogFascicolo")
public class FasRifIndSog implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idIndRifSog;
    private String dsIndDifRif;

    private FasSogFascicolo fasSogFascicolo;

    public FasRifIndSog() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_IND_RIF_SOG_IDINDRIFSOG_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_IND_RIF_SOG"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_IND_RIF_SOG_IDINDRIFSOG_GENERATOR")
    @Column(name = "ID_IND_RIF_SOG")
    public Long getIdIndRifSog() {
	return this.idIndRifSog;
    }

    public void setIdIndRifSog(Long idIndRifSog) {
	this.idIndRifSog = idIndRifSog;
    }

    @Column(name = "DS_IND_DIF_RIF")
    public String getDsIndDifRif() {
	return this.dsIndDifRif;
    }

    public void setDsIndDifRif(String dsIndDifRif) {
	this.dsIndDifRif = dsIndDifRif;
    }

    // bi-directional many-to-one association to FasFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SOG_FASCICOLO")
    public FasSogFascicolo getFasSogFascicolo() {
	return this.fasSogFascicolo;
    }

    public void setFasSogFascicolo(FasSogFascicolo fasSogFascicolo) {
	this.fasSogFascicolo = fasSogFascicolo;
    }

}
