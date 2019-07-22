package com.itechart.alifanov.deikstra.service.search;

import javafx.util.Pair;

import java.util.List;
import java.util.Map;

public interface PathFinder {
    Pair<List<String>, Double> findShortestPath(Map<String, Map<String, Double>> map, String fromCity, String toCity);

    List<Pair<List<String>, Double>> findAllPaths(Map<String, Map<String, Double>> map, String fromCity, String toCity);
}
