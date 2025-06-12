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
import jakarta.persistence.Transient;

/**
 * The persistent class for the FAS_LINK_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_LINK_FASCICOLO")
@NamedQuery(name = "FasLinkFascicolo.find", query = "SELECT f FROM FasLinkFascicolo f WHERE f.fasFascicolo.idFascicolo = :idFascicolo")
@NamedQuery(name = "FasLinkFascicolo.findByIdFasLink", query = "SELECT f FROM FasLinkFascicolo f WHERE f.fasFascicoloLink.idFascicolo = :idFascicolo ORDER BY f.fasFascicoloLink.cdKeyOrd DESC")
public class FasLinkFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idLinkFascicolo;
    private BigDecimal aaFascicoloLink;
    private String cdKeyFascicoloLink;
    private String dsLink;
    private FasFascicolo fasFascicolo;
    private FasFascicolo fasFascicoloLink;

    public FasLinkFascicolo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_LINK_FASCICOLO_IDLINKFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_LINK_FASCICOLO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_LINK_FASCICOLO_IDLINKFASCICOLO_GENERATOR")
    @Column(name = "ID_LINK_FASCICOLO")
    public Long getIdLinkFascicolo() {
	return this.idLinkFascicolo;
    }

    public void setIdLinkFascicolo(Long idLinkFascicolo) {
	this.idLinkFascicolo = idLinkFascicolo;
    }

    @Column(name = "AA_FASCICOLO_LINK")
    public BigDecimal getAaFascicoloLink() {
	return this.aaFascicoloLink;
    }

    public void setAaFascicoloLink(BigDecimal aaFascicoloLink) {
	this.aaFascicoloLink = aaFascicoloLink;
    }

    @Column(name = "CD_KEY_FASCICOLO_LINK")
    public String getCdKeyFascicoloLink() {
	return this.cdKeyFascicoloLink;
    }

    public void setCdKeyFascicoloLink(String cdKeyFascicoloLink) {
	this.cdKeyFascicoloLink = cdKeyFascicoloLink;
    }

    @Column(name = "DS_LINK")
    public String getDsLink() {
	return this.dsLink;
    }

    public void setDsLink(String dsLink) {
	this.dsLink = dsLink;
    }

    // bi-directional many-to-one association to FasFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FASCICOLO_LINK")
    public FasFascicolo getFasFascicoloLink() {
	return this.fasFascicoloLink;
    }

    public void setFasFascicoloLink(FasFascicolo fasFascicoloLink) {
	this.fasFascicoloLink = fasFascicoloLink;
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

    @Transient
    public boolean hasLink() {
	return this.fasFascicoloLink != null;
    }
}
