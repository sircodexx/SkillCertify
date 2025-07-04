package com.skillcert.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// DTO para crear un centro
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateCenterDto {
    
    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 255, message = "El nombre no puede exceder 255 caracteres")
    private String name;
    
    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;
    
    @Size(max = 500, message = "La dirección no puede exceder 500 caracteres")
    private String address;
    
    @Size(max = 100, message = "La ciudad no puede exceder 100 caracteres")
    private String city;
    
    @Size(max = 100, message = "La región no puede exceder 100 caracteres")
    private String region;
    
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Formato de teléfono inválido")
    @Size(max = 20, message = "El teléfono no puede exceder 20 caracteres")
    private String phone;
    
    @Email(message = "Formato de email inválido")
    private String email;
    
    @Size(max = 255, message = "El nombre del director no puede exceder 255 caracteres")
    private String director;
    
    @Min(value = 1, message = "La capacidad debe ser mayor a 0")
    @Max(value = 10000, message = "La capacidad no puede exceder 10000")
    private Integer capacity;
}