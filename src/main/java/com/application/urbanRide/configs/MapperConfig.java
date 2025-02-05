package com.application.urbanRide.configs;

import com.application.urbanRide.dtos.PointDto;
import com.application.urbanRide.utils.GeometryUtil;
import org.locationtech.jts.geom.Point;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

//    GeometryUtil geometryUtil;
    @Bean
    public ModelMapper getModelMapper()
    {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.typeMap(PointDto.class, Point.class).setConverter(mappingContext ->{
                  PointDto pointDto = mappingContext.getSource();
                  return GeometryUtil.createPoint(pointDto);
                });

        modelMapper.typeMap(Point.class, PointDto.class).setConverter(mappingContext -> {
            Point point = mappingContext.getSource();
            Double[] coordinates = {
                                    point.getX(),point.getY()
                                                        };
            return new PointDto(coordinates);
        });

        return modelMapper;
    }
}
