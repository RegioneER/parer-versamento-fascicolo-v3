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

import it.eng.parer.fascicolo.jpa.entity.constraint.FasRespFascicolo.TiOggResp;
import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequenceGenerator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the FAS_RESP_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_RESP_FASCICOLO")
@NamedQuery(name = "FasRespFascicolo.find", query = "SELECT f FROM FasRespFascicolo f WHERE f.fasFascicolo.idFascicolo = :idFascicolo")
public class FasRespFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idRespFascicolo;
    private String cdResp;
    private String nmCognResp;
    private String nmNomeResp;
    private String tiCdResp;
    private TiOggResp tiOggResp;
    private String tiResp;
    private FasFascicolo fasFascicolo;

    public FasRespFascicolo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_RESP_FASCICOLO_IDRESPFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_RESP_FASCICOLO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_RESP_FASCICOLO_IDRESPFASCICOLO_GENERATOR")
    @Column(name = "ID_RESP_FASCICOLO")
    public Long getIdRespFascicolo() {
	return this.idRespFascicolo;
    }

    public void setIdRespFascicolo(Long idRespFascicolo) {
	this.idRespFascicolo = idRespFascicolo;
    }

    @Column(name = "CD_RESP")
    public String getCdResp() {
	return this.cdResp;
    }

    public void setCdResp(String cdResp) {
	this.cdResp = cdResp;
    }

    @Column(name = "NM_COGN_RESP")
    public String getNmCognResp() {
	return this.nmCognResp;
    }

    public void setNmCognResp(String nmCognResp) {
	this.nmCognResp = nmCognResp;
    }

    @Column(name = "NM_NOME_RESP")
    public String getNmNomeResp() {
	return this.nmNomeResp;
    }

    public void setNmNomeResp(String nmNomeResp) {
	this.nmNomeResp = nmNomeResp;
    }

    @Column(name = "TI_CD_RESP")
    public String getTiCdResp() {
	return this.tiCdResp;
    }

    public void setTiCdResp(String tiCdResp) {
	this.tiCdResp = tiCdResp;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TI_OGG_RESP")
    public TiOggResp getTiOggResp() {
	return this.tiOggResp;
    }

    public void setTiOggResp(TiOggResp tiOggResp) {
	this.tiOggResp = tiOggResp;
    }

    @Column(name = "TI_RESP")
    public String getTiResp() {
	return this.tiResp;
    }

    public void setTiResp(String tiResp) {
	this.tiResp = tiResp;
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
