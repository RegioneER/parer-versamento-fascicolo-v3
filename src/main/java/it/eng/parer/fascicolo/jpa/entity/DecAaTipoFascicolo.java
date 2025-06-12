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
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequenceGenerator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The persistent class for the DEC_AA_TIPO_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "DEC_AA_TIPO_FASCICOLO")
@NamedQuery(name = "DecAaTipoFascicolo.findAll", query = "SELECT d FROM DecAaTipoFascicolo d")
public class DecAaTipoFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idAaTipoFascicolo;
    private BigDecimal aaFinTipoFascicolo;
    private BigDecimal aaIniTipoFascicolo;
    private String flUpdFmtNumero;
    private BigDecimal niCharPadParteClassif;
    private DecTipoFascicolo decTipoFascicolo;
    private List<DecErrAaTipoFascicolo> decErrAaTipoFascicolos = new ArrayList<>();
    private List<DecParteNumeroFascicolo> decParteNumeroFascicolos = new ArrayList<>();
    private List<DecUsoModelloXsdFasc> decUsoModelloXsdFascs = new ArrayList<>();
    private List<DecWarnAaTipoFascicolo> decWarnAaTipoFascicolos = new ArrayList<>();
    private List<AplValoreParamApplic> aplValoreParamApplics = new ArrayList<>();
    private List<DecAttribFascicolo> decAttribFascicolos = new ArrayList<>();

    public DecAaTipoFascicolo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "DEC_AA_TIPO_FASCICOLO_IDAATIPOFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SDEC_AA_TIPO_FASCICOLO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEC_AA_TIPO_FASCICOLO_IDAATIPOFASCICOLO_GENERATOR")
    @Column(name = "ID_AA_TIPO_FASCICOLO")
    public Long getIdAaTipoFascicolo() {
	return this.idAaTipoFascicolo;
    }

    public void setIdAaTipoFascicolo(Long idAaTipoFascicolo) {
	this.idAaTipoFascicolo = idAaTipoFascicolo;
    }

    @Column(name = "AA_FIN_TIPO_FASCICOLO")
    public BigDecimal getAaFinTipoFascicolo() {
	return this.aaFinTipoFascicolo;
    }

    public void setAaFinTipoFascicolo(BigDecimal aaFinTipoFascicolo) {
	this.aaFinTipoFascicolo = aaFinTipoFascicolo;
    }

    @Column(name = "AA_INI_TIPO_FASCICOLO")
    public BigDecimal getAaIniTipoFascicolo() {
	return this.aaIniTipoFascicolo;
    }

    public void setAaIniTipoFascicolo(BigDecimal aaIniTipoFascicolo) {
	this.aaIniTipoFascicolo = aaIniTipoFascicolo;
    }

    @Column(name = "FL_UPD_FMT_NUMERO", columnDefinition = "CHAR")
    public String getFlUpdFmtNumero() {
	return this.flUpdFmtNumero;
    }

    public void setFlUpdFmtNumero(String flUpdFmtNumero) {
	this.flUpdFmtNumero = flUpdFmtNumero;
    }

    @Column(name = "NI_CHAR_PAD_PARTE_CLASSIF")
    public BigDecimal getNiCharPadParteClassif() {
	return this.niCharPadParteClassif;
    }

    public void setNiCharPadParteClassif(BigDecimal niCharPadParteClassif) {
	this.niCharPadParteClassif = niCharPadParteClassif;
    }

    // bi-directional many-to-one association to AplValoreParamApplic
    @OneToMany(mappedBy = "decAaTipoFascicolo")
    public List<AplValoreParamApplic> getAplValoreParamApplics() {
	return this.aplValoreParamApplics;
    }

    public void setAplValoreParamApplics(List<AplValoreParamApplic> aplValoreParamApplics) {
	this.aplValoreParamApplics = aplValoreParamApplics;
    }

    public AplValoreParamApplic addAplValoreParamApplic(AplValoreParamApplic aplValoreParamApplic) {
	getAplValoreParamApplics().add(aplValoreParamApplic);
	aplValoreParamApplic.setDecAaTipoFascicolo(this);

	return aplValoreParamApplic;
    }

    public AplValoreParamApplic removeAplValoreParamApplic(
	    AplValoreParamApplic aplValoreParamApplic) {
	getAplValoreParamApplics().remove(aplValoreParamApplic);
	aplValoreParamApplic.setDecAaTipoFascicolo(null);

	return aplValoreParamApplic;
    }

    // bi-directional many-to-one association to DecTipoFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_FASCICOLO")
    public DecTipoFascicolo getDecTipoFascicolo() {
	return this.decTipoFascicolo;
    }

    public void setDecTipoFascicolo(DecTipoFascicolo decTipoFascicolo) {
	this.decTipoFascicolo = decTipoFascicolo;
    }

    // bi-directional many-to-one association to DecErrAaTipoFascicolo
    @OneToMany(mappedBy = "decAaTipoFascicolo", cascade = CascadeType.REMOVE)
    public List<DecErrAaTipoFascicolo> getDecErrAaTipoFascicolos() {
	return this.decErrAaTipoFascicolos;
    }

    public void setDecErrAaTipoFascicolos(List<DecErrAaTipoFascicolo> decErrAaTipoFascicolos) {
	this.decErrAaTipoFascicolos = decErrAaTipoFascicolos;
    }

    public DecErrAaTipoFascicolo addDecErrAaTipoFascicolo(
	    DecErrAaTipoFascicolo decErrAaTipoFascicolo) {
	getDecErrAaTipoFascicolos().add(decErrAaTipoFascicolo);
	decErrAaTipoFascicolo.setDecAaTipoFascicolo(this);

	return decErrAaTipoFascicolo;
    }

    public DecErrAaTipoFascicolo removeDecErrAaTipoFascicolo(
	    DecErrAaTipoFascicolo decErrAaTipoFascicolo) {
	getDecErrAaTipoFascicolos().remove(decErrAaTipoFascicolo);
	decErrAaTipoFascicolo.setDecAaTipoFascicolo(null);

	return decErrAaTipoFascicolo;
    }

    // bi-directional many-to-one association to DecParteNumeroFascicolo
    @OneToMany(mappedBy = "decAaTipoFascicolo", cascade = {
	    CascadeType.PERSIST, CascadeType.REMOVE })
    public List<DecParteNumeroFascicolo> getDecParteNumeroFascicolos() {
	return this.decParteNumeroFascicolos;
    }

    public void setDecParteNumeroFascicolos(
	    List<DecParteNumeroFascicolo> decParteNumeroFascicolos) {
	this.decParteNumeroFascicolos = decParteNumeroFascicolos;
    }

    public DecParteNumeroFascicolo addDecParteNumeroFascicolo(
	    DecParteNumeroFascicolo decParteNumeroFascicolo) {
	getDecParteNumeroFascicolos().add(decParteNumeroFascicolo);
	decParteNumeroFascicolo.setDecAaTipoFascicolo(this);

	return decParteNumeroFascicolo;
    }

    public DecParteNumeroFascicolo removeDecParteNumeroFascicolo(
	    DecParteNumeroFascicolo decParteNumeroFascicolo) {
	getDecParteNumeroFascicolos().remove(decParteNumeroFascicolo);
	decParteNumeroFascicolo.setDecAaTipoFascicolo(null);

	return decParteNumeroFascicolo;
    }

    // bi-directional one-to-many association to DecUsoModelloXsdFasc
    @OneToMany(mappedBy = "decAaTipoFascicolo", cascade = {
	    CascadeType.PERSIST, CascadeType.REMOVE })
    public List<DecUsoModelloXsdFasc> getDecUsoModelloXsdFascs() {
	return this.decUsoModelloXsdFascs;
    }

    public void setDecUsoModelloXsdFascs(List<DecUsoModelloXsdFasc> decUsoModelloXsdFascs) {
	this.decUsoModelloXsdFascs = decUsoModelloXsdFascs;
    }

    public DecUsoModelloXsdFasc addDecUsoModelloXsdFasc(DecUsoModelloXsdFasc decUsoModelloXsdFasc) {
	getDecUsoModelloXsdFascs().add(decUsoModelloXsdFasc);
	decUsoModelloXsdFasc.setDecAaTipoFascicolo(this);

	return decUsoModelloXsdFasc;
    }

    public DecUsoModelloXsdFasc removeDecUsoModelloXsdFasc(
	    DecUsoModelloXsdFasc decUsoModelloXsdFasc) {
	getDecUsoModelloXsdFascs().remove(decUsoModelloXsdFasc);
	decUsoModelloXsdFasc.setDecAaTipoFascicolo(null);

	return decUsoModelloXsdFasc;
    }

    // bi-directional many-to-one association to DecWarnAaTipoFascicolo
    @OneToMany(mappedBy = "decAaTipoFascicolo", cascade = CascadeType.REMOVE)
    public List<DecWarnAaTipoFascicolo> getDecWarnAaTipoFascicolos() {
	return this.decWarnAaTipoFascicolos;
    }

    public void setDecWarnAaTipoFascicolos(List<DecWarnAaTipoFascicolo> decWarnAaTipoFascicolos) {
	this.decWarnAaTipoFascicolos = decWarnAaTipoFascicolos;
    }

    public DecWarnAaTipoFascicolo addDecWarnAaTipoFascicolo(
	    DecWarnAaTipoFascicolo decWarnAaTipoFascicolo) {
	getDecWarnAaTipoFascicolos().add(decWarnAaTipoFascicolo);
	decWarnAaTipoFascicolo.setDecAaTipoFascicolo(this);

	return decWarnAaTipoFascicolo;
    }

    public DecWarnAaTipoFascicolo removeDecWarnAaTipoFascicolo(
	    DecWarnAaTipoFascicolo decWarnAaTipoFascicolo) {
	getDecWarnAaTipoFascicolos().remove(decWarnAaTipoFascicolo);
	decWarnAaTipoFascicolo.setDecAaTipoFascicolo(null);

	return decWarnAaTipoFascicolo;
    }

    // bi-directional many-to-one association to DecAttribFascicolo
    @OneToMany(mappedBy = "decAaTipoFascicolo")
    public List<DecAttribFascicolo> getDecAttribFascicolos() {
	return this.decAttribFascicolos;
    }

    public void setDecAttribFascicolos(List<DecAttribFascicolo> decAttribFascicolos) {
	this.decAttribFascicolos = decAttribFascicolos;
    }

    public DecAttribFascicolo addAplValoreParamApplic(DecAttribFascicolo decAttribFascicolo) {
	getDecAttribFascicolos().add(decAttribFascicolo);
	decAttribFascicolo.setDecAaTipoFascicolo(this);

	return decAttribFascicolo;
    }

    public DecAttribFascicolo removeAplValoreParamApplic(DecAttribFascicolo decAttribFascicolo) {
	getDecAttribFascicolos().remove(decAttribFascicolo);
	decAttribFascicolo.setDecAaTipoFascicolo(null);

	return decAttribFascicolo;
    }

}
