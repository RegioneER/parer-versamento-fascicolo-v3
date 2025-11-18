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

import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequence;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Table;

/**
 * The persistent class for the DEC_ERR_AA_TIPO_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "DEC_ERR_AA_TIPO_FASCICOLO")
@NamedQuery(name = "DecErrAaTipoFascicolo.findAll", query = "SELECT d FROM DecErrAaTipoFascicolo d")
public class DecErrAaTipoFascicolo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idErrAaTipoFascicolo;
    private BigDecimal aaFascicolo;
    private String dsErrFmtNumero;
    private BigDecimal idFascicoloErrFmtNumero;
    private DecAaTipoFascicolo decAaTipoFascicolo;

    public DecErrAaTipoFascicolo() {
        // hibernate constructor
    }

    @Id
    @NonMonotonicSequence(sequenceName = "SDEC_ERR_AA_TIPO_FASCICOLO", incrementBy = 1)
    @Column(name = "ID_ERR_AA_TIPO_FASCICOLO")
    public Long getIdErrAaTipoFascicolo() {
        return this.idErrAaTipoFascicolo;
    }

    public void setIdErrAaTipoFascicolo(Long idErrAaTipoFascicolo) {
        this.idErrAaTipoFascicolo = idErrAaTipoFascicolo;
    }

    @Column(name = "AA_FASCICOLO")
    public BigDecimal getAaFascicolo() {
        return this.aaFascicolo;
    }

    public void setAaFascicolo(BigDecimal aaFascicolo) {
        this.aaFascicolo = aaFascicolo;
    }

    @Column(name = "DS_ERR_FMT_NUMERO")
    public String getDsErrFmtNumero() {
        return this.dsErrFmtNumero;
    }

    public void setDsErrFmtNumero(String dsErrFmtNumero) {
        this.dsErrFmtNumero = dsErrFmtNumero;
    }

    @Column(name = "ID_FASCICOLO_ERR_FMT_NUMERO")
    public BigDecimal getIdFascicoloErrFmtNumero() {
        return this.idFascicoloErrFmtNumero;
    }

    public void setIdFascicoloErrFmtNumero(BigDecimal idFascicoloErrFmtNumero) {
        this.idFascicoloErrFmtNumero = idFascicoloErrFmtNumero;
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
