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
        // verifico che il numero generato non sia il precedente e/o il successivo rispetto all'insert precedente
        Assertions.assertNotEquals(entity1.getIdAmbiente(), entity2.getIdAmbiente());
        Assertions.assertNotEquals(entity1.getIdAmbiente(), entity2.getIdAmbiente() + 1);
        Assertions.assertNotEquals(entity1.getIdAmbiente(), entity2.getIdAmbiente() - 1);
    }

}