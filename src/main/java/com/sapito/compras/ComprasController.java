/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sapito.compras;

import com.sapito.db.dao.GenericDao;
import com.sapito.db.entities.OrdenCompra;
import com.sapito.db.entities.ProducoProveedor;
import com.sapito.db.entities.Producto;
import com.sapito.db.entities.ProductoComprado;
import com.sapito.db.entities.ProductoProveedor;
import com.sapito.db.entities.Proveedor;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author giovanni
 */
@Controller
public class ComprasController
{

    private GenericDao<Proveedor> daoProveedor;
    private GenericDao<Producto> daoProducto;
    private GenericDao<ProductoProveedor> daoProductoProveedor;
    private GenericDao<OrdenCompra> daoOrdenCompra;
    private GenericDao<ProductoComprado> daoInfoProveedor;

    //Set
    @Autowired
    public void setDaoProveedor(GenericDao<Proveedor> daoProveedor)
    {
        this.daoProveedor = daoProveedor;
        daoProveedor.setClass(Proveedor.class);
    }

    @Autowired
    public void setDaoProducto(GenericDao<Producto> daoProducto)
    {
        this.daoProducto = daoProducto;
        daoProducto.setClass(Producto.class);
    }

    @Autowired
    public void setDaoProductoProveedor(GenericDao<ProductoProveedor> daoProducoProveedor)
    {
        this.daoProductoProveedor = daoProducoProveedor;
        daoProducoProveedor.setClass(ProductoProveedor.class);
    }

    @Autowired
    public void serDaoOrdenCompra(GenericDao<OrdenCompra> daoOrdenCompra)
    {
        this.daoOrdenCompra = daoOrdenCompra;
        daoOrdenCompra.setClass(OrdenCompra.class);
    }
    @Autowired
    public void serDaoInfoProveedor(GenericDao<ProductoComprado> daoInfoProveedor)
    {
        this.daoInfoProveedor = daoInfoProveedor;
        daoInfoProveedor.setClass(ProductoComprado.class);
    }

    //Alta Proveedor//
    @RequestMapping(value = "compras/altaproveedor", method = RequestMethod.GET)
    public String altaproveedor(Model model)
    {
        Proveedor proveedor = new Proveedor();

        model.addAttribute("proveedor", proveedor);
        model.addAttribute("showSaveConfirmation", "false");
        return "Compras/altaproveedor";
    }

