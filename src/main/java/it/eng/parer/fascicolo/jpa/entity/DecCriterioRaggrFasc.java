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
 * The persistent class for the DEC_CRITERIO_RAGGR_FASC database table.
 *
 */
@Entity
@Table(name = "DEC_CRITERIO_RAGGR_FASC")
@NamedQuery(name = "DecCriterioRaggrFasc.findAll", query = "SELECT d FROM DecCriterioRaggrFasc d")
public class DecCriterioRaggrFasc implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idCriterioRaggrFasc;
    private BigDecimal aaFascicolo;
    private BigDecimal aaFascicoloA;
    private BigDecimal aaFascicoloDa;
    private String dsCriterioRaggr;
    private String dsOggettoFascicolo;
    private LocalDateTime dtApeFascicoloA;
    private LocalDateTime dtApeFascicoloDa;
    private LocalDateTime dtChiuFascicoloA;
    private LocalDateTime dtChiuFascicoloDa;
    private LocalDateTime dtIstituz;
    private LocalDateTime dtSoppres;
    private LocalDateTime dtVersA;
    private LocalDateTime dtVersDa;
    private String flCriterioRaggrStandard;
    private String flFiltroSistemaMigraz;
    private String flFiltroTipoFascicolo;
    private String flFiltroVoceTitol;
    private BigDecimal niMaxFasc;
    private BigDecimal niTempoScadChius;
    private String nmCriterioRaggr;
    private String ntCriterioRaggr;
    private String tiConservazione;
    private String tiScadChius;
    private String tiTempoScadChius;
    private OrgStrut orgStrut;
    private List<DecSelCriterioRaggrFasc> decSelCriterioRaggrFascicoli = new ArrayList<>();
    private List<ElvElencoVersFasc> elvElencoVersFasc = new ArrayList<>();

    public DecCriterioRaggrFasc() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "DEC_CRITERIO_RAGGR_FASC_IDCRITERIORAGGRFASC_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SDEC_CRITERIO_RAGGR_FASC"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEC_CRITERIO_RAGGR_FASC_IDCRITERIORAGGRFASC_GENERATOR")
    @Column(name = "ID_CRITERIO_RAGGR_FASC")
    public Long getIdCriterioRaggrFasc() {
	return this.idCriterioRaggrFasc;
    }

    public void setIdCriterioRaggrFasc(Long idCriterioRaggrFasc) {
	this.idCriterioRaggrFasc = idCriterioRaggrFasc;
    }

    @Column(name = "AA_FASCICOLO")
    public BigDecimal getAaFascicolo() {
	return this.aaFascicolo;
    }

    public void setAaFascicolo(BigDecimal aaFascicolo) {
	this.aaFascicolo = aaFascicolo;
    }

    @Column(name = "AA_FASCICOLO_A")
    public BigDecimal getAaFascicoloA() {
	return this.aaFascicoloA;
    }

    public void setAaFascicoloA(BigDecimal aaFascicoloA) {
	this.aaFascicoloA = aaFascicoloA;
    }

    @Column(name = "AA_FASCICOLO_DA")
    public BigDecimal getAaFascicoloDa() {
	return this.aaFascicoloDa;
    }

    public void setAaFascicoloDa(BigDecimal aaFascicoloDa) {
	this.aaFascicoloDa = aaFascicoloDa;
    }

    @Column(name = "DS_CRITERIO_RAGGR")
    public String getDsCriterioRaggr() {
	return this.dsCriterioRaggr;
    }

    public void setDsCriterioRaggr(String dsCriterioRaggr) {
	this.dsCriterioRaggr = dsCriterioRaggr;
    }

    @Column(name = "DS_OGGETTO_FASCICOLO")
    public String getDsOggettoFascicolo() {
	return this.dsOggettoFascicolo;
    }

    public void setDsOggettoFascicolo(String dsOggettoFascicolo) {
	this.dsOggettoFascicolo = dsOggettoFascicolo;
    }

    @Column(name = "DT_APE_FASCICOLO_A")
    public LocalDateTime getDtApeFascicoloA() {
	return this.dtApeFascicoloA;
    }

    public void setDtApeFascicoloA(LocalDateTime dtApeFascicoloA) {
	this.dtApeFascicoloA = dtApeFascicoloA;
    }

    @Column(name = "DT_APE_FASCICOLO_DA")
    public LocalDateTime getDtApeFascicoloDa() {
	return this.dtApeFascicoloDa;
    }

    public void setDtApeFascicoloDa(LocalDateTime dtApeFascicoloDa) {
	this.dtApeFascicoloDa = dtApeFascicoloDa;
    }

    @Column(name = "DT_CHIU_FASCICOLO_A")
    public LocalDateTime getDtChiuFascicoloA() {
	return this.dtChiuFascicoloA;
    }

    public void setDtChiuFascicoloA(LocalDateTime dtChiuFascicoloA) {
	this.dtChiuFascicoloA = dtChiuFascicoloA;
    }

    @Column(name = "DT_CHIU_FASCICOLO_DA")
    public LocalDateTime getDtChiuFascicoloDa() {
	return this.dtChiuFascicoloDa;
    }

    public void setDtChiuFascicoloDa(LocalDateTime dtChiuFascicoloDa) {
	this.dtChiuFascicoloDa = dtChiuFascicoloDa;
    }

    @Column(name = "DT_ISTITUZ")
    public LocalDateTime getDtIstituz() {
	return this.dtIstituz;
    }

    public void setDtIstituz(LocalDateTime dtIstituz) {
	this.dtIstituz = dtIstituz;
    }

    @Column(name = "DT_SOPPRES")
    public LocalDateTime getDtSoppres() {
	return this.dtSoppres;
    }

    public void setDtSoppres(LocalDateTime dtSoppres) {
	this.dtSoppres = dtSoppres;
    }

    @Column(name = "DT_VERS_A")
    public LocalDateTime getDtVersA() {
	return this.dtVersA;
    }

    public void setDtVersA(LocalDateTime dtVersA) {
	this.dtVersA = dtVersA;
    }

    @Column(name = "DT_VERS_DA")
    public LocalDateTime getDtVersDa() {
	return this.dtVersDa;
    }

    public void setDtVersDa(LocalDateTime dtVersDa) {
	this.dtVersDa = dtVersDa;
    }

    @Column(name = "FL_CRITERIO_RAGGR_STANDARD", columnDefinition = "CHAR")
    public String getFlCriterioRaggrStandard() {
	return this.flCriterioRaggrStandard;
    }

    public void setFlCriterioRaggrStandard(String flCriterioRaggrStandard) {
	this.flCriterioRaggrStandard = flCriterioRaggrStandard;
    }

    @Column(name = "FL_FILTRO_SISTEMA_MIGRAZ", columnDefinition = "CHAR")
    public String getFlFiltroSistemaMigraz() {
	return this.flFiltroSistemaMigraz;
    }

    public void setFlFiltroSistemaMigraz(String flFiltroSistemaMigraz) {
	this.flFiltroSistemaMigraz = flFiltroSistemaMigraz;
    }

    @Column(name = "FL_FILTRO_TIPO_FASCICOLO", columnDefinition = "CHAR")
    public String getFlFiltroTipoFascicolo() {
	return this.flFiltroTipoFascicolo;
    }

    public void setFlFiltroTipoFascicolo(String flFiltroTipoFascicolo) {
	this.flFiltroTipoFascicolo = flFiltroTipoFascicolo;
    }

    @Column(name = "FL_FILTRO_VOCE_TITOL", columnDefinition = "CHAR")
    public String getFlFiltroVoceTitol() {
	return this.flFiltroVoceTitol;
    }

    public void setFlFiltroVoceTitol(String flFiltroVoceTitol) {
	this.flFiltroVoceTitol = flFiltroVoceTitol;
    }

    @Column(name = "NI_MAX_FASC")
    public BigDecimal getNiMaxFasc() {
	return this.niMaxFasc;
    }

    public void setNiMaxFasc(BigDecimal niMaxFasc) {
	this.niMaxFasc = niMaxFasc;
    }

    @Column(name = "NI_TEMPO_SCAD_CHIUS")
    public BigDecimal getNiTempoScadChius() {
	return this.niTempoScadChius;
    }

    public void setNiTempoScadChius(BigDecimal niTempoScadChius) {
	this.niTempoScadChius = niTempoScadChius;
    }

    @Column(name = "NM_CRITERIO_RAGGR")
    public String getNmCriterioRaggr() {
	return this.nmCriterioRaggr;
    }

    public void setNmCriterioRaggr(String nmCriterioRaggr) {
	this.nmCriterioRaggr = nmCriterioRaggr;
    }

    @Column(name = "NT_CRITERIO_RAGGR")
    public String getNtCriterioRaggr() {
	return this.ntCriterioRaggr;
    }

    public void setNtCriterioRaggr(String ntCriterioRaggr) {
	this.ntCriterioRaggr = ntCriterioRaggr;
    }

    @Column(name = "TI_CONSERVAZIONE")
    public String getTiConservazione() {
	return this.tiConservazione;
    }

    public void setTiConservazione(String tiConservazione) {
	this.tiConservazione = tiConservazione;
    }

    @Column(name = "TI_SCAD_CHIUS")
    public String getTiScadChius() {
	return this.tiScadChius;
    }

    public void setTiScadChius(String tiScadChius) {
	this.tiScadChius = tiScadChius;
    }

    @Column(name = "TI_TEMPO_SCAD_CHIUS")
    public String getTiTempoScadChius() {
	return this.tiTempoScadChius;
    }

    public void setTiTempoScadChius(String tiTempoScadChius) {
	this.tiTempoScadChius = tiTempoScadChius;
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

    // bi-directional one-to-many association to DecSelCriterioRaggrFasc
    @OneToMany(mappedBy = "decCriterioRaggrFasc", cascade = {
	    CascadeType.PERSIST, CascadeType.REMOVE })
    public List<DecSelCriterioRaggrFasc> getDecSelCriterioRaggrFascicoli() {
	return this.decSelCriterioRaggrFascicoli;
    }

    public void setDecSelCriterioRaggrFascicoli(
	    List<DecSelCriterioRaggrFasc> decSelCriterioRaggrFascicoli) {
	this.decSelCriterioRaggrFascicoli = decSelCriterioRaggrFascicoli;
    }

    // bi-directional many-to-one association to ElvElencoVersFasc
    @OneToMany(mappedBy = "decCriterioRaggrFasc")
    public List<ElvElencoVersFasc> getElvElencoVersFasc() {
	return this.elvElencoVersFasc;
    }

    public void setElvElencoVersFasc(List<ElvElencoVersFasc> elvElencoVersFasc) {
	this.elvElencoVersFasc = elvElencoVersFasc;
    }
}
