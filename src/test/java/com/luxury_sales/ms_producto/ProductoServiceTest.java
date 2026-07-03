package com.luxury_sales.ms_producto;

import com.luxury_sales.ms_producto.dto.ProductoRequestDTO;
import com.luxury_sales.ms_producto.dto.ProductoResponseDTO;
import com.luxury_sales.ms_producto.model.Producto;
import com.luxury_sales.ms_producto.repository.ProductoRepository;
import com.luxury_sales.ms_producto.service.ProductoService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;

    @InjectMocks
    private ProductoService productoService;

    private Producto producto1;
    private Producto producto2;
    private Producto producto3;
    private ProductoRequestDTO requestDTO;

    @BeforeEach
    void setUp() {
        // precio es double, cantidad es Integer — igual que tu Producto.java
        producto1 = new Producto();
        producto1.setId(1L);
        producto1.setNombre("Pastillas de Freno");
        producto1.setPrecio(45990.0);
        producto1.setCantidad(20);

        producto2 = new Producto();
        producto2.setId(2L);
        producto2.setNombre("Amortiguador Hidraulico");
        producto2.setPrecio(130000.0);
        producto2.setCantidad(15);

        producto3 = new Producto();
        producto3.setId(3L);
        producto3.setNombre("Filtro de Aceite Simple");
        producto3.setPrecio(12000.0);
        producto3.setCantidad(50);

        // precio es Double, cantidad es Integer — igual que tu ProductoRequestDTO.java
        requestDTO = new ProductoRequestDTO();
        requestDTO.setNombre("Pastillas de Freno");
        requestDTO.setPrecio(45990.0);
        requestDTO.setCantidad(20);
    }

    // --- TEST 1: obtener todos retorna los 3 productos ---
    @Test
    void obtenerTodos_debeRetornarLosTresProductos() {
        // GIVEN
        when(productoRepository.findAll())
            .thenReturn(Arrays.asList(producto1, producto2, producto3));

        // WHEN
        List<ProductoResponseDTO> resultado = productoService.obtenerTodos();

        // THEN
        assertNotNull(resultado);
        assertEquals(3, resultado.size());
        assertEquals("Pastillas de Freno", resultado.get(0).getNombre());
        assertEquals("Amortiguador Hidraulico", resultado.get(1).getNombre());
        assertEquals("Filtro de Aceite Simple", resultado.get(2).getNombre());
        verify(productoRepository, times(1)).findAll();
    }

    // --- TEST 2: obtener Pastillas de Freno por ID 1 ---
    @Test
    void obtenerPorId_pastillasDeFreno_debeRetornarProductoCorrecto() {
        // GIVEN
        when(productoRepository.findById(1L)).thenReturn(Optional.of(producto1));

        // WHEN
        Optional<ProductoResponseDTO> resultado = productoService.obtenerPorId(1L);

        // THEN
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        assertEquals("Pastillas de Freno", resultado.get().getNombre());
        assertEquals(45990.0, resultado.get().getPrecio());
        assertEquals(20, resultado.get().getCantidad());
    }

    // --- TEST 3: obtener Amortiguador por ID 2 ---
    @Test
    void obtenerPorId_amortiguador_debeRetornarPrecioYCantidadCorrectos() {
        // GIVEN
        when(productoRepository.findById(2L)).thenReturn(Optional.of(producto2));

        // WHEN
        Optional<ProductoResponseDTO> resultado = productoService.obtenerPorId(2L);

        // THEN
        assertTrue(resultado.isPresent());
        assertEquals(130000.0, resultado.get().getPrecio());
        assertEquals(15, resultado.get().getCantidad());
    }

    // --- TEST 4: obtener producto que NO existe devuelve empty ---
    @Test
    void obtenerPorId_cuandoNoExiste_debeRetornarEmpty() {
        // GIVEN
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN
        Optional<ProductoResponseDTO> resultado = productoService.obtenerPorId(99L);

        // THEN
        assertFalse(resultado.isPresent());
    }

    // --- TEST 5: guardar nuevo producto ---
    @Test
    void guardar_nuevoProducto_debeRetornarProductoGuardado() {
        // GIVEN
        when(productoRepository.save(any(Producto.class))).thenReturn(producto1);

        // WHEN
        ProductoResponseDTO resultado = productoService.guardar(requestDTO);

        // THEN
        assertNotNull(resultado);
        assertEquals("Pastillas de Freno", resultado.getNombre());
        assertEquals(45990.0, resultado.getPrecio());
        assertEquals(20, resultado.getCantidad());
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    // --- TEST 6: actualizar Filtro de Aceite (ID 3) ---
    @Test
    void actualizar_filtroDeAceite_debeActualizarDatosCorrectamente() {
        // GIVEN
        when(productoRepository.findById(3L)).thenReturn(Optional.of(producto3));
        when(productoRepository.save(any(Producto.class))).thenReturn(producto3);

        ProductoRequestDTO updateDTO = new ProductoRequestDTO();
        updateDTO.setNombre("Filtro de Aceite Simple");
        updateDTO.setPrecio(12000.0);
        updateDTO.setCantidad(30); // actualizamos cantidad de 50 a 30

        // WHEN
        ProductoResponseDTO resultado = productoService.actualizar(3L, updateDTO);

        // THEN
        assertNotNull(resultado);
        verify(productoRepository, times(1)).findById(3L);
        verify(productoRepository, times(1)).save(any(Producto.class));
    }

    // --- TEST 7: actualizar producto inexistente lanza excepción ---
    @Test
    void actualizar_cuandoNoExiste_debeLanzarExcepcionConMensaje() {
        // GIVEN
        when(productoRepository.findById(99L)).thenReturn(Optional.empty());

        // WHEN / THEN
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            productoService.actualizar(99L, requestDTO)
        );
        // verifica que el mensaje incluye el ID y "no encontrado"
        assertTrue(ex.getMessage().contains("99"));
        assertTrue(ex.getMessage().contains("no encontrado"));
    }

    // --- TEST 8: eliminar Amortiguador (ID 2) exitosamente ---
    @Test
    void eliminar_amortiguador_debeEjecutarsesinError() {
        // GIVEN
        when(productoRepository.existsById(2L)).thenReturn(true);
        doNothing().when(productoRepository).deleteById(2L);

        // WHEN
        assertDoesNotThrow(() -> productoService.eliminar(2L));

        // THEN
        verify(productoRepository, times(1)).deleteById(2L);
    }

    // --- TEST 9: eliminar producto inexistente lanza excepción ---
    @Test
    void eliminar_cuandoNoExiste_debeLanzarExcepcionConMensaje() {
        // GIVEN
        when(productoRepository.existsById(99L)).thenReturn(false);

        // WHEN / THEN
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
            productoService.eliminar(99L)
        );
        assertTrue(ex.getMessage().contains("99"));
        assertTrue(ex.getMessage().contains("no encontrado"));
    }
}