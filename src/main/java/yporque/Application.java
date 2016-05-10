package yporque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import yporque.model.Articulo;
import yporque.model.Vendedor;
import yporque.repository.ArticuloRepository;
import yporque.repository.VendedorRepository;

import java.util.List;

@SpringBootApplication
@ComponentScan("yporque")
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
				articuloRepository.save(new Articulo("N.CREDITO","Nota de credito por la diferencia",0.0,1.0,1.0,1000000,1000000));
				articuloRepository.save(new Articulo("100001","Remera",300.0,1.0,1.5,50,50));
				articuloRepository.save(new Articulo("100002","Pantalon",450.0,1.0,2.0,10,10));
				articuloRepository.save(new Articulo("100003","Campera",500.0,2.0,2.0,5,5));
				articuloRepository.save(new Articulo("100004","Zapatilla",350.0,1.0,1.6,7,7));
			}


		};
	}

}
