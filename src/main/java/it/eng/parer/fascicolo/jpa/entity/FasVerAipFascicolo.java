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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The persistent class for the FAS_VER_AIP_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_VER_AIP_FASCICOLO")
public class FasVerAipFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idVerAipFascicolo;
    private String cdVerAip;
    private String dsCausale;
    private LocalDateTime dtCreazione;
    private BigDecimal pgVerAipFascicolo;
    private List<FasContenVerAipFascicolo> fasContenVerAipFascicolos = new ArrayList<>();
    private List<FasMetaVerAipFascicolo> fasMetaVerAipFascicolos = new ArrayList<>();
    private List<FasSipVerAipFascicolo> fasSipVerAipFascicolos = new ArrayList<>();
    private FasFascicolo fasFascicolo;
    private ElvElencoVersFasc elvElencoVersFasc;
    private BigDecimal idEnteConserv;
    private String dsUrnAipFascicolo;
    private String dsUrnNormalizAipFascicolo;

    public FasVerAipFascicolo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_VER_AIP_FASCICOLO_IDVERAIPFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_VER_AIP_FASCICOLO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_VER_AIP_FASCICOLO_IDVERAIPFASCICOLO_GENERATOR")
    @Column(name = "ID_VER_AIP_FASCICOLO")
    public Long getIdVerAipFascicolo() {
	return this.idVerAipFascicolo;
    }

    public void setIdVerAipFascicolo(Long idVerAipFascicolo) {
	this.idVerAipFascicolo = idVerAipFascicolo;
    }

    @Column(name = "CD_VER_AIP")
    public String getCdVerAip() {
	return this.cdVerAip;
    }

    public void setCdVerAip(String cdVerAip) {
	this.cdVerAip = cdVerAip;
    }

    @Column(name = "DS_CAUSALE")
    public String getDsCausale() {
	return this.dsCausale;
    }

    public void setDsCausale(String dsCausale) {
	this.dsCausale = dsCausale;
    }

    @Column(name = "DT_CREAZIONE")
    public LocalDateTime getDtCreazione() {
	return this.dtCreazione;
    }

    public void setDtCreazione(LocalDateTime dtCreazione) {
	this.dtCreazione = dtCreazione;
    }

    @Column(name = "PG_VER_AIP_FASCICOLO")
    public BigDecimal getPgVerAipFascicolo() {
	return this.pgVerAipFascicolo;
    }

    public void setPgVerAipFascicolo(BigDecimal pgVerAipFascicolo) {
	this.pgVerAipFascicolo = pgVerAipFascicolo;
    }

    // bi-directional many-to-one association to FasContenVerAipFascicolo
    @OneToMany(mappedBy = "fasVerAipFascicolo", cascade = {
	    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    public List<FasContenVerAipFascicolo> getFasContenVerAipFascicolos() {
	return this.fasContenVerAipFascicolos;
    }

    public void setFasContenVerAipFascicolos(
	    List<FasContenVerAipFascicolo> fasContenVerAipFascicolos) {
	this.fasContenVerAipFascicolos = fasContenVerAipFascicolos;
    }

    // bi-directional many-to-one association to FasMetaVerAipFascicolo
    @OneToMany(mappedBy = "fasVerAipFascicolo", cascade = {
	    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    public List<FasMetaVerAipFascicolo> getFasMetaVerAipFascicolos() {
	return this.fasMetaVerAipFascicolos;
    }

    public void setFasMetaVerAipFascicolos(List<FasMetaVerAipFascicolo> fasMetaVerAipFascicolos) {
	this.fasMetaVerAipFascicolos = fasMetaVerAipFascicolos;
    }

    // bi-directional many-to-one association to FasSipVerAipFascicolo
    @OneToMany(mappedBy = "fasVerAipFascicolo", cascade = {
	    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    public List<FasSipVerAipFascicolo> getFasSipVerAipFascicolos() {
	return this.fasSipVerAipFascicolos;
    }

    public void setFasSipVerAipFascicolos(List<FasSipVerAipFascicolo> fasSipVerAipFascicolos) {
	this.fasSipVerAipFascicolos = fasSipVerAipFascicolos;
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

    public void setElvElencoVersFasc(ElvElencoVersFasc elvElencoVersFasc) {
	this.elvElencoVersFasc = elvElencoVersFasc;
    }

    @Column(name = "ID_ENTE_CONSERV")
    public BigDecimal getIdEnteConserv() {
	return this.idEnteConserv;
    }

    public void setIdEnteConserv(BigDecimal idEnteConserv) {
	this.idEnteConserv = idEnteConserv;
    }

    @Column(name = "DS_URN_AIP_FASCICOLO")
    public String getDsUrnAipFascicolo() {
	return this.dsUrnAipFascicolo;
    }

    public void setDsUrnAipFascicolo(String dsUrnAipFascicolo) {
	this.dsUrnAipFascicolo = dsUrnAipFascicolo;
    }

    @Column(name = "DS_URN_NORMALIZ_AIP_FASCICOLO")
    public String getDsUrnNormalizAipFascicolo() {
	return this.dsUrnNormalizAipFascicolo;
    }

    public void setDsUrnNormalizAipFascicolo(String dsUrnNormalizAipFascicolo) {
	this.dsUrnNormalizAipFascicolo = dsUrnNormalizAipFascicolo;
    }

}
