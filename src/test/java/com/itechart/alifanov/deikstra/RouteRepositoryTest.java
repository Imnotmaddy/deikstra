package com.itechart.alifanov.deikstra;

import com.itechart.alifanov.deikstra.model.Route;
import com.itechart.alifanov.deikstra.repository.RouteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = DeikstraApplication.class)
public class RouteRepositoryTest {

    @Autowired
    private RouteRepository repository;

    @Test(expected = ObjectOptimisticLockingFailureException.class)
    public void testIfVersionConflict_thenOptimisticExceptionThrown() {
        String cityA = "WhateverName1";
        String cityB = "WhateverName2";
        if (repository.findByCityAAndCityB(cityA, cityB) == null) {
            repository.save(new Route(cityA, cityB, (double) 3));
        }

        Route route1 = repository.findByCityAAndCityB(cityA, cityB);
        Route route2 = repository.findByCityAAndCityB(cityA, cityB);

        route1.setDistance((double) 10);
        route2.setDistance((double) 30);

        repository.save(route1);
        repository.save(route2);
    }
}
