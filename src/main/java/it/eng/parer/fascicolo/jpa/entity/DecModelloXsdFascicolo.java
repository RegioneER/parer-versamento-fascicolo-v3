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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The persistent class for the DEC_MODELLO_XSD_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "DEC_MODELLO_XSD_FASCICOLO")
@NamedQuery(name = "DecModelloXsdFascicolo.findAll", query = "SELECT d FROM DecModelloXsdFascicolo d")
public class DecModelloXsdFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idModelloXsdFascicolo;
    private String blXsd;
    private String cdXsd;
    private String dsXsd;
    private LocalDateTime dtIstituz;
    private LocalDateTime dtSoppres;
    private String flDefault;
    private String tiModelloXsd;
    private String tiUsoModelloXsd;
    private AplSistemaMigraz aplSistemaMigraz;
    private OrgAmbiente orgAmbiente;
    private List<DecUsoModelloXsdFasc> decUsoModelloXsdFascs = new ArrayList<>();
    private List<FasXmlFascicolo> fasXmlFascicolos = new ArrayList<>();
    private List<FasXsdMetaVerAipFasc> fasXsdMetaVerAipFascs = new ArrayList<>();
    private List<DecModelloXsdAttribFascicolo> decModelloXsdAttribFascicolos = new ArrayList<>();

    public DecModelloXsdFascicolo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "DEC_MODELLO_XSD_FASCICOLO_IDMODELLOXSDFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SDEC_MODELLO_XSD_FASCICOLO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEC_MODELLO_XSD_FASCICOLO_IDMODELLOXSDFASCICOLO_GENERATOR")
    @Column(name = "ID_MODELLO_XSD_FASCICOLO")
    public Long getIdModelloXsdFascicolo() {
	return this.idModelloXsdFascicolo;
    }

    public void setIdModelloXsdFascicolo(Long idModelloXsdFascicolo) {
	this.idModelloXsdFascicolo = idModelloXsdFascicolo;
    }

    @Lob
    @Column(name = "BL_XSD")
    public String getBlXsd() {
	return this.blXsd;
    }

    public void setBlXsd(String blXsd) {
	this.blXsd = blXsd;
    }

    @Column(name = "CD_XSD")
    public String getCdXsd() {
	return this.cdXsd;
    }

    public void setCdXsd(String cdXsd) {
	this.cdXsd = cdXsd;
    }

    @Column(name = "DS_XSD")
    public String getDsXsd() {
	return this.dsXsd;
    }

    public void setDsXsd(String dsXsd) {
	this.dsXsd = dsXsd;
    }

    @Column(name = "DT_ISTITUZ")
    public LocalDateTime getDtIstituz() {
	return this.dtIstituz;
    }

    public void setDtIstituz(LocalDateTime dtIstituz) {
	this.dtIstituz = dtIstituz;
    }

    @Column(name = "DT_SOPPRES")
    public LocalDateTime getDtSoppres() {
	return this.dtSoppres;
    }

    public void setDtSoppres(LocalDateTime dtSoppres) {
	this.dtSoppres = dtSoppres;
    }

    @Column(name = "FL_DEFAULT", columnDefinition = "CHAR")
    public String getFlDefault() {
	return this.flDefault;
    }

    public void setFlDefault(String flDefault) {
	this.flDefault = flDefault;
    }

    @Column(name = "TI_MODELLO_XSD")
    public String getTiModelloXsd() {
	return this.tiModelloXsd;
    }

    public void setTiModelloXsd(String tiModelloXsd) {
	this.tiModelloXsd = tiModelloXsd;
    }

    @Column(name = "TI_USO_MODELLO_XSD")
    public String getTiUsoModelloXsd() {
	return this.tiUsoModelloXsd;
    }

    public void setTiUsoModelloXsd(String tiUsoModelloXsd) {
	this.tiUsoModelloXsd = tiUsoModelloXsd;
    }

    // bi-directional many-to-one association to AplSistemaMigraz
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SISTEMA_MIGRAZ")
    public AplSistemaMigraz getAplSistemaMigraz() {
	return this.aplSistemaMigraz;
    }

    public void setAplSistemaMigraz(AplSistemaMigraz aplSistemaMigraz) {
	this.aplSistemaMigraz = aplSistemaMigraz;
    }

    // bi-directional many-to-one association to OrgAmbiente
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AMBIENTE")
    public OrgAmbiente getOrgAmbiente() {
	return this.orgAmbiente;
    }

    public void setOrgAmbiente(OrgAmbiente orgAmbiente) {
	this.orgAmbiente = orgAmbiente;
    }

    // bi-directional many-to-one association to DecUsoModelloXsdFasc
    @OneToMany(mappedBy = "decModelloXsdFascicolo")
    public List<DecUsoModelloXsdFasc> getDecUsoModelloXsdFascs() {
	return this.decUsoModelloXsdFascs;
    }

    public void setDecUsoModelloXsdFascs(List<DecUsoModelloXsdFasc> decUsoModelloXsdFascs) {
	this.decUsoModelloXsdFascs = decUsoModelloXsdFascs;
    }

    public DecUsoModelloXsdFasc addDecUsoModelloXsdFasc(DecUsoModelloXsdFasc decUsoModelloXsdFasc) {
	getDecUsoModelloXsdFascs().add(decUsoModelloXsdFasc);
	decUsoModelloXsdFasc.setDecModelloXsdFascicolo(this);

	return decUsoModelloXsdFasc;
    }

    public DecUsoModelloXsdFasc removeDecUsoModelloXsdFasc(
	    DecUsoModelloXsdFasc decUsoModelloXsdFasc) {
	getDecUsoModelloXsdFascs().remove(decUsoModelloXsdFasc);
	decUsoModelloXsdFasc.setDecModelloXsdFascicolo(null);

	return decUsoModelloXsdFasc;
    }

    // bi-directional many-to-one association to FasXmlFascicolo
    @OneToMany(mappedBy = "decModelloXsdFascicolo")
    public List<FasXmlFascicolo> getFasXmlFascicolos() {
	return fasXmlFascicolos;
    }

    public void setFasXmlFascicolos(List<FasXmlFascicolo> fasXmlFascicolos) {
	this.fasXmlFascicolos = fasXmlFascicolos;
    }

    // bi-directional many-to-one association to FasXsdMetaVerAipFasc
    @OneToMany(mappedBy = "decModelloXsdFascicolo")
    public List<FasXsdMetaVerAipFasc> getFasXsdMetaVerAipFascs() {
	return fasXsdMetaVerAipFascs;
    }

    public void setFasXsdMetaVerAipFascs(List<FasXsdMetaVerAipFasc> fasXsdMetaVerAipFascs) {
	this.fasXsdMetaVerAipFascs = fasXsdMetaVerAipFascs;
    }

    // bi-directional one-to-many association to DecModelloXsdAttribFascicolo
    @OneToMany(mappedBy = "decModelloXsdFascicolo")
    public List<DecModelloXsdAttribFascicolo> getDecModelloXsdAttribFascicolos() {
	return this.decModelloXsdAttribFascicolos;
    }

    public void setDecModelloXsdAttribFascicolos(
	    List<DecModelloXsdAttribFascicolo> decModelloXsdAttribFascicolos) {
	this.decModelloXsdAttribFascicolos = decModelloXsdAttribFascicolos;
    }

    public DecModelloXsdAttribFascicolo addDecModelloXsdAttribFascicolo(
	    DecModelloXsdAttribFascicolo decModelloXsdAttribFascicolo) {
	getDecModelloXsdAttribFascicolos().add(decModelloXsdAttribFascicolo);
	decModelloXsdAttribFascicolo.setDecModelloXsdFascicolo(this);

	return decModelloXsdAttribFascicolo;
    }

    public DecModelloXsdAttribFascicolo removeDecModelloXsdAttribFascicolo(
	    DecModelloXsdAttribFascicolo decModelloXsdAttribFascicolo) {
	getDecModelloXsdAttribFascicolos().remove(decModelloXsdAttribFascicolo);
	decModelloXsdAttribFascicolo.setDecModelloXsdFascicolo(null);

	return decModelloXsdAttribFascicolo;
    }
}
