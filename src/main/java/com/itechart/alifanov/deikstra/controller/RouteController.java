package com.itechart.alifanov.deikstra.controller;

import com.itechart.alifanov.deikstra.dto.RouteDto;
import com.itechart.alifanov.deikstra.service.DeikstraService;
import com.itechart.alifanov.deikstra.service.search.PathFinderException;
import com.itechart.alifanov.deikstra.service.search.searchImpl.SearchResultDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class RouteController {

    private final DeikstraService routeService;

    @PostMapping("/route")
    public void createRoute(@ModelAttribute @Valid RouteDto routeDto) throws OptimisticLockException {
        routeService.save(routeDto);
    }

    @PostMapping("/route/calculate")
    @ResponseBody
    public List<SearchResultDto> calculateRoute(@ModelAttribute @Valid RouteDto routeDto) throws PathFinderException {
        final List<SearchResultDto> result;
        result = routeService.calculateAllRoutes(routeDto.getCityA(), routeDto.getCityB());
        return result;
    }

    @ExceptionHandler(OptimisticLockException.class)
    @ResponseStatus(value = HttpStatus.CONFLICT)
    public String handleAppErrors(OptimisticLockException ex) {
        return ex.getMessage();
    }

    @ExceptionHandler(PathFinderException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle(PathFinderException ex) {
        return ex.getMessage();
    }

}

