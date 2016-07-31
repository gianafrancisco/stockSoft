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
				vendedorRepository.save(new Vendedor("Administrador","1qaz2wsx","Administrador","Administrador"));
				vendedorRepository.save(new Vendedor("vendedor1","123456","Vendedor","1"));
				vendedorRepository.save(new Vendedor("vendedor2","123456","Vendedor","2"));
				vendedorRepository.save(new Vendedor("vendedor3","123456","Vendedor","3"));
				vendedorRepository.save(new Vendedor("vendedor4","123456","Vendedor","4"));
			}

			List<Articulo> articulos = articuloRepository.findAll();
			if(articulos.isEmpty()){
				articuloRepository.save(new Articulo("100000","Caudalimetro"));
				articuloRepository.save(new Articulo("100001","Sensor de presion"));
				articuloRepository.save(new Articulo("100002","Sensor de temperatura"));
				articuloRepository.save(new Articulo("100003","Sensor de luz"));
				articuloRepository.save(new Articulo("100004","Rectificador"));
				List<Item> itemList = new ArrayList<>();
				for(int i = 0; i<1000; i++){
					Item item = new Item();
					item.setArticulo(articuloRepository.getOne(((long)(Math.random()*5) + 1)));
					itemList.add(item);
				}
				itemRepository.save(itemList);

			}


		};
	}

}
