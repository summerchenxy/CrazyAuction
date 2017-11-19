/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackage;
import java.util.List;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import util.exception.CreditPackageNotFoundException;

/**
 *
 * @author Summer
 */
@Stateless
@Local(CreditPackageControllerLocal.class)
@Remote(CreditPackageControllerRemote.class)
public class CreditPackageController implements CreditPackageControllerRemote, CreditPackageControllerLocal {

    @PersistenceContext(unitName = "CrazyAuction-ejbPU")
    private EntityManager em;

    public CreditPackageController() {
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
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
    public void deleteCreditPackage(CreditPackage creditPackage)
    {
        System.out.println("Delete method" +creditPackage.getCreditPackageId());
        if (getTransactionsNum(creditPackage)==0){
            em.remove(creditPackage);
            em.flush();
        }
        else{//package used. mark as disabled
            disableCreditPackage(creditPackage);
        }
    }
    
    @Override
    public void disableCreditPackage(CreditPackage creditPackage)
    {
        creditPackage.setEnabled(Boolean.FALSE);
        updateCreditPackage(creditPackage);
    }
    
    @Override
    public int getTransactionsNum(CreditPackage creditPackage){
        Long creditPackageId = creditPackage.getCreditPackageId();
        Query query = em.createQuery("SELECT ct FROM CreditTransaction ct WHERE ct.creditPackage.creditPackageId = :inCreditPackageId");
        query.setParameter("inCreditPackageId", creditPackageId);
        return query.getResultList().size();
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

    public void persist(Object object) {
        em.persist(object);
    }
    
}