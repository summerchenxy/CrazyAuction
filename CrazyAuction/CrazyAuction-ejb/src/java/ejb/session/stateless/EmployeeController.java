/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListing;
import entity.CreditPackage;
import entity.Employee;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.NonUniqueResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import static util.enumeration.AuctionStatus.CLOSED;
import util.exception.AuctionListingNotFoundException;
import util.exception.CreditPackageNotFoundException;
import util.exception.EmployeeNotFoundException;
import util.exception.EmployeePasswordChangeException;
import util.exception.InvalidLoginCredentialException;

/**
 *
 * @author Summer
 */
@Stateless
@Local(EmployeeControllerLocal.class)
@Remote(EmployeeControllerRemote.class)
public class EmployeeController implements EmployeeControllerRemote, EmployeeControllerLocal {

    @PersistenceContext(unitName = "CrazyAuction-ejbPU")
    private EntityManager em;

    public EmployeeController(){
    }
    
    
    @Override
    public Employee employeeLogin(String username, String password) throws InvalidLoginCredentialException
    {
        try
        {
            Employee employee = retrieveEmployeeByUsername(username);
            
            if(employee.getPassword().equals(password))
            {
                return employee;
            }
            else
            {
                throw new InvalidLoginCredentialException("Invalid password!");
            }
        }
        catch(EmployeeNotFoundException ex)
        {
            throw new InvalidLoginCredentialException("Username does not exist!");
        }
    }
    
    @Override
    public void changePassword(String username, String currentPw, String newPw, String confirmationPw) throws EmployeeNotFoundException, EmployeePasswordChangeException
    {
        Employee employee = retrieveEmployeeByUsername(username);
        
        if(employee.getPassword().equals(currentPw))
        {
            if (newPw.equals(confirmationPw)){
                employee.setPassword(newPw);
            }
            else{
                throw new EmployeePasswordChangeException("Confirmation password does not match with the new password");
            }
        }
        else
        {
            throw new EmployeePasswordChangeException("Current password is invalid");
        }
    }
    
    @Override
    public Employee createNewEmployee(Employee newEmployee)
    {
        em.persist(newEmployee);
        em.flush();
        em.refresh(newEmployee);
        
        
        return newEmployee;
    }
    
    @Override
    public void updateEmployee(Employee employee)
    {
        em.merge(employee);
    }
    
    
    @Override
    public void deleteEmployee(Long employeeId) throws EmployeeNotFoundException
    {
        Employee employeeToRemove = retrieveEmployeeByEmployeeId(employeeId);
        em.remove(employeeToRemove);
        em.flush();
    }
    
    @Override
    public List<Employee> retrieveAllEmployees()
    {
        Query query = em.createQuery("SELECT s FROM Employee s");
        
        return query.getResultList();
    }
    
    @Override
    public Employee retrieveEmployeeByUsername(String username) throws EmployeeNotFoundException
    {
        Query query = em.createQuery("SELECT s FROM Employee s WHERE s.username = :inUsername");
        query.setParameter("inUsername", username);
        
        try
        {
            return (Employee)query.getSingleResult();
        }
        catch(NoResultException | NonUniqueResultException ex)
        {
            throw new EmployeeNotFoundException("Employee Username " + username + " does not exist!");
        }
    }
    
    @Override
    public Employee retrieveEmployeeByEmployeeId(Long employeeId) throws EmployeeNotFoundException
    {
        Employee employee = em.find(Employee.class, employeeId);
        
        if(employeeId != null)
        {
            return employee;
        }
        else
        {
            throw new EmployeeNotFoundException("Employee ID " + employeeId + " does not exist!");
        }
    }
    
    @Override
    public CreditPackage createCreditPackage(CreditPackage creditPackage)
    {
        em.persist(creditPackage);
        em.flush();
        em.refresh(creditPackage);
        
        
        return creditPackage;
    }
    
    
    @Override
    public void updateCreditPackage(CreditPackage creditPackage)
    {
        em.merge(creditPackage);
    }
    
    
   
    @Override
    public void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException
    {
        CreditPackage creditPackage = retrieveCreditPackageByCreditPackageId(creditPackageId);
        if (creditPackage.getInitialCredit().equals(creditPackage.getAvailableCredit())){
            em.remove(creditPackage);
            em.flush();
        }
        else{//package used. mark as disabled
            creditPackage.setEnabled(Boolean.FALSE);
        }
    }
    
  
    @Override
    public List<CreditPackage> retrieveAllCreditPackages()
    {
        Query query = em.createQuery("SELECT s FROM CreditPackage s");
        
        return query.getResultList();
    }

    @Override
    public CreditPackage retrieveCreditPackageByCreditPackageId(Long creditPackageId) throws CreditPackageNotFoundException
    {
        CreditPackage creditPackage = em.find(CreditPackage.class, creditPackageId);
        
        if(creditPackageId != null)
        {
            return creditPackage;
        }
        else
        {
            throw new CreditPackageNotFoundException("Credit Package ID " + creditPackageId + " does not exist!");
        }
    }
    
    @Override
    public AuctionListing createAuctionListing(AuctionListing auctionListing)
    {
        em.persist(auctionListing);
        em.flush();
        em.refresh(auctionListing);
        
        
        return auctionListing;
    }
    
    @Override
    public void updateAuctionListing(AuctionListing auctionListing)
    {
        em.merge(auctionListing);
    }
    
    @Override
    public void deleteAuctionListing(Long auctionListingId) throws AuctionListingNotFoundException
    {
        AuctionListing auctionListing = retrieveAuctionListingByAuctionListingId(auctionListingId);
        if (auctionListing.getBidList().isEmpty()){
            em.remove(auctionListing);
            em.flush();
        }
        else{//listing used. mark as disabled
            auctionListing.setStatus(CLOSED);
        };
    }
    
 
    @Override
    public List<AuctionListing> retrieveAllAuctionListings()
    {
        Query query = em.createQuery("SELECT s FROM AuctionListing s");
        
        return query.getResultList();
    }

    @Override
    public AuctionListing retrieveAuctionListingByAuctionListingId(Long auctionListingId) throws AuctionListingNotFoundException
    {
        AuctionListing auctionListing = em.find(AuctionListing.class, auctionListingId);
        
        if(auctionListingId != null)
        {
            return auctionListing;
        }
        else
        {
            throw new AuctionListingNotFoundException("Auction Listing ID " + auctionListingId + " does not exist!");
        }
    }
}
