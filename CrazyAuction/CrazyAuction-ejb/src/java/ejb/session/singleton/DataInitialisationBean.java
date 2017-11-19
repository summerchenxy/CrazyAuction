/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.Customer;
import entity.Employee;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static util.enumeration.AccessRightEnum.ADMIN;
import static util.enumeration.AccessRightEnum.FINANCE;
import static util.enumeration.AccessRightEnum.SALES;

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
    public void postConstruct() {
        if (em.find(Employee.class, 1l) == null) {
            initialiseEmployeeData();
        }
        if (em.find(Customer.class, 1l) == null) {
            initialiseCustomereeData();
        }
    }

    private void initialiseEmployeeData() {
        Employee employee = new Employee("Bob", "Brown", "BobManager", "password", ADMIN);
        em.persist(employee);
        employee = new Employee("Alice", "White", "AliceFin", "password", FINANCE);
        em.persist(employee);
        employee = new Employee("Charles", "Woods", "CharlesSale", "password", SALES);
        em.persist(employee);

    }

    private void initialiseCustomereeData() {
        Customer customer = new Customer("Xiao", "Ming", "G00000001", "password", new BigDecimal(0), "xiaoming@test.com");
        em.persist(customer);
        customer = new Customer("Xiao", "Hong", "G00000002", "password", new BigDecimal(0), "xiaohong@test.com");
        em.persist(customer);
        customer = new Customer("Xiao", "Hua", "G00000003", "password", new BigDecimal(0), "xiaohua@test.com");
        em.persist(customer);
    }
}
