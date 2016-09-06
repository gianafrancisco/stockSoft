/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.backend;

import fransis.mpm.config.MemoryDBConfig;
import fransis.mpm.model.Articulo;
import fransis.mpm.model.Estado;
import fransis.mpm.model.Item;
import fransis.mpm.model.Reserva;
import fransis.mpm.repository.ArticuloRepository;
import fransis.mpm.repository.ItemRepository;
import fransis.mpm.repository.ReservaRepository;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by francisco on 04/12/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("fransis.mpm.model")
@ContextConfiguration(classes = {MemoryDBConfig.class})
public class TaskReservaTest {

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ReservaRepository reservaRepository;


    @After
    public void tearDown() throws Exception {
        itemRepository.deleteAll();
        reservaRepository.deleteAll();
        articuloRepository.deleteAll();
    }

    private Articulo articulo = null;
    private Reserva reserva = null;

/*
    @Before
    public void setUp() throws Exception {
        articulo = new Articulo();
        articulo.setDescripcion("Articulo 1");
        articulo = articuloRepository.saveAndFlush(articulo);
        reserva = new Reserva("Reserva 1", "r@domain.com", null, LocalDate.now().toEpochDay());
        reserva = reservaRepository.saveAndFlush(reserva);
    }
*/
    @Test
    public void test_automatic_cancel_after_7_days() throws Exception {

        articulo = new Articulo();
        articulo.setDescripcion("Articulo 1");
        articulo = articuloRepository.saveAndFlush(articulo);

        reserva = new Reserva("Reserva 1", "r@domain.com", null, LocalDateTime.now().minusDays(8).toInstant(ZoneOffset.UTC).toEpochMilli());
        reserva = reservaRepository.saveAndFlush(reserva);

        Item item = new Item();
        item.setArticulo(articulo);
        item.setEstado(Estado.RESERVADO);
        item.setReserva(reserva);
        Item item1 = itemRepository.saveAndFlush(item);

        TaskReserva.cerrarReserva(reservaRepository, itemRepository, 7);

        List<Item> itemsList = itemRepository.findAll();
        Assert.assertThat(itemsList.size(),is(2)); // Because we crate a new Item in order to hold a summary
        Assert.assertThat(itemsList.get(0).getEstado(),is(Estado.DISPONIBLE));
        Assert.assertThat(itemsList.get(0).getArticulo().getDescripcion(),is(articulo.getDescripcion()));

        Assert.assertThat(itemsList.get(1).getEstado(),is(Estado.CANCELADO));
        Assert.assertThat(itemsList.get(1).getArticulo().getDescripcion(),is(articulo.getDescripcion()));
    }

    @Test
    public void test_not_cancel_after_6_days() throws Exception {

        articulo = new Articulo();
        articulo.setDescripcion("Articulo 1");
        articulo = articuloRepository.saveAndFlush(articulo);

        reserva = new Reserva("Reserva 1", "r@domain.com", null, LocalDateTime.now().minusDays(6).toInstant(ZoneOffset.UTC).toEpochMilli());
        reserva = reservaRepository.saveAndFlush(reserva);

        Item item = new Item();
        item.setArticulo(articulo);
        item.setEstado(Estado.RESERVADO);
        item.setReserva(reserva);
        Item item1 = itemRepository.save(item);

        TaskReserva.cerrarReserva(reservaRepository, itemRepository, 7);

        List<Item> itemsList = itemRepository.findAll();
        Assert.assertThat(itemsList.size(),is(1));
        Assert.assertThat(itemsList.get(0).getEstado(),is(Estado.RESERVADO));
        Assert.assertThat(itemsList.get(0).getArticulo().getDescripcion(),is(articulo.getDescripcion()));
    }

}
