/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sapito.ventas;

import com.sapito.db.dao.GenericDao;
import com.sapito.db.entities.Cliente;
import com.sapito.db.entities.Inventario;
import com.sapito.db.entities.OrdenVenta;
import com.sapito.db.entities.ProductoVendido;
import com.sapito.db.entities.SancionCliente;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author giovanni
 */
@Controller
public class VentasController
{
    private GenericDao<Cliente> daoCliente;
    private GenericDao<Inventario> daoInventario;
    private GenericDao<OrdenVenta> daoOrdenVenta;
    
    @Autowired
    public void setDaoCliente(GenericDao<Cliente> daoCliente)
    {
        this.daoCliente = daoCliente;
        daoCliente.setClass(Cliente.class);
    }
    
    @Autowired
    public void setDaoInventario(GenericDao<Inventario> daoInventario)
    {
        this.daoInventario = daoInventario;
        daoInventario.setClass(Inventario.class);
    }
    
    @Autowired
    public void setDaoOrdenVenta(GenericDao<OrdenVenta> daoOrdenVenta)
    {
        this.daoOrdenVenta = daoOrdenVenta;
        daoOrdenVenta.setClass(OrdenVenta.class);
    }
    
    

    @RequestMapping(value = "ventas", method = RequestMethod.GET)
    public String index(Model model)
    {
        return "Ventas/indexVentas";
    }

    @RequestMapping(value = "ventas/nvocliente", method = RequestMethod.GET)
    public String nvoCliente(Model model)
    {
        Cliente cliente = new Cliente();
        cliente.setStatus(true);
        
        model.addAttribute("cliente", cliente);
        return "Ventas/nvoCliente";
    }
    