    @RequestMapping(value = "compras/altaproveedor", method = RequestMethod.POST)
    public String regproveedor(Model model, @Valid Proveedor proveedor, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            System.out.println("Invalid with: " + bindingResult.getErrorCount() + " errors");
            System.out.println("Error: " + bindingResult.getFieldError().getField());
            model.addAttribute("showSaveConfirmation", "false");
            return "Compras/altaproveedor";
        } else
        {
            proveedor.setStatus(true);
            daoProveedor.create(proveedor);

//            List<Proveedor> proveedores = daoProveedor.findAll();
//            model.addAttribute("proveedores", proveedores);
//            return "Compras/consultaproveedor";
            model.addAttribute("showSaveConfirmation", "true");
            return "Compras/altaproveedor";
        }
    }

    //Consulta Proveedor//
    @RequestMapping(value = "compras/consultaproveedor", method = RequestMethod.GET)
    public String buscarProveedor(Model model)
    {
        List<Proveedor> proveedor = daoProveedor.findAll();

        if(proveedor != null && proveedor.size() > 0)
        {
            model.addAttribute("proveedores", proveedor);
            return "Compras/consultaproveedor";
        } else
        {
            model.addAttribute("proveedores", new ArrayList<Proveedor>());
            return "Compras/consultaproveedor";
        }

    }

    //Modificar Proveedor //
    @RequestMapping(value = "compras/modificarproveedor", method = RequestMethod.GET)
    public String modificarproveedor(Model model, String idproveedor)
    {
        try
        {
            Long.valueOf(idproveedor);
        } catch(NumberFormatException ex)
        {
            return null;
        }

        Proveedor proveedor = (Proveedor) daoProveedor.find(Long.valueOf(idproveedor));
        model.addAttribute("proveedor", proveedor);
        return "Compras/modificarproveedor";
    }

    @RequestMapping(value = "compras/modificarproveedor", method = RequestMethod.POST)
    public String modproveedor(Model model, @Valid Proveedor proveedor, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            System.out.println("Invalid with: " + bindingResult.getErrorCount() + " errors");
            System.out.println("Error: " + bindingResult.getFieldError().getField());
            return "Compras/modificarproveedor";
        } else
        {
            Proveedor proveedor2 = (Proveedor) daoProveedor
                    .find(Long.valueOf(proveedor.getId()));

            proveedor2.setApellidoMaternoContacto(proveedor.getApellidoMaternoContacto());
            proveedor2.setApellidoPaternoContacto(proveedor.getApellidoPaternoContacto());
            proveedor2.setCalle(proveedor.getCalle());
            proveedor2.setColonia(proveedor.getColonia());
            proveedor2.setCp(proveedor.getCp());
            proveedor2.setEmail(proveedor.getEmail());
            proveedor2.setEmpresa(proveedor.getEmpresa());
            proveedor2.setEstado(proveedor.getEstado());
            proveedor2.setExtension1(proveedor.getExtension1());
            proveedor2.setExtension2(proveedor.getExtension2());
            proveedor2.setMunicipio(proveedor.getMunicipio());
            proveedor2.setNombreContacto(proveedor.getNombreContacto());
            proveedor2.setNumeroE(proveedor.getNumeroE());
            proveedor2.setNumeroI(proveedor.getNumeroI());
            proveedor2.setPais(proveedor.getPais());

            daoProveedor.edit(proveedor2);

            List<Proveedor> proveedores = daoProveedor.findAll();
            model.addAttribute("proveedores", proveedores);
            return "Compras/consultaproveedor";
        }
    }

    //Fin Modificar Proveedor//
    //Consulta Producto//
    @RequestMapping(value = "compras/ConsultaProducto", method = RequestMethod.GET)
    public String buscarProducto(Model model)
    {
        List<Producto> producto = daoProducto.findAll();

        if(producto != null && producto.size() > 0)
        {
            model.addAttribute("producto1", producto);
            return "Compras/ConsultaProducto";
        } 
        else
        {
            model.addAttribute("producto1", new ArrayList<Producto>());
            return "Compras/ConsultaProducto";
        }

    }

    //Alta Producto
    @RequestMapping(value = "compras/AltaProducto", method = RequestMethod.GET)
    public String altaproducto(Model model)
    {
        Producto producto = new Producto();

        Map selectCategoria = new HashMap<>();
        selectCategoria.put("MATERIAPRIMA", "Materia prima");
        selectCategoria.put("ACTIVOFIJO", "Activo fijo");
        model.addAttribute("selectCategoria", selectCategoria);

        model.addAttribute("producto", producto);
        model.addAttribute("showConfirm", "false");
        return "Compras/AltaProducto";
    }

    @RequestMapping(value = "compras/AltaProducto", method = RequestMethod.POST)
    public String regproducto(Model model, @Valid Producto producto, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            System.out.println("Invalid with: " + bindingResult.getErrorCount() + " errors");
            System.out.println("Error: " + bindingResult.getFieldError().getField());
            Map selectCategoria = new HashMap<>();
            selectCategoria.put("MATERIAPRIMA", "Materia prima");
            selectCategoria.put("ACTIVOFIJO", "Activo fijo");
            model.addAttribute("selectCategoria", selectCategoria);
            model.addAttribute("showConfirm", "false");
            return "Compras/AltaProducto";
        } 
        else
        {
            daoProducto.create(producto);

            List<Producto> producto1 = daoProducto.findAll();
            model.addAttribute("producto1", producto1);
            model.addAttribute("showConfirm", "true");
            return "Compras/AltaProducto";
        }
    }
