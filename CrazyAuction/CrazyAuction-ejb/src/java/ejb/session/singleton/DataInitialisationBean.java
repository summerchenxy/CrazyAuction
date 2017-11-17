/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.Employee;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static util.enumeration.AccessRightEnum.ADMIN;

/**
 *
 * @author Summer
 */
@Singleton
@LocalBean
@Startup
public class DataInitialisationBean {

    @PersistenceContext(unitName = "CrazyAuction-ejbPU")
    private EntityManager em;

    public void persist(Object object) {
        em.persist(object);
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    public DataInitialisationBean() {
    }
    @PostConstruct
    public void postConstruct()
    {
        if(em.find(Employee.class, 1l) == null)
        {
            initialiseData();
        }
    }
    
    private void initialiseData()
    {
        Employee employee = new Employee("Bob", "Brown", "BobManager", "password", ADMIN);
        em.persist(employee);
        
    }
}
