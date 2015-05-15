/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.sapito.inventarios;

//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;

import com.sapito.db.dao.GenericDao;
import com.sapito.db.entities.Inventario;
import java.util.List;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller

public class InventariosController

{
    private GenericDao<Inventario> daoInventario;
    
    @Autowired
    public void setDaoInventario(GenericDao<Inventario> daoInventario)
    {
        this.daoInventario = daoInventario;
        daoInventario.setClass(Inventario.class);
    }
    
    
     @RequestMapping(value="inventarios", method=RequestMethod.GET)
    public String indexInventarios(Model model)
    {
        return "Inventarios/index";
    }
    
    @RequestMapping(value="inventarioMinimo", method=RequestMethod.GET)
    public String inventarioMinimo(Model model)
    {
        return "Inventarios/inventarioMinimoView";
    } 
    
    @RequestMapping(value="inventarioMaximo", method=RequestMethod.GET)
    public String inventarioMaximo(Model model)
    {
        return "Inventarios/inventarioMaximoView";
    }
      
    
    @RequestMapping(value="buscarProducto", method=RequestMethod.GET)
    public @ResponseBody Inventario buscarProducto(Model model, String codigoInventario)
    {
        List<Inventario> inventarios = daoInventario
                .findBySpecificField("codigoInventario", codigoInventario.trim(), "equal", null, null);
        
        if(inventarios.size() > 0)
        {
            return inventarios.get(0);
        }
        else
        {
            return null;
        }
        
        
//        public Inventario findInventario(String id) {
//        Query query1 = daoInventario.getEntityMgr().createQuery("SELECT a FROM Inventario a where a.idinventario=:idinventario");
//        query1.setParameter("idinventario", Integer.parseInt(id));
//        List<Inventario> inventario = query1.getResultList();
//        return inventario.get(0);
//    }
    }
    
    
    
    @RequestMapping(value="materiaPrima", method=RequestMethod.GET)
    public String materiaPrima(Model model)
    {
        return "Inventarios/registrarMateriaPrima";
    }
    
    @RequestMapping(value="productoTerminado", method=RequestMethod.GET)
    public String productoTerminado(Model model)
    {
      return "Inventarios/productoTerminadoView";
    }
    
    //------------Materia prima--------------------
    
    
    
    
    @RequestMapping(value = "Inventarios/registrarMateriaPrima", method = RequestMethod.GET)
    public String registrarMateriaPrima(Model model) 
    {
        Inventario inventario = new Inventario();        
        model.addAttribute("inventario", inventario);        
        return "Inventarios/registrarMateriaPrimaView";
    }
    
    @RequestMapping(value = "Inventarios/registrarMateriaPrima", method = RequestMethod.POST)
    public String regRegistrarMateriaPrima(Model model, @Valid Inventario inventario, BindingResult bindingResult) 
    {

        if (bindingResult.hasErrors()) {
            System.out.println("Invalid with: " + bindingResult.getErrorCount() + " errors");
            System.out.println("Error: " + bindingResult.getFieldError().getField());
            return "Inventarios/registrarMateriaPrimaView";
        } else 
        {            
            inventario.setStatus(true);
            
            daoInventario.create(inventario);            

            Query query1 = daoInventario.getEntityMgr().createQuery("SELECT a FROM Inventario a where a.status=:status and a.tipoProducto=:tipo");
            query1.setParameter("status", true);
            query1.setParameter("tipo","Materia");
            List<Inventario> inventarios = query1.getResultList();
            model.addAttribute("inventarios", inventarios);
            return "Inventarios/bajaMateriaPrimaView";
        }
    }
    
    
    @RequestMapping(value = "Inventarios/bajaMateriaPrima", method = RequestMethod.GET)
    public String eliminarTransporte(Model model, HttpServletRequest request) {
        
        Inventario em = findInventario(request.getParameter("id"));
        em.setStatus(false);
        daoInventario.edit(em);

        Query query1 = daoInventario.getEntityMgr().createQuery("SELECT a FROM Inventario a where a.status=:status and a.tipoProducto=:tipo");
        query1.setParameter("status", true);
        query1.setParameter("tipo", "Materia");
        List<Inventario> inventarios = query1.getResultList();
        model.addAttribute("inventarios", inventarios);
        return "Inventarios/bajaMateriaPrimaView";
    }
    
    
    public Inventario findInventario(String id) {
        Query query2 = daoInventario.getEntityMgr().createQuery("SELECT a FROM Inventario a where a.idinventario=:idinventario");
        query2.setParameter("idinventario", Integer.parseInt(id));
        List<Inventario> inventario = query2.getResultList();
        return inventario.get(0);
    }
    
    
    
    //------------Fin Materia Prima----------------
    
  
   @RequestMapping(value = "Inventarios/registrarProductoTerminado", method = RequestMethod.POST)
    public String registrarProductoTerminado(Model model,@Valid Inventario inventario, BindingResult bindingResult)
     {
        if(bindingResult.hasErrors())
        {
           System.out.println("Invalid with: " + bindingResult.getErrorCount() + " errors");
           System.out.println("Error: " + bindingResult.getFieldError().getField());
            return "Inventarios/registrarProductoTerminadoView";
        }
        else
        {
            daoInventario.create(inventario);
            
            List<Inventario> inventarios = daoInventario.findAll();
            model.addAttribute("inventarios", inventarios);
            return "Inventarios/registrarProductoTerminadoView";
        }
    } //fin de la alta
   
    @RequestMapping(value="Inventarios/registrarProductoTerminado", method=RequestMethod.GET)
    public String registrarProductoTerminado(Model model)
    {    
       Inventario inventario = new Inventario();
       model.addAttribute("inventario", inventario);
       return "Inventarios/registrarProductoTerminadoView";
    }
    
   
    @RequestMapping(value="bajaMateriaPrima", method=RequestMethod.GET)
    public String bajaMateriaPrima(Model model)
    {
        return "Inventarios/bajaMateriaPrimaView";
    }
    
    @RequestMapping(value="existencias", method=RequestMethod.GET)
    public String existencias(Model model)
    {
        return "Inventarios/existenciasView";
    }
}