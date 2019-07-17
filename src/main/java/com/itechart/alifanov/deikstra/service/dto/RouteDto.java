package com.itechart.alifanov.deikstra.service.dto;

import lombok.*;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto implements Dto {
    private String cityA;
    private String cityB;
    private Double distance;
}
