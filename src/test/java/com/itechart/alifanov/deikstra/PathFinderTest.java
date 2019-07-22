package com.itechart.alifanov.deikstra;

import com.itechart.alifanov.deikstra.model.Route;
import com.itechart.alifanov.deikstra.repository.RouteRepository;
import com.itechart.alifanov.deikstra.service.RouteService;
import com.itechart.alifanov.deikstra.service.dto.RouteDto;
import com.itechart.alifanov.deikstra.service.dtoTransformer.RouteTransformer;
import com.itechart.alifanov.deikstra.service.search.PathFinder;
import com.itechart.alifanov.deikstra.service.search.searchImpl.PathFinderImpl;
import javafx.util.Pair;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeikstraApplication.class)
public class PathFinderTest {


    private RouteService routeService;

    @MockBean
    private RouteRepository routeRepository;

    private RouteTransformer routeTransformer = new RouteTransformer();


    private PathFinder pathFinder = new PathFinderImpl();

    @Before
    public void init() {
        routeService = new RouteService(routeRepository, routeTransformer, new PathFinderImpl());
    }

    @Test
    public void testFindAllStoreRoutes() {
        //Given
        List<Route> routes = new ArrayList<>();
        routes.add(new Route("Tokyo", "Moscow", (double) 2));
        routes.add(new Route("Tokyo", "Berlin", (double) 3));
        routes.add(new Route("Moscow", "Berlin", (double) 4));

        //When
        when(routeRepository.findAll()).thenReturn(routes);
        //Then
        assertThat(routeService.findAllStoredRoutes()).isSameAs(routes);
    }

    @Test
    public void testRoute_2Cities_1Connection() {
        //Given
        RouteDto routeDto = new RouteDto("Tokyo", "Polotsk", (double) 25);

        Map<String, Double> innerMap = new HashMap<>();
        innerMap.put(routeDto.getCityB(), routeDto.getDistance());

        Map<String, Map<String, Double>> outerMap = new HashMap<>();
        outerMap.put(routeDto.getCityA(), innerMap);

        List<String> expectedListResult = Arrays.asList(routeDto.getCityA(), "Polotsk");
        Pair<List<String>, Double> expectedResult = new Pair<>(expectedListResult, routeDto.getDistance());

        //When
        final List<Pair<List<String>, Double>> allPaths = pathFinder.findAllPaths(outerMap, routeDto.getCityA(), routeDto.getCityB());
        //Then
        assertThat(allPaths)
                .contains(expectedResult);
    }

    @Test
    public void testRoute_7Cities_11Roads() {
        //Given
        Route route1 = new Route("Tokyo", "AngelTown", (double) 1);
        Route route2 = new Route("Tokyo", "Berlin", (double) 2);
        Route route3 = new Route("Tokyo", "Moscow", (double) 3);
        Route route4 = new Route("AngelTown", "BrightTown", (double) 4);
        Route route5 = new Route("BrightTown", "Polotsk", (double) 5);
        Route route10 = new Route("BrightTown", "Moscow", (double) 11);
        Route route6 = new Route("Moscow", "Berlin", (double) 6);
        Route route7 = new Route("Moscow", "Minsk", (double) 7);
        Route route8 = new Route("Minsk", "Polotsk", (double) 8);
        Route route9 = new Route("Polotsk", "Moscow", (double) 9);
        Route route0 = new Route("Berlin", "Polotsk", (double) 10);

        List<Route> routes = new LinkedList<>();
        routes.add(route0);routes.add(route1);routes.add(route2);routes.add(route3);
        routes.add(route4);routes.add(route5);routes.add(route6);routes.add(route7);
        routes.add(route8);routes.add(route9);routes.add(route10);

        Map<String, Map<String, Double>> outerMap = routeService.buildMatrix(routes);

        //When
        final List<Pair<List<String>, Double>> pathsToBerlin = pathFinder.findAllPaths(outerMap, "Tokyo", "Berlin");
        pathFinder = new PathFinderImpl();
        final List<Pair<List<String>, Double>> pathsToPolotsk = pathFinder.findAllPaths(outerMap, "Tokyo", "Polotsk");
        pathFinder = new PathFinderImpl();
        final List<Pair<List<String>, Double>> pathsToMinsk = pathFinder.findAllPaths(outerMap, "Tokyo", "Minsk");

        //Then
        assertThat(pathsToBerlin).hasSize(4);
        assertThat(pathsToPolotsk).hasSize(6);
        assertThat(pathsToMinsk).hasSize(4);
    }
}


