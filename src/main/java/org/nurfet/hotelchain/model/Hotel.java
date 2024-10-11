package org.nurfet.hotelchain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hotels")
@Entity
public class Hotel extends AbstractEntity {

    @Serial
    private static final long serialVersionUID = 2L;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Пожалуйста, укажите название отеля")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Пожалуйста, укажите расположение отеля")
    private String location;

    @ElementCollection
    @NotEmpty(message = "Пожалуйста, укажите сервисы, которые предоставляет отель")
    private List<String> services;

    @Column(precision = 2, scale = 1)
    @DecimalMin(value = "0.1", message = "Рейтинг не может быть меньше 0.1")
    @DecimalMax(value = "5.0", message = "Рейтинг не может быть больше 5")
    @NotNull(message = "Пожалуйста, укажите рейтинг отеля")
    private BigDecimal rating;

    @Column(columnDefinition = "TEXT")
    @NotBlank(message = "Пожалуйста, дайте описание отеля")
    private String description;

    @OneToMany(mappedBy = "hotel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Room> rooms;
}
