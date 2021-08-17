package com.restaurant.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Builder
@Data
public class Restaurant {

    private Long id;
    private String name;
    private String location;
    private Map<Long, Long> tablesWithCapacity;

}
