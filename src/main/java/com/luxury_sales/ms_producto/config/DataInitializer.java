package com.luxury_sales.ms_producto.config;

import com.luxury_sales.ms_producto.model.Producto;
import com.luxury_sales.ms_producto.repository.ProductoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j

public class DataInitializer implements CommandLineRunner {

    
    private final ProductoRepository  productoRepository; 

    @Override
    public void run(String... args) throws Exception {
        if (productoRepository.count() == 0) {
            productoRepository.save(new Producto("Pastillas de Freno Delanteras", 45990, 20));
            productoRepository.save(new Producto("Amortiguador Hidráulico", 130000, 15));
            productoRepository.save(new Producto("Filtro de Aceite Sintético", 12000, 50));
            
            System.out.println(">>> ms-producto: 3 repuestos de autos insertados");
        }
    }
}