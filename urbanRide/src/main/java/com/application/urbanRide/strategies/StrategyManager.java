package com.application.urbanRide.strategies;

import com.application.urbanRide.strategies.impl.DriverMatchingHighestRatedDriverStrategy;
import com.application.urbanRide.strategies.impl.DriverMatchingNearestDriverStrategy;
import com.application.urbanRide.strategies.impl.RideFareDefaultFareCalculationStrategy;
import com.application.urbanRide.strategies.impl.RideFareSurgePricingFareCalculationStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalTime;

@Component
@RequiredArgsConstructor
public class StrategyManager {
    private final DriverMatchingHighestRatedDriverStrategy driverMatchingHighestRatedDriverStrategy;
    private final DriverMatchingNearestDriverStrategy driverMatchingNearestDriverStrategy;
    private final RideFareDefaultFareCalculationStrategy rideFareDefaultFareCalculationStrategy;
    private final RideFareSurgePricingFareCalculationStrategy rideFareSurgePricingFareCalculationStrategy;

    public DriverMatchingStrategy getDriverMatchingStrategy(Double riderRating)
    {
        if(riderRating >4)
            return driverMatchingHighestRatedDriverStrategy;
        else
            return driverMatchingNearestDriverStrategy;
    }

    public RideFareCalculationStrategy getRideFareCalculationStrategy()
    {
        LocalTime currentTime = LocalTime.now();
        boolean surgeTime = currentTime.isAfter(LocalTime.of(18,0)) && currentTime.isBefore(LocalTime.of(23,0));
        if(surgeTime)
            return rideFareSurgePricingFareCalculationStrategy;
        else
            return rideFareDefaultFareCalculationStrategy;
    }
}
