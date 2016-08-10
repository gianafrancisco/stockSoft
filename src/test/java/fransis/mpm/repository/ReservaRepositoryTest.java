/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.repository;

import fransis.mpm.config.MemoryDBConfig;
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

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
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
        LocalDate now = LocalDate.now();
        Reserva rsv = new Reserva("reserva 1", "email@email.com", null, now.toEpochDay());
        reservaRepository.save(rsv);
        Reserva r = reservaRepository.findOne(rsv.getId());
        Assert.assertThat(r.getDescripcion(),is("reserva 1"));
        Assert.assertThat(r.getEmail(),is("email@email.com"));
        Assert.assertThat(r.getEstado(),is(EstadoReserva.ACTIVA));
        Assert.assertThat(r.getFechaReserva(), is(now.toEpochDay()));
    }

    @Test
    public void test_update() throws Exception {
        Reserva rsv = new Reserva("reserva 1", "email@email.com", null, LocalDate.now().toEpochDay());
        reservaRepository.save(rsv);
        Reserva r = reservaRepository.findOne(rsv.getId());
        Assert.assertThat(r.getFechaCierre(), is(0L));
        Assert.assertThat(r.getEstado(),is(EstadoReserva.ACTIVA));
        r.setEstado(EstadoReserva.CONFIRMADA);
        LocalDate nowCierre = LocalDate.now();
        r.setFechaCierre(nowCierre.toEpochDay());
        reservaRepository.save(r);
        r = reservaRepository.findOne(rsv.getId());
        Assert.assertThat(r.getEstado(),is(EstadoReserva.CONFIRMADA));
        Assert.assertThat(r.getFechaCierre(), is(nowCierre.toEpochDay()));
    }

    @Test
    public void test_findByDescripcionOrEmail() throws Exception {
        Reserva rsv = new Reserva("reserva 1", "email@email.com", "usuario1", LocalDate.now().toEpochDay());
        reservaRepository.save(rsv);

        Reserva rsv1 = new Reserva("reserva 2", "email@email.com", "usuario2", LocalDate.now().toEpochDay());
        reservaRepository.save(rsv1);

        List<EstadoReserva> estadoReservaList = new ArrayList<>();
        estadoReservaList.add(EstadoReserva.ACTIVA);
        Page<Reserva> reservaPage = reservaRepository.findByVendedorAndDescripcionContainingIgnoreCaseOrEmailContainingIgnoreCaseAndEstadoIn("usuario1", "reserva", "reserva", estadoReservaList, new PageRequest(0, 10));
        Assert.assertThat(reservaPage.getTotalElements(),is(1L));
        Assert.assertThat(reservaPage.getContent().get(0).getDescripcion(),is(rsv.getDescripcion()));
        Assert.assertThat(reservaPage.getContent().get(0).getEmail(),is(rsv.getEmail()));

        reservaPage = reservaRepository.findByVendedorAndDescripcionContainingIgnoreCaseOrEmailContainingIgnoreCaseAndEstadoIn("usuario1", "email@email.co", "email@email.co", estadoReservaList, new PageRequest(0, 10));
        Assert.assertThat(reservaPage.getTotalElements(),is(1L));
        Assert.assertThat(reservaPage.getContent().get(0).getDescripcion(),is(rsv.getDescripcion()));
        Assert.assertThat(reservaPage.getContent().get(0).getEmail(),is(rsv.getEmail()));

        reservaPage = reservaRepository.findByVendedorAndDescripcionContainingIgnoreCaseOrEmailContainingIgnoreCaseAndEstadoIn("usuario1", "cualquier", "cualquier", estadoReservaList, new PageRequest(0, 10));
        Assert.assertThat(reservaPage.getTotalElements(),is(0L));
    }

    @Test
    public void test_findAllAndEstadoIn() throws Exception {
        Reserva rsv = new Reserva("reserva 1", "email@email.com", "username1", LocalDate.now().toEpochDay());
        reservaRepository.save(rsv);
        List<EstadoReserva> estadoReservaList = new ArrayList<>();
        estadoReservaList.add(EstadoReserva.ACTIVA);
        Page<Reserva> reservaPage = reservaRepository.findByVendedorAndEstadoIn("username1", estadoReservaList, new PageRequest(0, 10));
        Assert.assertThat(reservaPage.getTotalElements(),is(1L));
        Assert.assertThat(reservaPage.getContent().get(0).getDescripcion(),is(rsv.getDescripcion()));
        Assert.assertThat(reservaPage.getContent().get(0).getEmail(),is(rsv.getEmail()));

        estadoReservaList.remove(0);
        estadoReservaList.add(EstadoReserva.CANCELADA);
        reservaPage = reservaRepository.findByVendedorAndEstadoIn("username1", estadoReservaList, new PageRequest(0, 10));
        Assert.assertThat(reservaPage.getTotalElements(),is(0L));
    }

}
