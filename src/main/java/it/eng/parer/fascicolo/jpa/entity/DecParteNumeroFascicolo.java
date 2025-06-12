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

/**
 * The persistent class for the DEC_PARTE_NUMERO_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "DEC_PARTE_NUMERO_FASCICOLO")
@NamedQuery(name = "DecParteNumeroFascicolo.findAll", query = "SELECT d FROM DecParteNumeroFascicolo d")
public class DecParteNumeroFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idParteNumeroFascicolo;
    private String dlValoriParte;
    private String dsParteNumero;
    private BigDecimal niMaxCharParte;
    private BigDecimal niMinCharParte;
    private BigDecimal niParteNumero;
    private String nmParteNumero;
    private String tiCharParte;
    private String tiCharSep;
    private String tiPadParte;
    private String tiParte;
    private DecAaTipoFascicolo decAaTipoFascicolo;

    public DecParteNumeroFascicolo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "DEC_PARTE_NUMERO_FASCICOLO_IDPARTENUMEROFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SDEC_PARTE_NUMERO_FASCICOLO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEC_PARTE_NUMERO_FASCICOLO_IDPARTENUMEROFASCICOLO_GENERATOR")
    @Column(name = "ID_PARTE_NUMERO_FASCICOLO")
    public Long getIdParteNumeroFascicolo() {
	return this.idParteNumeroFascicolo;
    }

    public void setIdParteNumeroFascicolo(Long idParteNumeroFascicolo) {
	this.idParteNumeroFascicolo = idParteNumeroFascicolo;
    }

    @Column(name = "DL_VALORI_PARTE")
    public String getDlValoriParte() {
	return this.dlValoriParte;
    }

    public void setDlValoriParte(String dlValoriParte) {
	this.dlValoriParte = dlValoriParte;
    }

    @Column(name = "DS_PARTE_NUMERO")
    public String getDsParteNumero() {
	return this.dsParteNumero;
    }

    public void setDsParteNumero(String dsParteNumero) {
	this.dsParteNumero = dsParteNumero;
    }

    @Column(name = "NI_MAX_CHAR_PARTE")
    public BigDecimal getNiMaxCharParte() {
	return this.niMaxCharParte;
    }

    public void setNiMaxCharParte(BigDecimal niMaxCharParte) {
	this.niMaxCharParte = niMaxCharParte;
    }

    @Column(name = "NI_MIN_CHAR_PARTE")
    public BigDecimal getNiMinCharParte() {
	return this.niMinCharParte;
    }

    public void setNiMinCharParte(BigDecimal niMinCharParte) {
	this.niMinCharParte = niMinCharParte;
    }

    @Column(name = "NI_PARTE_NUMERO")
    public BigDecimal getNiParteNumero() {
	return this.niParteNumero;
    }

    public void setNiParteNumero(BigDecimal niParteNumero) {
	this.niParteNumero = niParteNumero;
    }

    @Column(name = "NM_PARTE_NUMERO")
    public String getNmParteNumero() {
	return this.nmParteNumero;
    }

    public void setNmParteNumero(String nmParteNumero) {
	this.nmParteNumero = nmParteNumero;
    }

    @Column(name = "TI_CHAR_PARTE")
    public String getTiCharParte() {
	return this.tiCharParte;
    }

    public void setTiCharParte(String tiCharParte) {
	this.tiCharParte = tiCharParte;
    }

    @Column(name = "TI_CHAR_SEP", columnDefinition = "CHAR")
    public String getTiCharSep() {
	return this.tiCharSep;
    }

    public void setTiCharSep(String tiCharSep) {
	this.tiCharSep = tiCharSep;
    }

    @Column(name = "TI_PAD_PARTE")
    public String getTiPadParte() {
	return this.tiPadParte;
    }

    public void setTiPadParte(String tiPadParte) {
	this.tiPadParte = tiPadParte;
    }

    @Column(name = "TI_PARTE")
    public String getTiParte() {
	return this.tiParte;
    }

    public void setTiParte(String tiParte) {
	this.tiParte = tiParte;
    }

    // bi-directional many-to-one association to DecAaTipoFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_AA_TIPO_FASCICOLO")
    public DecAaTipoFascicolo getDecAaTipoFascicolo() {
	return this.decAaTipoFascicolo;
    }

    public void setDecAaTipoFascicolo(DecAaTipoFascicolo decAaTipoFascicolo) {
	this.decAaTipoFascicolo = decAaTipoFascicolo;
    }

}
