package com.letscode.starwarsapi.repositories;

import com.letscode.starwarsapi.models.entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EquipmentRepository extends JpaRepository<Equipment,Long> {
}
