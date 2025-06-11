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

/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package it.eng.parer.fascicolo.jpa.grantedEntity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

/**
 *
 * @author fioravanti_f
 */
@Entity
@Table(schema = "SACER_IAM", name = "APL_APPLIC")
public class SIAplApplic implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Long idApplic;
    private String cdPswReplicaUser;
    private String dsApplic;
    private String dsUrlReplicaUser;
    private String nmApplic;
    private String nmUserReplicaUser;
    private List<SIAplTipoOrganiz> aplTipoOrganizs = new ArrayList<>();

    public SIAplApplic() {
	// hibernate constructor
    }

    @Id
    @SequenceGenerator(name = "APL_APPLIC_IDAPPLIC_GENERATOR", sequenceName = "SACER_IAM.SAPL_APPLIC", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APL_APPLIC_IDAPPLIC_GENERATOR")
    @Column(name = "ID_APPLIC")
    public Long getIdApplic() {
	return this.idApplic;
    }

    public void setIdApplic(Long idApplic) {
	this.idApplic = idApplic;
    }

    @Column(name = "CD_PSW_REPLICA_USER")
    public String getCdPswReplicaUser() {
	return this.cdPswReplicaUser;
    }

    public void setCdPswReplicaUser(String cdPswReplicaUser) {
	this.cdPswReplicaUser = cdPswReplicaUser;
    }

    @Column(name = "DS_APPLIC")
    public String getDsApplic() {
	return this.dsApplic;
    }

    public void setDsApplic(String dsApplic) {
	this.dsApplic = dsApplic;
    }

    @Column(name = "DS_URL_REPLICA_USER")
    public String getDsUrlReplicaUser() {
	return this.dsUrlReplicaUser;
    }

    public void setDsUrlReplicaUser(String dsUrlReplicaUser) {
	this.dsUrlReplicaUser = dsUrlReplicaUser;
    }

    @Column(name = "NM_APPLIC")
    public String getNmApplic() {
	return this.nmApplic;
    }

    public void setNmApplic(String nmApplic) {
	this.nmApplic = nmApplic;
    }

    @Column(name = "NM_USER_REPLICA_USER")
    public String getNmUserReplicaUser() {
	return this.nmUserReplicaUser;
    }

    public void setNmUserReplicaUser(String nmUserReplicaUser) {
	this.nmUserReplicaUser = nmUserReplicaUser;
    }

    @OneToMany(mappedBy = "sIAplApplic")
    public List<SIAplTipoOrganiz> getAplTipoOrganizs() {
	return aplTipoOrganizs;
    }

    public void setAplTipoOrganizs(List<SIAplTipoOrganiz> aplTipoOrganizs) {
	this.aplTipoOrganizs = aplTipoOrganizs;
    }

}
