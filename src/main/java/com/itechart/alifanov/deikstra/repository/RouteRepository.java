package com.itechart.alifanov.deikstra.repository;

import com.itechart.alifanov.deikstra.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteRepository extends JpaRepository<Route, Integer> {
}
