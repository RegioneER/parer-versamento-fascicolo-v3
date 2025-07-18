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
 * The persistent class for the ELV_ELENCO_VERS_FASC_ANNUL database table.
 *
 */
@Entity
@Table(name = "ELV_ELENCO_VERS_FASC_ANNUL")
@NamedQuery(name = "ElvElencoVersFascAnnul.findAll", query = "SELECT e FROM ElvElencoVersFascAnnul e")
public class ElvElencoVersFascAnnul implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idElencoVersFascAnnul;
    private String dsUrnFascicoloAnnul;
    private FasFascicolo fasFascicolo;
    private ElvElencoVersFasc elvElencoVersFasc;

    public ElvElencoVersFascAnnul() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "ELV_ELENCO_VERS_FASC_ANNUL_IDELENCOVERSFASCANNUL_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SELV_ELENCO_VERS_FASC_ANNUL"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ELV_ELENCO_VERS_FASC_ANNUL_IDELENCOVERSFASCANNUL_GENERATOR")
    @Column(name = "ID_ELENCO_VERS_FASC_ANNUL")
    public Long getIdElencoVersFascAnnul() {
	return this.idElencoVersFascAnnul;
    }

    public void setIdElencoVersFascAnnul(Long idElencoVersFascAnnul) {
	this.idElencoVersFascAnnul = idElencoVersFascAnnul;
    }

    @Column(name = "DS_URN_FASCICOLO_ANNUL")
    public String getDsUrnFascicoloAnnul() {
	return this.dsUrnFascicoloAnnul;
    }

    public void setDsUrnFascicoloAnnul(String dsUrnFascicoloAnnul) {
	this.dsUrnFascicoloAnnul = dsUrnFascicoloAnnul;
    }

    // bi-directional many-to-one association to FasFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FASCICOLO")
    public FasFascicolo getFasFascicolo() {
	return this.fasFascicolo;
    }

    public void setFasFascicolo(FasFascicolo fasFascicolo) {
	this.fasFascicolo = fasFascicolo;
    }

    // bi-directional many-to-one association to ElvElencoVersFasc
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ELENCO_VERS_FASC")
    public ElvElencoVersFasc getElvElencoVersFasc() {
	return this.elvElencoVersFasc;
    }

    public void setElvElencoVersFasc(ElvElencoVersFasc elvElencoVerFasc) {
	this.elvElencoVersFasc = elvElencoVerFasc;
    }

}
