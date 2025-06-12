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

import it.eng.parer.fascicolo.jpa.entity.constraint.FasFascicolo.TiConservazione;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasFascicolo.TiStatoConservazione;
import it.eng.parer.fascicolo.jpa.entity.constraint.FasFascicolo.TiStatoFascElencoVers;
import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequenceGenerator;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;

/**
 * The persistent class for the FAS_FASCICOLO database table.
 *
 */
@Entity
@Table(name = "FAS_FASCICOLO")
@NamedQuery(name = "FasFascicolo.findAll", query = "SELECT f FROM FasFascicolo f")
@NamedQuery(name = "FasFascicolo.findByStrutAnnoNum", query = "SELECT f FROM FasFascicolo f WHERE f.orgStrut = :orgStrut AND f.aaFascicolo=:aaFascicolo AND f.cdKeyFascicolo=:cdKeyFascicolo AND f.dtAnnull=:dtAnnull")
@NamedQuery(name = "FasFascicolo.findCountFascicoliVersatiNelGiorno", query = "SELECT f.orgStrut.idStrut, f.decTipoFascicolo.idTipoFascicolo, f.aaFascicolo, f.iamUser.idUserIam, COUNT(f.idFascicolo) FROM FasFascicolo f JOIN f.orgStrut strut WHERE FUNC('trunc', f.tsIniSes, 'DD')=FUNC('to_date',FUNC('to_char',:data,'DD/MM/YYYY'),'DD/MM/YYYY') GROUP BY f.orgStrut.idStrut, f.decTipoFascicolo.idTipoFascicolo, f.aaFascicolo, f.iamUser.idUserIam")
public class FasFascicolo implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idFascicolo;
    private BigDecimal aaFascicolo;
    private BigDecimal aaFascicoloPadre;
    private String cdIndIpClient;
    private String cdIndServer;
    private String cdIndiceClassif;
    private String cdAmminTitol;
    private String cdKeyFascicolo;
    private String cdKeyFascicoloPadre;
    private String cdKeyOrd;
    private String cdLivelloRiserv;
    private String cdProcAmmin;
    private String dsAmminTitol;
    private String dsIndiceClassif;
    private String dsNota;
    private String dsOggettoFascicolo;
    private String dsOggettoFascicoloPadre;
    private String dsProcAmmin;
    private String dsProcAmminMateriaArgStrut;
    private LocalDateTime dtAnnull;
    private LocalDateTime dtApeFascicolo;
    private LocalDateTime dtChiuFascicolo;
    private String flForzaContrClassif;
    private String flForzaContrColleg;
    private String flForzaContrNumero;
    private String flSesFascicoloKo;
    private String flUpdAnnulUnitaDoc;
    private String flUpdModifUnitaDoc;
    private String cdKeyNormalizFascicolo;
    private String dsInfoConservazione;
    private BigDecimal niAaConservazione;
    private BigDecimal niSottoFascicoli;
    private BigDecimal niUnitaDoc;
    private String ntAnnul;
    private TiConservazione tiConservazione;
    private TiStatoConservazione tiStatoConservazione;
    private TiStatoFascElencoVers tiStatoFascElencoVers;
    private String tiCodiceAmminTitol;
    private LocalDateTime tsFineSes;
    private LocalDateTime tsIniSes;
    private List<ElvFascDaElabElenco> elvFascDaElabElencos = new ArrayList<>();
    private List<FasAmminPartec> fasAmminPartecs = new ArrayList<>();
    private AplSistemaMigraz aplSistemaMigraz;
    private DecTipoFascicolo decTipoFascicolo;
    private DecVoceTitol decVoceTitol;
    private ElvElencoVersFasc elvElencoVersFasc;
    private FasFascicolo fasFascicoloPadre;
    private List<FasFascicolo> fasFascicolos = new ArrayList<>();
    private List<FasStatoConservFascicolo> fasStatoConservFascicoloElencos = new ArrayList<>();
    private List<FasStatoFascicoloElenco> fasStatoFascicoloElencos = new ArrayList<>();
    private IamUser iamUser;
    private OrgStrut orgStrut;
    private List<FasLinkFascicolo> fasLinkFascicolos1 = new ArrayList<>();
    private List<FasRespFascicolo> fasRespFascicolos = new ArrayList<>();
    private List<FasUniOrgRespFascicolo> fasUniOrgRespFascicolos = new ArrayList<>();
    private List<FasSogFascicolo> fasSogFascicolos = new ArrayList<>();
    private List<FasUnitaDocFascicolo> fasUnitaDocFascicolos = new ArrayList<>();
    private List<FasWarnFascicolo> fasWarnFascicolos = new ArrayList<>();
    private List<FasXmlFascicolo> fasXmlFascicolos = new ArrayList<>();
    private FasXmlFascObjectStorage fasXmlFascObjectStorage;
    private List<FasXmlVersFascicolo> fasXmlVersFascicolos = new ArrayList<>();
    private FasXmlVersFascObjectStorage fasXmlVersFascObjectStorage;
    private List<FasVerAipFascicolo> fasVerAipFascicolos = new ArrayList<>();
    private List<FasAipFascicoloDaElab> fasAipFascicoloDaElabs = new ArrayList<>();
    private List<ElvElencoVersFascAnnul> elvElencoVersFascAnnuls = new ArrayList<>();
    private List<FasEventoFascicolo> fasEventoFascicolos = new ArrayList<>();

    public FasFascicolo() {
	// hibernate constructor
    }

    @Id
    @GenericGenerator(name = "FAS_FASCICOLO_IDFASCICOLO_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
	    @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SFAS_FASCICOLO"),
	    @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FAS_FASCICOLO_IDFASCICOLO_GENERATOR")
    @Column(name = "ID_FASCICOLO")
    public Long getIdFascicolo() {
	return this.idFascicolo;
    }

    public void setIdFascicolo(Long idFascicolo) {
	this.idFascicolo = idFascicolo;
    }

    @Column(name = "AA_FASCICOLO")
    public BigDecimal getAaFascicolo() {
	return this.aaFascicolo;
    }

    public void setAaFascicolo(BigDecimal aaFascicolo) {
	this.aaFascicolo = aaFascicolo;
    }

    @Column(name = "AA_FASCICOLO_PADRE")
    public BigDecimal getAaFascicoloPadre() {
	return this.aaFascicoloPadre;
    }

    public void setAaFascicoloPadre(BigDecimal aaFascicoloPadre) {
	this.aaFascicoloPadre = aaFascicoloPadre;
    }

    @Column(name = "CD_IND_IP_CLIENT")
    public String getCdIndIpClient() {
	return this.cdIndIpClient;
    }

    public void setCdIndIpClient(String cdIndIpClient) {
	this.cdIndIpClient = cdIndIpClient;
    }

    @Column(name = "CD_IND_SERVER")
    public String getCdIndServer() {
	return this.cdIndServer;
    }

    public void setCdIndServer(String cdIndServer) {
	this.cdIndServer = cdIndServer;
    }

    @Column(name = "CD_INDICE_CLASSIF")
    public String getCdIndiceClassif() {
	return this.cdIndiceClassif;
    }

    public void setCdIndiceClassif(String cdIndiceClassif) {
	this.cdIndiceClassif = cdIndiceClassif;
    }

    @Column(name = "CD_AMMIN_TITOL")
    public String getCdAmminTitol() {
	return this.cdAmminTitol;
    }

    public void setCdAmminTitol(String cdAmminTitol) {
	this.cdAmminTitol = cdAmminTitol;
    }

    @Column(name = "CD_KEY_FASCICOLO")
    public String getCdKeyFascicolo() {
	return this.cdKeyFascicolo;
    }

    public void setCdKeyFascicolo(String cdKeyFascicolo) {
	this.cdKeyFascicolo = cdKeyFascicolo;
    }

    @Column(name = "CD_KEY_FASCICOLO_PADRE")
    public String getCdKeyFascicoloPadre() {
	return this.cdKeyFascicoloPadre;
    }

    public void setCdKeyFascicoloPadre(String cdKeyFascicoloPadre) {
	this.cdKeyFascicoloPadre = cdKeyFascicoloPadre;
    }

    @Column(name = "CD_KEY_ORD")
    public String getCdKeyOrd() {
	return this.cdKeyOrd;
    }

    public void setCdKeyOrd(String cdKeyOrd) {
	this.cdKeyOrd = cdKeyOrd;
    }

    @Column(name = "CD_LIVELLO_RISERV")
    public String getCdLivelloRiserv() {
	return this.cdLivelloRiserv;
    }

    public void setCdLivelloRiserv(String cdLivelloRiserv) {
	this.cdLivelloRiserv = cdLivelloRiserv;
    }

    @Column(name = "CD_PROC_AMMIN")
    public String getCdProcAmmin() {
	return this.cdProcAmmin;
    }

    public void setCdProcAmmin(String cdProcAmmin) {
	this.cdProcAmmin = cdProcAmmin;
    }

    @Column(name = "DS_AMMIN_TITOL")
    public String getDsAmminTitol() {
	return this.dsAmminTitol;
    }

    public void setDsAmminTitol(String dsAmminTitol) {
	this.dsAmminTitol = dsAmminTitol;
    }

    @Column(name = "DS_INDICE_CLASSIF")
    public String getDsIndiceClassif() {
	return this.dsIndiceClassif;
    }

    public void setDsIndiceClassif(String dsIndiceClassif) {
	this.dsIndiceClassif = dsIndiceClassif;
    }

    @Column(name = "DS_NOTA")
    public String getDsNota() {
	return this.dsNota;
    }

    public void setDsNota(String dsNota) {
	this.dsNota = dsNota;
    }

    @Column(name = "DS_OGGETTO_FASCICOLO")
    public String getDsOggettoFascicolo() {
	return this.dsOggettoFascicolo;
    }

    public void setDsOggettoFascicolo(String dsOggettoFascicolo) {
	this.dsOggettoFascicolo = dsOggettoFascicolo;
    }

    @Column(name = "DS_OGGETTO_FASCICOLO_PADRE")
    public String getDsOggettoFascicoloPadre() {
	return this.dsOggettoFascicoloPadre;
    }

    public void setDsOggettoFascicoloPadre(String dsOggettoFascicoloPadre) {
	this.dsOggettoFascicoloPadre = dsOggettoFascicoloPadre;
    }

    @Column(name = "DS_PROC_AMMIN")
    public String getDsProcAmmin() {
	return this.dsProcAmmin;
    }

    public void setDsProcAmmin(String dsProcAmmin) {
	this.dsProcAmmin = dsProcAmmin;
    }

    @Column(name = "DS_PROC_AMMIN_MATERIA_ARG_STRUT")
    public String getDsProcAmminMateriaArgStrut() {
	return this.dsProcAmminMateriaArgStrut;
    }

    public void setDsProcAmminMateriaArgStrut(String dsProcAmminMateriaArgStrut) {
	this.dsProcAmminMateriaArgStrut = dsProcAmminMateriaArgStrut;
    }

    @Column(name = "DT_ANNULL")
    public LocalDateTime getDtAnnull() {
	return this.dtAnnull;
    }

    public void setDtAnnull(LocalDateTime dtAnnull) {
	this.dtAnnull = dtAnnull;
    }

    @Column(name = "DT_APE_FASCICOLO")
    public LocalDateTime getDtApeFascicolo() {
	return this.dtApeFascicolo;
    }

    public void setDtApeFascicolo(LocalDateTime dtApeFascicolo) {
	this.dtApeFascicolo = dtApeFascicolo;
    }

    @Column(name = "DT_CHIU_FASCICOLO")
    public LocalDateTime getDtChiuFascicolo() {
	return this.dtChiuFascicolo;
    }

    public void setDtChiuFascicolo(LocalDateTime dtChiuFascicolo) {
	this.dtChiuFascicolo = dtChiuFascicolo;
    }

    @Column(name = "FL_FORZA_CONTR_CLASSIF", columnDefinition = "CHAR")
    public String getFlForzaContrClassif() {
	return this.flForzaContrClassif;
    }

    public void setFlForzaContrClassif(String flForzaContrClassif) {
	this.flForzaContrClassif = flForzaContrClassif;
    }

    @Column(name = "FL_FORZA_CONTR_COLLEG", columnDefinition = "CHAR")
    public String getFlForzaContrColleg() {
	return this.flForzaContrColleg;
    }

    public void setFlForzaContrColleg(String flForzaContrColleg) {
	this.flForzaContrColleg = flForzaContrColleg;
    }

    @Column(name = "FL_FORZA_CONTR_NUMERO", columnDefinition = "CHAR")
    public String getFlForzaContrNumero() {
	return this.flForzaContrNumero;
    }

    public void setFlForzaContrNumero(String flForzaContrNumero) {
	this.flForzaContrNumero = flForzaContrNumero;
    }

    @Column(name = "FL_SES_FASCICOLO_KO", columnDefinition = "CHAR")
    public String getFlSesFascicoloKo() {
	return this.flSesFascicoloKo;
    }

    public void setFlSesFascicoloKo(String flSesFascicoloKo) {
	this.flSesFascicoloKo = flSesFascicoloKo;
    }

    @Column(name = "FL_UPD_ANNUL_UNITA_DOC", columnDefinition = "CHAR")
    public String getFlUpdAnnulUnitaDoc() {
	return this.flUpdAnnulUnitaDoc;
    }

    public void setFlUpdAnnulUnitaDoc(String flUpdAnnulUnitaDoc) {
	this.flUpdAnnulUnitaDoc = flUpdAnnulUnitaDoc;
    }

    @Column(name = "FL_UPD_MODIF_UNITA_DOC", columnDefinition = "CHAR")
    public String getFlUpdModifUnitaDoc() {
	return this.flUpdModifUnitaDoc;
    }

    public void setFlUpdModifUnitaDoc(String flUpdModifUnitaDoc) {
	this.flUpdModifUnitaDoc = flUpdModifUnitaDoc;
    }

    @Column(name = "NI_AA_CONSERVAZIONE")
    public BigDecimal getNiAaConservazione() {
	return this.niAaConservazione;
    }

    public void setNiAaConservazione(BigDecimal niAaConservazione) {
	this.niAaConservazione = niAaConservazione;
    }

    @Column(name = "NI_SOTTO_FASCICOLI")
    public BigDecimal getNiSottoFascicoli() {
	return this.niSottoFascicoli;
    }

    public void setNiSottoFascicoli(BigDecimal niSottoFascicoli) {
	this.niSottoFascicoli = niSottoFascicoli;
    }

    @Column(name = "NI_UNITA_DOC")
    public BigDecimal getNiUnitaDoc() {
	return this.niUnitaDoc;
    }

    public void setNiUnitaDoc(BigDecimal niUnitaDoc) {
	this.niUnitaDoc = niUnitaDoc;
    }

    @Column(name = "NT_ANNUL")
    public String getNtAnnul() {
	return this.ntAnnul;
    }

    public void setNtAnnul(String ntAnnul) {
	this.ntAnnul = ntAnnul;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TI_CONSERVAZIONE")
    public TiConservazione getTiConservazione() {
	return this.tiConservazione;
    }

    public void setTiConservazione(TiConservazione tiConservazione) {
	this.tiConservazione = tiConservazione;
    }

    /* XML non serializable */
    @Enumerated(EnumType.STRING)
    @Column(name = "TI_STATO_CONSERVAZIONE")
    public TiStatoConservazione getTiStatoConservazione() {
	return this.tiStatoConservazione;
    }

    public void setTiStatoConservazione(TiStatoConservazione tiStatoConservazione) {
	this.tiStatoConservazione = tiStatoConservazione;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = "TI_STATO_FASC_ELENCO_VERS")
    public TiStatoFascElencoVers getTiStatoFascElencoVers() {
	return this.tiStatoFascElencoVers;
    }

    public void setTiStatoFascElencoVers(TiStatoFascElencoVers tiStatoFascElencoVers) {
	this.tiStatoFascElencoVers = tiStatoFascElencoVers;
    }

    @Column(name = "TI_CODICE_AMMIN_TITOL")
    public String getTiCodiceAmminTitol() {
	return this.tiCodiceAmminTitol;
    }

    public void setTiCodiceAmminTitol(String tiCodiceAmminTitol) {
	this.tiCodiceAmminTitol = tiCodiceAmminTitol;
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

    // bi-directional many-to-one association to FasUnitaDocFascicolo
    @OneToMany(mappedBy = "fasFascicolo")
    public List<FasAmminPartec> getFasAmminPartecs() {
	return fasAmminPartecs;
    }

    public void setFasAmminPartecs(List<FasAmminPartec> fasAmminPartecs) {
	this.fasAmminPartecs = fasAmminPartecs;
    }

    // bi-directional many-to-one association to FasUnitaDocFascicolo
    @OneToMany(mappedBy = "fasFascicolo")
    public List<ElvFascDaElabElenco> getElvFascDaElabElencos() {
	return elvFascDaElabElencos;
    }

    public void setElvFascDaElabElencos(List<ElvFascDaElabElenco> elvFascDaElabElencos) {
	this.elvFascDaElabElencos = elvFascDaElabElencos;
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

    // bi-directional many-to-one association to DecTipoFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TIPO_FASCICOLO")
    public DecTipoFascicolo getDecTipoFascicolo() {
	return this.decTipoFascicolo;
    }

    public void setDecTipoFascicolo(DecTipoFascicolo decTipoFascicolo) {
	this.decTipoFascicolo = decTipoFascicolo;
    }

    // bi-directional many-to-one association to DecVoceTitol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VOCE_TITOL")
    public DecVoceTitol getDecVoceTitol() {
	return this.decVoceTitol;
    }

    public void setDecVoceTitol(DecVoceTitol decVoceTitol) {
	this.decVoceTitol = decVoceTitol;
    }

    // bi-directional many-to-one association to ElvElencoVersFasc
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_ELENCO_VERS_FASC")
    public ElvElencoVersFasc getElvElencoVersFasc() {
	return this.elvElencoVersFasc;
    }

    public void setElvElencoVersFasc(ElvElencoVersFasc elvElencoVersFasc) {
	this.elvElencoVersFasc = elvElencoVersFasc;
    }

    // bi-directional many-to-one association to FasFascicolo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_FASCICOLO_PADRE")
    public FasFascicolo getFasFascicoloPadre() {
	return this.fasFascicoloPadre;
    }

    public void setFasFascicoloPadre(FasFascicolo fasFascicoloPadre) {
	this.fasFascicoloPadre = fasFascicoloPadre;
    }

    // bi-directional many-to-one association to FasFascicolo
    @OneToMany(mappedBy = "fasFascicoloPadre")
    public List<FasFascicolo> getFasFascicolos() {
	return this.fasFascicolos;
    }

    public void setFasFascicolos(List<FasFascicolo> fasFascicolos) {
	this.fasFascicolos = fasFascicolos;
    }

    // bi-directional many-to-one association to FasStatoConservFascicoloElenco
    @OneToMany(mappedBy = "fasFascicolo", cascade = CascadeType.PERSIST)
    public List<FasStatoConservFascicolo> getFasStatoConservFascicoloElencos() {
	return this.fasStatoConservFascicoloElencos;
    }

    public void setFasStatoConservFascicoloElencos(
	    List<FasStatoConservFascicolo> fasStatoConservFascicoloElencos) {
	this.fasStatoConservFascicoloElencos = fasStatoConservFascicoloElencos;
    }

    // bi-directional many-to-one association to FasStatoFascicoloElenco
    @OneToMany(mappedBy = "fasFascicolo", cascade = CascadeType.PERSIST)
    public List<FasStatoFascicoloElenco> getFasStatoFascicoloElencos() {
	return this.fasStatoFascicoloElencos;
    }

    public void setFasStatoFascicoloElencos(
	    List<FasStatoFascicoloElenco> fasStatoFascicoloElencos) {
	this.fasStatoFascicoloElencos = fasStatoFascicoloElencos;
    }

    // bi-directional many-to-one association to IamUser
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USER_IAM_VERS")
    public IamUser getIamUser() {
	return this.iamUser;
    }

    public void setIamUser(IamUser iamUser) {
	this.iamUser = iamUser;
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

    @Column(name = "CD_KEY_NORMALIZ_FASCICOLO")
    public String getCdKeyNormalizFascicolo() {
	return this.cdKeyNormalizFascicolo;
    }

    public void setCdKeyNormalizFascicolo(String cdKeyNormalizFascicolo) {
	this.cdKeyNormalizFascicolo = cdKeyNormalizFascicolo;
    }

    @Column(name = "DS_INFO_CONSERVAZIONE")
    public String getDsInfoConservazione() {
	return this.dsInfoConservazione;
    }

    public void setDsInfoConservazione(String dsInfoConservazione) {
	this.dsInfoConservazione = dsInfoConservazione;
    }

    // bi-directional many-to-one association to FasUnitaDocFascicolo
    @OneToMany(mappedBy = "fasFascicolo")
    public List<FasLinkFascicolo> getFasLinkFascicolos1() {
	return fasLinkFascicolos1;
    }

    public void setFasLinkFascicolos1(List<FasLinkFascicolo> fasLinkFascicolos1) {
	this.fasLinkFascicolos1 = fasLinkFascicolos1;
    }

    // bi-directional many-to-one association to FasUnitaDocFascicolo
    @OneToMany(mappedBy = "fasFascicolo")
    public List<FasRespFascicolo> getFasRespFascicolos() {
	return fasRespFascicolos;
    }

    public void setFasRespFascicolos(List<FasRespFascicolo> fasRespFascicolos) {
	this.fasRespFascicolos = fasRespFascicolos;
    }

    // bi-directional many-to-one association to FasUnitaDocFascicolo
    @OneToMany(mappedBy = "fasFascicolo")
    public List<FasUniOrgRespFascicolo> getFasUniOrgRespFascicolos() {
	return fasUniOrgRespFascicolos;
    }

    public void setFasUniOrgRespFascicolos(List<FasUniOrgRespFascicolo> fasUniOrgRespFascicolos) {
	this.fasUniOrgRespFascicolos = fasUniOrgRespFascicolos;
    }

    // bi-directional many-to-one association to FasUnitaDocFascicolo
    @OneToMany(mappedBy = "fasFascicolo")
    public List<FasSogFascicolo> getFasSogFascicolos() {
	return fasSogFascicolos;
    }

    public void setFasSogFascicolos(List<FasSogFascicolo> fasSogFascicolos) {
	this.fasSogFascicolos = fasSogFascicolos;
    }

    // bi-directional many-to-one association to FasUnitaDocFascicolo
    @OneToMany(mappedBy = "fasFascicolo")
    public List<FasUnitaDocFascicolo> getFasUnitaDocFascicolos() {
	return fasUnitaDocFascicolos;
    }

    public void setFasUnitaDocFascicolos(List<FasUnitaDocFascicolo> fasUnitaDocFascicolos) {
	this.fasUnitaDocFascicolos = fasUnitaDocFascicolos;
    }

    // bi-directional many-to-one association to FasWarnFascicolo
    @OneToMany(mappedBy = "fasFascicolo")
    public List<FasWarnFascicolo> getFasWarnFascicolos() {
	return fasWarnFascicolos;
    }

    public void setFasWarnFascicolos(List<FasWarnFascicolo> fasWarnFascicolos) {
	this.fasWarnFascicolos = fasWarnFascicolos;
    }

    // bi-directional many-to-one association to FasXmlFascicolo
    @OneToMany(mappedBy = "fasFascicolo")
    public List<FasXmlFascicolo> getFasXmlFascicolos() {
	return fasXmlFascicolos;
    }

    public void setFasXmlFascicolos(List<FasXmlFascicolo> fasXmlFascicolos) {
	this.fasXmlFascicolos = fasXmlFascicolos;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public FasXmlFascObjectStorage getFasXmlFascObjectStorage() {
	return fasXmlFascObjectStorage;
    }

    public void setFasXmlFascObjectStorage(FasXmlFascObjectStorage fasXmlFascObjectStorage) {
	this.fasXmlFascObjectStorage = fasXmlFascObjectStorage;
    }

    // bi-directional many-to-one association to FasXmlVersFascicolo
    @OneToMany(mappedBy = "fasFascicolo")
    public List<FasXmlVersFascicolo> getFasXmlVersFascicolos() {
	return fasXmlVersFascicolos;
    }

    public void setFasXmlVersFascicolos(List<FasXmlVersFascicolo> fasXmlVersFascicolos) {
	this.fasXmlVersFascicolos = fasXmlVersFascicolos;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    public FasXmlVersFascObjectStorage getFasXmlVersFascObjectStorage() {
	return fasXmlVersFascObjectStorage;
    }

    public void setFasXmlVersFascObjectStorage(
	    FasXmlVersFascObjectStorage fasXmlVersFascObjectStorage) {
	this.fasXmlVersFascObjectStorage = fasXmlVersFascObjectStorage;
    }

    // bi-directional many-to-one association to FasVerAipFascicolo
    @OneToMany(mappedBy = "fasFascicolo", cascade = {
	    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    public List<FasVerAipFascicolo> getFasVerAipFascicolos() {
	return this.fasVerAipFascicolos;
    }

    public void setFasVerAipFascicolos(List<FasVerAipFascicolo> fasVerAipFascicolos) {
	this.fasVerAipFascicolos = fasVerAipFascicolos;
    }

    // bi-directional many-to-one association to FasAipFascicoloDaElab
    @OneToMany(mappedBy = "fasFascicolo", cascade = {
	    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    public List<FasAipFascicoloDaElab> getFasAipFascicoloDaElabs() {
	return this.fasAipFascicoloDaElabs;
    }

    public void setFasAipFascicoloDaElabs(List<FasAipFascicoloDaElab> fasAipFascicoloDaElab) {
	this.fasAipFascicoloDaElabs = fasAipFascicoloDaElab;
    }

    // bi-directional many-to-one association to ElvElencoVersFascAnnul
    @OneToMany(mappedBy = "fasFascicolo")
    public List<ElvElencoVersFascAnnul> getElvElencoVersFascAnnuls() {
	return this.elvElencoVersFascAnnuls;
    }

    public void setElvElencoVersFascAnnuls(List<ElvElencoVersFascAnnul> elvElencoVersFascAnnuls) {
	this.elvElencoVersFascAnnuls = elvElencoVersFascAnnuls;
    }

    public ElvElencoVersFascAnnul addElvElencoVersFascAnnul(
	    ElvElencoVersFascAnnul elvElencoVersFascAnnul) {
	getElvElencoVersFascAnnuls().add(elvElencoVersFascAnnul);
	elvElencoVersFascAnnul.setFasFascicolo(this);

	return elvElencoVersFascAnnul;
    }

    public ElvElencoVersFascAnnul removeElvElencoVersFascAnnul(
	    ElvElencoVersFascAnnul elvElencoVersFascAnnul) {
	getElvElencoVersFascAnnuls().remove(elvElencoVersFascAnnul);
	elvElencoVersFascAnnul.setFasFascicolo(null);

	return elvElencoVersFascAnnul;
    }

    // bi-directional many-to-one association to ElvElencoVersFascAnnul
    @OneToMany(mappedBy = "fasFascicolo", cascade = {
	    CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH })
    public List<FasEventoFascicolo> getFasEventoFascicolos() {
	return this.fasEventoFascicolos;
    }

    public void setFasEventoFascicolos(List<FasEventoFascicolo> fasFasEventoFascicolos) {
	this.fasEventoFascicolos = fasFasEventoFascicolos;
    }

    /**
     * Gestione dei default. Risulta la migliore pratica in quanto è indipendente dal db utilizzato
     * e sfrutta diretta JPA quindi calabile sotto ogni contesto in termini di ORM
     *
     * ref. https://stackoverflow.com/a/13432234
     */
    @PrePersist
    void preInsert() {
	if (this.flUpdModifUnitaDoc == null) {
	    this.flUpdModifUnitaDoc = "0";
	}
	if (this.flUpdAnnulUnitaDoc == null) {
	    this.flUpdAnnulUnitaDoc = "0";
	}
    }

}
