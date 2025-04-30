package it.eng.parer.lab;

import it.eng.parer.fascicolo.jpa.entity.AplValoreParamApplic;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

@ApplicationScoped
public class AplValoreParamApplicTestDao implements IAplValoreParamApplicTestDao {

    @Inject
    EntityManager entityManager;

    @Override
    public void save(AplValoreParamApplic aplValoreParamApplic) {
        entityManager.persist(aplValoreParamApplic);
        entityManager.flush();
    }
}
