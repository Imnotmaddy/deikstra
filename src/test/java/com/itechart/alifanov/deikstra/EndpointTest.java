package com.itechart.alifanov.deikstra;


import com.itechart.alifanov.deikstra.dto.RouteDto;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DeikstraApplication.class})
@AutoConfigureMockMvc
public class EndpointTest {

    @Autowired
    MockMvc mvc;

    private final static String ROUTE_CALCULATE = "/route/calculate";
    private final static String ROUTE = "/route";

    @Test
    public void testCalculateEndpoint() throws Exception {
        RouteDto route = new RouteDto("WhateverCity1", "WhateverCity2", (double) 0);
        mvc.perform(post(ROUTE).flashAttr("routeDto", route)).andExpect(status().isOk());

        mvc.perform(post(ROUTE_CALCULATE).flashAttr("routeDto", route))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testIfNoPathBetweenCities_thenInternalServerError() throws Exception {
        mvc.perform(post(ROUTE_CALCULATE).flashAttr("routeDto", new RouteDto("whatever", "stillWhatever", (double) 0)))
                .andExpect(status().isInternalServerError())
                .andDo(print());
    }
}


