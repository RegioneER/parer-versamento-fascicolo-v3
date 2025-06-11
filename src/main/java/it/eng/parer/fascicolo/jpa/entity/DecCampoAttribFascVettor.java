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
import jakarta.persistence.Table;

/**
 * The persistent class for the DEC_ATTRIB_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "DEC_CAMPO_ATTRIB_FASC_VETTOR")
public class DecCampoAttribFascVettor implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idCampoAttribFascVettor;
    private DecModelloXsdAttribFascicolo decModelloXsdAttribFascicolo;
    private String nmCampo;
    private String dsCampo;
    private String tiCampo;
    private BigDecimal niOrd;
    private String flObbl;
    private BigDecimal niCharMin;
    private BigDecimal niCharMax;
    private String dsValEnum;
    private String dsRegExp;
    private String cdLabel;

    public DecCampoAttribFascVettor() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "DEC_CAMPO_ATTRIB_FASC_VETTOR_IDCAMPOATTRIBFASCVETTOR_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SDEC_CAMPO_ATTRIB_FASC_VETTOR"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEC_CAMPO_ATTRIB_FASC_VETTOR_IDCAMPOATTRIBFASCVETTOR_GENERATOR")
    @Column(name = "ID_CAMPO_ATTRIB_FASC_VETTOR")
    public Long getIdCampoAttribFascVettor() {
	return this.idCampoAttribFascVettor;
    }

    public void setIdCampoAttribFascVettor(Long idCampoAttribFascVettor) {
	this.idCampoAttribFascVettor = idCampoAttribFascVettor;
    }

    // bi-directional many-to-one association to DecModelloXsdAttribFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MODELLO_XSD_ATTRIB_FASC")
    public DecModelloXsdAttribFascicolo getDecModelloXsdAttribFascicolo() {
	return this.decModelloXsdAttribFascicolo;
    }

    public void setDecModelloXsdAttribFascicolo(
	    DecModelloXsdAttribFascicolo decModelloXsdAttribFascicolo) {
	this.decModelloXsdAttribFascicolo = decModelloXsdAttribFascicolo;
    }

    @Column(name = "NM_CAMPO")
    public String getNmCampo() {
	return this.nmCampo;
    }

    public void setNmCampo(String nmCampo) {
	this.nmCampo = nmCampo;
    }

    @Column(name = "DS_CAMPO")
    public String getDsCampo() {
	return this.dsCampo;
    }

    public void setDsCampo(String dsCampo) {
	this.dsCampo = dsCampo;
    }

    @Column(name = "TI_CAMPO")
    public String getTiCampo() {
	return this.tiCampo;
    }

    public void setTiCampo(String tiCampo) {
	this.tiCampo = tiCampo;
    }

    @Column(name = "NI_ORD")
    public BigDecimal getNiOrd() {
	return this.niOrd;
    }

    public void setNiOrd(BigDecimal niOrd) {
	this.niOrd = niOrd;
    }

    @Column(name = "FL_OBBL", columnDefinition = "CHAR")
    public String getFlObbl() {
	return this.flObbl;
    }

    public void setFlObbl(String flObbl) {
	this.flObbl = flObbl;
    }

    @Column(name = "NI_CHAR_MIN")
    public BigDecimal getNiCharMin() {
	return this.niCharMin;
    }

    public void setNiCharMin(BigDecimal niCharMin) {
	this.niCharMin = niCharMin;
    }

    @Column(name = "NI_CHAR_MAX")
    public BigDecimal getNiCharMax() {
	return this.niCharMax;
    }

    public void setNiCharMax(BigDecimal niCharMax) {
	this.niCharMax = niCharMax;
    }

    @Column(name = "DS_VAL_ENUM")
    public String getDsValEnum() {
	return this.dsValEnum;
    }

    public void setDsValEnum(String dsValEnum) {
	this.dsValEnum = dsValEnum;
    }

    @Column(name = "DS_REG_EXP")
    public String getDsRegExp() {
	return this.dsRegExp;
    }

    public void setDsRegExp(String dsRegExp) {
	this.dsRegExp = dsRegExp;
    }

    @Column(name = "CD_LABEL")
    public String getCdLabel() {
	return this.cdLabel;
    }

    public void setCdLabel(String cdLabel) {
	this.cdLabel = cdLabel;
    }
}
