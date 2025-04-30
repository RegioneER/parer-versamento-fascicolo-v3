package it.eng.parer.lab;

import it.eng.parer.fascicolo.jpa.entity.AplParamApplic;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class AplParamApplicTestDao implements IAplParamApplicTestDao {

    @Inject
    EntityManager entityManager;

    @Override
    public AplParamApplic save(AplParamApplic aplParamApplic) {
        entityManager.persist(aplParamApplic);
        entityManager.flush();
        return aplParamApplic;
    }
}
