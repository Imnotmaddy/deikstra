package com.itechart.alifanov.deikstra.service;

import com.itechart.alifanov.deikstra.model.Route;
import com.itechart.alifanov.deikstra.repository.RouteRepository;
import com.itechart.alifanov.deikstra.service.dto.RouteDto;
import com.itechart.alifanov.deikstra.service.dtoTransformer.RouteTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final RouteTransformer routeTransformer;

    @Transactional
    public RouteDto save(final RouteDto routeDto) {
        final Route newRoute = routeTransformer.transform(routeDto);
        Route oldRoute = ifPresent(newRoute.getCityA(), newRoute.getCityB());
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
        return routeTransformer.transform(routeRepository.findAll());
    }

    public List<RouteDto> calculateRoute(RouteDto routeDto) {

        return new ArrayList<>();
    }

    private Route ifPresent(String cityA, String cityB) {
        return routeRepository.findByCityAAndCityB(cityA, cityB);
    }

}
