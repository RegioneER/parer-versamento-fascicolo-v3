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
 * The persistent class for the VRS_ERR_SES_FASCICOLO_KO database table.
 *
 */
@Entity
@Table(name = "VRS_ERR_SES_FASCICOLO_KO")
@NamedQuery(name = "VrsErrSesFascicoloKo.findAll", query = "SELECT v FROM VrsErrSesFascicoloKo v")
public class VrsErrSesFascicoloKo implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idErrSesFascicoloKo;
    private String dsErr;
    private String flErrPrinc;
    private BigDecimal pgErr;
    private String tiErr;
    private DecErrSacer decErrSacer;
    private VrsSesFascicoloKo vrsSesFascicoloKo;

    public VrsErrSesFascicoloKo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "VRS_ERR_SES_FASCICOLO_KO_IDERRSESFASCICOLOKO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SVRS_ERR_SES_FASCICOLO_KO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "VRS_ERR_SES_FASCICOLO_KO_IDERRSESFASCICOLOKO_GENERATOR")
    @Column(name = "ID_ERR_SES_FASCICOLO_KO")
    public Long getIdErrSesFascicoloKo() {
	return this.idErrSesFascicoloKo;
    }

    public void setIdErrSesFascicoloKo(Long idErrSesFascicoloKo) {
	this.idErrSesFascicoloKo = idErrSesFascicoloKo;
    }

    @Column(name = "DS_ERR")
    public String getDsErr() {
	return this.dsErr;
    }

    public void setDsErr(String dsErr) {
	this.dsErr = dsErr;
    }

    @Column(name = "FL_ERR_PRINC", columnDefinition = "CHAR")
    public String getFlErrPrinc() {
	return this.flErrPrinc;
    }

    public void setFlErrPrinc(String flErrPrinc) {
	this.flErrPrinc = flErrPrinc;
    }

    @Column(name = "PG_ERR")
    public BigDecimal getPgErr() {
	return this.pgErr;
    }

    public void setPgErr(BigDecimal pgErr) {
	this.pgErr = pgErr;
    }

    @Column(name = "TI_ERR")
    public String getTiErr() {
	return this.tiErr;
    }

    public void setTiErr(String tiErr) {
	this.tiErr = tiErr;
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

    // bi-directional many-to-one association to VrsSesFascicoloKo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SES_FASCICOLO_KO")
    public VrsSesFascicoloKo getVrsSesFascicoloKo() {
	return this.vrsSesFascicoloKo;
    }

    public void setVrsSesFascicoloKo(VrsSesFascicoloKo vrsSesFascicoloKo) {
	this.vrsSesFascicoloKo = vrsSesFascicoloKo;
    }

}
