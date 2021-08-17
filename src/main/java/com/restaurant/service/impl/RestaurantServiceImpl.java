package com.restaurant.service.impl;

import com.restaurant.controller.request.RestaurantRequest;
import com.restaurant.data.entity.RestaurantEntity;
import com.restaurant.data.entity.RestaurantTableEntity;
import com.restaurant.data.repository.RestaurantRepository;
import com.restaurant.data.repository.RestaurantTableRepository;
import com.restaurant.domain.Restaurant;
import com.restaurant.service.RestaurantService;
import com.restaurant.validation.RestaurantSameNameAndLocationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final RestaurantTableRepository restaurantTableRepository;

    @Autowired
    public RestaurantServiceImpl(RestaurantRepository restaurantRepository,
                                 RestaurantTableRepository restaurantTableRepository) {
        this.restaurantRepository = restaurantRepository;
        this.restaurantTableRepository = restaurantTableRepository;
    }

    /**
     * Return all restaurants
     * @return List<Restaurant>
     */
    public List<Restaurant> getAllRestaurants() {
        List<RestaurantEntity> restaurantEntities = restaurantRepository.findAll();

        return restaurantEntities.stream()
                .map(this::convertToRestaurant)
                .collect(Collectors.toList());
    }

    /**
     * Return a restaurant by id
     * @param restaurantId
     * @return Optional<Restaurant>
     */
    public Optional<Restaurant> getRestaurantById(Long restaurantId) {
        Optional<RestaurantEntity> optionalRestaurantEntity = restaurantRepository.findById(restaurantId);

        if(optionalRestaurantEntity.isPresent()) {
            RestaurantEntity entity = optionalRestaurantEntity.get();
            return Optional.of(convertToRestaurant(entity));
        }
        return Optional.empty();
    }

    /**
     * Create a new restaurant with tables and capacities
     * @param restaurantRequest
     * @return Optional<Restaurant> created
     */
    @Override
    public Optional<Restaurant> createRestaurant(RestaurantRequest restaurantRequest)
            throws RestaurantSameNameAndLocationException {

        Optional<RestaurantEntity> optionalRestaurantEntity =
                restaurantRepository.findRestaurantEntityByRestaurantNameAndLocation(restaurantRequest.getRestaurantName(),
                        restaurantRequest.getLocation());

        if(optionalRestaurantEntity.isPresent()) {
            throw new RestaurantSameNameAndLocationException("A restaurant with the same name exists in the location");
        }

        RestaurantEntity restaurantEntity =
                RestaurantEntity.builder()
                .restaurantName(restaurantRequest.getRestaurantName())
                .location(restaurantRequest.getLocation())
                .build();

        RestaurantEntity createdRestaurantEntity = restaurantRepository.save(restaurantEntity);

        for(Long capacity: restaurantRequest.getTableCapacities()) {
            restaurantTableRepository.save(
                    RestaurantTableEntity.builder()
                            .capacity(capacity)
                            .restaurantId(createdRestaurantEntity.getRestaurantId())
                            .build());
        }

        return Optional.of(convertToRestaurant(restaurantRepository.findById(restaurantEntity.getRestaurantId()).get()));
    }

    private Restaurant convertToRestaurant(RestaurantEntity restaurantEntity) {
        Map<Long, Long> tablesWithCapacity = new HashMap<>();
        Set<RestaurantTableEntity> tableEntitySet = restaurantEntity.getTables();

        if(Objects.nonNull(tableEntitySet)) {
            restaurantEntity.getTables()
                    .stream()
                    .forEach(t -> tablesWithCapacity.put(t.getTableId(), t.getCapacity()));
        }

        return Restaurant.builder()
                .id(restaurantEntity.getRestaurantId())
                .name(restaurantEntity.getRestaurantName())
                .location(restaurantEntity.getLocation())
                .tablesWithCapacity(tablesWithCapacity)
                .build();
    }

}