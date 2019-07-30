package com.itechart.alifanov.deikstra.service.search.searchImpl;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class SearchResultDto {
    private List<String> path;
    private Double distance;
}
