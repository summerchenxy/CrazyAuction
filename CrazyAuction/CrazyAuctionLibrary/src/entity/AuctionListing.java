/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import util.enumeration.AuctionStatus;

/**
 *
 * @author Summer
 */
public class AuctionListing {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long auctionListingId;
    @Column(nullable = false)
    private Date startDateTime;
    @Column(nullable = false)
    private Date endDateTime;
    private AuctionStatus status;
    @Column(length = 32, nullable = false)    
    private String description;    
    @Column(precision = 11, scale = 2)
    private BigDecimal reservePrice;    
    private Bid winningBid;
    
    @ManyToOne(optional = false)
    @JoinColumn(nullable = false)
    private Timer timer;
    @OneToMany(mappedBy = "auctionListing")
    private List<Bid> bidList;

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 67 * hash + Objects.hashCode(this.getAuctionListingId());
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AuctionListing other = (AuctionListing) obj;
        if (!Objects.equals(this.auctionListingId, other.auctionListingId)) {
            return false;
        }
        return true;
    }
    
    
    public AuctionListing() {
    }

    public AuctionListing(Date startDateTime, Date endDateTime, AuctionStatus status, String description, BigDecimal reservePrice) {
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
        this.status = status;
        this.description = description;
        this.reservePrice = reservePrice;
    }

    /**
     * @return the auctionListingId
     */
    public Long getAuctionListingId() {
        return auctionListingId;
    }

    /**
     * @param auctionListingId the auctionListingId to set
     */
    public void setAuctionListingId(Long auctionListingId) {
        this.auctionListingId = auctionListingId;
    }

    /**
     * @return the startDateTime
     */
    public Date getStartDateTime() {
        return startDateTime;
    }

    /**
     * @param startDateTime the startDateTime to set
     */
    public void setStartDateTime(Date startDateTime) {
        this.startDateTime = startDateTime;
    }

    /**
     * @return the endDateTime
     */
    public Date getEndDateTime() {
        return endDateTime;
    }

    /**
     * @param endDateTime the endDateTime to set
     */
    public void setEndDateTime(Date endDateTime) {
        this.endDateTime = endDateTime;
    }

    /**
     * @return the status
     */
    public AuctionStatus getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(AuctionStatus status) {
        this.status = status;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return the reservePrice
     */
    public BigDecimal getReservePrice() {
        return reservePrice;
    }

    /**
     * @param reservePrice the reservePrice to set
     */
    public void setReservePrice(BigDecimal reservePrice) {
        this.reservePrice = reservePrice;
    }

    /**
     * @return the winningBid
     */
    public Bid getWinningBid() {
        return winningBid;
    }

    /**
     * @param winningBid the winningBid to set
     */
    public void setWinningBid(Bid winningBid) {
        this.winningBid = winningBid;
    }

    /**
     * @return the timer
     */
    public Timer getTimer() {
        return timer;
    }

    /**
     * @param timer the timer to set
     */
    public void setTimer(Timer timer) {
        this.timer = timer;
    }

    /**
     * @return the bidList
     */
    public List<Bid> getBidList() {
        return bidList;
    }

    /**
     * @param bidList the bidList to set
     */
    public void setBidList(List<Bid> bidList) {
        this.bidList = bidList;
    }
    
    
}
