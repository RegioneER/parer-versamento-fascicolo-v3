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

import it.eng.parer.fascicolo.jpa.entity.constraint.ElvStatoElencoVersFasc.TiStatoElencoFasc;
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
 * The persistent class for the ELV_STATO_ELENCO_VERS_FASC database table.
 *
 */
@Entity
@Table(name = "ELV_STATO_ELENCO_VERS_FASC")
@NamedQuery(name = "ElvStatoElencoVersFasc.findAll", query = "SELECT e FROM ElvStatoElencoVersFasc e")
@NamedQuery(name = "ElvStatoElencoVersFasc.findByElencoVersFasc", query = "SELECT e FROM ElvStatoElencoVersFasc e WHERE e.elvElencoVersFasc = :elvElencoVersFasc ORDER BY e.tsStato ASC")
public class ElvStatoElencoVersFasc implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idStatoElencoVersFasc;
    private IamUser iamUser;
    private TiStatoElencoFasc tiStato;
    private LocalDateTime tsStato;
    private ElvElencoVersFasc elvElencoVersFasc;

    public ElvStatoElencoVersFasc() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "ELV_STATO_ELENCO_VERS_FASC_IDSTATOELENCOVERSFASC_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SELV_STATO_ELENCO_VERS_FASC"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ELV_STATO_ELENCO_VERS_FASC_IDSTATOELENCOVERSFASC_GENERATOR")
    @Column(name = "ID_STATO_ELENCO_VERS_FASC")
    public Long getIdStatoElencoVersFasc() {
	return this.idStatoElencoVersFasc;
    }

    public void setIdStatoElencoVersFasc(Long idStatoElencoVersFasc) {
	this.idStatoElencoVersFasc = idStatoElencoVersFasc;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TI_STATO")
    public TiStatoElencoFasc getTiStato() {
	return this.tiStato;
    }

    public void setTiStato(TiStatoElencoFasc tiStato) {
	this.tiStato = tiStato;
    }

    // bi-directional many-to-one association to IamUser
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USER_IAM")
    public IamUser getIamUser() {
	return this.iamUser;
    }

    public void setIamUser(IamUser iamUser) {
	this.iamUser = iamUser;
    }

    @Column(name = "TS_STATO")
    public LocalDateTime getTsStato() {
	return this.tsStato;
    }

    public void setTsStato(LocalDateTime tsStato) {
	this.tsStato = tsStato;
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
}
