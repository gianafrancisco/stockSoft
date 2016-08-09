/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.controller;

import fransis.mpm.model.Vendedor;
import fransis.mpm.repository.VendedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 * Created by francisco on 23/12/15.
 */
@RestController
@Component("vendedorController")
public class VendedorController {

    @Autowired
    private VendedorRepository vendedorRepository;


    @RequestMapping("/vendedores")
    public Page<Vendedor> obtenerLista(Pageable pageRequest, Principal principal){
        if("Administrador".equals(principal.getName())){
            return vendedorRepository.findAll(pageRequest);
        }else{
            //return vendedorRepository.findByUsername(principal.getName());
            return null;
        }
    }

    @RequestMapping("/vendedor/search")
    public Page<Vendedor> filtrar(@RequestParam(value = "") String search, Pageable pageRequest, Principal principal){
        if("Administrador".equals(principal.getName())){
            return vendedorRepository.findByUsernameContainingIgnoreCaseOrNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(search,search,search,pageRequest);
        }else{
            return null;
        }
    }

    @RequestMapping("/vendedor/agregar")
    public Vendedor agregar(@RequestBody Vendedor vendedor, Principal principal){
        if("Administrador".equals(principal.getName())){
            return vendedorRepository.saveAndFlush(vendedor);
        }else{
            return null;
        }
    }

    @RequestMapping("/vendedor/delete/{vendedorId}")
    public void delete(@PathVariable Long vendedorId, Principal principal){
        if("Administrador".equals(principal.getName())){
            Vendedor vendedor = vendedorRepository.findOne(vendedorId);
            if(!"administrador".equalsIgnoreCase(vendedor.getUsername())){
                vendedorRepository.delete(vendedorId);
            }
        }
    }

}
