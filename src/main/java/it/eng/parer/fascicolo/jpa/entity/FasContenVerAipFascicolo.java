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

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 * The persistent class for the FAS_CONTEN_VER_AIP_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_CONTEN_VER_AIP_FASCICOLO")
public class FasContenVerAipFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idContenVerAipFascicolo;
    private String nmConten;
    private String tiConten;
    private BigDecimal aroVerIndiceAipUd;
    private FasVerAipFascicolo fasVerAipFascicolo;
    private FasVerAipFascicolo fasVerAipFascicoloFiglio;

    public FasContenVerAipFascicolo() {
	// hibernate constructor
    }

    @Id
    @SequenceGenerator(name = "FAS_CONTEN_VER_AIP_FASCICOLO_IDCONTENVERAIPFASCICOLO_GENERATOR", allocationSize = 1, sequenceName = "SFAS_CONTEN_VER_AIP_FASCICOLO")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_CONTEN_VER_AIP_FASCICOLO_IDCONTENVERAIPFASCICOLO_GENERATOR")
    @Column(name = "ID_CONTEN_VER_AIP_FASCICOLO")
    public Long getIdContenVerAipFascicolo() {
	return this.idContenVerAipFascicolo;
    }

    public void setIdContenVerAipFascicolo(Long idContenVerAipFascicolo) {
	this.idContenVerAipFascicolo = idContenVerAipFascicolo;
    }

    @Column(name = "NM_CONTEN")
    public String getNmConten() {
	return this.nmConten;
    }

    public void setNmConten(String nmConten) {
	this.nmConten = nmConten;
    }

    @Column(name = "TI_CONTEN")
    public String getTiConten() {
	return this.tiConten;
    }

    public void setTiConten(String tiConten) {
	this.tiConten = tiConten;
    }

    // bi-directional many-to-one association to AroVerIndiceAipUd
    // @ManyToOne(fetch = FetchType.LAZY)
    @Column(name = "ID_VER_INDICE_AIP_UD")
    public BigDecimal getAroVerIndiceAipUd() {
	return this.aroVerIndiceAipUd;
    }

    public void setAroVerIndiceAipUd(BigDecimal aroVerIndiceAipUd) {
	this.aroVerIndiceAipUd = aroVerIndiceAipUd;
    }

    // bi-directional many-to-one association to FasVerAipFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VER_AIP_FASCICOLO")
    public FasVerAipFascicolo getFasVerAipFascicolo() {
	return this.fasVerAipFascicolo;
    }

    public void setFasVerAipFascicolo(FasVerAipFascicolo fasVerAipFascicolo) {
	this.fasVerAipFascicolo = fasVerAipFascicolo;
    }

    // bi-directional many-to-one association to FasVerAipFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VER_AIP_FASCICOLO_FIGLIO")
    public FasVerAipFascicolo getFasVerAipFascicoloFiglio() {
	return this.fasVerAipFascicoloFiglio;
    }

    public void setFasVerAipFascicoloFiglio(FasVerAipFascicolo fasVerAipFascicoloFiglio) {
	this.fasVerAipFascicoloFiglio = fasVerAipFascicoloFiglio;
    }

}
