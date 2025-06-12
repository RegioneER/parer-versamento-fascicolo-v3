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
 * The persistent class for the FAS_SOG_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_SOG_FASCICOLO")
@NamedQuery(name = "FasSogFascicolo.find", query = "SELECT f FROM FasSogFascicolo f WHERE f.fasFascicolo.idFascicolo = :idFascicolo")
public class FasSogFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idSogFascicolo;
    private String dsDenomSog;
    private String nmCognSog;
    private String nmNomeSog;
    private String tiRapp;
    private LocalDateTime dtNas;
    private String dsCmnNsc;
    private String tiSes;
    private String dsCit;

    private FasFascicolo fasFascicolo;
    private List<FasRifIndSog> fasRifIndSogs = new ArrayList<>();
    private List<FasCodIdeSog> fasCodIdeSogs = new ArrayList<>();
    private List<FasEventoSog> fasEventoSogs = new ArrayList<>();

    public FasSogFascicolo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_SOG_FASCICOLO_IDSOGFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_SOG_FASCICOLO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_SOG_FASCICOLO_IDSOGFASCICOLO_GENERATOR")
    @Column(name = "ID_SOG_FASCICOLO")
    public Long getIdSogFascicolo() {
	return this.idSogFascicolo;
    }

    public void setIdSogFascicolo(Long idSogFascicolo) {
	this.idSogFascicolo = idSogFascicolo;
    }

    @Column(name = "DS_DENOM_SOG")
    public String getDsDenomSog() {
	return this.dsDenomSog;
    }

    public void setDsDenomSog(String dsDenomSog) {
	this.dsDenomSog = dsDenomSog;
    }

    @Column(name = "NM_COGN_SOG")
    public String getNmCognSog() {
	return this.nmCognSog;
    }

    public void setNmCognSog(String nmCognSog) {
	this.nmCognSog = nmCognSog;
    }

    @Column(name = "NM_NOME_SOG")
    public String getNmNomeSog() {
	return this.nmNomeSog;
    }

    public void setNmNomeSog(String nmNomeSog) {
	this.nmNomeSog = nmNomeSog;
    }

    @Column(name = "TI_RAPP")
    public String getTiRapp() {
	return this.tiRapp;
    }

    public void setTiRapp(String tiRapp) {
	this.tiRapp = tiRapp;
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

    @Column(name = "DT_NAS")
    public LocalDateTime getDtNas() {
	return dtNas;
    }

    public void setDtNas(LocalDateTime dtNas) {
	this.dtNas = dtNas;
    }

    @Column(name = "DS_CMN_NSC")
    public String getDsCmnNsc() {
	return this.dsCmnNsc;
    }

    public void setDsCmnNsc(String dsCmnNsc) {
	this.dsCmnNsc = dsCmnNsc;
    }

    @Column(name = "TI_SES")
    public String getTiSes() {
	return this.tiSes;
    }

    public void setTiSes(String tiSes) {
	this.tiSes = tiSes;
    }

    @Column(name = "DS_CIT")
    public String getDsCit() {
	return this.dsCit;
    }

    public void setDsCit(String dsCit) {
	this.dsCit = dsCit;
    }

    // bi-directional many-to-one association to FirCertifFirmatario
    @OneToMany(mappedBy = "fasSogFascicolo", cascade = {
	    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    public List<FasRifIndSog> getFasRifIndSogs() {
	return this.fasRifIndSogs;
    }

    public void setFasRifIndSogs(List<FasRifIndSog> fasRifIndSogs) {
	this.fasRifIndSogs = fasRifIndSogs;
    }

    // bi-directional many-to-one association to FirCertifFirmatario
    @OneToMany(mappedBy = "fasSogFascicolo", cascade = {
	    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    public List<FasCodIdeSog> getFasCodIdeSogs() {
	return this.fasCodIdeSogs;
    }

    public void setFasCodIdeSogs(List<FasCodIdeSog> fasCodIdeSogs) {
	this.fasCodIdeSogs = fasCodIdeSogs;
    }

    // bi-directional many-to-one association to FirCertifFirmatario
    @OneToMany(mappedBy = "fasSogFascicolo", cascade = {
	    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    public List<FasEventoSog> getFasEventoSogs() {
	return this.fasEventoSogs;
    }

    public void setFasEventoSogs(List<FasEventoSog> fasFasEventoSogs) {
	this.fasEventoSogs = fasFasEventoSogs;
    }
}
