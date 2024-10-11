package org.nurfet.hotelchain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "rooms")
@Entity
public class Room extends AbstractEntity {

    @Serial
    private static final long serialVersionUID = 3L;

    @Column(nullable = false)
    @NotBlank(message = "Пожалуйста, укажите категория номера")
    private String category;

    @Column(precision = 10, scale = 2)
    @NotNull(message = "Пожалуйста, укажите стоимость номера")
    private BigDecimal price;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Пожалуйста, добавьте описание номера")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "hotel_id", nullable = false)
    private Hotel hotel;
}
