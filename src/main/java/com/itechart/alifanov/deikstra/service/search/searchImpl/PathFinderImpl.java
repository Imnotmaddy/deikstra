package com.itechart.alifanov.deikstra.service.search.searchImpl;

import com.itechart.alifanov.deikstra.service.search.PathFinderException;
import com.itechart.alifanov.deikstra.service.search.PathFinder;
import javafx.util.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
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
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class PathFinderImpl implements PathFinder {

    private List<Pair<List<Node>, Double>> allPaths = new LinkedList<>();

    /**
     * @param map      - source data for building nodes
     * @param fromCity - starting point
     * @param toCity   - destination point
     * @return - If path exists method returns
     * list of pairs, where each pair is a route between starting point and destination represented
     * as List<String> and overall path distance as Double value;
     */
    @Override
    public List<Pair<List<String>, Double>> findAllPaths(Map<String, Map<String, Double>> map, String fromCity, String toCity) throws PathFinderException {
        if (map == null || map.isEmpty()) {
            throw new PathFinderException("No paths between cities were found");
        }

        List<Node> nodes = buildNodes(map);
        Node startingNode = findNode(nodes, fromCity);
        Node endingNode = findNode(nodes, toCity);

        if (startingNode == null || endingNode == null)
            throw new PathFinderException("No paths between cities were found");

        Set<Node> areVisited = new HashSet<>();
        List<Node> queue = new ArrayList<>();
        queue.add(startingNode);
        findPath(startingNode, endingNode, areVisited, queue, (double) 0);
        return buildResult(this.allPaths);
    }

    /**
     * Transforms node values into field values. Basically it replaces Node with its name
     * if there is no path, exception is thrown.
     *
     * @param source - data for building result
     * @return - result representation
     */
    private List<Pair<List<String>, Double>> buildResult(List<Pair<List<Node>, Double>> source) throws PathFinderException {
        List<Pair<List<String>, Double>> result = new ArrayList<>(source.size());
        for (Pair<List<Node>, Double> value : source) {
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
     * Recursive methods which checks every neighbour of every node. when it finds a path to targetNode it stores it
     * in allPaths.
     *
     * @param currentNode     - node, which neighbours are being evaluated
     * @param targetNode      - stop point for recursion. Destination node
     * @param areVisited      - list of nodes which were visited to avoiud endless cycles
     * @param currentPath     - currently built path. every current node is added here at some point
     * @param currentDistance - based in currentPath distance
     */
    private void findPath(Node currentNode, Node targetNode, Set<Node> areVisited, List<Node> currentPath, Double currentDistance) {
        areVisited.add(currentNode);
        if (currentNode == targetNode) {
            areVisited.remove(currentNode);
            allPaths.add(new Pair<>(new ArrayList<>(currentPath), currentDistance));
            return;
        }
        for (Map.Entry<Node, Double> entry : currentNode.getNeighbours().entrySet()) {
            Node evaluatedNeighbour = entry.getKey();
            if (!areVisited.contains(evaluatedNeighbour)) {
                currentPath.add(evaluatedNeighbour);
                currentDistance += entry.getValue();
                findPath(evaluatedNeighbour, targetNode, areVisited, currentPath, currentDistance);
                currentPath.remove(evaluatedNeighbour);
                currentDistance -= entry.getValue();
            }
        }
        areVisited.remove(currentNode);
    }

    /**
     * finds target node in the list
     *
     * @param nodes  - list to be searched
     * @param target - node to be found
     * @return returns Node if it exists. returns null if node doesnt exists
     */
    private Node findNode(List<Node> nodes, String target) {
        for (Node node : nodes) {
            if (node.getName().equals(target))
                return node;
        }
        return null;
    }

    /**
     * builds list of nodes from input data. it creates node for every distinct key
     * of each map and creates neighbours for every node based on outerMap value
     *
     * @param map- source data
     * @return list of built nodes
     */
    private List<Node> buildNodes(Map<String, Map<String, Double>> map) {
        Map<String, Node> createdNodes = new HashMap<>();
        for (Map.Entry<String, Map<String, Double>> entry : map.entrySet()) {
            String city = entry.getKey();
            Map<String, Double> neighbours = entry.getValue();
            Map<Node, Double> nodeNeighbours = new HashMap<>();

            neighbours.forEach((neighbourCity, distance) -> {
                Node currentNeighbour;
                if (createdNodes.containsKey(neighbourCity)) {
                    currentNeighbour = createdNodes.get(neighbourCity);
                } else {
                    currentNeighbour = new Node(neighbourCity, null);
                }
                nodeNeighbours.put(currentNeighbour, distance);
                createdNodes.put(neighbourCity, currentNeighbour);
            });

            Node node;
            if (createdNodes.containsKey(city)) {
                node = createdNodes.get(city);
                node.setNeighbours(nodeNeighbours);
            } else {
                node = new Node(city, nodeNeighbours);
            }
            createdNodes.put(city, node);
        }
        return new ArrayList<>(createdNodes.values());
    }
}
