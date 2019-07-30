package com.itechart.alifanov.deikstra.service;

import com.itechart.alifanov.deikstra.dto.RouteDto;
import com.itechart.alifanov.deikstra.model.Route;
import com.itechart.alifanov.deikstra.repository.RouteRepository;
import com.itechart.alifanov.deikstra.service.mapper.RouteMapper;
import com.itechart.alifanov.deikstra.service.search.PathFinder;
import com.itechart.alifanov.deikstra.service.search.PathFinderException;
import com.itechart.alifanov.deikstra.service.search.searchImpl.Node;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RouteService {


    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
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
        final Route newRoute = routeMapper.transform(routeDto);
        Route oldRoute = ifPresent(newRoute);
        if (oldRoute == null) {
            routeRepository.save(newRoute);
            return routeMapper.transform(newRoute);
        } else {
            oldRoute.setDistance(newRoute.getDistance());
            routeRepository.save(oldRoute);
            return routeMapper.transform(oldRoute);
        }
    }

    /**
     * @return returns all stored in DB routes
     */
    private List<Route> findAllStoredRoutes() {
        return routeRepository.findAll();
    }

    /**
     * @param fromCity - starting city
     * @param toCity   - destination city
     * @return - throws exception if no paths were found. If path exists method returns
     * list of pairs, where each pair is a route between starting point and destination represented
     * as List<String> and overall path distance as Double value;
     */
    public List<Pair<List<String>, Double>> calculateAllRoutes(String fromCity, String toCity) throws PathFinderException {
        final List<Route> routes = this.findAllStoredRoutes();
        return pathFinder.findAllPaths(getStartingNode(routes, fromCity), toCity);
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

    public Node getStartingNode(List<Route> routes, String fromCity) {
        return buildNodes(routes).getOrDefault(fromCity, null);
    }

    private Map<String, Node> buildNodes(List<Route> routes) {
        if (routes == null || routes.isEmpty())
            return new HashMap<>();

        Map<String, Node> createdNodes = new HashMap<>();

        for (Route route : routes) {
            String cityA = route.getCityA();
            String cityB = route.getCityB();

            Node currentNode = createdNodes.getOrDefault(cityA, new Node(cityA, new HashMap<>()));
            Node neighbour = createdNodes.getOrDefault(cityB, new Node(cityB, new HashMap<>()));

            if (currentNode.getNeighbours().isEmpty()) {
                Map<Node, Double> neighbours = new HashMap<>();
                neighbours.put(neighbour, route.getDistance());
                currentNode.setNeighbours(neighbours);
            } else {
                currentNode.getNeighbours().put(neighbour, route.getDistance());
            }
            createdNodes.put(cityA, currentNode);
            createdNodes.put(cityB, neighbour);
        }

        return createdNodes;
    }

}
