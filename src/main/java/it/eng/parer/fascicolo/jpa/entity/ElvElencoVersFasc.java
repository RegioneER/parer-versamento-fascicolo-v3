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
import java.time.LocalDateTime;
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
 * The persistent class for the ELV_ELENCO_VERS_FASC database table.
 *
 */
@Entity
@Table(name = "ELV_ELENCO_VERS_FASC")
@NamedQuery(name = "ElvElencoVersFasc.findAll", query = "SELECT e FROM ElvElencoVersFasc e")
public class ElvElencoVersFasc implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idElencoVersFasc;
    private BigDecimal aaFascicolo;
    private String dlMotivoChius;
    private String dsUrnElenco;
    private String dsUrnNormalizElenco;
    private LocalDateTime dtScadChius;
    private String flElencoStandard;
    private BigDecimal idStatoElencoVersFascCor;
    private BigDecimal niFascVersElenco;
    private BigDecimal niMaxFascCrit;
    private BigDecimal niTempoScadChiusCrit;
    private BigDecimal niIndiciAip;
    private String ntElencoChiuso;
    private String ntIndiceElenco;
    private String tiScadChiusCrit;
    private String tiTempoScadChiusCrit;
    private LocalDateTime tsCreazioneElenco;
    private List<FasFascicolo> fasFascicoli = new ArrayList<>();
    private DecCriterioRaggrFasc decCriterioRaggrFasc;
    private OrgStrut orgStrut;
    private List<ElvElencoVersFascDaElab> elvElencoVersFascDaElabs = new ArrayList<>();
    private List<ElvStatoElencoVersFasc> elvStatoElencoVersFascicoli = new ArrayList<>();
    private List<ElvFileElencoVersFasc> elvFileElencoVersFasc = new ArrayList<>();
    private List<FasVerAipFascicolo> fasVerAipFascicolos = new ArrayList<>();
    private List<FasAipFascicoloDaElab> fasAipFascicoloDaElabs = new ArrayList<>();
    private List<ElvElencoVersFascAnnul> elvElencoVersFascAnnuls = new ArrayList<>();

    public ElvElencoVersFasc() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "ELV_ELENCO_VERS_FASC_IDELENCOVERSFASC_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SELV_ELENCO_VERS_FASC"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ELV_ELENCO_VERS_FASC_IDELENCOVERSFASC_GENERATOR")
    @Column(name = "ID_ELENCO_VERS_FASC")
    public Long getIdElencoVersFasc() {
	return this.idElencoVersFasc;
    }

    public void setIdElencoVersFasc(Long idElencoVersFasc) {
	this.idElencoVersFasc = idElencoVersFasc;
    }

    @Column(name = "AA_FASCICOLO")
    public BigDecimal getAaFascicolo() {
	return this.aaFascicolo;
    }

    public void setAaFascicolo(BigDecimal aaFascicolo) {
	this.aaFascicolo = aaFascicolo;
    }

    @Column(name = "DL_MOTIVO_CHIUS")
    public String getDlMotivoChius() {
	return this.dlMotivoChius;
    }

    public void setDlMotivoChius(String dlMotivoChius) {
	this.dlMotivoChius = dlMotivoChius;
    }

    @Column(name = "DS_URN_ELENCO")
    public String getDsUrnElenco() {
	return this.dsUrnElenco;
    }

    public void setDsUrnElenco(String dsUrnElenco) {
	this.dsUrnElenco = dsUrnElenco;
    }

    @Column(name = "DS_URN_NORMALIZ_ELENCO")
    public String getDsUrnNormalizElenco() {
	return this.dsUrnNormalizElenco;
    }

    public void setDsUrnNormalizElenco(String dsUrnNormalizElenco) {
	this.dsUrnNormalizElenco = dsUrnNormalizElenco;
    }

    @Column(name = "DT_SCAD_CHIUS")
    public LocalDateTime getDtScadChius() {
	return this.dtScadChius;
    }

    public void setDtScadChius(LocalDateTime dtScadChius) {
	this.dtScadChius = dtScadChius;
    }

    @Column(name = "FL_ELENCO_STANDARD", columnDefinition = "CHAR")
    public String getFlElencoStandard() {
	return this.flElencoStandard;
    }

    public void setFlElencoStandard(String flElencoStandard) {
	this.flElencoStandard = flElencoStandard;
    }

    @Column(name = "ID_STATO_ELENCO_VERS_FASC_COR")
    public BigDecimal getIdStatoElencoVersFascCor() {
	return this.idStatoElencoVersFascCor;
    }

    public void setIdStatoElencoVersFascCor(BigDecimal idStatoElencoVersFascCor) {
	this.idStatoElencoVersFascCor = idStatoElencoVersFascCor;
    }

    @Column(name = "NI_FASC_VERS_ELENCO")
    public BigDecimal getNiFascVersElenco() {
	return this.niFascVersElenco;
    }

    public void setNiFascVersElenco(BigDecimal niFascVersElenco) {
	this.niFascVersElenco = niFascVersElenco;
    }

    @Column(name = "NI_MAX_FASC_CRIT")
    public BigDecimal getNiMaxFascCrit() {
	return this.niMaxFascCrit;
    }

    public void setNiMaxFascCrit(BigDecimal niMaxFascCrit) {
	this.niMaxFascCrit = niMaxFascCrit;
    }

    @Column(name = "NI_TEMPO_SCAD_CHIUS_CRIT")
    public BigDecimal getNiTempoScadChiusCrit() {
	return this.niTempoScadChiusCrit;
    }

    public void setNiTempoScadChiusCrit(BigDecimal niTempoScadChiusCrit) {
	this.niTempoScadChiusCrit = niTempoScadChiusCrit;
    }

    @Column(name = "NI_INDICI_AIP")
    public BigDecimal getNiIndiciAip() {
	return this.niIndiciAip;
    }

    public void setNiIndiciAip(BigDecimal niIndiciAip) {
	this.niIndiciAip = niIndiciAip;
    }

    @Column(name = "NT_ELENCO_CHIUSO")
    public String getNtElencoChiuso() {
	return this.ntElencoChiuso;
    }

    public void setNtElencoChiuso(String ntElencoChiuso) {
	this.ntElencoChiuso = ntElencoChiuso;
    }

    @Column(name = "NT_INDICE_ELENCO")
    public String getNtIndiceElenco() {
	return this.ntIndiceElenco;
    }

    public void setNtIndiceElenco(String ntIndiceElenco) {
	this.ntIndiceElenco = ntIndiceElenco;
    }

    @Column(name = "TI_SCAD_CHIUS_CRIT")
    public String getTiScadChiusCrit() {
	return this.tiScadChiusCrit;
    }

    public void setTiScadChiusCrit(String tiScadChiusCrit) {
	this.tiScadChiusCrit = tiScadChiusCrit;
    }

    @Column(name = "TI_TEMPO_SCAD_CHIUS_CRIT")
    public String getTiTempoScadChiusCrit() {
	return this.tiTempoScadChiusCrit;
    }

    public void setTiTempoScadChiusCrit(String tiTempoScadChiusCrit) {
	this.tiTempoScadChiusCrit = tiTempoScadChiusCrit;
    }

    @Column(name = "TS_CREAZIONE_ELENCO")
    public LocalDateTime getTsCreazioneElenco() {
	return this.tsCreazioneElenco;
    }

    public void setTsCreazioneElenco(LocalDateTime tsCreazioneElenco) {
	this.tsCreazioneElenco = tsCreazioneElenco;
    }

    // bi-directional many-to-one association to FasFascicolo
    @OneToMany(mappedBy = "elvElencoVersFasc")
    public List<FasFascicolo> getFasFascicoli() {
	return this.fasFascicoli;
    }

    public void setFasFascicoli(List<FasFascicolo> fasFascicoli) {
	this.fasFascicoli = fasFascicoli;
    }

    // bi-directional many-to-one association to DecCriterioRaggrFasc
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_CRITERIO_RAGGR_FASC")
    public DecCriterioRaggrFasc getDecCriterioRaggrFasc() {
	return this.decCriterioRaggrFasc;
    }

    public void setDecCriterioRaggrFasc(DecCriterioRaggrFasc decCriterioRaggrFasc) {
	this.decCriterioRaggrFasc = decCriterioRaggrFasc;
    }

    // bi-directional many-to-one association to OrgStrut
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_STRUT")
    public OrgStrut getOrgStrut() {
	return this.orgStrut;
    }

    public void setOrgStrut(OrgStrut orgStrut) {
	this.orgStrut = orgStrut;
    }

    // bi-directional many-to-one association to ElvElencoVersFascDaElab
    @OneToMany(mappedBy = "elvElencoVersFasc")
    public List<ElvElencoVersFascDaElab> getElvElencoVersFascDaElabs() {
	return this.elvElencoVersFascDaElabs;
    }

    public void setElvElencoVersFascDaElabs(
	    List<ElvElencoVersFascDaElab> elvElencoVersFascDaElabs) {
	this.elvElencoVersFascDaElabs = elvElencoVersFascDaElabs;
    }

    // bi-directional many-to-one association to ElvStatoElencoVersFasc
    @OneToMany(mappedBy = "elvElencoVersFasc", cascade = CascadeType.PERSIST)
    public List<ElvStatoElencoVersFasc> getElvStatoElencoVersFascicoli() {
	return this.elvStatoElencoVersFascicoli;
    }

    public void setElvStatoElencoVersFascicoli(
	    List<ElvStatoElencoVersFasc> elvStatoElencoVersFasc) {
	this.elvStatoElencoVersFascicoli = elvStatoElencoVersFasc;
    }

    // bi-directional many-to-one association to ElvFileElencoVersFasc
    @OneToMany(mappedBy = "elvElencoVersFasc", cascade = {
	    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
    public List<ElvFileElencoVersFasc> getElvFileElencoVersFasc() {
	return this.elvFileElencoVersFasc;
    }

    public void setElvFileElencoVersFasc(List<ElvFileElencoVersFasc> elvFileElencoVersFasc) {
	this.elvFileElencoVersFasc = elvFileElencoVersFasc;
    }

    public ElvFileElencoVersFasc addElvFileElencoVersFasc(
	    ElvFileElencoVersFasc elvFileElencoVersFasc) {
	getElvFileElencoVersFasc().add(elvFileElencoVersFasc);
	elvFileElencoVersFasc.setElvElencoVersFasc(this);

	return elvFileElencoVersFasc;
    }

    public ElvFileElencoVersFasc removeElvFileElencoVersFasc(
	    ElvFileElencoVersFasc elvFileElencoVersFasc) {
	getElvFileElencoVersFasc().remove(elvFileElencoVersFasc);
	elvFileElencoVersFasc.setElvElencoVersFasc(null);

	return elvFileElencoVersFasc;
    }

    // bi-directional many-to-one association to FasVerAipFascicolo
    @OneToMany(mappedBy = "elvElencoVersFasc")
    public List<FasVerAipFascicolo> getFasVerAipFascicolos() {
	return this.fasVerAipFascicolos;
    }

    public void setFasVerAipFascicolos(List<FasVerAipFascicolo> fasVerAipFascicolos) {
	this.fasVerAipFascicolos = fasVerAipFascicolos;
    }

    public FasVerAipFascicolo addFasVerAipFascicolo(FasVerAipFascicolo fasVerAipFascicolo) {
	getFasVerAipFascicolos().add(fasVerAipFascicolo);
	fasVerAipFascicolo.setElvElencoVersFasc(this);

	return fasVerAipFascicolo;
    }

    public FasVerAipFascicolo removeFasVerAipFascicolo(FasVerAipFascicolo fasVerAipFascicolo) {
	getFasVerAipFascicolos().remove(fasVerAipFascicolo);
	fasVerAipFascicolo.setElvElencoVersFasc(null);

	return fasVerAipFascicolo;
    }

    // bi-directional many-to-one association to FasAipFascicoloDaElab
    @OneToMany(mappedBy = "elvElencoVersFasc")
    public List<FasAipFascicoloDaElab> getFasAipFascicoloDaElabs() {
	return this.fasAipFascicoloDaElabs;
    }

    public void setFasAipFascicoloDaElabs(List<FasAipFascicoloDaElab> fasAipFascicoloDaElabs) {
	this.fasAipFascicoloDaElabs = fasAipFascicoloDaElabs;
    }

    public FasAipFascicoloDaElab addFasAipFascicoloDaElab(
	    FasAipFascicoloDaElab fasAipFascicoloDaElab) {
	getFasAipFascicoloDaElabs().add(fasAipFascicoloDaElab);
	fasAipFascicoloDaElab.setElvElencoVersFasc(this);

	return fasAipFascicoloDaElab;
    }

    public FasAipFascicoloDaElab removeFasAipFascicoloDaElab(
	    FasAipFascicoloDaElab fasAipFascicoloDaElab) {
	getFasAipFascicoloDaElabs().remove(fasAipFascicoloDaElab);
	fasAipFascicoloDaElab.setElvElencoVersFasc(null);

	return fasAipFascicoloDaElab;
    }

    // bi-directional many-to-one association to ElvElencoVersFascAnnul
    @OneToMany(mappedBy = "elvElencoVersFasc")
    public List<ElvElencoVersFascAnnul> getElvElencoVersFascAnnuls() {
	return this.elvElencoVersFascAnnuls;
    }

    public void setElvElencoVersFascAnnuls(List<ElvElencoVersFascAnnul> elvElencoVersFascAnnuls) {
	this.elvElencoVersFascAnnuls = elvElencoVersFascAnnuls;
    }

    public ElvElencoVersFascAnnul addElvElencoVersFascAnnul(
	    ElvElencoVersFascAnnul elvElencoVersFascAnnul) {
	getElvElencoVersFascAnnuls().add(elvElencoVersFascAnnul);
	elvElencoVersFascAnnul.setElvElencoVersFasc(this);

	return elvElencoVersFascAnnul;
    }

    public ElvElencoVersFascAnnul removeElvElencoVersFascAnnul(
	    ElvElencoVersFascAnnul elvElencoVersFascAnnul) {
	getElvElencoVersFascAnnuls().remove(elvElencoVersFascAnnul);
	elvElencoVersFascAnnul.setElvElencoVersFasc(null);

	return elvElencoVersFascAnnul;
    }
}
