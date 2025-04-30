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
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The persistent class for the DEC_LIVELLO_TITOL database table.
 *
 */
@Entity
@Table(name = "DEC_LIVELLO_TITOL")
public class DecLivelloTitol implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idLivelloTitol;
    private String cdSepLivello;
    private BigDecimal niLivello;
    private String nmLivelloTitol;
    private String tiFmtVoceTitol;
    private DecTitol decTitol;
    private List<DecVoceTitol> decVoceTitols = new ArrayList<>();

    public DecLivelloTitol() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "DEC_LIVELLO_TITOL_IDLIVELLOTITOL_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SDEC_LIVELLO_TITOL"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEC_LIVELLO_TITOL_IDLIVELLOTITOL_GENERATOR")
    @Column(name = "ID_LIVELLO_TITOL")
    public Long getIdLivelloTitol() {
        return this.idLivelloTitol;
    }

    public void setIdLivelloTitol(Long idLivelloTitol) {
        this.idLivelloTitol = idLivelloTitol;
    }

    @Column(name = "CD_SEP_LIVELLO", columnDefinition = "CHAR")
    public String getCdSepLivello() {
        return this.cdSepLivello;
    }

    public void setCdSepLivello(String cdSepLivello) {
        this.cdSepLivello = cdSepLivello;
    }

    @Column(name = "NI_LIVELLO")
    public BigDecimal getNiLivello() {
        return this.niLivello;
    }

    public void setNiLivello(BigDecimal niLivello) {
        this.niLivello = niLivello;
    }

    @Column(name = "NM_LIVELLO_TITOL")
    public String getNmLivelloTitol() {
        return this.nmLivelloTitol;
    }

    public void setNmLivelloTitol(String nmLivelloTitol) {
        this.nmLivelloTitol = nmLivelloTitol;
    }

    @Column(name = "TI_FMT_VOCE_TITOL")
    public String getTiFmtVoceTitol() {
        return this.tiFmtVoceTitol;
    }

    public void setTiFmtVoceTitol(String tiFmtVoceTitol) {
        this.tiFmtVoceTitol = tiFmtVoceTitol;
    }

    // bi-directional many-to-one association to DecTitol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_TITOL")
    public DecTitol getDecTitol() {
        return this.decTitol;
    }

    public void setDecTitol(DecTitol decTitol) {
        this.decTitol = decTitol;
    }

    // bi-directional many-to-one association to DecVoceTitol
    @OneToMany(mappedBy = "decLivelloTitol")
    public List<DecVoceTitol> getDecVoceTitols() {
        return this.decVoceTitols;
    }

    public void setDecVoceTitols(List<DecVoceTitol> decVoceTitols) {
        this.decVoceTitols = decVoceTitols;
    }

    public DecVoceTitol addDecVoceTitol(DecVoceTitol decVoceTitol) {
        getDecVoceTitols().add(decVoceTitol);
        decVoceTitol.setDecLivelloTitol(this);

        return decVoceTitol;
    }

    public DecVoceTitol removeDecVoceTitol(DecVoceTitol decVoceTitol) {
        getDecVoceTitols().remove(decVoceTitol);
        decVoceTitol.setDecLivelloTitol(null);

        return decVoceTitol;
    }

}