    @RequestMapping(value = "ventas/nvocliente", method = RequestMethod.POST)
    public String regNvoCliente(Model model, @Valid Cliente cliente, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
//            System.out.println("Invalid with: " + bindingResult.getErrorCount() + " errors");
//            System.out.println("Error: " + bindingResult.getFieldError().getField());
            return "Ventas/nvoCliente";
        }
        else
        {
            daoCliente.create(cliente);
            
            List<Cliente> clientes = daoCliente.findAll();
            model.addAttribute("clientes", clientes);
            return "Ventas/clientes";
        }
    }

    @RequestMapping(value = "ventas/clientes", method = RequestMethod.GET)
    public String clientes(Model model)
    {
        List<Cliente> clientes = daoCliente.findAll();
        model.addAttribute("clientes", clientes);        
        return "Ventas/clientes";
    }
    
    @RequestMapping(value = "ventas/buscarcliente", method = RequestMethod.GET)
    public @ResponseBody Cliente buscarCliente(Model model, String rfc)
    {
        List<Cliente> clientes = daoCliente
                .findBySpecificField("rfc", rfc.trim(), "equal", null, null);
        
        if(clientes.size() > 0)
        {
            return clientes.get(0);
        }
        else
        {
            return null;
        }
    }
    
    @RequestMapping(value = "ventas/buscarproducto", method = RequestMethod.GET)
    public @ResponseBody Inventario buscarProductoInventario(Model model, String cinventario)
    {
        List<Inventario> productos = daoInventario
                .findBySpecificField("codigoInventario", cinventario.trim(), "equal", null, null);
        
        if(productos.size() > 0)
        {
            return productos.get(0);
        }
        else
        {
            return null;
        }
    }

    @RequestMapping(value = "ventas/vendedores", method = RequestMethod.GET)
    public String vendedores(Model model)
    {
        return "Ventas/vendedores";
    }

    @RequestMapping(value = "ventas/nvaorden")
    public String nvaOrdenVentas(Model model)
    {
        return "Ventas/nvaOrdenVenta";
    }
    
    @RequestMapping(value = "ventas/nvaorden", method = RequestMethod.POST)
    public @ResponseBody OrdenVenta nvaOrdenVenta(
            Model model, @RequestBody OrdenVentaTransport ordenVentaTransport)
    {
//        System.out.println("ClientID:" + ordenVentaTransport.getClientId());
//        System.out.println("Monto: " + ordenVentaTransport.getMonto());
//        System.out.println("We receiVe Cargos Extra: ");
//        for(CargoExtra ce : ordenVentaTransport.getCargosExtra())
//        {
//            System.out.println(ce.getCantidad() + "\t" + ce.getConcepto());
//        }
//        
//        System.out.println("We receive Productos:");
//        for(ProductoEnOrden peo : ordenVentaTransport.getProductosEnOrden())
//        {
//            System.out.println(peo.getIdInventario() + "\t" + peo.getCantidad());
//        }
        Cliente cliente = (Cliente) daoCliente.find(ordenVentaTransport.getClientId());
        
        OrdenVenta orden = new OrdenVenta();
        orden.setCliente(cliente);
        orden.setFechaEntrega(new Date());
        orden.setFechaPedido(new Date());
        orden.setMonto(ordenVentaTransport.getMonto());
        orden.setMontoConCargos(ordenVentaTransport.getMontoConCargos());
        
        // Productos en la orden
        List<ProductoVendido> lpv = new ArrayList<>();
        orden.setProductosVendidos(lpv);
        for(ProductoEnOrden peo : ordenVentaTransport.getProductosEnOrden())
        {
            Inventario producto = (Inventario) daoInventario.find(peo.getIdInventario());
            ProductoVendido pv = new ProductoVendido();
            pv.setCantidad(peo.getCantidad());
            pv.setProductoInventario(producto);
            pv.setOrdenVenta(orden);
            
            orden.getProductosVendidos().add(pv);
        }
        
        // Cargos extra
        List<SancionCliente> cargosExtra = new ArrayList<>();
        orden.setSancionesCliente(cargosExtra);
        for(CargoExtra ce : ordenVentaTransport.getCargosExtra())
        {
            SancionCliente sc = new SancionCliente();
            sc.setDescripcion(ce.getConcepto());
            sc.setMonto(ce.getCantidad());
            sc.setOrdenVenta(orden);
            
            orden.getSancionesCliente().add(sc);
        }
        
        // Persiste and return 201 http status
        daoOrdenVenta.create(orden);
        return orden;
    }

    @RequestMapping(value = "ventas/altaclientes", method = RequestMethod.GET)
    public String altaClientes(Model model)
    {
        return "Ventas/altaClientes";
    }

    @RequestMapping(value = "ventas/ordenes", method = RequestMethod.GET)
    public String ordenes(Model model)
    {
        return "Ventas/ordenes";
    }

    @RequestMapping(value = "ventas/ofertas", method = RequestMethod.GET)
    public String ofertas(Model model)
    {
        return "Ventas/ofertas";
    }

    @RequestMapping(value = "ventas/historial", method = RequestMethod.GET)
    public String historial(Model model)
    {
        return "Ventas/historialVentas";
    }

    @RequestMapping(value = "ventas/facturas", method = RequestMethod.GET)
    public String facturas(Model model)
    {
        return "Ventas/facturas";
    }

    @RequestMapping(value = "ventas/devoluciones", method = RequestMethod.GET)
    public String devolucion(Model model)
    {
        return "Ventas/nvaDevolucion";
    }

    @RequestMapping(value = "ventas/cambios", method = RequestMethod.GET)
    public String cambio(Model model)
    {
        return "Ventas/cambio";
    }
    
    @RequestMapping(value = "ventas/demoin", method = RequestMethod.GET)
    public @ResponseBody List<Inventario> demoInserts(Model model)
    {
        for(Iterator it = daoInventario.findAll().iterator(); it.hasNext();)
        {
            Inventario in = (Inventario) it.next();
            daoInventario.remove(in);
        }
        for(Object c1 : daoCliente.findAll())
        {
            Cliente c = (Cliente) c1;
            daoInventario.remove(c);
        }
        
        // Insert productos
        for(int i=0; i<5; i++)
        {
            Inventario inv = new Inventario();
            inv.setCantidad(i + 10);
            inv.setCategoria("Electronicos");
            inv.setCodigoInventario("ABC123" + i);
            inv.setFechaEntrada(new Date(2015, 02, 25));
            inv.setFechaProduccion(new Date(2014, 01, 05));
            inv.setPrecioUnitario(81.5);
            inv.setMaximo(50);
            inv.setMinimo(i);
            inv.setNombre("Producto" + i);
            inv.setUbicacion("Almacen" + i);
            inv.setTipoProducto("MATERIAPRIMA");
            
            daoInventario.create(inv);
        }
        
        // Insert clientes
        for(int i=0; i<3; i++)
        {
            Cliente cl = new Cliente();
            cl.setApellidoMaternoContacto("Node");
            cl.setApellidoPaternoContacto("Bash");
            cl.setCalle("Nothing");
            cl.setColonia("Some");
            cl.setCp(80800);
            cl.setEmail("cl.test@example.com");
            cl.setEmpresa("Else"+i);
            cl.setPais("Mexico");
            cl.setEstado("Mexico");
            cl.setMunicipio("Metepec");
            cl.setNombreContacto("HeIs");
            cl.setRfc("ABCDEFGHIJKL"+i);
            cl.setStatus(true);
            
            daoCliente.create(cl);
        }
        
        return daoInventario.findAll();
    }
}
