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

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.entity.constraint.ElvFascDaElabElenco.TiStatoFascDaElab;
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
 * The persistent class for the ELV_FASC_DA_ELAB_ELENCO database table.
 *
 */
@Entity
@Table(name = "ELV_FASC_DA_ELAB_ELENCO")
@NamedQuery(name = "ElvFascDaElabElenco.findAll", query = "SELECT e FROM ElvFascDaElabElenco e")
public class ElvFascDaElabElenco implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idFascDaElabElenco;
    private BigDecimal aaFascicolo;
    private LocalDateTime tsVersFascicolo;
    private BigDecimal idStrut;
    private TiStatoFascDaElab tiStatoFascDaElab;
    private FasFascicolo fasFascicolo;
    private BigDecimal idTipoFascicolo;

    public ElvFascDaElabElenco() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "ELV_FASC_DA_ELAB_ELENCO_IDFASCDAELABELENCO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SELV_FASC_DA_ELAB_ELENCO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ELV_FASC_DA_ELAB_ELENCO_IDFASCDAELABELENCO_GENERATOR")
    @Column(name = "ID_FASC_DA_ELAB_ELENCO")
    public Long getIdFascDaElabElenco() {
	return this.idFascDaElabElenco;
    }

    public void setIdFascDaElabElenco(Long idFascDaElabElenco) {
	this.idFascDaElabElenco = idFascDaElabElenco;
    }

    @Column(name = "AA_FASCICOLO")
    public BigDecimal getAaFascicolo() {
	return this.aaFascicolo;
    }

    public void setAaFascicolo(BigDecimal aaFascicolo) {
	this.aaFascicolo = aaFascicolo;
    }

    @Column(name = "TS_VERS_FASCICOLO")
    public LocalDateTime getTsVersFascicolo() {
	return this.tsVersFascicolo;
    }

    public void setTsVersFascicolo(LocalDateTime tsVersFascicolo) {
	this.tsVersFascicolo = tsVersFascicolo;
    }

    @Column(name = "ID_STRUT")
    public BigDecimal getIdStrut() {
	return this.idStrut;
    }

    public void setIdStrut(BigDecimal idStrut) {
	this.idStrut = idStrut;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TI_STATO_FASC_DA_ELAB")
    public TiStatoFascDaElab getTiStatoFascDaElab() {
	return this.tiStatoFascDaElab;
    }

    public void setTiStatoFascDaElab(TiStatoFascDaElab tiStatoFascDaElab) {
	this.tiStatoFascDaElab = tiStatoFascDaElab;
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

    @Column(name = "ID_TIPO_FASCICOLO")
    public BigDecimal getIdTipoFascicolo() {
	return this.idTipoFascicolo;
    }

    public void setIdTipoFascicolo(BigDecimal idTipoFascicolo) {
	this.idTipoFascicolo = idTipoFascicolo;
    }

}
