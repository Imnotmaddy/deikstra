package com.itechart.alifanov.deikstra.service.search.searchImpl;

import com.itechart.alifanov.deikstra.service.search.PathFinder;
import com.itechart.alifanov.deikstra.service.search.PathFinderException;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * This class implements algorithm for finding all paths between two
 * Nodes with breadth-first search.
 * <p>
 * allPaths - stores all found paths from one Node to another
 */
@Component
public class PathFinderImpl implements PathFinder {

    /**
     * @param toCity - destination point
     * @return - If path exists method returns
     * list of pairs, where each pair is a route between starting point and destination represented
     * as List<String> and overall path distance as Double value;
     */
    @Override
    public List<SearchResultDto> findAllPaths(Node startingNode, String toCity) throws PathFinderException {
        if (startingNode == null) {
            throw new PathFinderException("No paths between cities were found");
        }

        Queue<String> queue = new LinkedList<>();
        queue.add(startingNode.getName());

        return findPath(startingNode, toCity, new HashSet<>(), queue, (double) 0, new LinkedList<>());
    }

    /**
     * Recursive methods which checks every neighbour of every node. when it finds a path to targetNode it stores it
     * in allPaths.
     *
     * @param currentNode     - node, which neighbours are being evaluated
     * @param areVisited      - list of nodes which were visited to avoid endless cycles
     * @param currentPath     - currently built path. every current node is added here at some point
     * @param currentDistance - based in currentPath distance
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
