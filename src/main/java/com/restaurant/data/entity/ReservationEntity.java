package com.restaurant.data.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.sql.Date;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "RESERVATION")
public class ReservationEntity {

    @Id
    @Column(name="RESERVATION_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long reservationId;

    @Column(name="RES_DATE")
    private Date reservationDate;

    @Column(name="PARTY_SIZE")
    private long partySize;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "RESTAURANT_ID", nullable = false)
    private RestaurantEntity restaurantEntity;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private CustomerEntity customerEntity;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "RESTAURANT_TABLE_ID", nullable = false)
    private RestaurantTableEntity restaurantTableEntity;

}
