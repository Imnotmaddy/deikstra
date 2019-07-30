package com.itechart.alifanov.deikstra.service.search.searchImpl;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;


@Getter
@Setter
@RequiredArgsConstructor
public class Node {
    final private String name;

    private Map<Node, Double> neighbours = new HashMap<>();

    public Node(String name, Map<Node, Double> neighbours) {
        this.name = name;
        this.neighbours = neighbours;
    }
}
