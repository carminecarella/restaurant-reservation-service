package com.restaurant.data.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "RESTAURANT_TABLE ")
public class RestaurantTableEntity {

    @Id
    @Column(name="TABLE_ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long tableId;

    @Column(name="CAPACITY")
    private long capacity;

    @Column(name="RESTAURANT_ID")
    private long restaurantId;

}
