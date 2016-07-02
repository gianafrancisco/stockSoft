/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.repository;

import fransis.mpm.config.MemoryDBConfig;
import fransis.mpm.model.Articulo;
import fransis.mpm.model.EstadoReserva;
import fransis.mpm.model.Reserva;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;

/**
 * Created by francisco on 04/12/2015.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("fransis.mpm.model")
@ContextConfiguration(classes = {MemoryDBConfig.class})
public class ReservaRepositoryTest {

    @Autowired
    private ReservaRepository reservaRepository;

    @After
    public void tearDown() throws Exception {
        reservaRepository.deleteAll();
    }

    @Test
    public void test_insert() throws Exception {
        Reserva rsv = new Reserva("reserva 1", "email@email.com", null);
        reservaRepository.save(rsv);
        Reserva r = reservaRepository.findOne(rsv.getReservaId());
        Assert.assertThat(r.getDescripcion(),is("reserva 1"));
        Assert.assertThat(r.getEmail(),is("email@email.com"));
        Assert.assertThat(r.getEstado(),is(EstadoReserva.ACTIVA));
    }

    @Test
    public void test_update() throws Exception {
        Reserva rsv = new Reserva("reserva 1", "email@email.com", null);
        reservaRepository.save(rsv);
        Reserva r = reservaRepository.findOne(rsv.getReservaId());
        Assert.assertThat(r.getEstado(),is(EstadoReserva.ACTIVA));
        r.setEstado(EstadoReserva.CONFIRMADA);
        reservaRepository.save(r);
        r = reservaRepository.findOne(rsv.getReservaId());
        Assert.assertThat(r.getEstado(),is(EstadoReserva.CONFIRMADA));
    }

}
