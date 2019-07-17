package com.itechart.alifanov.deikstra.service;

import com.itechart.alifanov.deikstra.model.Route;
import com.itechart.alifanov.deikstra.repository.RouteRepository;
import com.itechart.alifanov.deikstra.service.dto.RouteDto;
import com.itechart.alifanov.deikstra.service.dtoTransformer.RouteTransformer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final RouteTransformer routeTransformer;

    public RouteDto save(final RouteDto routeDto) {
        final Route route = routeTransformer.transform(routeDto);
        routeRepository.save(route);
        return routeTransformer.transform(route);
    }

    public List<RouteDto> findAll() {
        return routeTransformer.transform(routeRepository.findAll());
    }

    public List<RouteDto> calculateRoute(RouteDto routeDto){
        return new ArrayList<>();
    }

}
