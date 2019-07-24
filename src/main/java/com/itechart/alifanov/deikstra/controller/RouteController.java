package com.itechart.alifanov.deikstra.controller;

import com.itechart.alifanov.deikstra.service.RouteService;
import com.itechart.alifanov.deikstra.service.dto.RouteDto;
import com.itechart.alifanov.deikstra.service.exception.DeikstraAppException;
import com.itechart.alifanov.deikstra.service.search.PathFinderException;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/createRoute")
    public void createRoute(@ModelAttribute @Valid RouteDto routeDto) throws DeikstraAppException {
        try {
            routeService.save(routeDto);
        } catch (OptimisticLockException ex) {
            throw new DeikstraAppException("Concurrency issue while saving data", "500");
        }
    }

    @PostMapping("/calculateRoute")
    @ResponseBody
    public List<Pair<List<String>, Double>> calculateRoute(@ModelAttribute RouteDto routeDto) throws DeikstraAppException {
        final List<Pair<List<String>, Double>> result;
        try {
            result = routeService.calculateAllRoutes(routeDto.getCityA(), routeDto.getCityB());
        } catch (PathFinderException ex) {
            throw new DeikstraAppException(ex.getMessage(), "500");
        }
        return result;
    }
}

