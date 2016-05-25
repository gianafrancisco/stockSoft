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

/**
 * Created by francisco on 23/12/15.
 */
@RestController
@Component("vendedorController")
public class VendedorController {

    @Autowired
    private VendedorRepository vendedorRepository;


    @RequestMapping("/vendedores")
    public Page<Vendedor> obtenerLista(Pageable pageRequest){
        return vendedorRepository.findAll(pageRequest);
    }

    @RequestMapping("/vendedor/search")
    public Page<Vendedor> filtrar(@RequestParam(value = "") String search, Pageable pageRequest){
        return vendedorRepository.findByUsernameContainingIgnoreCaseOrNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(search,search,search,pageRequest);
    }

    @RequestMapping("/vendedor/agregar")
    public Vendedor agregar(@RequestBody Vendedor vendedor){
        return vendedorRepository.saveAndFlush(vendedor);
    }

    @RequestMapping("/vendedor/delete/{vendedorId}")
    public void delete(@PathVariable Long vendedorId){

        Vendedor vendedor = vendedorRepository.findOne(vendedorId);
        if(!"administrador".equalsIgnoreCase(vendedor.getUsername())){
            vendedorRepository.delete(vendedorId);
        }

    }

}
