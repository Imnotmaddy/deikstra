package com.itechart.alifanov.deikstra.service;

import com.itechart.alifanov.deikstra.model.Route;
import com.itechart.alifanov.deikstra.repository.RouteRepository;
import com.itechart.alifanov.deikstra.service.dto.RouteDto;
import com.itechart.alifanov.deikstra.service.dtoTransformer.RouteTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final RouteTransformer routeTransformer;

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

    public List<RouteDto> calculateRoute(RouteDto routeDto) {
        List<Route> routes = routeTransformer.transformListToRoute(this.findAll());
        buildMatrix(routes);

        return new ArrayList<>();
    }

    private Route ifPresent(Route route) {
        Route existingRoute = routeRepository.findByCityAAndCityB(route.getCityA(), route.getCityB());
        Route reverseRoute = routeRepository.findByCityAAndCityB(route.getCityB(), route.getCityA());
        if (existingRoute == null) {
            return reverseRoute;
        }
        return existingRoute;
    }

    private Set<String> findAllDistinctCities(List<Route> routes) {
        Set<String> cities = new HashSet<>();
        for (Route route : routes) {
            cities.add(route.getCityA());
            cities.add(route.getCityB());
        }
        return cities;
    }

    private Map<String, Map<String, List<Double>>> buildMatrix(List<Route> routes) {
        int size = findAllDistinctCities(routes).size();
        Map<String, Map<String, List<Double>>> map = new LinkedHashMap<>();
        LinkedList<Map<String, List<Double>>> innerMaps = new LinkedList<>();

        LinkedList<String> cities = new LinkedList<>();
        for (Route route : routes) {
            String cityA = route.getCityA();
            String cityB = route.getCityB();

            Map<String, List<Double>> currentColumn = new LinkedHashMap<>();
            List<Double> columnValues = new LinkedList<>();


            if (!cities.contains(cityA)) {
                for (int j = 0; j < size; j++) {
                    columnValues.add(null);
                }
                cities.add(cityA);
                currentColumn.put(cityA, columnValues);
                map.put(cityA, currentColumn);
                innerMaps.add(currentColumn);
            } else {
                Map<String, List<Double>> stringListMap = innerMaps.get(cities.indexOf(cityA));
                columnValues = stringListMap.get(cityA);
                currentColumn = stringListMap;
            }

            if (!cities.contains(cityB)) {
                cities.add(cityB);
                columnValues.set(cities.indexOf(cityB), route.getDistance());
                Map<String, List<Double>> nextColumn = new LinkedHashMap<>();
                List<Double> nextColumnValues = new ArrayList<>();
                for (int j = 0; j < size; j++) {
                    nextColumnValues.add(null);
                }

                nextColumnValues.set(cities.indexOf(cityA), route.getDistance());
                nextColumn.put(cityB, nextColumnValues);
                map.put(cityB, nextColumn);
                innerMaps.add(nextColumn);
            } else {
                currentColumn.get(cityA).set(cities.indexOf(cityB), route.getDistance());
                Map<String, List<Double>> stringListMap = innerMaps.get(cities.indexOf(cityB));
                stringListMap.get(cityB).set(cities.indexOf(cityA), route.getDistance());
                int i = 1;
            }
        }
        return map;
    }


}
