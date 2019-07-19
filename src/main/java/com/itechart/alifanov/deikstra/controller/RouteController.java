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
        routeService.save(routeDto);
        model.addAttribute("routes", routeService.findAll());
        return "redirect:/";
    }

    @PostMapping("/calculateRoute")
    public String calculateRoute(@ModelAttribute RouteDto routeDto, Model model) {
        final Pair<List<String>, Double> path = routeService.calculateRoute(routeDto.getCityA(), routeDto.getCityB());
        if (path != null) {
            model.addAttribute("path", path.getKey());
            model.addAttribute("distance", path.getValue());
        } else {
            model.addAttribute("error", "No connection between cities");
        }
        model.addAttribute("routeDto", new RouteDto());
        model.addAttribute("routes", routeService.findAll());
        return "home";
    }

}
