package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The persistent class for the IAM_USER database table.
 *
 */
@Entity
@Table(name = "IAM_USER")
public class IamUser implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idUserIam;
    private String cdFisc;
    private String cdPsw;
    private String cdSalt;
    private String dsEmail;
    private LocalDateTime dtRegPsw;
    private LocalDateTime dtScadPsw;
    private String flAttivo;
    private String flContrIp;
    private String flUserAdmin;
    private String nmCognomeUser;
    private String nmNomeUser;
    private String nmUserid;
    private String tipoUser;
    private String tipoAuth;
    private List<AroUnitaDoc> aroUnitaDocs = new ArrayList<>();
    private List<IamAbilOrganiz> iamAbilOrganizs = new ArrayList<>();
    private List<IamIndIpUser> iamIndIpUsers = new ArrayList<>();
    private List<ElvStatoElencoVersFasc> elvStatoElencoVersFascs = new ArrayList<>();

    public IamUser() {
        // hibernate constructor
    }

    @Id
    @Column(name = "ID_USER_IAM")
    public Long getIdUserIam() {
        return this.idUserIam;
    }

    public void setIdUserIam(Long idUserIam) {
        this.idUserIam = idUserIam;
    }

    @Column(name = "CD_FISC")
    public String getCdFisc() {
        return this.cdFisc;
    }

    public void setCdFisc(String cdFisc) {
        this.cdFisc = cdFisc;
    }

    @Column(name = "CD_PSW")
    public String getCdPsw() {
        return this.cdPsw;
    }

    public void setCdPsw(String cdPsw) {
        this.cdPsw = cdPsw;
    }

    @Column(name = "CD_SALT")
    public String getCdSalt() {
        return this.cdSalt;
    }

    public void setCdSalt(String cdSalt) {
        this.cdSalt = cdSalt;
    }

    @Column(name = "DS_EMAIL")
    public String getDsEmail() {
        return this.dsEmail;
    }

    public void setDsEmail(String dsEmail) {
        this.dsEmail = dsEmail;
    }

    @Column(name = "DT_REG_PSW")
    public LocalDateTime getDtRegPsw() {
        return this.dtRegPsw;
    }

    public void setDtRegPsw(LocalDateTime dtRegPsw) {
        this.dtRegPsw = dtRegPsw;
    }

    @Column(name = "DT_SCAD_PSW")
    public LocalDateTime getDtScadPsw() {
        return this.dtScadPsw;
    }

    public void setDtScadPsw(LocalDateTime dtScadPsw) {
        this.dtScadPsw = dtScadPsw;
    }

    @Column(name = "FL_ATTIVO", columnDefinition = "CHAR")
    public String getFlAttivo() {
        return this.flAttivo;
    }

    public void setFlAttivo(String flAttivo) {
        this.flAttivo = flAttivo;
    }

    @Column(name = "FL_CONTR_IP", columnDefinition = "CHAR")
    public String getFlContrIp() {
        return this.flContrIp;
    }

    public void setFlContrIp(String flContrIp) {
        this.flContrIp = flContrIp;
    }

    @Column(name = "FL_USER_ADMIN", columnDefinition = "CHAR")
    public String getFlUserAdmin() {
        return this.flUserAdmin;
    }

    public void setFlUserAdmin(String flUserAdmin) {
        this.flUserAdmin = flUserAdmin;
    }

    @Column(name = "NM_COGNOME_USER")
    public String getNmCognomeUser() {
        return this.nmCognomeUser;
    }

    public void setNmCognomeUser(String nmCognomeUser) {
        this.nmCognomeUser = nmCognomeUser;
    }

    @Column(name = "NM_NOME_USER")
    public String getNmNomeUser() {
        return this.nmNomeUser;
    }

    public void setNmNomeUser(String nmNomeUser) {
        this.nmNomeUser = nmNomeUser;
    }

    @Column(name = "NM_USERID")
    public String getNmUserid() {
        return this.nmUserid;
    }

    public void setNmUserid(String nmUserid) {
        this.nmUserid = nmUserid;
    }

    @Column(name = "TIPO_USER")
    public String getTipoUser() {
        return this.tipoUser;
    }

    public void setTipoUser(String tipoUser) {
        this.tipoUser = tipoUser;
    }

    @Column(name = "TIPO_AUTH")
    public String getTipoAuth() {
        return this.tipoAuth;
    }

    public void setTipoAuth(String tipoAuth) {
        this.tipoAuth = tipoAuth;
    }

    // bi-directional many-to-one association to IamAbilOrganiz
    @OneToMany(mappedBy = "iamUser", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    public List<IamAbilOrganiz> getIamAbilOrganizs() {
        return this.iamAbilOrganizs;
    }

    public void setIamAbilOrganizs(List<IamAbilOrganiz> iamAbilOrganizs) {
        this.iamAbilOrganizs = iamAbilOrganizs;
    }

    // bi-directional many-to-one association to IamIndIpUser
    @OneToMany(mappedBy = "iamUser", cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    public List<IamIndIpUser> getIamIndIpUsers() {
        return this.iamIndIpUsers;
    }

    public void setIamIndIpUsers(List<IamIndIpUser> iamIndIpUsers) {
        this.iamIndIpUsers = iamIndIpUsers;
    }

    // bi-directional many-to-one association to AroUnitaDoc
    @OneToMany(mappedBy = "iamUser")
    public List<AroUnitaDoc> getAroUnitaDocs() {
        return this.aroUnitaDocs;
    }

    public void setAroUnitaDocs(List<AroUnitaDoc> aroUnitaDocs) {
        this.aroUnitaDocs = aroUnitaDocs;
    }

    // bi-directional many-to-one association to ElvStatoElencoVersFasc
    @OneToMany(mappedBy = "iamUser")
    public List<ElvStatoElencoVersFasc> getElvStatoElencoVersFascs() {
        return this.elvStatoElencoVersFascs;
    }

    public void setElvStatoElencoVersFascs(List<ElvStatoElencoVersFasc> elvStatoElencoVersFascs) {
        this.elvStatoElencoVersFascs = elvStatoElencoVersFascs;
    }
}
