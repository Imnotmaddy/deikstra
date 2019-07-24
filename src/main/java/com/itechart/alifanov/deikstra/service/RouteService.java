package com.itechart.alifanov.deikstra.service;

import com.itechart.alifanov.deikstra.model.Route;
import com.itechart.alifanov.deikstra.repository.RouteRepository;
import com.itechart.alifanov.deikstra.service.dto.RouteDto;
import com.itechart.alifanov.deikstra.service.dtoTransformer.RouteTransformer;
import com.itechart.alifanov.deikstra.service.search.PathFinder;
import com.itechart.alifanov.deikstra.service.search.PathFinderException;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RouteService {


    private final RouteRepository routeRepository;
    private final RouteTransformer routeTransformer;
    private final PathFinder pathFinder;

    /**
     * Method transforms dto into Route object, checks if any Route with
     * both cities already exists in dataBase. If so it overrides existing data.
     * Otherwise it saves new Route entity;
     *
     * @param routeDto - instance for saving
     * @return returns dto of just saved object
     */
    @Transactional
    public RouteDto save(final RouteDto routeDto) throws OptimisticLockException {
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

    /**
     * @return returns all stored in DB routes
     */
    public List<Route> findAllStoredRoutes() {
        return routeRepository.findAll();
    }

    /**
     * Method finds shortest path between two cities if it exists.
     *
     * @param fromCity - starting city
     * @param toCity   - destination city
     * @return - returns null if no paths were found. If path exists method
     * returns pair of List<String> and Double, where list value is the shortest path and double value is the
     * overall distance.
     */
    public Pair<List<String>, Double> calculateShortestRoute(String fromCity, String toCity) {
        final List<Route> routes = this.findAllStoredRoutes();
        final Map<String, Map<String, Double>> routeMap = buildMatrix(routes);
        return pathFinder.findShortestPath(routeMap, fromCity, toCity);
    }

    /**
     * @param fromCity - starting city
     * @param toCity   - destination city
     * @return - returns null if no paths were found. If path exists method returns
     * list of pairs, where each pair is a route between starting point and destination represented
     * as List<String> and overall path distance as Double value;
     */
    public List<Pair<List<String>, Double>> calculateAllRoutes(String fromCity, String toCity) throws PathFinderException {
        final List<Route> routes = this.findAllStoredRoutes();
        final Map<String, Map<String, Double>> routeMap = buildMatrix(routes);
        return pathFinder.findAllPaths(routeMap, fromCity, toCity);
    }

    /**
     * method checks if route with its cities already exists in dataBase;
     *
     * @param route -
     * @return - returns null if no such route was found. otherwise returns found instance;
     */
    private Route ifPresent(Route route) {
        Route existingRoute = routeRepository.findByCityAAndCityB(route.getCityA(), route.getCityB());
        Route reverseRoute = routeRepository.findByCityAAndCityB(route.getCityB(), route.getCityA());
        if (existingRoute == null) {
            return reverseRoute;
        }
        return existingRoute;
    }

    /**
     * @param routes list of all routes
     * @return returns map, where key is a distinct city and value is
     * all neighbours of the city with distance between them
     */
    public Map<String, Map<String, Double>> buildMatrix(List<Route> routes) {
        HashMap<String, Map<String, Double>> map = new HashMap<>();
        for (Route route : routes) {
            String cityA = route.getCityA();
            String cityB = route.getCityB();
            Map<String, Double> innerMap = map.getOrDefault(cityA, new HashMap<>());
            innerMap.put(cityB, route.getDistance());
            map.put(cityA, innerMap);
        }
        return map;
    }

}
