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
 * The persistent class for the FAS_UNI_ORG_RESP_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_UNI_ORG_RESP_FASCICOLO")
@NamedQuery(name = "FasUniOrgRespFascicolo.find", query = "SELECT f FROM FasUniOrgRespFascicolo f WHERE f.fasFascicolo.idFascicolo = :idFascicolo")
public class FasUniOrgRespFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idUniOrgRespFascicolo;
    private String cdUniOrgResp;
    private FasFascicolo fasFascicolo;

    public FasUniOrgRespFascicolo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_UNI_ORG_RESP_FASCICOLO_IDUNIORGRESPFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_UNI_ORG_RESP_FASCICOLO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_UNI_ORG_RESP_FASCICOLO_IDUNIORGRESPFASCICOLO_GENERATOR")
    @Column(name = "ID_UNI_ORG_RESP_FASCICOLO")
    public Long getIdUniOrgRespFascicolo() {
	return this.idUniOrgRespFascicolo;
    }

    public void setIdUniOrgRespFascicolo(Long idUniOrgRespFascicolo) {
	this.idUniOrgRespFascicolo = idUniOrgRespFascicolo;
    }

    @Column(name = "CD_UNI_ORG_RESP")
    public String getCdUniOrgResp() {
	return this.cdUniOrgResp;
    }

    public void setCdUniOrgResp(String cdUniOrgResp) {
	this.cdUniOrgResp = cdUniOrgResp;
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
}
