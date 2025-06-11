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
 * The persistent class for the FAS_UNITA_DOC_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_UNITA_DOC_FASCICOLO")
@NamedQuery(name = "FasUnitaDocFascicolo.findAll", query = "SELECT f FROM FasUnitaDocFascicolo f")
public class FasUnitaDocFascicolo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idUnitaDocFascicolo;
    private AroUnitaDoc aroUnitaDoc;
    private FasFascicolo fasFascicolo;
    private LocalDateTime dtDataInserimentoFas;
    private BigDecimal niPosizione;

    public FasUnitaDocFascicolo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_UNITA_DOC_FASCICOLO_IDUNITADOCFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_UNITA_DOC_FASCICOLO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_UNITA_DOC_FASCICOLO_IDUNITADOCFASCICOLO_GENERATOR")
    @Column(name = "ID_UNITA_DOC_FASCICOLO")
    public Long getIdUnitaDocFascicolo() {
	return this.idUnitaDocFascicolo;
    }

    public void setIdUnitaDocFascicolo(Long idUnitaDocFascicolo) {
	this.idUnitaDocFascicolo = idUnitaDocFascicolo;
    }

    // bi-directional many-to-one association to AroUnitaDoc
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_UNITA_DOC")
    public AroUnitaDoc getAroUnitaDoc() {
	return this.aroUnitaDoc;
    }

    public void setAroUnitaDoc(AroUnitaDoc aroUnitaDoc) {
	this.aroUnitaDoc = aroUnitaDoc;
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

    @Column(name = "DT_DATA_INSERIMENTO_FAS")
    public LocalDateTime getDtDataInserimentoFas() {
	return this.dtDataInserimentoFas;
    }

    public void setDtDataInserimentoFas(LocalDateTime dtDataInserimentoFas) {
	this.dtDataInserimentoFas = dtDataInserimentoFas;
    }

    @Column(name = "NI_POSIZIONE")
    public BigDecimal getNiPosizione() {
	return this.niPosizione;
    }

    public void setNiPosizione(BigDecimal niPosizione) {
	this.niPosizione = niPosizione;
    }
}
