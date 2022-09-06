package com.cesar.demo;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Role;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest // Anotación que permite probar datos con la base de datos
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
// Anotación que ya no hace uso de la base de datos h2 y hacer las operaciones a la base de datos
// que estemos ocupando
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductoTests {

    @Autowired // Anotacion que permite inyectar dependencias
    private ProductoRepositorio productoRepositorio;

    @Test // Anotación que indica que va a ser un metodo que será testeado
    @Rollback(false) // Anotación que indica que se van deshacer los cambios realizados en la base de datos
    @Order(1) // Anotación que indica el orden de ejecución de las pruebas
    public void testGuardarProducto() {
        Producto producto = new Producto("Televisor Elektra", 3000F);
        Producto productoGuardado = productoRepositorio.save(producto);

        assertNotNull(productoGuardado); // Prueba para confirmar que no se registre vacio

    }

    @Test
    @Order(2) // Anotación que indica el orden de ejecución de las pruebas
    public void testBuscarProductoPorNombre() {
        String nombre = "Televisor Elektra";
        Producto producto = productoRepositorio.findByNombre(nombre);

        assertThat(producto.getNombre()).isEqualTo(nombre);
        // Prueba para confirmar que el nombre del producto es igual a un nombre de la base de datos
    }

    @Test
    @Order(3) // Anotación que indica el orden de ejecución de las pruebas
    public void testBuscarProductoPorNombreNoExistente() {
        String nombre = "Televisor Elektra HD";
        Producto producto = productoRepositorio.findByNombre(nombre);

        assertNull(producto);
        // Prueba para confirmar que el nombre no existe
    }

    @Test
    @Rollback(false)
    @Order(4) // Anotación que indica el orden de ejecución de las pruebas
    public void testActualizarProducto() {
        String nombreProducto = "Televisor HD"; // nuevo valor
        Producto producto = new Producto(nombreProducto, 2500F); // valores nuevos
        producto.setId(1); // id del producto a modificar
        productoRepositorio.save(producto);

        Producto productoActualizado = productoRepositorio.findByNombre(nombreProducto);
        assertThat(productoActualizado.getNombre()).isEqualTo(nombreProducto);
        // Prueba para cofirmar que el nombre escrito es igual al producto actualizado
    }

    @Test
    @Order(5) // Anotación que indica el orden de ejecución de las pruebas
    public void testListarProducto() {
        List<Producto> productos = (List<Producto>) productoRepositorio.findAll();

        for(Producto producto : productos) {
            System.out.println(producto);
        }

        assertThat(productos).size().isGreaterThan(0);
        // Prueba para confirmar que el tamaño de productos es mayor a 0
    }

    @Test
    @Rollback(false)
    @Order(6) // Anotación que indica el orden de ejecución de las pruebas
    public void testEliminarProducto() {
        Integer id = 4;
        boolean existeAntesDeEliminar = productoRepositorio.findById(id).isPresent();
        productoRepositorio.deleteById(id);
        boolean noExisteDespuesDeEliminar = productoRepositorio.findById(id).isPresent();
        assertTrue(existeAntesDeEliminar); // Prueba que permite saber si existe antes de eliminarse
        assertFalse(noExisteDespuesDeEliminar); // Prueba que permite saber si exsite despues de eliminarse
    }
}
