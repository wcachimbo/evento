package com.evento.service;

import com.evento.model.Product;
import com.evento.model.ProductDTO;

import java.util.List;

public interface ProductService {
    /**
     * Servicio encargado de realizar la logica del negocio para obtener los productos
     *
     * @param company identificación de la empresa
     * @return Listado de productos
     */
    List<Product> getAllProduct(Long company);

    /**
     * Servicio encargado de obtener la lista de productos con su disponibilidad
     * para una fecha específica dentro de una empresa.
     * Este método recibe un listado de productos de entrada y calcula la
     * disponibilidad real de cada uno, considerando el stock registrado y
     * las cantidades ya ocupadas en órdenes para la fecha indicada.
     * La lógica del servicio se apoya en la capa de persistencia para realizar
     * el cálculo de disponibilidad, y filtra únicamente los productos que
     * pertenecen a la empresa indicada.
     * La disponibilidad de los productos se determina con la siguiente regla:
     * disponibilidad = stock_total - cantidad_ocupada_en_ordenes
     * En caso de no existir stock disponible para la fecha consultada, el
     * servicio puede lanzar una excepción de negocio o retornar una lista vacía,
     * dependiendo de la implementación.
     *
     * @param products Lista de productos a evaluar. A partir de esta lista se
     *                 obtienen los identificadores de los productos a consultar.
     * @param company  Identificador de la empresa para la cual se realiza la consulta.
     * @param date     Fecha para la cual se desea obtener la disponibilidad de los productos,
     *                 representada como timestamp (epoch).
     * @return Lista de {@link Product} con la cantidad total ocupada y la cantidad
     * disponible para la fecha indicada.
     */
    List<Product> getListProduct(List<ProductDTO> products, int company, Integer date);
}
