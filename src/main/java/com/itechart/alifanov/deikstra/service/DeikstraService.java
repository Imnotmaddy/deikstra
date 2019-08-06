package com.itechart.alifanov.deikstra.service;

import com.itechart.alifanov.deikstra.dto.RouteDto;
import com.itechart.alifanov.deikstra.model.Route;
import com.itechart.alifanov.deikstra.service.search.PathFinderException;
import com.itechart.alifanov.deikstra.service.search.searchImpl.Node;
import com.itechart.alifanov.deikstra.service.search.searchImpl.SearchResultDto;

import javax.persistence.OptimisticLockException;
import java.util.List;
import java.util.Map;

public interface DeikstraService {
    RouteDto save(final RouteDto routeDto) throws OptimisticLockException;
    List<SearchResultDto> calculateAllRoutes(String fromCity, String toCity) throws PathFinderException;
    Map<String, Node> buildNodes(List<Route> routes);
}
