package com.itechart.alifanov.deikstra.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
public class Node {
    final private String name;

    private Integer distance = Integer.MAX_VALUE;

    private Map<Node, Double> neighbours;

    public Node(String name, Map<Node, Double> neighbours) {
        this.name = name;
        this.neighbours = neighbours;
    }
}
