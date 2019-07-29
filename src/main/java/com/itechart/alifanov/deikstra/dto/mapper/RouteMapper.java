package com.itechart.alifanov.deikstra.dto.mapper;

import com.itechart.alifanov.deikstra.dto.RouteDto;
import com.itechart.alifanov.deikstra.model.Route;
import org.springframework.stereotype.Component;

/**
 * This class transforms lists and single instances of RouteDto into Route and vice versa.
 */
@Component
public class RouteMapper {
    /**
     * @param routeDto - Dto object for transformation
     * @return returns new Route based on routeDto insides.
     */
    public Route transform(RouteDto routeDto) {
        return Route.builder()
                .cityA(routeDto.getCityA())
                .cityB(routeDto.getCityB())
                .distance(routeDto.getDistance())
                .build();
    }

    /**
     * @param route - Route object for transformation
     * @return returns new RouteDto based on Route insides
     */
    public RouteDto transform(Route route) {
        return RouteDto.builder()
                .cityA(route.getCityA())
                .cityB(route.getCityB())
                .distance(route.getDistance())
                .build();
    }
}
