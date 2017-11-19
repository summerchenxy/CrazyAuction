/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Bid;
import java.math.BigDecimal;
import util.exception.BidNotFoundException;

public interface BidControllerRemote {

    Bid createNewBid(Bid bid);

    void refundToCustomer(Bid bid);

    Bid retrieveBidByBidId(Long bidId) throws BidNotFoundException;

    Bid retrieveBidByCreditValue(BigDecimal creditValue) throws BidNotFoundException;

    public void updateBid(Bid bid);
    public void placeBid(Bid bid);

}
