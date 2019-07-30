package com.itechart.alifanov.deikstra;

import com.itechart.alifanov.deikstra.dto.RouteDto;
import com.itechart.alifanov.deikstra.model.Route;
import com.itechart.alifanov.deikstra.repository.RouteRepository;
import com.itechart.alifanov.deikstra.service.RouteService;
import com.itechart.alifanov.deikstra.service.mapper.RouteMapper;
import com.itechart.alifanov.deikstra.service.search.PathFinder;
import com.itechart.alifanov.deikstra.service.search.PathFinderException;
import com.itechart.alifanov.deikstra.service.search.searchImpl.Node;
import com.itechart.alifanov.deikstra.service.search.searchImpl.PathFinderImpl;
import com.itechart.alifanov.deikstra.service.search.searchImpl.SearchResultDto;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
public class PathFinderTest {

    private Node tokyoStartingNode;

    @MockBean
    private RouteService routeService;

    @MockBean
    private RouteRepository routeRepository;

    private PathFinder pathFinder = new PathFinderImpl();

    @Before
    public void init() {
        routeService = new RouteService(routeRepository, new RouteMapper(), new PathFinderImpl());
        Route route1 = new Route("Tokyo", "AngelTown", (double) 1);
        Route route2 = new Route("Tokyo", "Berlin", (double) 2);
        Route route3 = new Route("Tokyo", "Moscow", (double) 3);
        Route route4 = new Route("AngelTown", "BrightTown", (double) 4);
        Route route5 = new Route("BrightTown", "Polotsk", (double) 5);
        Route route6 = new Route("Moscow", "Berlin", (double) 6);
        Route route7 = new Route("Moscow", "Minsk", (double) 7);
        Route route8 = new Route("Minsk", "Polotsk", (double) 8);
        Route route9 = new Route("Polotsk", "Moscow", (double) 9);
        Route route0 = new Route("Berlin", "Polotsk", (double) 10);
        Route route = new Route("Tokyo", "NullPointerException", (double) 2);

        List<Route> routes = new ArrayList<>();
        routes.add(route0);
        routes.add(route1);
        routes.add(route2);
        routes.add(route3);
        routes.add(route4);
        routes.add(route5);
        routes.add(route6);
        routes.add(route7);
        routes.add(route8);
        routes.add(route9);
        routes.add(route);

        tokyoStartingNode = routeService.buildNodes(routes).getOrDefault("Tokyo", null);
    }

    @Test
    public void testRoute_2Cities_1Connection() throws PathFinderException {
        //Given
        RouteDto routeDto = new RouteDto("Tokyo", "Polotsk", (double) 25);

        Node nodeB = new Node(routeDto.getCityB(), null);
        Map<Node, Double> neighbours = new HashMap<>();
        neighbours.put(nodeB, routeDto.getDistance());
        Node nodeA = new Node(routeDto.getCityA(), neighbours);

        List<String> expectedListResult = Arrays.asList(routeDto.getCityA(), routeDto.getCityB());
        SearchResultDto expectedResult = new SearchResultDto(expectedListResult, routeDto.getDistance());
        //When
        final List<SearchResultDto> allPaths = pathFinder.findAllPaths(nodeA, routeDto.getCityB());
        //Then
        assertThat(allPaths)
                .containsOnly(expectedResult);
    }

    @Test
    public void testRouteFromTokyoToPolotsk() throws PathFinderException {
        SearchResultDto expectedPair1 = new SearchResultDto(Arrays.asList("Tokyo", "AngelTown", "BrightTown", "Polotsk"), (double) 10);
        SearchResultDto expectedPair2 = new SearchResultDto(Arrays.asList("Tokyo", "Moscow", "Minsk", "Polotsk"), (double) 18);
        SearchResultDto expectedPair3 = new SearchResultDto(Arrays.asList("Tokyo", "Moscow", "Berlin", "Polotsk"), (double) 19);
        SearchResultDto expectedPair4 = new SearchResultDto(Arrays.asList("Tokyo", "Berlin", "Polotsk"), (double) 12);
        List<SearchResultDto> expectedList = new ArrayList<>();
        expectedList.add(expectedPair1);
        expectedList.add(expectedPair2);
        expectedList.add(expectedPair3);
        expectedList.add(expectedPair4);

        //When
        final List<SearchResultDto> pathsToPolotsk = pathFinder.findAllPaths(tokyoStartingNode, "Polotsk");
        //Then
        assertThat(pathsToPolotsk).containsOnlyElementsOf(expectedList);
    }


