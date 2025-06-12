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

package it.eng.parer.fascicolo.beans.dao;

import static java.lang.Integer.MIN_VALUE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import it.eng.parer.DatabaseInit;
import it.eng.parer.Profiles;
import it.eng.parer.fascicolo.beans.IConfigurationDao;
import it.eng.parer.fascicolo.beans.exceptions.ParamApplicNotFoundException;
import it.eng.parer.fascicolo.beans.utils.ParametroApplDB;
import jakarta.inject.Inject;

@QuarkusTest
@TestProfile(Profiles.H2.class)
class ConfigurationDaoTest {

    @Inject
    IConfigurationDao dao;

    @Inject
    DatabaseInit databaseInit;

    @Test
    @TestTransaction
    void getParamApplicValue() {
	final String parametro = "NM_APPLIC";
	final String valore = "TEST";
	databaseInit.insertParametroApplicazione(parametro, valore);
	assertEquals(valore, dao.getParamApplicValue(parametro));
    }

    @Test
    @TestTransaction
    void getParamApplicValue_notFound() {
	assertThrows(ParamApplicNotFoundException.class,
		() -> dao.getParamApplicValue("NON_ESISTE"),
		"Should fail throwing ParamApplicNotFoundException");
	;
    }

    @Test
    @TestTransaction
    void getParamApplicValueTipoUD() {
	final String parametro = "NM_APPLIC";
	final String valore = "TEST";
	final long idTipoUnitaDoc = 1L;
	databaseInit.insertParametroTipoUD(parametro, valore, idTipoUnitaDoc);
	assertEquals(valore,
		dao.getParamApplicValue(parametro, MIN_VALUE, MIN_VALUE, idTipoUnitaDoc));
    }

    @Test
    @TestTransaction
    void getParamApplicValueTipoUD_notFound() {
	assertThrows(ParamApplicNotFoundException.class,
		() -> dao.getParamApplicValue("NON_ESISTE", MIN_VALUE, MIN_VALUE, 1L),
		"Should fail throwing ParamApplicNotFoundException");
	;
    }

    @Test
    @TestTransaction
    void getParamApplicValueAATipoFascicolo() {
	final String parametro = "NM_APPLIC";
	final String valore = "TEST";
	final long idAATipoFascicolo = 1L;
	databaseInit.insertParametroAATipoFascicolo(parametro, valore, idAATipoFascicolo);
	assertEquals(valore, dao.getParamApplicValue(parametro, MIN_VALUE, MIN_VALUE, MIN_VALUE,
		idAATipoFascicolo));
    }

    @Test
    @TestTransaction
    void getParamApplicValueAATipoFascicolo_notFound() {
	assertThrows(ParamApplicNotFoundException.class,
		() -> dao.getParamApplicValue("NON_ESISTE", MIN_VALUE, MIN_VALUE, MIN_VALUE, 1L),
		"Should fail throwing ParamApplicNotFoundException");
	;
    }

    @Test
    @TestTransaction
    void getValoreParamApplicByTiParamApplicAsMap() {
	final String parametro = "NM_APPLIC";
	final String valore = "TEST";
	final String tiParam = ParametroApplDB.TipoParametroAppl.IAM;
	databaseInit.insertParametroApplicazione(parametro, valore, tiParam);
	Map<String, String> map = dao
		.getValoreParamApplicByTiParamApplicAsMap(Arrays.asList(tiParam));
	assertFalse(map.isEmpty());
	assertEquals(parametro, map.keySet().iterator().next());
	assertEquals(valore, map.get(parametro));
    }

    @Test
    @TestTransaction
    void getValoreParamApplicByTiParamApplicAsMap_notFound() {
	final Map<String, String> mapResult = dao
		.getValoreParamApplicByTiParamApplicAsMap(Arrays.asList("BOH"));
	assertTrue(mapResult.isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
	    "false,0", "true,1", "stringCasuale,0" })
    @TestTransaction
    void getParamApplicValueAsFlAAFascicolo(String input, String expected) {
	final String parametro = "NM_APPLIC";
	final String valore = input;
	final long idAATipoFascicolo = 1L;
	databaseInit.insertParametroAATipoFascicolo(parametro, valore, idAATipoFascicolo);
	final String flag = dao.getParamApplicValueAsFl(parametro, MIN_VALUE, MIN_VALUE, MIN_VALUE,
		idAATipoFascicolo);
	assertEquals(expected, flag);
    }
}
