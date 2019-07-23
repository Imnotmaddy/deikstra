package com.itechart.alifanov.deikstra.service.dtoTransformer;

import com.itechart.alifanov.deikstra.model.Route;
import com.itechart.alifanov.deikstra.service.dto.RouteDto;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

/**
 * This class transforms lists and single instances of RouteDto into Route and vice versa.
 */
@Component
public class RouteTransformer {
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

    /**
     * @param routes - list of Route objects for transformation
     * @return returns new List of RouteDto objects based on routes
     */
    public List<RouteDto> transformListToDto(List<Route> routes) {
        List<RouteDto> routeDtos = new LinkedList<>();
        if (routes != null) {
            for (Route route : routes) {
                routeDtos.add(transform(route));
            }
        }
        return routeDtos;
    }

    /**
     * @param routeDtos - list of RouteDto objects for transformation
     * @return returns new List of Route objects based on routeDtos
     */
    public List<Route> transformListToRoute(List<RouteDto> routeDtos) {
        List<Route> routes = new LinkedList<>();
        if (routeDtos != null) {
            for (RouteDto routeDto : routeDtos) {
                routes.add(transform(routeDto));
            }
        }
        return routes;
    }
}
