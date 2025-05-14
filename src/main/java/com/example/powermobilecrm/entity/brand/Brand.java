package com.example.powermobilecrm.entity.brand;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Brand {

    @Id
    private String id;

    @Column(nullable = false)
    private String name;

}
