package com.letscode.starwarsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RebelRequest {
    private String name;
    private Integer age;
    private String gender;

    private List<EquipmentRequest> equipmentsRequest = new ArrayList<>();
}
