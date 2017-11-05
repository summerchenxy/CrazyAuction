/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import java.util.List;
import util.exception.CreditPackageNotFoundException;


/**
 *
 * @author Summer
 */
public interface CreditPackageControllerLocal {
    entity.CreditPackage createCreditPackage(entity.CreditPackage creditPackage);

    void updateCreditPackage(entity.CreditPackage creditPackage);

    void deleteCreditPackage(Long creditPackageId) throws CreditPackageNotFoundException;

    List<entity.CreditPackage> retrieveAllCreditPackages();
    
    entity.CreditPackage retrieveCreditPackageByCreditPackageId(Long creditPackageId) throws CreditPackageNotFoundException;

}
