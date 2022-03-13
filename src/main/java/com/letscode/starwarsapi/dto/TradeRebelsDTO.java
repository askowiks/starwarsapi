package com.letscode.starwarsapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TradeRebelsDTO {

    private Long id;
    private List<EquipmentRequestDTO> equipmentRequestDTOList;
}
