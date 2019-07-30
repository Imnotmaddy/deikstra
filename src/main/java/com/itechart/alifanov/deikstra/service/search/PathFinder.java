package com.itechart.alifanov.deikstra.service.search;

import com.itechart.alifanov.deikstra.service.search.searchImpl.Node;
import com.itechart.alifanov.deikstra.service.search.searchImpl.SearchResultDto;

import java.util.List;

public interface PathFinder {
    List<SearchResultDto> findAllPaths(Node startingNode, String toCity) throws PathFinderException;
}
