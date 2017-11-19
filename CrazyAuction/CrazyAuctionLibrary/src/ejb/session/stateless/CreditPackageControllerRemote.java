/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditPackage;
import entity.CreditTransaction;
import java.util.List;
import util.exception.CreditPackageNotFoundException;


/**
 *
 * @author Summer
 */
public interface CreditPackageControllerRemote {
    
    CreditPackage createCreditPackage(CreditPackage creditPackage);

    void updateCreditPackage(CreditPackage creditPackage);

    void deleteCreditPackage(CreditPackage creditPackage);

    List<CreditPackage> retrieveAllCreditPackages();
    
    CreditPackage retrieveCreditPackageByCreditPackageId(Long creditPackageId) throws CreditPackageNotFoundException;

    void disableCreditPackage(CreditPackage creditPackage);
    
    int getTransactionsNum(CreditPackage creditPackage);
    
    List<CreditTransaction> getTransactions(CreditPackage creditPackage);
}
