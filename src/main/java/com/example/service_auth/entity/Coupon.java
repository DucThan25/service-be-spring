package com.example.service_auth.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "code"))
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @NotNull
    String name;

    long discountPercentage;
    LocalDate createDate = LocalDate.now();
}
