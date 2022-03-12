package com.letscode.starwarsapi.repositories;

import com.letscode.starwarsapi.models.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;


public interface EquipmentRepository extends JpaRepository<Equipment,Long> {
}
