package com.itechart.alifanov.deikstra.service.dtoTransformer;

import com.itechart.alifanov.deikstra.model.Route;
import com.itechart.alifanov.deikstra.service.dto.RouteDto;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class RouteTransformer {

    public Route transform(RouteDto routeDto) {
        return Route.builder()
                .cityA(routeDto.getCityA())
                .cityB(routeDto.getCityB())
                .distance(routeDto.getDistance())
                .build();
    }

    public RouteDto transform(Route route) {
        return RouteDto.builder()
                .cityA(route.getCityA())
                .cityB(route.getCityB())
                .distance(route.getDistance())
                .build();
    }

    public List<RouteDto> transform(List<Route> cities) {
        List<RouteDto> routeDtos = new ArrayList<>();
        if (cities != null) {
            for (Route route : cities) {
                routeDtos.add(transform(route));
            }
            return routeDtos;
        }
        return routeDtos;
    }
}
