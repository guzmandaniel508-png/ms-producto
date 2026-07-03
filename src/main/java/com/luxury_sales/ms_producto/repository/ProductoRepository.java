package com.luxury_sales.ms_producto.repository;

import com.luxury_sales.ms_producto.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;



public interface ProductoRepository extends JpaRepository<Producto, Long> {
  
}