    @Test
    public void testRouteFromTokyoToMoscow() throws PathFinderException {
        SearchResultDto expectedPair1 = new SearchResultDto(Arrays.asList("Tokyo", "AngelTown", "BrightTown", "Polotsk", "Moscow"), (double) 19);
        SearchResultDto expectedPair2 = new SearchResultDto(Arrays.asList("Tokyo", "Moscow"), (double) 3);
        SearchResultDto expectedPair3 = new SearchResultDto(Arrays.asList("Tokyo", "Berlin", "Polotsk", "Moscow"), (double) 21);
        List<SearchResultDto> expectedList = new ArrayList<>();
        expectedList.add(expectedPair1);
        expectedList.add(expectedPair2);
        expectedList.add(expectedPair3);

        //When
        final List<SearchResultDto> pathsToPolotsk = pathFinder.findAllPaths(tokyoStartingNode, "Moscow");
        //Then
        assertThat(pathsToPolotsk).containsOnlyElementsOf(expectedList);
    }

    @Test
    public void testRouteFromTokyoToBerlin() throws PathFinderException {
        SearchResultDto expectedPair1 = new SearchResultDto(Arrays.asList("Tokyo", "AngelTown", "BrightTown", "Polotsk", "Moscow", "Berlin"), (double) 25);
        SearchResultDto expectedPair2 = new SearchResultDto(Arrays.asList("Tokyo", "Moscow", "Berlin"), (double) 9);
        SearchResultDto expectedPair3 = new SearchResultDto(Arrays.asList("Tokyo", "Berlin"), (double) 2);
        List<SearchResultDto> expectedList = new ArrayList<>();
        expectedList.add(expectedPair1);
        expectedList.add(expectedPair2);
        expectedList.add(expectedPair3);

        //When
        final List<SearchResultDto> pathsToPolotsk = pathFinder.findAllPaths(tokyoStartingNode, "Berlin");
        //Then
        assertThat(pathsToPolotsk).containsOnlyElementsOf(expectedList);
    }

    @Test
    public void testRouteFromTokyoToMinsk() throws PathFinderException {
        SearchResultDto expectedPair1 = new SearchResultDto(Arrays.asList("Tokyo", "AngelTown", "BrightTown", "Polotsk", "Moscow", "Minsk"), (double) 26);
        SearchResultDto expectedPair2 = new SearchResultDto(Arrays.asList("Tokyo", "Moscow", "Minsk"), (double) 10);
        SearchResultDto expectedPair3 = new SearchResultDto(Arrays.asList("Tokyo", "Berlin", "Polotsk", "Moscow", "Minsk"), (double) 28);
        List<SearchResultDto> expectedList = new ArrayList<>();
        expectedList.add(expectedPair1);
        expectedList.add(expectedPair2);
        expectedList.add(expectedPair3);

        //When
        final List<SearchResultDto> pathsToPolotsk = pathFinder.findAllPaths(tokyoStartingNode, "Minsk");
        //Then
        assertThat(pathsToPolotsk).containsOnlyElementsOf(expectedList);
    }

    @Test(expected = PathFinderException.class)
    public void ifNoNeighbours_thenPathFinderException() throws PathFinderException {
        Node node = new Node("Node", null);
        pathFinder.findAllPaths(node, "wherever");
    }

    @Test(expected = PathFinderException.class)
    public void whenNull_thenPathFinderException() throws PathFinderException{
        pathFinder.findAllPaths(null, "wherever");
    }


}


