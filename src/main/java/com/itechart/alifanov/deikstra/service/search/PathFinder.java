package com.itechart.alifanov.deikstra.service.search;

import com.itechart.alifanov.deikstra.service.search.searchImpl.Node;
import javafx.util.Pair;

import java.util.List;

public interface PathFinder {
    List<Pair<List<String>, Double>> findAllPaths(Node startingNode, String toCity) throws PathFinderException;
}
