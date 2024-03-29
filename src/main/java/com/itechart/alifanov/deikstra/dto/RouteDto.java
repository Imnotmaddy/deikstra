package com.itechart.alifanov.deikstra.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RouteDto {
    @NotBlank
    private String cityA;
    @NotBlank
    private String cityB;
    @NotNull
    @Min(value = 0)
    private Double distance;
}
