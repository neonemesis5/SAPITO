/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sapito.db.entities;

import com.sapito.db.util.RExp;
import com.sapito.db.util.RExpErrors;
import java.io.Serializable;

import javax.persistence.Basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;


/**
 *
 * @author chef
 */
@Entity
@Table(name = "OrdenCompra")
public class OrdenCompra implements Serializable {

    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotNull
    @Column(name = "Folio")
    @Pattern(regexp = RExp.letrasBasicasDigitos, message = RExpErrors.letrasBasicasDigitos)
    private String Folio;

    @Basic(optional = false)
    @NotNull
    @Column(name = "fechaPedido")
    
    @Temporal(TemporalType.DATE)
    private java.util.Date fechaPedido;
    
    @Basic(optional = false)
    @Column(name = "fechaEntrega")
    
    @Temporal(TemporalType.DATE)
    private java.util.Date fechaEntrega;
    
    @NotNull
    @Column(name = "costoTotal")
    
    private double costoTotal;

    @NotNull
    @Size(min=1, max=500, message = "Seleccione una forma de pago")
    @Column(name = "formapago")
    private String formapago;
    
    @NotNull
    
    @Column(name = "aprobada")
    private boolean aprobada;
    
    @OneToOne
    @JoinColumn(name = "ORDENID_PROVEEDOR")
    private ProducoProveedor proveedor;
    
    @OneToOne(mappedBy = "ordendcompra")
    private ProductoEnOrden describirorden;

    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the Folio
     */
    public String isFolio() {
        return getFolio();
    }

    /**
     * @param Folio the Folio to set
     */
    public void setFolio(String Folio) {
        this.Folio = Folio;
    }

    /**
     * @return the fechaOrden
     */
    public java.util.Date getFechaOrden() {
        return fechaPedido;
    }

    /**
     * @param fechaOrden the fechaOrden to set
     */
    public void setFechaOrden(java.util.Date fechaOrden) {
        this.fechaPedido = fechaOrden;
    }

    /**
     * @return the fechaEntrega
     */
    public java.util.Date getFechaEntrega() {
        return fechaEntrega;
    }

    /**
     * @param fechaEntrega the fechaEntrega to set
     */
    public void setFechaEntrega(java.util.Date fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }

    /**
     * @return the formapago
     */
    public String getFormapago() {
        return formapago;
    }

    /**
     * @param formapago the formapago to set
     */
    public void setFormapago(String formapago) {
        this.formapago = formapago;
    }

    /**
     * @return the proveedor
     */
    public ProducoProveedor getProveedor() {
        return proveedor;
    }

    /**
     * @param proveedor the proveedor to set
     */
    public void setProveedor(ProducoProveedor proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * @return the describirorden
     */
    public ProductoEnOrden getDescribirorden() {
        return describirorden;
    }

    /**
     * @param describirorden the describirorden to set
     */
    public void setDescribirorden(ProductoEnOrden describirorden) {
        this.describirorden = describirorden;
    }

    /**
     * @return the Folio
     */
    public String getFolio() {
        return Folio;
    }

    /**
     * @return the costoTotal
     */
    public double getCostoTotal() {
        return costoTotal;
    }

    /**
     * @param costoTotal the costoTotal to set
     */
    public void setCostoTotal(double costoTotal) {
        this.costoTotal = costoTotal;
    }

    /**
     * @return the aprobacion
     */
    public boolean isAprobacion() {
        return aprobada;
    }

    /**
     * @param aprobacion the aprobacion to set
     */
    public void setAprobacion(boolean aprobacion) {
        this.aprobada = aprobacion;
    }

}
