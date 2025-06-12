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
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The persistent class for the VRS_SES_FASCICOLO_ERR database table.
 *
 */
@Entity
@Table(name = "VRS_SES_FASCICOLO_ERR")
@NamedQuery(name = "VrsSesFascicoloErr.findAll", query = "SELECT v FROM VrsSesFascicoloErr v")
public class VrsSesFascicoloErr implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idSesFascicoloErr;
    private BigDecimal aaFascicolo;
    private String cdKeyFascicolo;
    private String dsErr;
    private String nmAmbiente;
    private String nmEnte;
    private String nmStrut;
    private String nmTipoFascicolo;
    private String nmUseridWs;
    private String tiStatoSes;
    private LocalDateTime tsFineSes;
    private LocalDateTime tsIniSes;
    private String cdVersioneWs;
    private String cdIndIpClient;
    private String cdIndServer;
    private DecErrSacer decErrSacer;
    private DecTipoFascicolo decTipoFascicolo;
    private OrgStrut orgStrut;
    private List<VrsXmlSesFascicoloErr> vrsXmlSesFascicoloErrs = new ArrayList<>();

    public VrsSesFascicoloErr() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "VRS_SES_FASCICOLO_ERR_IDSESFASCICOLOERR_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SVRS_SES_FASCICOLO_ERR"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VRS_SES_FASCICOLO_ERR_IDSESFASCICOLOERR_GENERATOR")
    @Column(name = "ID_SES_FASCICOLO_ERR")
    public Long getIdSesFascicoloErr() {
	return this.idSesFascicoloErr;
    }

    public void setIdSesFascicoloErr(Long idSesFascicoloErr) {
	this.idSesFascicoloErr = idSesFascicoloErr;
    }

    @Column(name = "AA_FASCICOLO")
    public BigDecimal getAaFascicolo() {
	return this.aaFascicolo;
    }

    public void setAaFascicolo(BigDecimal aaFascicolo) {
	this.aaFascicolo = aaFascicolo;
    }

    @Column(name = "CD_KEY_FASCICOLO")
    public String getCdKeyFascicolo() {
	return this.cdKeyFascicolo;
    }

    public void setCdKeyFascicolo(String cdKeyFascicolo) {
	this.cdKeyFascicolo = cdKeyFascicolo;
    }

    @Column(name = "DS_ERR")
    public String getDsErr() {
	return this.dsErr;
    }

    public void setDsErr(String dsErr) {
	this.dsErr = dsErr;
    }

    @Column(name = "NM_AMBIENTE")
    public String getNmAmbiente() {
	return this.nmAmbiente;
    }

    public void setNmAmbiente(String nmAmbiente) {
	this.nmAmbiente = nmAmbiente;
    }

    @Column(name = "NM_ENTE")
    public String getNmEnte() {
	return this.nmEnte;
    }

    public void setNmEnte(String nmEnte) {
	this.nmEnte = nmEnte;
    }

    @Column(name = "NM_STRUT")
    public String getNmStrut() {
	return this.nmStrut;
    }

    public void setNmStrut(String nmStrut) {
	this.nmStrut = nmStrut;
    }

    @Column(name = "NM_TIPO_FASCICOLO")
    public String getNmTipoFascicolo() {
	return this.nmTipoFascicolo;
    }

    public void setNmTipoFascicolo(String nmTipoFascicolo) {
	this.nmTipoFascicolo = nmTipoFascicolo;
    }

    @Column(name = "NM_USERID_WS")
    public String getNmUseridWs() {
	return this.nmUseridWs;
    }

    public void setNmUseridWs(String nmUseridWs) {
	this.nmUseridWs = nmUseridWs;
    }

    @Column(name = "TI_STATO_SES")
    public String getTiStatoSes() {
	return this.tiStatoSes;
    }

    public void setTiStatoSes(String tiStatoSes) {
	this.tiStatoSes = tiStatoSes;
    }

    @Column(name = "TS_FINE_SES")
    public LocalDateTime getTsFineSes() {
	return this.tsFineSes;
    }

    public void setTsFineSes(LocalDateTime tsFineSes) {
	this.tsFineSes = tsFineSes;
    }

    @Column(name = "TS_INI_SES")
    public LocalDateTime getTsIniSes() {
	return this.tsIniSes;
    }

    public void setTsIniSes(LocalDateTime tsIniSes) {
	this.tsIniSes = tsIniSes;
    }

    @Column(name = "CD_VERSIONE_WS")
    public String getCdVersioneWs() {
	return cdVersioneWs;
    }

    public void setCdVersioneWs(String cdVersioneWs) {
	this.cdVersioneWs = cdVersioneWs;
    }

    @Column(name = "CD_IND_IP_CLIENT")
    public String getCdIndIpClient() {
	return cdIndIpClient;
    }

    public void setCdIndIpClient(String cdIndIpClient) {
	this.cdIndIpClient = cdIndIpClient;
    }

    @Column(name = "CD_IND_SERVER")
    public String getCdIndServer() {
	return cdIndServer;
    }

    public void setCdIndServer(String cdIndServer) {
	this.cdIndServer = cdIndServer;
    }

    // bi-directional many-to-one association to DecErrSacer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ERR_SACER")
    public DecErrSacer getDecErrSacer() {
	return this.decErrSacer;
    }

    public void setDecErrSacer(DecErrSacer decErrSacer) {
	this.decErrSacer = decErrSacer;
    }

    // bi-directional many-to-one association to DecTipoFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_FASCICOLO")
    public DecTipoFascicolo getDecTipoFascicolo() {
	return this.decTipoFascicolo;
    }

    public void setDecTipoFascicolo(DecTipoFascicolo decTipoFascicolo) {
	this.decTipoFascicolo = decTipoFascicolo;
    }

    // bi-directional many-to-one association to OrgStrut
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_STRUT")
    public OrgStrut getOrgStrut() {
	return this.orgStrut;
    }

    public void setOrgStrut(OrgStrut orgStrut) {
	this.orgStrut = orgStrut;
    }

    // bi-directional many-to-one association to VrsXmlSesFascicoloErr
    @OneToMany(mappedBy = "vrsSesFascicoloErr")
    public List<VrsXmlSesFascicoloErr> getVrsXmlSesFascicoloErrs() {
	return this.vrsXmlSesFascicoloErrs;
    }

    public void setVrsXmlSesFascicoloErrs(List<VrsXmlSesFascicoloErr> vrsXmlSesFascicoloErrs) {
	this.vrsXmlSesFascicoloErrs = vrsXmlSesFascicoloErrs;
    }

    public VrsXmlSesFascicoloErr addVrsXmlSesFascicoloErr(
	    VrsXmlSesFascicoloErr vrsXmlSesFascicoloErr) {
	getVrsXmlSesFascicoloErrs().add(vrsXmlSesFascicoloErr);
	vrsXmlSesFascicoloErr.setVrsSesFascicoloErr(this);

	return vrsXmlSesFascicoloErr;
    }

    public VrsXmlSesFascicoloErr removeVrsXmlSesFascicoloErr(
	    VrsXmlSesFascicoloErr vrsXmlSesFascicoloErr) {
	getVrsXmlSesFascicoloErrs().remove(vrsXmlSesFascicoloErr);
	vrsXmlSesFascicoloErr.setVrsSesFascicoloErr(null);

	return vrsXmlSesFascicoloErr;
    }

}
