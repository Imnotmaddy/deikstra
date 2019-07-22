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
    public void testRoute_7Cities_11Roads(){
        //Given
        RouteDto routeDto1 = new RouteDto("Tokyo", "AngelTown", (double) 1);
        RouteDto routeDto2 = new RouteDto("Tokyo", "Berlin", (double) 2);
        RouteDto routeDto3 = new RouteDto("Tokyo", "Moscow", (double) 3);
        RouteDto routeDto4 = new RouteDto("AngelTown", "BrightTown", (double) 4);
        RouteDto routeDto5 = new RouteDto("BrightTown", "Polotsk", (double) 5);
        RouteDto routeDto10 = new RouteDto("BrightTown", "Moscow", (double) 11);
        RouteDto routeDto6 = new RouteDto("Moscow", "Berlin", (double) 6);
        RouteDto routeDto7 = new RouteDto("Moscow", "Minsk", (double) 7);
        RouteDto routeDto8 = new RouteDto("Minsk", "Polotsk", (double) 8);
        RouteDto routeDto9 = new RouteDto("Polotsk", "Moscow", (double) 9);
        RouteDto routeDto0 = new RouteDto("Berlin", "Polotsk", (double) 10);

        Map<String, Double> innerMapTokyo = new HashMap<>();
        innerMapTokyo.put(routeDto1.getCityB(), routeDto1.getDistance());
        innerMapTokyo.put(routeDto2.getCityB(), routeDto2.getDistance());
        innerMapTokyo.put(routeDto3.getCityB(), routeDto3.getDistance());

        Map<String, Double> innerMapAngelTown = new HashMap<>();
        innerMapAngelTown.put(routeDto4.getCityB(), routeDto4.getDistance());

        Map<String, Double> innerMapBrightTown = new HashMap<>();
        innerMapBrightTown.put(routeDto5.getCityB(), routeDto5.getDistance());
        innerMapBrightTown.put(routeDto10.getCityB(), routeDto10.getDistance());

        Map<String, Double> innerMapMoscow = new HashMap<>();
        innerMapMoscow.put(routeDto7.getCityB(), routeDto7.getDistance());
        innerMapMoscow.put(routeDto6.getCityB(), routeDto6.getDistance());

        Map<String, Double> innerMapMinsk = new HashMap<>();
        innerMapMinsk.put(routeDto8.getCityB(), routeDto8.getDistance());

        Map<String, Double> innerMapPolotsk = new HashMap<>();
        innerMapPolotsk.put(routeDto9.getCityB(), routeDto9.getDistance());

        Map<String, Double> innerMapBerlin = new HashMap<>();
        innerMapPolotsk.put(routeDto0.getCityB(), routeDto0.getDistance());

        Map<String, Map<String, Double>> outerMap = new HashMap<>();
        outerMap.put(routeDto1.getCityA(), innerMapTokyo);
        outerMap.put(routeDto4.getCityA(), innerMapAngelTown);
        outerMap.put(routeDto5.getCityA(), innerMapBrightTown);
        outerMap.put(routeDto6.getCityA(), innerMapMoscow);
        outerMap.put(routeDto8.getCityA(), innerMapMinsk);
        outerMap.put(routeDto9.getCityA(), innerMapPolotsk);
        outerMap.put(routeDto0.getCityA(), innerMapBerlin);

        //List<String> expectedListResult = Arrays.asList(routeDto.getCityA(), "Polotsk");
        //Pair<List<String>, Double> expectedResult = new Pair<>(expectedListResult, routeDto.getDistance());
    }
}


