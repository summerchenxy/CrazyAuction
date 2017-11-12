/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

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
    public entity.CreditPackage createCreditPackage(entity.CreditPackage creditPackage)
    {
        em.persist(creditPackage);
        em.flush();
        em.refresh(creditPackage);
        
        
        return creditPackage;
    }
    
    
    @Override
    public void updateCreditPackage(entity.CreditPackage creditPackage)
    {
        em.merge(creditPackage);
    }
    
    
   
    @Override
    public void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException
    {
        entity.CreditPackage creditPackage = retrieveCreditPackageByCreditPackageId(creditPackageId);
        if (creditPackage.getCredit().equals(creditPackage.getCredit())){
            em.remove(creditPackage);
            em.flush();
        }
        else{//package used. mark as disabled
            creditPackage.setEnabled(Boolean.FALSE);
        }
    }
    
  
    @Override
    public List<entity.CreditPackage> retrieveAllCreditPackages()
    {
        Query query = em.createQuery("SELECT s FROM CreditPackage s");
        
        return query.getResultList();
    }

    @Override
    public entity.CreditPackage retrieveCreditPackageByCreditPackageId(Long creditPackageId) throws CreditPackageNotFoundException
    {
        entity.CreditPackage creditPackage = em.find(entity.CreditPackage.class, creditPackageId);
        
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
