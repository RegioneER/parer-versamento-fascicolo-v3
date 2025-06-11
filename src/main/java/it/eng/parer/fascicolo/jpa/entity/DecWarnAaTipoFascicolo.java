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
import java.math.BigDecimal;

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
 * The persistent class for the DEC_WARN_AA_TIPO_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "DEC_WARN_AA_TIPO_FASCICOLO")
@NamedQuery(name = "DecWarnAaTipoFascicolo.findAll", query = "SELECT d FROM DecWarnAaTipoFascicolo d")
public class DecWarnAaTipoFascicolo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idWarnAaTipoFascicolo;
    private BigDecimal aaTipoFascicolo;
    private String flWarnAaTipoFascicolo;
    private DecAaTipoFascicolo decAaTipoFascicolo;

    public DecWarnAaTipoFascicolo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "DEC_WARN_AA_TIPO_FASCICOLO_IDWARNAATIPOFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SDEC_WARN_AA_TIPO_FASCICOLO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEC_WARN_AA_TIPO_FASCICOLO_IDWARNAATIPOFASCICOLO_GENERATOR")
    @Column(name = "ID_WARN_AA_TIPO_FASCICOLO")
    public Long getIdWarnAaTipoFascicolo() {
	return this.idWarnAaTipoFascicolo;
    }

    public void setIdWarnAaTipoFascicolo(Long idWarnAaTipoFascicolo) {
	this.idWarnAaTipoFascicolo = idWarnAaTipoFascicolo;
    }

    @Column(name = "AA_TIPO_FASCICOLO")
    public BigDecimal getAaTipoFascicolo() {
	return this.aaTipoFascicolo;
    }

    public void setAaTipoFascicolo(BigDecimal aaTipoFascicolo) {
	this.aaTipoFascicolo = aaTipoFascicolo;
    }

    @Column(name = "FL_WARN_AA_TIPO_FASCICOLO", columnDefinition = "CHAR")
    public String getFlWarnAaTipoFascicolo() {
	return this.flWarnAaTipoFascicolo;
    }

    public void setFlWarnAaTipoFascicolo(String flWarnAaTipoFascicolo) {
	this.flWarnAaTipoFascicolo = flWarnAaTipoFascicolo;
    }

    // bi-directional many-to-one association to DecAaTipoFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AA_TIPO_FASCICOLO")
    public DecAaTipoFascicolo getDecAaTipoFascicolo() {
	return this.decAaTipoFascicolo;
    }

    public void setDecAaTipoFascicolo(DecAaTipoFascicolo decAaTipoFascicolo) {
	this.decAaTipoFascicolo = decAaTipoFascicolo;
    }

}
