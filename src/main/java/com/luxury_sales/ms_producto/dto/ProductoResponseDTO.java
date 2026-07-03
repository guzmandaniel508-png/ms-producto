package com.luxury_sales.ms_producto.dto;

import lombok.Data;

@Data
public class ProductoResponseDTO {
    
    private Long id;        
    private String nombre; 
    private double precio; 
    private Integer cantidad; 
}