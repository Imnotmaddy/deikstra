package com.itechart.alifanov.deikstra.controller;

import com.itechart.alifanov.deikstra.service.RouteService;
import com.itechart.alifanov.deikstra.service.dto.RouteDto;
import javafx.util.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.persistence.OptimisticLockException;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @GetMapping("/")
    public String showHomeView(Model model) {
        model.addAttribute("routeDto", new RouteDto());
        model.addAttribute("routes", routeService.findAll());
        return "home";
    }

    @PostMapping("/createRoute")
    public String createRoute(@ModelAttribute @Valid RouteDto routeDto, Model model) {
        try {
            routeService.save(routeDto);
        } catch (OptimisticLockException ex) {
            model.addAttribute("error", "Repeat input please");
            model.addAttribute("routes", routeService.findAll());
            model.addAttribute("routeDto", new RouteDto());
            return "home";
        }
        model.addAttribute("routes", routeService.findAll());
        return "redirect:/";
    }

    @PostMapping("/calculateRoute")
    public String calculateRoute(@ModelAttribute RouteDto routeDto, Model model) {
        final List<Pair<List<String>, Double>> routes = routeService.calculateAllRoutes(routeDto.getCityA(), routeDto.getCityB());
        if (routes == null) {
            model.addAttribute("error", "No connection between cities");
        }
        model.addAttribute("paths", routes);
        model.addAttribute("routeDto", new RouteDto());
        model.addAttribute("routes", routeService.findAll());
        return "home";
    }

}
