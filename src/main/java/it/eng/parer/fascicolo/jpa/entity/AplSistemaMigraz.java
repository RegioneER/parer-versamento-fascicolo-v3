package it.eng.parer.fascicolo.jpa.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.id.OptimizableGenerator;
import org.hibernate.id.enhanced.SequenceStyleGenerator;

import it.eng.parer.fascicolo.jpa.sequence.NonMonotonicSequenceGenerator;
import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The persistent class for the APL_SISTEMA_MIGRAZ database table.
 *
 */
@Entity
@Cacheable(true)
@Table(name = "APL_SISTEMA_MIGRAZ")
public class AplSistemaMigraz implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idSistemaMigraz;
    private String dsSistemaMigraz;
    private String nmSistemaMigraz;
    private List<OrgUsoSistemaMigraz> orgUsoSistemaMigrazs = new ArrayList<>();

    public AplSistemaMigraz() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "APL_SISTEMA_MIGRAZ_IDSISTEMAMIGRAZ_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SAPL_SISTEMA_MIGRAZ"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "APL_SISTEMA_MIGRAZ_IDSISTEMAMIGRAZ_GENERATOR")
    @Column(name = "ID_SISTEMA_MIGRAZ")
    public Long getIdSistemaMigraz() {
        return this.idSistemaMigraz;
    }

    public void setIdSistemaMigraz(Long idSistemaMigraz) {
        this.idSistemaMigraz = idSistemaMigraz;
    }

    @Column(name = "DS_SISTEMA_MIGRAZ")
    public String getDsSistemaMigraz() {
        return this.dsSistemaMigraz;
    }

    public void setDsSistemaMigraz(String dsSistemaMigraz) {
        this.dsSistemaMigraz = dsSistemaMigraz;
    }

    @Column(name = "NM_SISTEMA_MIGRAZ")
    public String getNmSistemaMigraz() {
        return this.nmSistemaMigraz;
    }

    public void setNmSistemaMigraz(String nmSistemaMigraz) {
        this.nmSistemaMigraz = nmSistemaMigraz;
    }

    // bi-directional many-to-one association to OrgUsoSistemaMigraz
    @OneToMany(mappedBy = "aplSistemaMigraz")
    public List<OrgUsoSistemaMigraz> getOrgUsoSistemaMigrazs() {
        return this.orgUsoSistemaMigrazs;
    }

    public void setOrgUsoSistemaMigrazs(List<OrgUsoSistemaMigraz> orgUsoSistemaMigrazs) {
        this.orgUsoSistemaMigrazs = orgUsoSistemaMigrazs;
    }

}