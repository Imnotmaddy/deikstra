package com.itechart.alifanov.deikstra.model;


import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Builder
@Table(name = "Route")
@AllArgsConstructor
@NoArgsConstructor
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "cityA")
    @NotBlank
    private String cityA;

    @Column(name = "cityB")
    @NotBlank
    private String cityB;

    @Column(name = "distance")
    @NotNull
    private Double distance;

}
