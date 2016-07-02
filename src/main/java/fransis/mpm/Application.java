/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm;

import fransis.mpm.model.Articulo;
import fransis.mpm.model.Vendedor;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import fransis.mpm.repository.ArticuloRepository;
import fransis.mpm.repository.VendedorRepository;

import java.util.List;

@SpringBootApplication
@ComponentScan("fransis.mpm")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData(VendedorRepository vendedorRepository, ArticuloRepository articuloRepository) {
		return (args) -> {
			List<Vendedor> list = vendedorRepository.findAll();
			if(list.isEmpty()){
				vendedorRepository.save(new Vendedor("Administrador","1qaz2wsx","Administrador","Administrador"));
				vendedorRepository.save(new Vendedor("vendedor1","123456","Vendedor","1"));
				vendedorRepository.save(new Vendedor("vendedor2","123456","Vendedor","2"));
				vendedorRepository.save(new Vendedor("vendedor3","123456","Vendedor","3"));
				vendedorRepository.save(new Vendedor("vendedor4","123456","Vendedor","4"));
			}

			List<Articulo> articulos = articuloRepository.findAll();
			if(articulos.isEmpty()){
				articuloRepository.save(new Articulo("N.CREDITO","Nota de credito por la diferencia"));
				articuloRepository.save(new Articulo("100001","Remera"));
				articuloRepository.save(new Articulo("100002","Pantalon"));
				articuloRepository.save(new Articulo("100003","Campera"));
				articuloRepository.save(new Articulo("100004","Zapatilla"));
			}


		};
	}

}
