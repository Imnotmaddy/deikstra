package com.itechart.alifanov.deikstra.service;

import com.itechart.alifanov.deikstra.model.Route;
import com.itechart.alifanov.deikstra.repository.RouteRepository;
import com.itechart.alifanov.deikstra.service.dto.RouteDto;
import com.itechart.alifanov.deikstra.service.dtoTransformer.RouteTransformer;
import com.itechart.alifanov.deikstra.service.search.ShortestPathFinder;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final RouteTransformer routeTransformer;
    private final ShortestPathFinder pathFinder;

    @Transactional
    public RouteDto save(final RouteDto routeDto) {
        final Route newRoute = routeTransformer.transform(routeDto);
        Route oldRoute = ifPresent(newRoute);
        if (oldRoute == null) {
            routeRepository.save(newRoute);
            return routeTransformer.transform(newRoute);
        } else {
            oldRoute.setDistance(newRoute.getDistance());
            routeRepository.save(oldRoute);
            return routeTransformer.transform(oldRoute);
        }
    }

    public List<RouteDto> findAll() {
        return routeTransformer.transformListToDto(routeRepository.findAll());
    }

    public Pair<List<String>, Double> calculateRoute(String fromCity, String toCity) {
        final List<Route> routes = routeTransformer.transformListToRoute(this.findAll());
        final Map<String, Map<String, Double>> routeMap = buildMatrix(routes);
        return pathFinder.findShortestPath(routeMap, fromCity, toCity);
    }

    private Route ifPresent(Route route) {
        Route existingRoute = routeRepository.findByCityAAndCityB(route.getCityA(), route.getCityB());
        Route reverseRoute = routeRepository.findByCityAAndCityB(route.getCityB(), route.getCityA());
        if (existingRoute == null) {
            return reverseRoute;
        }
        return existingRoute;
    }

    private Map<String, Map<String, Double>> buildMatrix(List<Route> routes) {
        LinkedHashMap<String, Map<String, Double>> map = new LinkedHashMap<>();
        for (Route route : routes) {
            String cityA = route.getCityA();
            String cityB = route.getCityB();
            Map<String, Double> innerMap = map.getOrDefault(cityA, new LinkedHashMap<>());
            innerMap.put(cityB, route.getDistance());
            map.put(cityA, innerMap);
        }
        return map;
    }

}
