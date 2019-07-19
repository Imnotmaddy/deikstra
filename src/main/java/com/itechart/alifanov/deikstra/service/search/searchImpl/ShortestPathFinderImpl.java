package com.itechart.alifanov.deikstra.service.search.searchImpl;

import com.itechart.alifanov.deikstra.model.Node;
import com.itechart.alifanov.deikstra.service.search.ShortestPathFinder;
import javafx.util.Pair;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ShortestPathFinderImpl implements ShortestPathFinder {
    @Override
    public Pair<List<String>, Double> findShortestPath(Map<String, Map<String, Double>> map, String fromCity, String toCity) {

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        List<Node> nodes = buildNodes(map);

        Node startingNode = findStartingNode(nodes, fromCity);
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

        return buildResult(toCity,settledNodes);
    }

    private Pair<List<String>, Double> buildResult(String toCity, Set<Node> nodes) {
        Node node = findNode(toCity, nodes);
        if (node == null) return null;
        List<String> path = new LinkedList<>();
        for (Node pathNodes : node.getShortestPath()) {
            path.add(pathNodes.getName());
        }
        path.add(toCity);
        return new Pair<>(path, node.getDistance());
    }

    private Node findNode(String target, Set<Node> nodes) {
        for (Node node : nodes) {
            if (node.getName().equals(target))
                return node;
        }
        return null;
    }

    private Node getLowestDistanceNode(Set<Node> unsettledNodes) {
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

    private Node findStartingNode(List<Node> nodes, String target) {
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
