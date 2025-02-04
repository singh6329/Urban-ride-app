package com.application.urbanRide.services.impl;

import com.application.urbanRide.services.DistanceService;
import lombok.Data;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
public class DistanceServiceOSRMImpl implements DistanceService {

    private final String BASE_URL = "http://router.project-osrm.org/route/v1/driving/";

    @Override
    public Double calculateDistance(Point src, Point dest) {
        try {
            String uri = ""+src.getX()+","+src.getY()+";"+dest.getX()+","+dest.getY();
            OSRMResponseData osrmResponseData = RestClient
                    .builder()
                    .baseUrl(BASE_URL)
                    .build()
                    .get()
                    .uri(uri)
                    .retrieve()
                    .body(OSRMResponseData.class);
            return osrmResponseData.routes.get(0).getDistance() / 1000.0;
        }
        catch (Exception e)
        {
            throw new RuntimeException("Error in processing OSRM request!"+e.getMessage());
        }
    }
}

@Data
class OSRMResponseData
{
    List<OSRMRoutes> routes;
}

@Data
class OSRMRoutes
{
    private Double distance;
}
