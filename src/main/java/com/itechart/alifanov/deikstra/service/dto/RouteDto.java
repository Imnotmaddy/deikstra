package com.itechart.alifanov.deikstra.service.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto implements Dto {
    @NotNull
    @NotBlank
    private String cityA;
    @NotNull
    @NotBlank
    private String cityB;
    @NotNull
    private Double distance;
}
