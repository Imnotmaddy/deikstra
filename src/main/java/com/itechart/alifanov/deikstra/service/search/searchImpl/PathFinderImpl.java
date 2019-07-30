package com.itechart.alifanov.deikstra.service.search.searchImpl;

import com.itechart.alifanov.deikstra.service.search.PathFinder;
import com.itechart.alifanov.deikstra.service.search.PathFinderException;
import javafx.util.Pair;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * This class implements algorithm for finding all paths between two
 * Nodes with breadth-first search. It also implements Dijkstra algorithm for
 * finding shortest path between two Nodes;
 * <p>
 * allPaths - stores all found paths from one Node to another
 */
@Component
public class PathFinderImpl implements PathFinder {

    /**
     * @param nodes    - contains all nodes
     * @param fromCity - starting point
     * @param toCity   - destination point
     * @return - If path exists method returns
     * list of pairs, where each pair is a route between starting point and destination represented
     * as List<String> and overall path distance as Double value;
     */
    @Override
    public List<Pair<List<String>, Double>> findAllPaths(Set<Node> nodes, String fromCity, String toCity) throws PathFinderException {
        if (nodes == null || nodes.isEmpty()) {
            throw new PathFinderException("No paths between cities were found");
        }

        Node startingNode = findNode(nodes, fromCity);
        Node endingNode = findNode(nodes, toCity);

        if (startingNode == null || endingNode == null)
            throw new PathFinderException("No paths between cities were found");

        Set<Node> areVisited = new HashSet<>();

        Queue<Node> queue = new LinkedList<>();

        queue.add(startingNode);
        final List<Pair<Queue<Node>, Double>> result = findPath(startingNode, endingNode, areVisited, queue, (double) 0, new LinkedList<>());
        return buildResult(result);
    }

    /**
     * Recursive methods which checks every neighbour of every node. when it finds a path to targetNode it stores it
     * in allPaths.
     *
     * @param currentNode     - node, which neighbours are being evaluated
     * @param targetNode      - stop point for recursion. Destination node
     * @param areVisited      - list of nodes which were visited to avoiud endless cycles
     * @param currentPath     - currently built path. every current node is added here at some point
     * @param currentDistance - based in currentPath distance
     */
    private List<Pair<Queue<Node>, Double>> findPath(Node currentNode, Node targetNode, Set<Node> areVisited, Queue<Node> currentPath, Double currentDistance, List<Pair<Queue<Node>, Double>> result) {
        areVisited.add(currentNode);
        if (currentNode == targetNode) {
            areVisited.remove(currentNode);
            result.add(new Pair<>(new LinkedList<>(currentPath), currentDistance));
            return result;
        }
        for (Map.Entry<Node, Double> entry : currentNode.getNeighbours().entrySet()) {
            Node evaluatedNeighbour = entry.getKey();
            if (!areVisited.contains(evaluatedNeighbour)) {
                currentPath.add(evaluatedNeighbour);
                currentDistance += entry.getValue();
                findPath(evaluatedNeighbour, targetNode, areVisited, currentPath, currentDistance, result);
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


    /**
     * finds target node in the list
     *
     * @param nodes  - list to be searched
     * @param target - node to be found
     * @return returns Node if it exists. returns null if node doesnt exists
     */
    private Node findNode(Set<Node> nodes, String target) {
        for (Node node : nodes) {
            if (node.getName().equals(target))
                return node;
        }
        return null;
    }
}
