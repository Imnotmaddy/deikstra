package com.itechart.alifanov.deikstra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class Node {
    final private String name;

    private Double distance = Double.MAX_VALUE;

    private Map<Node, Double> neighbours;

    private List<Node> shortestPath = new LinkedList<>();
    private List<Node> allPaths = new LinkedList<>();

    public Node(String name, Map<Node, Double> neighbours) {
        this.name = name;
        this.neighbours = neighbours;
    }
}
