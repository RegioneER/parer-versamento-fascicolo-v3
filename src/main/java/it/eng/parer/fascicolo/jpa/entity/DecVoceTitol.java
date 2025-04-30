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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

/**
 * The persistent class for the DEC_VOCE_TITOL database table.
 *
 */
@Entity
@Table(name = "DEC_VOCE_TITOL")
public class DecVoceTitol implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long idVoceTitol;
    private String cdCompositoVoceTitol;
    private String cdVoceTitol;
    private LocalDateTime dtIstituz;
    private LocalDateTime dtSoppres;
    private BigDecimal niFascic;
    private BigDecimal niFascicVociFiglie;
    private BigDecimal niOrdVoceTitol;
    private List<DecValVoceTitol> decValVoceTitols = new ArrayList<>();
    private DecLivelloTitol decLivelloTitol;
    private DecTitol decTitol;
    private DecVoceTitol decVoceTitol;
    private List<DecVoceTitol> decVoceTitols = new ArrayList<>();

    public DecVoceTitol() {
        // hibernate constructor
    }

    @Id
    @GenericGenerator(name = "DEC_VOCE_TITOL_IDVOCETITOL_GENERATOR", type = NonMonotonicSequenceGenerator.class, parameters = {
            @Parameter(name = SequenceStyleGenerator.SEQUENCE_PARAM, value = "SDEC_VOCE_TITOL"),
            @Parameter(name = OptimizableGenerator.INCREMENT_PARAM, value = "1") })
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEC_VOCE_TITOL_IDVOCETITOL_GENERATOR")
    @Column(name = "ID_VOCE_TITOL")
    public Long getIdVoceTitol() {
        return this.idVoceTitol;
    }

    public void setIdVoceTitol(Long idVoceTitol) {
        this.idVoceTitol = idVoceTitol;
    }

    @Column(name = "CD_COMPOSITO_VOCE_TITOL")
    public String getCdCompositoVoceTitol() {
        return this.cdCompositoVoceTitol;
    }

    public void setCdCompositoVoceTitol(String cdCompositoVoceTitol) {
        this.cdCompositoVoceTitol = cdCompositoVoceTitol;
    }

    @Column(name = "CD_VOCE_TITOL")
    public String getCdVoceTitol() {
        return this.cdVoceTitol;
    }

    public void setCdVoceTitol(String cdVoceTitol) {
        this.cdVoceTitol = cdVoceTitol;
    }

    @Column(name = "DT_ISTITUZ")
    public LocalDateTime getDtIstituz() {
        return this.dtIstituz;
    }

    public void setDtIstituz(LocalDateTime dtIstituz) {
        this.dtIstituz = dtIstituz;
    }

    @Column(name = "DT_SOPPRES")
    public LocalDateTime getDtSoppres() {
        return this.dtSoppres;
    }

    public void setDtSoppres(LocalDateTime dtSoppres) {
        this.dtSoppres = dtSoppres;
    }

    @Column(name = "NI_FASCIC")
    public BigDecimal getNiFascic() {
        return this.niFascic;
    }

    public void setNiFascic(BigDecimal niFascic) {
        this.niFascic = niFascic;
    }

    @Column(name = "NI_FASCIC_VOCI_FIGLIE")
    public BigDecimal getNiFascicVociFiglie() {
        return this.niFascicVociFiglie;
    }

    public void setNiFascicVociFiglie(BigDecimal niFascicVociFiglie) {
        this.niFascicVociFiglie = niFascicVociFiglie;
    }

    @Column(name = "NI_ORD_VOCE_TITOL")
    public BigDecimal getNiOrdVoceTitol() {
        return this.niOrdVoceTitol;
    }

    public void setNiOrdVoceTitol(BigDecimal niOrdVoceTitol) {
        this.niOrdVoceTitol = niOrdVoceTitol;
    }

    // bi-directional many-to-one association to DecValVoceTitol
    @OneToMany(mappedBy = "decVoceTitol", cascade = { CascadeType.PERSIST, CascadeType.REMOVE })
    public List<DecValVoceTitol> getDecValVoceTitols() {
        return this.decValVoceTitols;
    }

    public void setDecValVoceTitols(List<DecValVoceTitol> decValVoceTitols) {
        this.decValVoceTitols = decValVoceTitols;
    }

    public DecValVoceTitol addDecValVoceTitol(DecValVoceTitol decValVoceTitol) {
        getDecValVoceTitols().add(decValVoceTitol);
        decValVoceTitol.setDecVoceTitol(this);

        return decValVoceTitol;
    }

    public DecValVoceTitol removeDecValVoceTitol(DecValVoceTitol decValVoceTitol) {
        getDecValVoceTitols().remove(decValVoceTitol);
        decValVoceTitol.setDecVoceTitol(null);

        return decValVoceTitol;
    }

    // bi-directional many-to-one association to DecLivelloTitol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_LIVELLO_TITOL")
    public DecLivelloTitol getDecLivelloTitol() {
        return this.decLivelloTitol;
    }

    public void setDecLivelloTitol(DecLivelloTitol decLivelloTitol) {
        this.decLivelloTitol = decLivelloTitol;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ID_VOCE_TITOL_PADRE")
    public DecVoceTitol getDecVoceTitol() {
        return this.decVoceTitol;
    }

    public void setDecVoceTitol(DecVoceTitol decVoceTitol) {
        this.decVoceTitol = decVoceTitol;
    }

    // bi-directional many-to-one association to DecVoceTitol
    @OneToMany(mappedBy = "decVoceTitol")
    public List<DecVoceTitol> getDecVoceTitols() {
        return this.decVoceTitols;
    }

    public void setDecVoceTitols(List<DecVoceTitol> decVoceTitols) {
        this.decVoceTitols = decVoceTitols;
    }

    public DecVoceTitol addDecVoceTitol(DecVoceTitol decVoceTitol) {
        getDecVoceTitols().add(decVoceTitol);
        decVoceTitol.setDecVoceTitol(this);

        return decVoceTitol;
    }

    public DecVoceTitol removeDecVoceTitol(DecVoceTitol decVoceTitol) {
        getDecVoceTitols().remove(decVoceTitol);
        decVoceTitol.setDecVoceTitol(null);

        return decVoceTitol;
    }

}