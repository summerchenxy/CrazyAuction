/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListing;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author alex_zy
 */
@Stateless
@Local(AuctionListingControllerLocal.class)
@Remote(AuctionListingControllerRemote.class)
public class AuctionListingController implements AuctionListingControllerRemote, AuctionListingControllerLocal {

    @PersistenceContext(unitName = "CrazyAuction-ejbPU")
    private EntityManager em;

    public AuctionListing createNewAuctionListing(AuctionListing auctionListing) {
        em.persist(auctionListing);
        em.flush();
        em.refresh(auctionListing);
        return auctionListing;
    }

    
}
