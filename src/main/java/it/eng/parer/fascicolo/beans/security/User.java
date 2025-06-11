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

package it.eng.parer.fascicolo.beans.security;

import java.math.BigDecimal;

import it.eng.parer.fascicolo.beans.security.profile.Profile;

public class User extends BaseUser<Profile> {

    private static final long serialVersionUID = 1L;
    private BigDecimal idOrganizzazioneFoglia;
    private String nomeStruttura;
    private String sistemaVersante;
    private Long idApplicazione;
    private boolean utenteDaAssociare;
    private String codiceFiscale;

    public User() {
	super();
    }

    public BigDecimal getIdOrganizzazioneFoglia() {
	return idOrganizzazioneFoglia;
    }

    public void setIdOrganizzazioneFoglia(BigDecimal idOrganizzazioneFoglia) {
	this.idOrganizzazioneFoglia = idOrganizzazioneFoglia;
    }

    public String getNomeStruttura() {
	return nomeStruttura;
    }

    public void setNomeStruttura(String nomeStruttura) {
	this.nomeStruttura = nomeStruttura;
    }

    public Long getIdApplicazione() {
	return idApplicazione;
    }

    public void setIdApplicazione(Long idApplicazione) {
	this.idApplicazione = idApplicazione;
    }

    public boolean isUtenteDaAssociare() {
	return utenteDaAssociare;
    }

    public void setUtenteDaAssociare(boolean utenteDaAssociare) {
	this.utenteDaAssociare = utenteDaAssociare;
    }

    public String getCodiceFiscale() {
	return codiceFiscale;
    }

    public void setCodiceFiscale(String codiceFiscale) {
	this.codiceFiscale = codiceFiscale;
    }

    public String getSistemaVersante() {
	return sistemaVersante;
    }

    public void setSistemaVersante(String sistemaVersante) {
	this.sistemaVersante = sistemaVersante;
    }

}
