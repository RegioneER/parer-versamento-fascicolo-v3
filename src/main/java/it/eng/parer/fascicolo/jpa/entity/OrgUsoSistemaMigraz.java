package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequenceGenerator;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

/**
 * The persistent class for the ORG_USO_SISTEMA_MIGRAZ database table.
 *
 */
@Entity
@Cacheable(true)
@Table(name = "ORG_USO_SISTEMA_MIGRAZ")
public class OrgUsoSistemaMigraz implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idUsoSistemaMigraz;
    private AplSistemaMigraz aplSistemaMigraz;
    private OrgStrut orgStrut;

    public OrgUsoSistemaMigraz() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "ORG_USO_SISTEMA_MIGRAZ_IDUSOSISTEMAMIGRAZ_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SORG_USO_SISTEMA_MIGRAZ"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ORG_USO_SISTEMA_MIGRAZ_IDUSOSISTEMAMIGRAZ_GENERATOR")
    @Column(name = "ID_USO_SISTEMA_MIGRAZ")
    public Long getIdUsoSistemaMigraz() {
        return this.idUsoSistemaMigraz;
    }

    public void setIdUsoSistemaMigraz(Long idUsoSistemaMigraz) {
        this.idUsoSistemaMigraz = idUsoSistemaMigraz;
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

    // bi-directional many-to-one association to OrgStrut
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_STRUT")
    public OrgStrut getOrgStrut() {
        return this.orgStrut;
    }

    public void setOrgStrut(OrgStrut orgStrut) {
        this.orgStrut = orgStrut;
    }

}