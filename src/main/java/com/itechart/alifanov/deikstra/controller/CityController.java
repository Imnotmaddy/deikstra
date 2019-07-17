package com.itechart.alifanov.deikstra.controller;

import com.itechart.alifanov.deikstra.service.RouteService;
import com.itechart.alifanov.deikstra.service.dto.RouteDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class CityController {

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
        model.addAttribute("calculatedRoute", routeService.calculateRoute(routeDto));
        return "redirect:/";
    }

}
