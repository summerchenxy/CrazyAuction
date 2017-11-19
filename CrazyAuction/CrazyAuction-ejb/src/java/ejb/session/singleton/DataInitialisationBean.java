/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.singleton;

import entity.AuctionListing;
import entity.CreditPackage;
import entity.Customer;
import entity.Employee;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.LocalBean;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import static util.enumeration.AccessRightEnum.ADMIN;
import static util.enumeration.AccessRightEnum.FINANCE;
import static util.enumeration.AccessRightEnum.SALES;
import util.enumeration.AuctionStatus;

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
    DateFormat format = new SimpleDateFormat("yyyy.MM.dd.HH.mm");

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
            initialiseCustomerData();
        }
        if (em.find(CreditPackage.class, 1l) == null) {
            initialiseCreditPackageData();
        }
        if (em.find(AuctionListing.class, 1l) == null) {
            initialiseAuctionListingData();
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

    private void initialiseCustomerData() {
        Customer customer = new Customer("Xiao", "Ming", "G00000001", "password", new BigDecimal(0), "xiaoming@test.com");
        em.persist(customer);
        customer = new Customer("Xiao", "Hong", "G00000002", "password", new BigDecimal(100.0), "xiaohong@test.com");
        em.persist(customer);
        customer = new Customer("Xiao", "Hua", "G00000003", "password", new BigDecimal(0), "xiaohua@test.com");
        em.persist(customer);
    }

    private void initialiseAuctionListingData() {

        AuctionListing auctionListing = null;
        try {
            auctionListing = new AuctionListing(new BigDecimal(10), format.parse("2000.01.01.00.01"),
                    format.parse("2020.01.01.00.01"), "testAL1",
                    new BigDecimal(5));
            auctionListing.setStatus(AuctionStatus.OPENED);
            em.persist(auctionListing);
            auctionListing = new AuctionListing(new BigDecimal(20), format.parse("2000.01.01.00.01"),
                    format.parse("2020.01.01.00.01"), "testAL2",
                    new BigDecimal(10));
            auctionListing.setStatus(AuctionStatus.OPENED);
            em.persist(auctionListing);
            auctionListing = new AuctionListing(new BigDecimal(50), format.parse("2000.01.01.00.01"),
                    format.parse("2020.01.01.00.01"), "testAL3",
                    new BigDecimal(0));
            auctionListing.setStatus(AuctionStatus.OPENED);
            em.persist(auctionListing);
        } catch (ParseException ex) {
        }
    }

    private void initialiseCreditPackageData() {
        CreditPackage creditPackage = new CreditPackage(new BigDecimal(10), new BigDecimal(10));
        em.persist(creditPackage);
        creditPackage = new CreditPackage(new BigDecimal(5), new BigDecimal(5));
        em.persist(creditPackage);

    }

}
