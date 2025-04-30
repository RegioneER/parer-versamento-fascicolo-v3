/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto.base;

import io.smallrye.common.constraint.Assert;

/**
 *
 * @author Fioravanti_F
 */
public class CSChiaveFasc implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 5071476901822325194L;

    private String numero;
    private Integer anno;

    public Integer getAnno() {
        return anno;
    }

    public void setAnno(Integer anno) {
        Assert.assertNotNull(anno);
        this.anno = anno;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        Assert.assertNotNull(numero);
        this.numero = numero;
    }

    @Override
    public String toString() {

        return anno + "-" + numero;

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((anno == null) ? 0 : anno.hashCode());
        result = prime * result + ((numero == null) ? 0 : numero.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        CSChiaveFasc other = (CSChiaveFasc) obj;
        if (anno == null) {
            if (other.anno != null)
                return false;
        } else if (!anno.equals(other.anno))
            return false;
        if (numero == null) {
            if (other.numero != null)
                return false;
        } else if (!numero.equals(other.numero))
            return false;
        return true;
    }

}
