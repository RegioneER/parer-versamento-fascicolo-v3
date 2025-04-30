package it.eng.parer.fascicolo.jpa.grantedEntity;

import java.io.Serializable;

import org.hibernate.annotations.Immutable;

import it.eng.parer.fascicolo.jpa.entity.AplSistemaVersante;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The persistent class for the USR_USER database table.
 *
 */
@Entity
@Immutable
@Table(name = "USR_USER", schema = "SACER_IAM")
public class UsrUser implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idUserIam;
    private AplSistemaVersante aplSistemaVersante;

    public UsrUser() {
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

    // bi-directional many-to-one association to AplSistemaVersante
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_SISTEMA_VERSANTE")
    public AplSistemaVersante getAplSistemaVersante() {
        return this.aplSistemaVersante;
    }

    public void setAplSistemaVersante(AplSistemaVersante aplSistemaVersante) {
        this.aplSistemaVersante = aplSistemaVersante;
    }
}
