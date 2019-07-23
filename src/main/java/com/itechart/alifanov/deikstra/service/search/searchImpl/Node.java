package com.itechart.alifanov.deikstra.service.search.searchImpl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * shortest path - stores path to this Node. required for Dijkstra algorithm
 * distance - stores distance to that Node from other nodes; required for Dijkstra algorithm
 * neighbours - holds all neighbours of current node with distance to them
 */
@Getter
@Setter
@RequiredArgsConstructor
class Node {
    final private String name;

    private Double distance = Double.MAX_VALUE;

    private Map<Node, Double> neighbours = new LinkedHashMap<>();

    private List<Node> shortestPath = new LinkedList<>();

    Node(String name, Map<Node, Double> neighbours) {
        this.name = name;
        this.neighbours = neighbours;
    }
}
