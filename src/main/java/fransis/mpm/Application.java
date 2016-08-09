/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm;

import fransis.mpm.model.Articulo;
import fransis.mpm.model.Item;
import fransis.mpm.model.Vendedor;

import fransis.mpm.repository.ItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import fransis.mpm.repository.ArticuloRepository;
import fransis.mpm.repository.VendedorRepository;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
@ComponentScan("fransis.mpm")
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData(VendedorRepository vendedorRepository, ArticuloRepository articuloRepository, ItemRepository itemRepository) {
		return (args) -> {
			List<Vendedor> list = vendedorRepository.findAll();
			if(list.isEmpty()){
				vendedorRepository.saveAndFlush(new Vendedor("Administrador","1qaz2wsx","Administrador","Administrador"));
				vendedorRepository.saveAndFlush(new Vendedor("egrande","123456","Emiliano","Grande"));
			}

			List<Articulo> articulos = articuloRepository.findAll();
			if(articulos.isEmpty()){
				articuloRepository.saveAndFlush(new Articulo("100000","Caudalimetro"));
				articuloRepository.saveAndFlush(new Articulo("100001","Sensor de presion"));
				articuloRepository.saveAndFlush(new Articulo("100002","Sensor de temperatura"));
				articuloRepository.saveAndFlush(new Articulo("100003","Sensor de luz"));
				articuloRepository.saveAndFlush(new Articulo("100004","Rectificador"));
				List<Item> itemList = new ArrayList<>();
				for(int i = 0; i<1000; i++){
					Item item = new Item();
					item.setOrdenDeCompra("0001");
					item.setArticulo(articuloRepository.getOne(((long)(Math.random()*5) + 1)));
					itemList.add(item);
				}
				itemRepository.save(itemList);
				itemRepository.flush();

			}


		};
	}

}
