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

import it.eng.parer.fascicolo.jpa.entity.constraint.FasXmlFascicolo.TiModXsdFasXmlFascicolo;
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
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the FAS_XML_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_XML_FASCICOLO")
@NamedQuery(name = "FasXmlFascicolo.findAll", query = "SELECT f FROM FasXmlFascicolo f")
public class FasXmlFascicolo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idXmlFascicolo;
    private String blXml;
    private LocalDateTime dtVersFascicolo;
    private BigDecimal idStrut;
    private TiModXsdFasXmlFascicolo tiModelloXsd;
    private DecModelloXsdFascicolo decModelloXsdFascicolo;
    private FasFascicolo fasFascicolo;
    private String flCanonicalized;

    public FasXmlFascicolo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_XML_FASCICOLO_IDXMLFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_XML_FASCICOLO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_XML_FASCICOLO_IDXMLFASCICOLO_GENERATOR")
    @Column(name = "ID_XML_FASCICOLO")
    public Long getIdXmlFascicolo() {
	return this.idXmlFascicolo;
    }

    public void setIdXmlFascicolo(Long idXmlFascicolo) {
	this.idXmlFascicolo = idXmlFascicolo;
    }

    @Lob
    @Column(name = "BL_XML")
    public String getBlXml() {
	return this.blXml;
    }

    public void setBlXml(String blXml) {
	this.blXml = blXml;
    }

    @Column(name = "DT_VERS_FASCICOLO")
    public LocalDateTime getDtVersFascicolo() {
	return this.dtVersFascicolo;
    }

    public void setDtVersFascicolo(LocalDateTime dtVersFascicolo) {
	this.dtVersFascicolo = dtVersFascicolo;
    }

    @Column(name = "ID_STRUT")
    public BigDecimal getIdStrut() {
	return this.idStrut;
    }

    public void setIdStrut(BigDecimal idStrut) {
	this.idStrut = idStrut;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TI_MODELLO_XSD")
    public TiModXsdFasXmlFascicolo getTiModelloXsd() {
	return this.tiModelloXsd;
    }

    public void setTiModelloXsd(TiModXsdFasXmlFascicolo tiModelloXsd) {
	this.tiModelloXsd = tiModelloXsd;
    }

    // bi-directional many-to-one association to DecModelloXsdFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_MODELLO_XSD_FASCICOLO")
    public DecModelloXsdFascicolo getDecModelloXsdFascicolo() {
	return this.decModelloXsdFascicolo;
    }

    public void setDecModelloXsdFascicolo(DecModelloXsdFascicolo decModelloXsdFascicolo) {
	this.decModelloXsdFascicolo = decModelloXsdFascicolo;
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

    @Column(name = "FL_CANONICALIZED", columnDefinition = "CHAR")
    public String getFlCanonicalized() {
	return this.flCanonicalized;
    }

    public void setFlCanonicalized(String flCanonicalized) {
	this.flCanonicalized = flCanonicalized;
    }

}
