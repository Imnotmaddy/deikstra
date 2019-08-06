package com.itechart.alifanov.deikstra.service;

import com.itechart.alifanov.deikstra.dto.RouteDto;
import com.itechart.alifanov.deikstra.model.Route;
import com.itechart.alifanov.deikstra.repository.RouteRepository;
import com.itechart.alifanov.deikstra.service.mapper.RouteMapper;
import com.itechart.alifanov.deikstra.service.search.PathFinder;
import com.itechart.alifanov.deikstra.service.search.PathFinderException;
import com.itechart.alifanov.deikstra.service.search.searchImpl.Node;
import com.itechart.alifanov.deikstra.service.search.searchImpl.SearchResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OptimisticLockException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class RouteService implements DeikstraService {


    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    private final PathFinder pathFinder;

    /**
     * Method transforms routeDto into Route object, checks if any Route with
     * cityA and cityB already exists in dataBase. If so it overrides existing data.
     * Otherwise it saves new Route entity;
     *
     * @param routeDto - instance for saving
     * @return returns dto of saved object
     */
    @Transactional
    public RouteDto save(final RouteDto routeDto) throws OptimisticLockException {
        final Route newRoute = routeMapper.toEntity(routeDto);
        Route oldRoute = ifPresent(newRoute);
        if (oldRoute == null) {
            routeRepository.save(newRoute);
            return routeMapper.toDto(newRoute);
        } else {
            oldRoute.setDistance(newRoute.getDistance());
            routeRepository.save(oldRoute);
            return routeMapper.toDto(oldRoute);
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
     * @return - if path exists method returns list of objects, where each object
     * contains a route between starting point and destination represented
     * as List of Strings and overall path distance as Double value;
     * @throws PathFinderException thrown if no paths between cities were found
     */
    public List<SearchResultDto> calculateAllRoutes(String fromCity, String toCity) throws PathFinderException {
        final List<Route> routes = this.findAllStoredRoutes();
        final Node startingNode = buildNodes(routes).getOrDefault(fromCity, null);
        return pathFinder.findAllPaths(startingNode, toCity);
    }

    /**
     * method checks if there is a route from cityA to cityB
     * or from cityB to cityA
     *
     * @param route - route to be checked
     * @return - returns null if no such route was found. otherwise returns found instance;
     */
    private Route ifPresent(Route route) {
        Route existingRoute = routeRepository.findByCityAAndCityB(route.getCityA(), route.getCityB());
        if (existingRoute == null) {
            return routeRepository.findByCityAAndCityB(route.getCityB(), route.getCityA());
        }
        return existingRoute;
    }

    /**
     * Method transforms routes into Nodes and builds neighbours for each Node.
     *
     * @param routes - routes to be transformed into Nodes
     * @return - returns map where key is name of the node, stored in value of that entry.
     */
    public Map<String, Node> buildNodes(List<Route> routes) {
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
