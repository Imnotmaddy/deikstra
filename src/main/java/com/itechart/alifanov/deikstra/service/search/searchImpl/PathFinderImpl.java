package com.itechart.alifanov.deikstra.service.search.searchImpl;

import com.itechart.alifanov.deikstra.service.search.PathFinder;
import com.itechart.alifanov.deikstra.service.search.PathFinderException;
import javafx.util.Pair;
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
     *
     * @param toCity   - destination point
     * @return - If path exists method returns
     * list of pairs, where each pair is a route between starting point and destination represented
     * as List<String> and overall path distance as Double value;
     */
    @Override
    public List<Pair<List<String>, Double>> findAllPaths(Node startingNode, String toCity) throws PathFinderException {
        if (startingNode == null) {
            throw new PathFinderException("No paths between cities were found");
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(startingNode);

        final List<Pair<Queue<Node>, Double>> result = findPath(startingNode, toCity, new HashSet<>(), queue, (double) 0, new LinkedList<>());
        return buildResult(result);
    }

    /**
     * Recursive methods which checks every neighbour of every node. when it finds a path to targetNode it stores it
     * in allPaths.
     *
     * @param currentNode     - node, which neighbours are being evaluated
     * @param areVisited      - list of nodes which were visited to avoiud endless cycles
     * @param currentPath     - currently built path. every current node is added here at some point
     * @param currentDistance - based in currentPath distance
     */
    private List<Pair<Queue<Node>, Double>> findPath(Node currentNode, String targetCity, Set<Node> areVisited, Queue<Node> currentPath, Double currentDistance, List<Pair<Queue<Node>, Double>> result) {
        areVisited.add(currentNode);
        if (currentNode.getName().equals(targetCity)) {
            areVisited.remove(currentNode);
            result.add(new Pair<>(new LinkedList<>(currentPath), currentDistance));
            return result;
        }
        for (Map.Entry<Node, Double> entry : currentNode.getNeighbours().entrySet()) {
            Node evaluatedNeighbour = entry.getKey();
            if (!areVisited.contains(evaluatedNeighbour)) {
                currentPath.add(evaluatedNeighbour);
                currentDistance += entry.getValue();
                findPath(evaluatedNeighbour, targetCity, areVisited, currentPath, currentDistance, result);
                currentPath.remove(evaluatedNeighbour);
                currentDistance -= entry.getValue();
            }
        }
        areVisited.remove(currentNode);
        return result;
    }

    /**
     * Transforms node values into field values. Basically it replaces Node with its name
     * if there is no path, exception is thrown.
     *
     * @param source - data for building result
     * @return - result representation
     */
    private List<Pair<List<String>, Double>> buildResult(List<Pair<Queue<Node>, Double>> source) throws PathFinderException {
        List<Pair<List<String>, Double>> result = new ArrayList<>(source.size());
        for (Pair<Queue<Node>, Double> value : source) {
            List<String> innerList = new ArrayList<>();
            for (Node node : value.getKey()) {
                innerList.add(node.getName());
            }
            result.add(new Pair<>(innerList, value.getValue()));
        }
        if (result.isEmpty())
            throw new PathFinderException("No paths between cities were found");
        return result;
    }
}
