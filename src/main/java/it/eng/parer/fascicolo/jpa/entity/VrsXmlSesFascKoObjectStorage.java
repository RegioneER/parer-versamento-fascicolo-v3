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

import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "VRS_XML_SES_FASC_KO_OBJECT_STORAGE")
public class VrsXmlSesFascKoObjectStorage implements Serializable {
    private static final long serialVersionUID = 1L;

    public VrsXmlSesFascKoObjectStorage() {
	// hibernate constructor
    }

    private Long idXmlSesFascKoObjectStorage;
    private DecBackend decBackend;
    private VrsSesFascicoloKo vrsSesFascicoloKo;
    private String nmTenant;
    private String nmBucket;
    private String nmKeyFile;
    private BigDecimal idStrut;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_XML_SES_FASC_KO_OBJECT_STORAGE")
    public Long getIdXmlSesFascKoObjectStorage() {
	return idXmlSesFascKoObjectStorage;
    }

    public void setIdXmlSesFascKoObjectStorage(Long idXmlSesFascKoObjectStorage) {
	this.idXmlSesFascKoObjectStorage = idXmlSesFascKoObjectStorage;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_DEC_BACKEND")
    public DecBackend getDecBackend() {
	return decBackend;
    }

    public void setDecBackend(DecBackend decBackend) {
	this.decBackend = decBackend;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SES_FASCICOLO_KO")
    public VrsSesFascicoloKo getVrsSesFascicoloKo() {
	return vrsSesFascicoloKo;
    }

    public void setVrsSesFascicoloKo(VrsSesFascicoloKo vrsSesFascicoloKo) {
	this.vrsSesFascicoloKo = vrsSesFascicoloKo;
    }

    @Column(name = "NM_TENANT")
    public String getNmTenant() {
	return nmTenant;
    }

    public void setNmTenant(String nmTenant) {
	this.nmTenant = nmTenant;
    }

    @Column(name = "NM_BUCKET")
    public String getNmBucket() {
	return nmBucket;
    }

    public void setNmBucket(String nmBucket) {
	this.nmBucket = nmBucket;
    }

    @Column(name = "CD_KEY_FILE")
    public String getNmKeyFile() {
	return nmKeyFile;
    }

    public void setNmKeyFile(String nmKeyFile) {
	this.nmKeyFile = nmKeyFile;
    }

    @Column(name = "ID_STRUT")
    public BigDecimal getIdStrut() {
	return this.idStrut;
    }

    public void setIdStrut(BigDecimal idStrut) {
	this.idStrut = idStrut;
    }
}
