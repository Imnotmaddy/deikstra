package com.itechart.alifanov.deikstra.controller;

import com.itechart.alifanov.deikstra.service.RouteService;
import com.itechart.alifanov.deikstra.dto.RouteDto;
import com.itechart.alifanov.deikstra.service.exception.DeikstraAppException;
import com.itechart.alifanov.deikstra.service.search.PathFinderException;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping("/route")
    public void createRoute(@ModelAttribute @Valid RouteDto routeDto) throws DeikstraAppException {
        try {
            routeService.save(routeDto);
        } catch (OptimisticLockException ex) {
            throw new DeikstraAppException("Concurrency issue while saving data");
        }
    }

    @PostMapping("/route/calculate")
    @ResponseBody
    public List<Pair<List<String>, Double>> calculateRoute(@ModelAttribute RouteDto routeDto) throws DeikstraAppException {
        final List<Pair<List<String>, Double>> result;
        try {
            result = routeService.calculateAllRoutes(routeDto.getCityA(), routeDto.getCityB());
        } catch (PathFinderException ex) {
            throw new DeikstraAppException(ex.getMessage());
        }
        return result;
    }

    @ExceptionHandler(DeikstraAppException.class)
    @ResponseStatus(value = HttpStatus.IM_USED)
    public String handleAppErrors(DeikstraAppException ex) {
        return ex.getMessage();
    }

}

