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

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.dto.base;

/**
 *
 * @author Fioravanti_F
 */
public class CSChiave implements java.io.Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3179459131532762427L;

    private String numero;
    private Long anno;
    private String tipoRegistro;

    public Long getAnno() {
	return anno;
    }

    public void setAnno(Long anno) {
	this.anno = anno;
    }

    public String getNumero() {
	return numero;
    }

    public void setNumero(String numero) {
	this.numero = numero;
    }

    public String getTipoRegistro() {
	return tipoRegistro;
    }

    public void setTipoRegistro(String tipoRegistro) {
	this.tipoRegistro = tipoRegistro;
    }

    @Override
    public String toString() {

	return tipoRegistro + "-" + anno + "-" + numero;

    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((anno == null) ? 0 : anno.hashCode());
	result = prime * result + ((numero == null) ? 0 : numero.hashCode());
	result = prime * result + ((tipoRegistro == null) ? 0 : tipoRegistro.hashCode());
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
	CSChiave other = (CSChiave) obj;
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
	if (tipoRegistro == null) {
	    if (other.tipoRegistro != null)
		return false;
	} else if (!tipoRegistro.equals(other.tipoRegistro))
	    return false;
	return true;
    }
}
