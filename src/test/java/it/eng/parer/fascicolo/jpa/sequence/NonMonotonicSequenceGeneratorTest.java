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

package it.eng.parer.fascicolo.jpa.sequence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import io.quarkus.test.TestTransaction;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.h2.H2DatabaseTestResource;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import it.eng.parer.Profiles;
import it.eng.parer.fascicolo.jpa.entity.OrgAmbiente;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@QuarkusTest
@QuarkusTestResource(H2DatabaseTestResource.class)
@TestProfile(Profiles.H2.class)
class NonMonotonicSequenceGeneratorTest {

    @Inject
    EntityManager entityManager;

    @Test
    @TestTransaction
    public void checkNonMonotonicSequenceValues() {
	OrgAmbiente entity1 = new OrgAmbiente();
	entityManager.persist(entity1);
	OrgAmbiente entity2 = new OrgAmbiente();
	entityManager.persist(entity2);
	Assertions.assertNotNull(entity1.getIdAmbiente());
	Assertions.assertNotNull(entity2.getIdAmbiente());
	// verifico che il numero generato non sia il precedente e/o il successivo rispetto
	// all'insert precedente
	Assertions.assertNotEquals(entity1.getIdAmbiente(), entity2.getIdAmbiente());
	Assertions.assertNotEquals(entity1.getIdAmbiente(), entity2.getIdAmbiente() + 1);
	Assertions.assertNotEquals(entity1.getIdAmbiente(), entity2.getIdAmbiente() - 1);
    }

}
