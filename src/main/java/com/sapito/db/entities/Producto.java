/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sapito.db.entities;

import com.sapito.db.util.RExp;
import com.sapito.db.util.RExpErrors;
import java.io.Serializable;
import java.util.Collection;
import javax.persistence.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 *
 * @author pablo
 */
@Entity
@Table(name = "Producto")
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column(name = "ID")
    private long id;

    @NotNull
    @Column(name = "Nombre")
    @Pattern(regexp = RExp.letrasAcentuadasPuntos, message = RExpErrors.letrasAcentuadasPuntos)
    @Size(min = 2, max = 100, message = "Debe tener entre 2 y 100 caracteres")
    private String nombreProducto;

    public String getNombreproducto() {
        return nombreProducto;
    }

    public void setNombreproducto(String nombreproducto) {
        this.nombreProducto = nombreproducto;
    }
    
    @NotNull
    @Column(name = "Descripcion")
    @Pattern(regexp = RExp.letrasAcentuadasPuntos, message = RExpErrors.letrasAcentuadasPuntos)
    @Size(min = 2, max = 100, message = "Debe tener entre 2 y 100 caracteres")
    private String descripcion;

    @NotNull
    @Column(name = "Marca")
    @Pattern(regexp = RExp.letrasBasicasDigitos, message = RExpErrors.letrasBasicasDigitos)
    @Size(min = 2, max = 10, message = "Debe tener entre 2 y 10 caracteres")
    private String marca;

    @NotNull
    @Column(name = "Categoria")
    @Pattern(regexp = RExp.letrasBasicas, message = RExpErrors.letrasBasicas)
    private String categoria;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "producto")
    private Collection<ProducoProveedor> productoProveedor;
    


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    /**
     * @return the productoproveedor
     */
    public Collection<ProducoProveedor> getProductoproveedor() {
        return productoProveedor;
    }

    /**
     * @param productoproveedor the productoproveedor to set
     */
    public void setProductoproveedor(Collection<ProducoProveedor> productoproveedor) {
        this.productoProveedor = productoproveedor;
    }

    
}