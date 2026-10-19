package com.denkitronik.clienteservice.domain.services;

import com.denkitronik.clienteservice.domain.entities.Cliente;
import com.denkitronik.clienteservice.domain.entities.Region;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IClienteService {

    /** Lista todos los clientes con paginación. */
    Page<Cliente> findAll(Pageable pageable);

    /** Devuelve todos los clientes sin paginación. */
    List<Cliente> findAll();

    /**
     * Busca un cliente por ID.
     * @throws com.denkitronik.clienteservice.domain.exception.ClienteNotFoundException si no existe
     */
    Cliente findById(Long id);

    /**
     * Persiste un nuevo cliente.
     * @throws com.denkitronik.clienteservice.domain.exception.ClienteServiceException si falla la base de datos
     */
    Cliente save(Cliente cliente);

    /**
     * Actualiza un cliente existente preservando createAt.
     * @throws com.denkitronik.clienteservice.domain.exception.ClienteNotFoundException si no existe
     */
    Cliente update(Long id, Cliente cliente);

    /**
     * Elimina un cliente por ID.
     * @throws com.denkitronik.clienteservice.domain.exception.ClienteNotFoundException si no existe
     */
    void delete(Long id);

    /** Devuelve todas las regiones disponibles. */
    List<Region> findAllRegiones();
}
