package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequenceGenerator;
import jakarta.persistence.CascadeType;
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
 * The persistent class for the IAM_ABIL_ORGANIZ database table.
 *
 */
@Entity
@Table(name = "IAM_ABIL_ORGANIZ")
@NamedQuery(name = "IamAbilOrganiz.deleteByIdUser", query = "DELETE FROM IamAbilOrganiz i where i.iamUser = :iamUser")
public class IamAbilOrganiz implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idAbilOrganiz;
    private String flOrganizDefault;
    private BigDecimal idOrganizApplic;
    private IamUser iamUser;
    private List<IamAbilTipoDato> iamAbilTipoDatos = new ArrayList<>();
    private List<IamAutorServ> iamAutorServs = new ArrayList<>();

    public IamAbilOrganiz() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "IAM_ABIL_ORGANIZ_IDABILORGANIZ_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SIAM_ABIL_ORGANIZ"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IAM_ABIL_ORGANIZ_IDABILORGANIZ_GENERATOR")
    @Column(name = "ID_ABIL_ORGANIZ")
    public Long getIdAbilOrganiz() {
        return this.idAbilOrganiz;
    }

    public void setIdAbilOrganiz(Long idAbilOrganiz) {
        this.idAbilOrganiz = idAbilOrganiz;
    }

    @Column(name = "FL_ORGANIZ_DEFAULT", columnDefinition = "CHAR")
    public String getFlOrganizDefault() {
        return this.flOrganizDefault;
    }

    public void setFlOrganizDefault(String flOrganizDefault) {
        this.flOrganizDefault = flOrganizDefault;
    }

    @Column(name = "ID_ORGANIZ_APPLIC")
    public BigDecimal getIdOrganizApplic() {
        return this.idOrganizApplic;
    }

    public void setIdOrganizApplic(BigDecimal idOrganizApplic) {
        this.idOrganizApplic = idOrganizApplic;
    }

    // bi-directional many-to-one association to IamUser
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_USER_IAM")
    public IamUser getIamUser() {
        return this.iamUser;
    }

    public void setIamUser(IamUser iamUser) {
        this.iamUser = iamUser;
    }

    // bi-directional many-to-one association to IamAbilTipoDato
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.REMOVE }, mappedBy = "iamAbilOrganiz")
    public List<IamAbilTipoDato> getIamAbilTipoDatos() {
        return this.iamAbilTipoDatos;
    }

    public void setIamAbilTipoDatos(List<IamAbilTipoDato> iamAbilTipoDatos) {
        this.iamAbilTipoDatos = iamAbilTipoDatos;
    }

    // bi-directional many-to-one association to IamAutorServ
    @OneToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.REMOVE }, mappedBy = "iamAbilOrganiz")
    public List<IamAutorServ> getIamAutorServs() {
        return this.iamAutorServs;
    }

    public void setIamAutorServs(List<IamAutorServ> iamAutorServs) {
        this.iamAutorServs = iamAutorServs;
    }

}
