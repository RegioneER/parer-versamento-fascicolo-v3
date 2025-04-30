package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;

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
 * The persistent class for the IAM_IND_IP_USER database table.
 *
 */
@Entity
@Table(name = "IAM_IND_IP_USER")
@NamedQuery(name = "IamIndIpUser.deleteByIdUser", query = "DELETE FROM IamIndIpUser i where i.iamUser = :iamUser")
public class IamIndIpUser implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idIndIpUser;
    private String cdIndIpUser;
    private IamUser iamUser;

    public IamIndIpUser() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "IAM_IND_IP_USER_IDINDIPUSER_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SIAM_IND_IP_USER"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "IAM_IND_IP_USER_IDINDIPUSER_GENERATOR")
    @Column(name = "ID_IND_IP_USER")
    public Long getIdIndIpUser() {
        return this.idIndIpUser;
    }

    public void setIdIndIpUser(Long idIndIpUser) {
        this.idIndIpUser = idIndIpUser;
    }

    @Column(name = "CD_IND_IP_USER")
    public String getCdIndIpUser() {
        return this.cdIndIpUser;
    }

    public void setCdIndIpUser(String cdIndIpUser) {
        this.cdIndIpUser = cdIndIpUser;
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

}
