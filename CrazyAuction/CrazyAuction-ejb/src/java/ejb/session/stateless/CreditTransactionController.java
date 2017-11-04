/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditTransaction;
import javax.ejb.Stateless;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alex_zy
 */
@Stateless
@Local(CreditTransactionControllerLocal.class)
@Remote(CreditTransactionControllerRemote.class)
public class CreditTransactionController implements CreditTransactionControllerRemote, CreditTransactionControllerLocal {

    @PersistenceContext(unitName = "CrazyAuction-ejbPU")
    private EntityManager em;


    @Override
    public CreditTransaction createNewCreditTransaction(CreditTransaction ct)
    {
        em.persist(ct);
        em.flush();
        em.refresh(ct);
        return ct;
    }
}
