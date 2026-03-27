package com.empresa.api.model.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 * Clase response, entidad de ordenes.
 * id, id de la nueva orden generada.
 * origin, lugar de origen.
 * destination, ñugar de destino.
 * driver, conductor asignado
 * createdAt, fecha de creación
 * updatedAt, fecha de última actualización
 */
@Entity
@Table(name = "orders")
@Data
@Getter @Setter @NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(generator = "UUID")
    private UUID id;

    @Enumerated(EnumType.STRING)
    private OrderStatus status = OrderStatus.CREATED;

    private String origin;
    private String destination;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private Driver driver;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
