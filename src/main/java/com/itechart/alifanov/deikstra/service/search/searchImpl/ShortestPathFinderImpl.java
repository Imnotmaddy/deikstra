package com.itechart.alifanov.deikstra.service.search.searchImpl;

import com.itechart.alifanov.deikstra.model.Node;
import com.itechart.alifanov.deikstra.service.search.ShortestPathFinder;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class ShortestPathFinderImpl implements ShortestPathFinder {
    @Override
    public void findShortestPath(Map<String, Map<String, Double>> map, String fromCity, String toCity) {
        List<String> shortestPath = new LinkedList<>();

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        List<Node> nodes = buildNodes(map);
        Node startingNode = findStartingNode(nodes, fromCity);
        if (startingNode == null) return;
        startingNode.setDistance(0);

        while (unsettledNodes.size() != 0) {

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
        List<Node> nodes = new LinkedList<>();
        for (Map.Entry<String, Map<String, Double>> entry : map.entrySet()) {
            String city = entry.getKey();
            Map<String, Double> neighbours = entry.getValue();


        }
        return nodes;
    }

    private List<Node> buildNeighbours(Map<String, Double> map){

    }

    private Node exists(String name, List<Node> nodes) {
        for (Node node : nodes) {
            if (node.getName().equals(name))
                return node;
        }
        return null;
    }
}
