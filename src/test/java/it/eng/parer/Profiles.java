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

package it.eng.parer;

import io.quarkus.test.junit.QuarkusTestProfile;

import java.util.*;

public class Profiles {

    public static class Lab implements QuarkusTestProfile {
	@Override
	public Set<String> tags() {
	    return new HashSet<>(Arrays.asList("lab"));
	}
    }

    public static class H2 implements QuarkusTestProfile {
	@Override
	public Set<String> tags() {
	    return new HashSet<>(Arrays.asList("unit"));
	}

	@Override
	public String getConfigProfile() {
	    return "h2";
	}

	@SuppressWarnings({
		"rawtypes", "unchecked" })
	@Override
	public Map getConfigOverrides() {
	    /*
	     * da yml non si riesce a impostare quarkus.hibernate-orm.database.generation e
	     * quarkus.hibernate-orm.database.generation.create-schemas quindi devo forzarlo da qua
	     */
	    return Collections.singletonMap("quarkus.hibernate-orm.database.generation",
		    "drop-and-create");
	}
    }

    public static class EndToEnd implements QuarkusTestProfile {
	@Override
	public Set<String> tags() {
	    return new HashSet<>(Arrays.asList("e2e"));
	}

	@Override
	public String getConfigProfile() {
	    return "test";
	}
    }
}
