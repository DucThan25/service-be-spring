package com.example.service_auth.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Brand {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    boolean isActive;
    boolean isDelete;
    LocalDate createDate = LocalDate.now();
}
