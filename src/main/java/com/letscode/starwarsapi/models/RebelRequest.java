package com.letscode.starwarsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RebelRequest {
    private String name;
    private Integer age;
    private String gender;
}
