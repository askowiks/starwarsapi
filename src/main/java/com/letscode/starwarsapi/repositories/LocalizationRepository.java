package com.letscode.starwarsapi.repositories;

import com.letscode.starwarsapi.models.entities.Localization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocalizationRepository extends JpaRepository<Localization,Long> {
}
