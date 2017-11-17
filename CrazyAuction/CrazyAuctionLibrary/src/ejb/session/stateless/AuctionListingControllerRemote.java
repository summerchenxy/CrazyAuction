/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.AuctionListing;
import java.math.BigDecimal;
import java.util.List;
import util.exception.AuctionListingNotFoundException;

/**
 *
 * @author Summer
 */
public interface AuctionListingControllerRemote {
    
    AuctionListing createAuctionListing(AuctionListing auctionListing);

    void updateAuctionListing(AuctionListing auctionListing);

    void deleteAuctionListing(Long auctionListingId) throws AuctionListingNotFoundException;

    List<AuctionListing> retrieveAllAuctionListings();

    AuctionListing retrieveAuctionListingByAuctionListingId(Long auctionListingId) throws AuctionListingNotFoundException;
    
    List<AuctionListing> retrieveAllAuctionListingsRequiringManualIntervention();
    
    void openAuction(AuctionListing auctionListing);

    void closeAuction(AuctionListing auctionListing);
    
    
}
