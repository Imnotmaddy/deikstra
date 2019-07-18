package com.itechart.alifanov.deikstra.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class Node {
    final private String name;

    private Integer distance = Integer.MAX_VALUE;

    Map<Node, Double> neighbours;
}