//Fin Alta Producto

    @RequestMapping(value = "compras/productoproveedor", method = RequestMethod.GET)
    public String regProductoProveedor(Model model, @RequestParam String idProducto)
    {
        ProductoProveedor productoProveedor = new ProductoProveedor();
        model.addAttribute("productoProveedor", productoProveedor);

        // Tipos de unidad
        Map unidades = new HashMap();
        unidades.put("CAJA", "CAJA");
        unidades.put("PIEZA", "PIEZA");
        unidades.put("LOTE", "LOTE");

        // Producto
        Producto producto = (Producto) daoProducto.find(Long.valueOf(idProducto));
        productoProveedor.setProducto(producto);

        // Lista de proveedores
        List<Proveedor> proveedores = daoProveedor.findAll();

        model.addAttribute("unidades", unidades);
        model.addAttribute("producto", producto);
        model.addAttribute("proveedores", proveedores);
        return "Compras/ProductoProveedor";
    }

    @RequestMapping(value = "compras/productoproveedor", method = RequestMethod.POST)
    public String regProductoProveedorPost(Model model, @Valid ProductoProveedor productoProveedor,
            BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            System.out.println("Invalid with: " + bindingResult.getErrorCount() + " errors");
            System.out.println("Error: " + bindingResult.getFieldError().getField());
            return "Compras/ProductoProveedor";
        } 
        else
        {
            Proveedor proveedor = (Proveedor) daoProveedor
                    .find(productoProveedor.getProveedor().getId());
            Producto producto = (Producto) daoProducto
                    .find(productoProveedor.getProducto().getId());

            productoProveedor.setProducto(producto);
            productoProveedor.setProveedor(proveedor);
            daoProductoProveedor.create(productoProveedor);

            List<Producto> producto1 = daoProducto.findAll();
            model.addAttribute("producto1", producto1);
            return "Compras/ConsultaProducto";
        }
    }

    @RequestMapping(value = "compras/inhabilitarproductoproveedor", method = RequestMethod.GET)
    @ResponseBody
    public ProductoProveedor inhabilitarProdProv(Model model, @RequestParam String idProdProv)
    {
        try
        {
            Long.valueOf(idProdProv);
        } catch(NumberFormatException ex)
        {
            return null;
        }

        ProductoProveedor prodProv = (ProductoProveedor) daoProductoProveedor
                .find(Long.valueOf(idProdProv));

        if(prodProv != null)
        {
            System.out.println("Cambiando status");
            prodProv.setStatus(false);
            daoProductoProveedor.edit(prodProv);
            return prodProv;
        } else
        {
            System.out.println("No inhabilita");
            return null;
        }
    }

    //Orden de Compra 
    @RequestMapping(value = "compras/ordenCompra", method = RequestMethod.GET)
    public String ordenCompra(Model model)
    {
        return "Compras/ordenCompra";
    }

    @RequestMapping(value = "compras/buscarProducto", method = RequestMethod.GET)
    public @ResponseBody
    Producto buscarProducto(Model model, String idp)
    {
        try
        {
            Long.valueOf(idp);
        } catch(NumberFormatException ex)
        {
            return null;
        }
        Producto producto = (Producto) daoProducto.find(Long.valueOf(idp));
        return (producto != null) ? producto : null;
        //System.out.println("To find: " + idp);
        //return (ProductoProveedor) daoProductoProveedor.find(Long.valueOf(idp));
    }

    @RequestMapping(value = "compras/proveedorproducto", method = RequestMethod.GET)
    public @ResponseBody
    Proveedor proveedorDeProducto(Model model, String idProductoProveedor)
    {
        try
        {
            Long.valueOf(idProductoProveedor);
        } catch(NumberFormatException ex)
        {
            return null;
        }
        ProductoProveedor producto = (ProductoProveedor) daoProductoProveedor
                .find(Long.valueOf(idProductoProveedor));
        return producto.getProveedor();
    }

    //Consulta Ordenes
    @RequestMapping(value = "compras/ConsultarOrdenes", method = RequestMethod.GET)
    public String consultarOrdenes(Model model)
    {
        List<OrdenCompra> ordenes = daoOrdenCompra.findAll();

        model.addAttribute("ordenes", ordenes);
        return "Compras/ConsultarOrdenes";
    }
    
    @RequestMapping(value = "compras/informacionproveedor", method = RequestMethod.GET)
    public String buscarInfoProveedor(Model model) {
        List<ProductoComprado> infoproveedor = daoInfoProveedor.findAll();
        List<Proveedor> cantProveedores = daoProveedor.findAll();
        if (infoproveedor != null && infoproveedor.size() > 0) {
            double Arregl[][] = new double[cantProveedores.size()][2];
            int contador = 0;
            for (Iterator iterator = cantProveedores.listIterator(); iterator.hasNext();) {
                Proveedor proveedor = (Proveedor) iterator.next();
                if (contador != cantProveedores.size()) {
                    Arregl[contador][0] = proveedor.getId();
                    contador++;
                }

            }
            for (Iterator iterador = infoproveedor.listIterator(); iterador.hasNext();) {
                ProductoComprado porproveedor = (ProductoComprado) iterador.next();//fecha_pedido
                for (int i = 0; i < cantProveedores.size(); i++) {
                    if (porproveedor.getProductoProveedor().getProveedor().getId() == Arregl[i][0]) {
                        double totalcomprados = porproveedor.getCantidad() * porproveedor.getProductoProveedor().getCosto();
                        Arregl[i][1] = totalcomprados + Arregl[i][1];
                    }
                }
            }
            String mejorprovee="", segundomejor="", terceromejor="";
            double primero = Arregl[0][1], segundo = 0, tercero = 0;
            int quitarprimero = 0, quietarsegundo = 0, quietartercero=0;
            for (int i = 0; i < cantProveedores.size(); i++) {
                if (primero < Arregl[i][0]) {
                    primero = Arregl[i][0];
                    quitarprimero = i;
                }
            }
            for (int i = 0; i < cantProveedores.size(); i++) {
                if (segundo < Arregl[i][0] && i != quitarprimero) {
                    segundo = Arregl[i][0];
                    quietarsegundo = i;
                }
            }
            for (int i = 0; i < cantProveedores.size(); i++) {
                if (tercero < Arregl[i][0] && i != quitarprimero && i != quietarsegundo) {
                    tercero = Arregl[i][0];
                    quietartercero=i;
                }
            }
            for (Iterator iterator = cantProveedores.listIterator(); iterator.hasNext();) {
                Proveedor proveedor = (Proveedor) iterator.next();
                if (Arregl[quitarprimero][0]==proveedor.getId()) {
                    mejorprovee=proveedor.getEmpresa();
                }
                if (Arregl[quietarsegundo][0]==proveedor.getId()) {
                    segundomejor=proveedor.getEmpresa();
                }
                if (Arregl[quietartercero][0]==proveedor.getId()) {
                    terceromejor=proveedor.getEmpresa();
                }

            }
            System.out.println(primero);
            System.out.println(segundo);
            System.out.println(tercero);
            System.out.println(mejorprovee);
            System.out.println(segundomejor);
            System.out.println(terceromejor);
            model.addAttribute("primero", primero);
            model.addAttribute("segundo", segundo);
            model.addAttribute("tercero", tercero);
            model.addAttribute("MejorProv", mejorprovee);
            model.addAttribute("SegundoMejor", segundomejor);
            model.addAttribute("TercerMejor", terceromejor);
            return "Compras/informacionproveedor";
        } else {
            model.addAttribute("infoproveedor", new ArrayList<Proveedor>());
            return "Compras/informacionproveedor";
        }

    }

    

    @RequestMapping(value = "compras", method = RequestMethod.GET)
    public String index(Model model)
    {
        return "Compras/indexcompras";
    }

    @RequestMapping(value = "compras/ordencompra", method = RequestMethod.POST)
    @ResponseBody
    public OrdenCompra crearOrdenCompra(Model model, @RequestBody OrdenCompraTransport ordenCompraTransport)
    {
        OrdenCompra ordenCompra = new OrdenCompra();

        // Productos en la orden
        List<ProductoComprado> productosEnOrden = new ArrayList<>();
        for(ProductoEnOrdenCompra productoEnOrdenCompra : ordenCompraTransport.getProductosEnOrden())
        {
            ProductoProveedor productoProveedor = (ProductoProveedor) daoProductoProveedor
                    .find(productoEnOrdenCompra.getIdProductoProveedor());

            ProductoComprado productoComprado = new ProductoComprado();
            productoComprado.setProductoProveedor(productoProveedor);
            productoComprado.setCantidad(productoEnOrdenCompra.getCantidad());
            productoComprado.setOrdenCompra(ordenCompra);

            productosEnOrden.add(productoComprado);
        }

        // Datos de la orden de compra
        ordenCompra.setProductosEnOrden(productosEnOrden);
        ordenCompra.setAprobada(false);
        ordenCompra.setCostoTotal(ordenCompraTransport.getMontoTotal());
        ordenCompra.setFechaPedido(new Date());
        ordenCompra.setFormaPago(ordenCompraTransport.getFormaPago());

        daoOrdenCompra.create(ordenCompra);
        return ordenCompra;
    }

    @RequestMapping(value = "confirmacionProducto", method = RequestMethod.GET)
    public String confirmacionProducto(Model model)
    {
        return "Compras/confirmacionProducto";
    }

    @RequestMapping(value = "modificarproveedor", method = RequestMethod.GET)
    public String modificarproveedor(Model model)
    {
        return "Compras/modificarproveedor";
    }

    @RequestMapping(value = "informacionproveedor", method = RequestMethod.GET)
    public String informacion1proveedor(Model model)
    {
        return "Compras/informacionproveedor";
    }

    @RequestMapping(value = "Consulta1Orden", method = RequestMethod.GET)
    public String Consulta1Orden(Model model)
    {
        return "Compras/Consulta1Orden";
    }

    @RequestMapping(value = "modificaOrden", method = RequestMethod.GET)
    public String modificaOrden(Model model)
    {
        return "Compras/modificaOrden";
    }

    @RequestMapping(value = "confirmacionProductoEliminacion", method = RequestMethod.GET)
    public String confirmacionProductoEliminacion(Model model)
    {
        return "Compras/confirmacionProductoEliminacion";
    }

    @RequestMapping(value = "confirmacionProductoModificacion", method = RequestMethod.GET)
    public String confirmacionProductModificacion(Model model)
    {
        return "Compras/confirmacionProductoModificacion";
    }

    @RequestMapping(value = "ConsultaProducto", method = RequestMethod.GET)
    public String ConsultaProducto(Model model)
    {
        return "Compras/ConsultaProducto";

    }

    @RequestMapping(value = "Consulta1Producto", method = RequestMethod.GET)
    public String Consulta1Producto(Model model)
    {
        return "Compras/Consulta1Producto";
    }

    //Modifica Producto
    @RequestMapping(value = "compras/Modifica1Producto", method = RequestMethod.GET)
    public String Modifica1Producto(Model model, @RequestParam String idproducto)
    {

        Map selectCategoria = new HashMap<>();
        selectCategoria.put("MATERIAPRIMA", "Materia prima");
        selectCategoria.put("ACTIVOFIJO", "Activo fijo");
        model.addAttribute("selectCategoria", selectCategoria);

        try
        {
            Long.valueOf(idproducto);
        } catch(NumberFormatException ex)
        {
            return null;
        }
        Producto producto = (Producto) daoProducto.find(Long.valueOf(idproducto));
        model.addAttribute("producto", producto);

        return "Compras/Modifica1Producto";
    }

    @RequestMapping(value = "compras/Modifica1Producto", method = RequestMethod.POST)
    public String modproducto(Model model, @Valid Producto prodi2, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            System.out.println("Invalid with: " + bindingResult.getErrorCount() + " errors");
            System.out.println("Error: " + bindingResult.getFieldError().getField());

            Map selectCategoria = new HashMap<>();
            selectCategoria.put("MATERIAPRIMA", "Materia prima");
            selectCategoria.put("ACTIVOFIJO", "Activo fijo");
            model.addAttribute("selectCategoria", selectCategoria);
            return "Compras/Modifica1Producto";
        } 
        else
        {
            Producto prodi3 = (Producto) daoProducto.find(prodi2.getId());
            prodi3.setCategoria(prodi2.getCategoria());
            prodi3.setDescripcion(prodi2.getDescripcion());
            prodi3.setMarca(prodi2.getMarca());
            prodi3.setNombreProducto(prodi2.getNombreProducto());
            daoProducto.edit(prodi2);

            List<Producto> prodi4 = daoProducto.findAll();
            model.addAttribute("producto1", prodi4);
            return "Compras/ConsultaProducto";
        }
    }
//Modifica Producto FIN

    @RequestMapping(value = "InformacionProducto", method = RequestMethod.GET)
    public String InformacionProducto(Model model)
    {
        return "Compras/InformacionProducto";
    }

    @RequestMapping(value = "confirmacionProveedor", method = RequestMethod.GET)
    public String confirmacionProveedor(Model model)
    {
        return "Compras/confirmacionProveedor";
    }

    @RequestMapping(value = "confirmacionProveedorMod", method = RequestMethod.GET)
    public String confirmacionProveedorMod(Model model)
    {
        return "Compras/confirmacionProveedorMod";
    }

    @RequestMapping(value = "confirmacionProveedorEli", method = RequestMethod.GET)
    public String confirmacionProveedorEli(Model model)
    {
        return "Compras/confirmacionProveedorEli";
    }

}
