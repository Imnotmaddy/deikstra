package com.itechart.alifanov.deikstra;

import com.itechart.alifanov.deikstra.repository.RouteRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@DataJpaTest
@SpringBootTest
public class RouteRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private RouteRepository routeRepository;

    @Test
    public void whenFindByCity_thenReturnRoute(){

    }
}
