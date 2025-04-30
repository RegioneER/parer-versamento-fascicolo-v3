package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * The persistent class for the APL_SISTEMA_VERSANTE database table.
 *
 */
@Entity
@Immutable
@Table(schema = "SACER_IAM", name = "APL_SISTEMA_VERSANTE")
public class AplSistemaVersante implements Serializable {

    private static final long serialVersionUID = 1L;
    private Long idSistemaVersante;
    private String nmSistemaVersante;

    public AplSistemaVersante() {
        // hibernate constructor
    }

    @Id
    @Column(name = "ID_SISTEMA_VERSANTE")
    public Long getIdSistemaVersante() {
        return this.idSistemaVersante;
    }

    public void setIdSistemaVersante(Long idSistemaVersante) {
        this.idSistemaVersante = idSistemaVersante;
    }

    @Column(name = "NM_SISTEMA_VERSANTE")
    public String getNmSistemaVersante() {
        return this.nmSistemaVersante;
    }

    public void setNmSistemaVersante(String nmSistemaVersante) {
        this.nmSistemaVersante = nmSistemaVersante;
    }

}
