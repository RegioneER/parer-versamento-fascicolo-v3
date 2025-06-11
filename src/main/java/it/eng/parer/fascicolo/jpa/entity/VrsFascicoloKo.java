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
 * The persistent class for the VRS_FASCICOLO_KO database table.
 *
 */
@Entity
@Table(name = "VRS_FASCICOLO_KO")
@NamedQuery(name = "VrsFascicoloKo.findByFascKo", query = "SELECT v FROM VrsFascicoloKo v")
@NamedQuery(name = "VrsFascicoloKo.findByStrutAnnoNum", query = "SELECT f FROM VrsFascicoloKo f WHERE f.orgStrut = :orgStrut AND f.aaFascicolo=:aaFascicolo AND f.cdKeyFascicolo=:cdKeyFascicolo")
@NamedQuery(name = "VrsFascicoloKo.findCountFascicoliNonVersatiNelGiorno", query = "SELECT f.orgStrut.idStrut, f.decTipoFascicolo.idTipoFascicolo, f.aaFascicolo, COUNT(f.idFascicoloKo), f.tiStatoFascicoloKo FROM VrsFascicoloKo f JOIN f.orgStrut strut WHERE FUNC('trunc', f.tsIniLastSes, 'DD')=FUNC('to_date',FUNC('to_char',:data,'DD/MM/YYYY'),'DD/MM/YYYY') GROUP BY f.orgStrut.idStrut, f.decTipoFascicolo.idTipoFascicolo, f.aaFascicolo, f.tiStatoFascicoloKo")
public class VrsFascicoloKo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idFascicoloKo;
    private BigDecimal aaFascicolo;
    private String cdKeyFascicolo;
    private String dsErrPrinc;
    private BigDecimal idSesFascicoloKoFirst;
    private BigDecimal idSesFascicoloKoLast;
    private String tiStatoFascicoloKo;
    private LocalDateTime tsIniFirstSes;
    private LocalDateTime tsIniLastSes;
    private DecErrSacer decErrSacer;
    private DecTipoFascicolo decTipoFascicolo;
    private OrgStrut orgStrut;
    private List<VrsSesFascicoloKo> vrsSesFascicoloKos = new ArrayList<>();

    public VrsFascicoloKo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "VRS_FASCICOLO_KO_IDFASCICOLOKO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SVRS_FASCICOLO_KO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VRS_FASCICOLO_KO_IDFASCICOLOKO_GENERATOR")
    @Column(name = "ID_FASCICOLO_KO")
    public Long getIdFascicoloKo() {
	return this.idFascicoloKo;
    }

    public void setIdFascicoloKo(Long idFascicoloKo) {
	this.idFascicoloKo = idFascicoloKo;
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

    @Column(name = "DS_ERR_PRINC")
    public String getDsErrPrinc() {
	return this.dsErrPrinc;
    }

    public void setDsErrPrinc(String dsErrPrinc) {
	this.dsErrPrinc = dsErrPrinc;
    }

    @Column(name = "ID_SES_FASCICOLO_KO_FIRST")
    public BigDecimal getIdSesFascicoloKoFirst() {
	return this.idSesFascicoloKoFirst;
    }

    public void setIdSesFascicoloKoFirst(BigDecimal idSesFascicoloKoFirst) {
	this.idSesFascicoloKoFirst = idSesFascicoloKoFirst;
    }

    @Column(name = "ID_SES_FASCICOLO_KO_LAST")
    public BigDecimal getIdSesFascicoloKoLast() {
	return this.idSesFascicoloKoLast;
    }

    public void setIdSesFascicoloKoLast(BigDecimal idSesFascicoloKoLast) {
	this.idSesFascicoloKoLast = idSesFascicoloKoLast;
    }

    @Column(name = "TI_STATO_FASCICOLO_KO")
    public String getTiStatoFascicoloKo() {
	return this.tiStatoFascicoloKo;
    }

    public void setTiStatoFascicoloKo(String tiStatoFascicoloKo) {
	this.tiStatoFascicoloKo = tiStatoFascicoloKo;
    }

    @Column(name = "TS_INI_FIRST_SES")
    public LocalDateTime getTsIniFirstSes() {
	return this.tsIniFirstSes;
    }

    public void setTsIniFirstSes(LocalDateTime tsIniFirstSes) {
	this.tsIniFirstSes = tsIniFirstSes;
    }

    @Column(name = "TS_INI_LAST_SES")
    public LocalDateTime getTsIniLastSes() {
	return this.tsIniLastSes;
    }

    public void setTsIniLastSes(LocalDateTime tsIniLastSes) {
	this.tsIniLastSes = tsIniLastSes;
    }

    // bi-directional many-to-one association to DecErrSacer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ERR_SACER_PRINC")
    public DecErrSacer getDecErrSacer() {
	return this.decErrSacer;
    }

    public void setDecErrSacer(DecErrSacer decErrSacer) {
	this.decErrSacer = decErrSacer;
    }

    // bi-directional many-to-one association to DecTipoFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_FASCICOLO_LAST")
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

    // bi-directional many-to-one association to VrsSesFascicoloKo
    @OneToMany(mappedBy = "vrsFascicoloKo")
    public List<VrsSesFascicoloKo> getVrsSesFascicoloKos() {
	return this.vrsSesFascicoloKos;
    }

    public void setVrsSesFascicoloKos(List<VrsSesFascicoloKo> vrsSesFascicoloKos) {
	this.vrsSesFascicoloKos = vrsSesFascicoloKos;
    }

    public VrsSesFascicoloKo addVrsSesFascicoloKo(VrsSesFascicoloKo vrsSesFascicoloKo) {
	getVrsSesFascicoloKos().add(vrsSesFascicoloKo);
	vrsSesFascicoloKo.setVrsFascicoloKo(this);

	return vrsSesFascicoloKo;
    }

    public VrsSesFascicoloKo removeVrsSesFascicoloKo(VrsSesFascicoloKo vrsSesFascicoloKo) {
	getVrsSesFascicoloKos().remove(vrsSesFascicoloKo);
	vrsSesFascicoloKo.setVrsFascicoloKo(null);

	return vrsSesFascicoloKo;
    }

}
