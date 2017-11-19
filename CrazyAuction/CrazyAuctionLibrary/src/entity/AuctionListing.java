/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import util.enumeration.AuctionStatus;
import static util.enumeration.AuctionStatus.CLOSED;

/**
 *
 * @author alex_zy
 */
@Entity
public class AuctionListing implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
     Long auctionListingId;
    @Column(precision = 11, scale = 2)
    private BigDecimal startingBidAmount;
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
    private BigDecimal winningBidValue;
    @Column(nullable = false)
    private Boolean enabled;//false if it is disabled
    @Column(nullable = true)
    private Boolean isFinal; //true if is manually intervened 

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
        this.bidList = new ArrayList<>();
        this.status = AuctionStatus.CLOSED;//by default close until start date is reached
        this.enabled = true;
        this.isFinal = false;
    }

    public AuctionListing(BigDecimal startingBidAmount, Date startDateTime, Date endDateTime, String description, BigDecimal reservePrice) {
        this();
        this.startingBidAmount = startingBidAmount;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
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

    public BigDecimal getStartingBidAmount() {
        return startingBidAmount;
    }

    public void setStartingBidAmount(BigDecimal startingBidAmount) {
        this.startingBidAmount = startingBidAmount;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
        if (reservePrice == null) {
            System.out.print("No reserve price!");
        }
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
        if (winningBid == null) {
            System.out.print("No winning bid!");
        }
        return winningBid;
    }

    /**
     * @param winningBid the winningBid to set
     */
    public void setWinningBid(Bid winningBid) {
        if (isFinal = false){
            this.winningBid = winningBid;
        }
    }

    public final void setWinningBidManually(Bid winningBid) {
        setWinningBid(winningBid);
        this.isFinal = true;
    }

    public void setWinningBidValue(BigDecimal winningBidValue) {
        if (isFinal = false)
        {
            this.winningBidValue = winningBidValue;//only can be changedd if it is not final
        }
    }

    public final void setWinningBidValueManually(BigDecimal winningBidValue) {
        setWinningBidValue(winningBidValue);
        this.isFinal = true;
    }

    public BigDecimal getWinningBidValue() {
        if (winningBidValue == null) {
            System.out.print("No winning bid!");
        }
        return winningBidValue;
    }

    /**
     * @return the bidList
     */
    public List<Bid> getBidList() {
        if (bidList.isEmpty()) {
            System.out.print("Empty Bid List!");
        }
        return bidList;
    }

    /**
     * @param bidList the bidList to set
     */
    public void setBidList(List<Bid> bidList) {
        this.bidList = bidList;
    }

    public Boolean getIsFinal() {
        return isFinal;
    }

    public void setIsFinal(Boolean isFinal) {
        this.isFinal = isFinal;
    }
    
}
