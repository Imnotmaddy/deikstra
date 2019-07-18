package com.itechart.alifanov.deikstra.service.search;

import java.util.Map;

public interface ShortestPathFinder {
    void findShortestPath(Map<String, Map<String, Double>> map, String fromCity, String toCity);
}
