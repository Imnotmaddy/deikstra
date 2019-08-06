package com.itechart.alifanov.deikstra.service.search.searchImpl;

import com.itechart.alifanov.deikstra.service.search.PathFinder;
import com.itechart.alifanov.deikstra.service.search.PathFinderException;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * This class implements algorithm for finding all paths between two
 * Nodes with breadth-first search.
 */
@Service
public class PathFinderImpl implements PathFinder {

    /**
     * @param startingNode - entry point of the search
     * @param toCity       - destination point
     * @return - if path exists method returns list of objects, where each object
     * * contains a route between starting point and destination represented
     * * as List<String> and overall path distance as Double value;
     * @throws PathFinderException - thrown if no path was found
     */
    @Override
    public List<SearchResultDto> findAllPaths(Node startingNode, String toCity) throws PathFinderException {
        if (startingNode == null || startingNode.getNeighbours() == null) {
            throw new PathFinderException("No paths between cities were found");
        }

        Queue<String> queue = new LinkedList<>();
        queue.add(startingNode.getName());

        return findPath(startingNode, toCity, new HashSet<>(), queue, (double) 0, new LinkedList<>());
    }

    /**
     * @param currentNode     -    node, which neighbours are being evaluated
     * @param targetCity      - name of the node, which is the end point for search
     * @param areVisited      -     set of node names which were visited, to avoid endless cycles
     * @param currentPath     - list of node names, which are present in currently built path
     * @param currentDistance - sum of distances between nodes in currentPath
     * @param result          - placeholder for all found paths
     * @return - if path exists method returns list of objects, where each object
     * * * contains a route between starting point and destination represented
     * * * as List<String> and overall path distance as Double value;
     */
    private List<SearchResultDto> findPath(Node currentNode, String targetCity, Set<String> areVisited, Queue<String> currentPath, Double currentDistance, List<SearchResultDto> result) {
        areVisited.add(currentNode.getName());
        if (currentNode.getName().equals(targetCity)) {
            areVisited.remove(currentNode.getName());
            result.add(new SearchResultDto(new LinkedList<>(currentPath), currentDistance));
            return result;
        }
        for (Map.Entry<Node, Double> entry : currentNode.getNeighbours().entrySet()) {
            Node evaluatedNeighbour = entry.getKey();
            if (!areVisited.contains(evaluatedNeighbour.getName())) {
                currentPath.add(evaluatedNeighbour.getName());
                currentDistance += entry.getValue();
                findPath(evaluatedNeighbour, targetCity, areVisited, currentPath, currentDistance, result);
                currentPath.remove(evaluatedNeighbour.getName());
                currentDistance -= entry.getValue();
            }
        }
        areVisited.remove(currentNode.getName());
        return result;
    }
}
