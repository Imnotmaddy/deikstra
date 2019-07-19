package com.itechart.alifanov.deikstra.service.search.searchImpl;

import com.itechart.alifanov.deikstra.model.Node;
import com.itechart.alifanov.deikstra.service.search.ShortestPathFinder;
import javafx.util.Pair;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class ShortestPathFinderImpl implements ShortestPathFinder {

    private List<Pair<List<Node>, Double>> allPaths = new LinkedList<>();

    @Override
    public Pair<List<String>, Double> findShortestPath(Map<String, Map<String, Double>> map, String fromCity, String toCity) {

        List<Node> settledNodes = new LinkedList<>();
        List<Node> unsettledNodes = new LinkedList<>();
        List<Node> nodes = buildNodes(map);

        Node startingNode = findNode(nodes, fromCity);
        if (startingNode == null) return null;
        startingNode.setDistance((double) 0);
        unsettledNodes.add(startingNode);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            if (currentNode != null && currentNode.getNeighbours() != null) {
                for (Map.Entry<Node, Double> pair : currentNode.getNeighbours().entrySet()) {
                    Node neighbour = pair.getKey();
                    Double distance = pair.getValue();
                    if (!settledNodes.contains(neighbour)) {
                        calculateMinimumDistance(neighbour, distance, currentNode);
                        unsettledNodes.add(neighbour);
                    }
                }
            }
            settledNodes.add(currentNode);
        }

        return buildResult(toCity, settledNodes);
    }

    @Override
    public List<Pair<List<String>, Double>> findAllPaths(Map<String, Map<String, Double>> map, String fromCity, String toCity) {
        List<Node> nodes = buildNodes(map);
        Node startingNode = findNode(nodes, fromCity);
        Node endingNode = findNode(nodes, toCity);
        if (startingNode == null || endingNode == null) return null;

        List<Node> areVisited = new LinkedList<>();
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(startingNode);
        recursive(startingNode, endingNode, areVisited, queue, (double) 0);
        return buildResult(this.allPaths);
    }

    private List<Pair<List<String>, Double>> buildResult(List<Pair<List<Node>, Double>> source) {
        if (source.isEmpty()) return null;
        List<Pair<List<String>, Double>> result = new LinkedList<>();
        for (Pair<List<Node>, Double> value : source) {
            List<String> innerList = new LinkedList<>();
            for (Node node : value.getKey()) {
                innerList.add(node.getName());
            }
            result.add(new Pair<>(innerList, value.getValue()));
        }
        return result;
    }


    private void recursive(Node currentNode, Node targetNode, List<Node> areVisited, List<Node> currentPath, Double distance) {
        areVisited.add(currentNode);
        if (currentNode == targetNode) {
            areVisited.remove(currentNode);
            allPaths.add(new Pair<>(new ArrayList<>(currentPath), distance));
            return;
        }
        for (Map.Entry<Node, Double> entry : currentNode.getNeighbours().entrySet()) {
            Node evaluatedNeighbour = entry.getKey();
            if (!areVisited.contains(evaluatedNeighbour)) {
                currentPath.add(evaluatedNeighbour);
                distance += entry.getValue();
                recursive(evaluatedNeighbour, targetNode, areVisited, currentPath, distance);
                currentPath.remove(evaluatedNeighbour);
                distance = (double) 0;
            }
        }
        areVisited.remove(currentNode);
    }


    private Pair<List<String>, Double> buildResult(String toCity, List<Node> nodes) {
        Node node = findNode(nodes, toCity);
        if (node == null) return null;
        List<String> path = new LinkedList<>();
        for (Node pathNodes : node.getShortestPath()) {
            path.add(pathNodes.getName());
        }
        path.add(toCity);
        return new Pair<>(path, node.getDistance());
    }

    private Node getLowestDistanceNode(List<Node> unsettledNodes) {
        Node lowestDistanceNode = null;
        double lowestDistance = Double.MAX_VALUE;

        for (Node unsettledNode : unsettledNodes) {
            if (unsettledNode.getDistance() < lowestDistance) {
                lowestDistance = unsettledNode.getDistance();
                lowestDistanceNode = unsettledNode;
            }
        }
        return lowestDistanceNode;
    }

    private void calculateMinimumDistance(Node evaluationNode, Double distance, Node sourceNode) {
        Double sourceDistance = sourceNode.getDistance();
        if (sourceDistance + distance < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + distance);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }

    private Node findNode(List<Node> nodes, String target) {
        for (Node node : nodes) {
            if (node.getName().equals(target))
                return node;
        }
        return null;
    }

    private List<Node> buildNodes(Map<String, Map<String, Double>> map) {
        Map<String, Node> createdNodes = new HashMap<>();
        for (Map.Entry<String, Map<String, Double>> entry : map.entrySet()) {
            String city = entry.getKey();
            Map<String, Double> neighbours = entry.getValue();
            Map<Node, Double> nodeNeighbours = new LinkedHashMap<>();

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
        return new LinkedList<>(createdNodes.values());
    }
}
