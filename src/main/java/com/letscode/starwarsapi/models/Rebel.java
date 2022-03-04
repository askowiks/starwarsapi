package com.letscode.starwarsapi.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import javax.persistence.*;

@Entity
@Table(name = "rebels_db")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Rebel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    private String gender;

    public static Rebel of(RebelRequest rebelRequest){
        Rebel rebel = new Rebel();
        BeanUtils.copyProperties(rebelRequest,rebel);
        return rebel;
    }
}
