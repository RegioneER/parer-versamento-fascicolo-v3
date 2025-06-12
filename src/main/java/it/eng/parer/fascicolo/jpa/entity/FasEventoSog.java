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
 * The persistent class for the FAS_EVENTO_SOG database table.
 *
 */
@Entity
@Table(name = "FAS_EVENTO_SOG")
@NamedQuery(name = "FasEventoSog.findAll", query = "SELECT f FROM FasEventoSog f")
public class FasEventoSog implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idEventoSog;
    private String dsDenomEvento;
    private LocalDateTime tsApertura;
    private LocalDateTime tsChiusura;

    private FasSogFascicolo fasSogFascicolo;

    public FasEventoSog() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_EVENTO_FASCICOLO_IDEVENTOSOG_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_EVENTO_SOG"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_EVENTO_FASCICOLO_IDEVENTOSOG_GENERATOR")
    @Column(name = "ID_EVENTO_SOG")
    public Long getIdEventoSog() {
	return this.idEventoSog;
    }

    public void setIdEventoSog(Long idEventoFascicolo) {
	this.idEventoSog = idEventoFascicolo;
    }

    @Column(name = "DS_DENOM_EVENTO")
    public String getDsDenomEvento() {
	return this.dsDenomEvento;
    }

    public void setDsDenomEvento(String dsDenomEvento) {
	this.dsDenomEvento = dsDenomEvento;
    }

    @Column(name = "TS_APERTURA")
    public LocalDateTime getTsApertura() {
	return this.tsApertura;
    }

    public void setTsApertura(LocalDateTime tsApertura) {
	this.tsApertura = tsApertura;
    }

    @Column(name = "TS_CHIUSURA")
    public LocalDateTime getTsChiusura() {
	return this.tsChiusura;
    }

    public void setTsChiusura(LocalDateTime tsChiusura) {
	this.tsChiusura = tsChiusura;
    }

    // bi-directional many-to-one association to FasFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SOG_FASCICOLO")
    public FasSogFascicolo getFasSogFascicolo() {
	return this.fasSogFascicolo;
    }

    public void setFasSogFascicolo(FasSogFascicolo fasSogFascicolo) {
	this.fasSogFascicolo = fasSogFascicolo;
    }

}
