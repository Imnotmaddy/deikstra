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
@SpringBootTest
@AutoConfigureMockMvc
public class EndpointTest {

    @Autowired
    MockMvc mvc;

    private final static String POST_ENDPOINT = "/route/calculate";

    @Test
    public void testCalculateEndpoint() throws Exception {
        mvc.perform(post(POST_ENDPOINT).flashAttr("routeDto", new RouteDto("Tokyo", "Polotsk", (double) 0)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testIfNoPathBetweenCities() throws Exception {
        mvc.perform(post(POST_ENDPOINT).flashAttr("routeDto", new RouteDto("fakecity", "anotherfakecirt", (double) 0)))
                .andExpect(status().isImUsed())
                .andDo(print());
    }

}


