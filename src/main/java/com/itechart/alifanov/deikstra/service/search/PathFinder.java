package com.itechart.alifanov.deikstra.service.search;

import com.itechart.alifanov.deikstra.service.search.searchImpl.Node;
import javafx.util.Pair;

import java.util.List;
import java.util.Set;

public interface PathFinder {
    List<Pair<List<String>, Double>> findAllPaths(Set<Node> nodes, String fromCity, String toCity) throws PathFinderException;
}
