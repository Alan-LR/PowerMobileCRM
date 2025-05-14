package com.example.powermobilecrm.entity.vehicle;

import com.example.powermobilecrm.dto.vehicle.VehicleRequestDTO;
import com.example.powermobilecrm.entity.brand.Brand;
import com.example.powermobilecrm.entity.model.Model;
import com.example.powermobilecrm.entity.users.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "vehicles")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString(exclude = "user")
@EqualsAndHashCode(of = "id")
public class Vehicle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String plate;

    @Column(name = "advertised_price", nullable = false)
    private BigDecimal advertisedPrice;

    @Column(nullable = false)
    private Integer year;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "brand_id", nullable = false)
    private Brand brand;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "model_id", nullable = false)
    private Model model;

    @Column(name = "fipe_price")
    private BigDecimal fipePrice;

    public Vehicle(VehicleRequestDTO data){
        this.plate = data.plate();
        this.advertisedPrice = data.advertisedPrice();
        this.year = data.year();
    }
}
