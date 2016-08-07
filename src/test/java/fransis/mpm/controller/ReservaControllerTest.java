/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.controller;

import fransis.mpm.config.MemoryDBConfig;
import fransis.mpm.model.*;
import fransis.mpm.repository.ArticuloRepository;
import fransis.mpm.repository.ItemRepository;
import fransis.mpm.repository.ReservaRepository;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.net.URI;
import java.security.Principal;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by francisco on 18/12/15.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("yporque")
@ContextConfiguration(classes = {MemoryDBConfig.class, ReservaController.class})
public class ReservaControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ItemRepository itemRepository;

    /*
    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ComponentScan.Filter springSecurityFilterChain;
    */
    private Principal principal = new Principal() {
        @Override
        public String getName() {
            return "usuario1";
        }
    };

    @Autowired
    private ReservaController reservaController;

    private Reserva reserva;
    private Articulo articulo;

    private Authentication authentication;

    @Before
    public void setUp() throws Exception {

        mockMvc = MockMvcBuilders.standaloneSetup(reservaController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .build();


        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList("USER");
        authentication =
                new UsernamePasswordAuthenticationToken("usuario1","password", authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        /*mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();*/
        articulo = new Articulo("articulo 1", "articulo 1 rojo");
        articuloRepository.saveAndFlush(articulo);
    }

    @After
    public void tearDown() throws Exception {
        itemRepository.deleteAll();
        articuloRepository.deleteAll();
        reservaRepository.deleteAll();
        SecurityContextHolder.clearContext();
    }

    @Test
    public void test_get_reservas() throws Exception {

        reserva = new Reserva("reserva 1", "demo@demo.com", principal.getName());
        reservaRepository.saveAndFlush(reserva);

        mockMvc.perform(
                get("/reservas").accept(MediaType.parseMediaType("application/json;charset=UTF-8")).principal(principal))
        .andExpect(status().isOk())
        .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.content[0].descripcion").value("reserva 1"))
                .andExpect(jsonPath("$.content[0].email").value("demo@demo.com"))
                .andExpect(jsonPath("$.content[0].estado").value(is(EstadoReserva.ACTIVA.toString())));

    }

    @Test
    public void test_get_reserva() throws Exception {

        reserva = new Reserva("reserva 1", "demo@demo.com", null);
        reserva = reservaRepository.saveAndFlush(reserva);

        mockMvc.perform(
                get("/reservas/"+reserva.getId()).accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"))
                .andExpect(jsonPath("$.descripcion").value("reserva 1"))
                .andExpect(jsonPath("$.email").value("demo@demo.com"))
                .andExpect(jsonPath("$.id").value(is(reserva.getId().intValue())));
    }

    @Test
    public void test_get_reserva_not_found() throws Exception {

        mockMvc.perform(
                get("/reservas/9999").accept(MediaType.parseMediaType("application/json;charset=UTF-8")))
                .andExpect(status().isNotFound());
    }

    @Test
    public void test_request_reserva_by_controller_method() throws Exception {

        reserva = new Reserva("reserva 1", "demo@demo.com", principal.getName());
        reserva = reservaRepository.saveAndFlush(reserva);

        Page<Reserva> page = reservaController.obtenerLista(new PageRequest(0,10), principal);

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getDescripcion(),is("reserva 1"));
        Assert.assertThat(page.getContent().get(0).getEmail(),is("demo@demo.com"));
        Assert.assertThat(page.getContent().get(0).getEstado(),is(EstadoReserva.ACTIVA));
        Assert.assertThat(page.getContent().get(0).getId(),is(reserva.getId()));

    }

    @Test
    public void test_put_not_content_by_controller_method() throws Exception {

        reserva = new Reserva("reserva 1", "demo@demo.com", null);
        reserva = reservaRepository.saveAndFlush(reserva);
        reserva.setEstado(EstadoReserva.CONFIRMADA);

        ResponseEntity<Void> responseEntity = reservaController.put(reserva.getId(), reserva);
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.NO_CONTENT));

        Reserva reserva1 = reservaRepository.findOne(reserva.getId());
        Assert.assertThat(reserva1.getEstado(),is(reserva.getEstado()));

    }

    @Test
    public void test_put_cerrada_reserva_by_controller_method() throws Exception {

        reserva = new Reserva("reserva 1", "demo@demo.com", null);
        reserva = reservaRepository.saveAndFlush(reserva);

        Item item = new Item();
        item.setReserva(reserva);
        item.setEstado(Estado.RESERVADO);
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);

        reserva.setEstado(EstadoReserva.CERRADA);

        ResponseEntity<Void> responseEntity = reservaController.put(reserva.getId(), reserva);
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.NO_CONTENT));

        Item item2 = itemRepository.findOne(item.getId());

        Assert.assertThat(item2.getEstado(),is(Estado.VENDIDO));
    }

    @Test
    public void test_put_cancelada_reserva_by_controller_method() throws Exception {

        reserva = new Reserva("reserva 1", "demo@demo.com", null);
        reserva = reservaRepository.saveAndFlush(reserva);

        Item item = new Item();
        item.setReserva(reserva);
        item.setEstado(Estado.RESERVADO);
        item.setArticulo(articulo);
        item = itemRepository.saveAndFlush(item);

        reserva.setEstado(EstadoReserva.CANCELADA);

        ResponseEntity<Void> responseEntity = reservaController.put(reserva.getId(), reserva);
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.NO_CONTENT));

        Item item2 = itemRepository.findOne(item.getId());

        Assert.assertThat(item2.getEstado(),is(Estado.DISPONIBLE));
    }


    @Test
    public void test_put_put_not_found_by_controller_method() throws Exception {

        reserva = new Reserva("reserva 1", "demo@demo.com", null);
        ResponseEntity<Void> responseEntity = reservaController.put(9L, reserva);
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.NOT_FOUND));

    }

    @Test
    public void test_search_reserva_by_controller_method() throws Exception {

        reserva = new Reserva("reserva 1", "demo@demo.com", principal.getName());
        reservaRepository.saveAndFlush(reserva);

        Page<Reserva> page = reservaController.filtrarReservas("demo@demo.co",new PageRequest(0,10), principal);

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getEmail(),is("demo@demo.com"));
        Assert.assertThat(page.getContent().get(0).getDescripcion(),is("reserva 1"));

        page = reservaController.filtrarReservas("cualquier_cosa",new PageRequest(0,10), principal);

        Assert.assertThat(page.getTotalPages(),is(0));
        Assert.assertThat(page.getTotalElements(),is(0L));

    }

    @Test
    public void test_get_articulo_by_id_by_controller_method() throws Exception {

        reserva = new Reserva("reserva 1", "demo@demo.com", null);

        ResponseEntity<Reserva> responseEntity = reservaController.agregar(reserva, principal);
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.CREATED));

        URI location = responseEntity.getHeaders().getLocation();
        reserva = responseEntity.getBody();
        Assert.assertThat(location.toString(),is("/reservas/"+reserva.getId()));

        Page<Reserva> page = reservaController.obtenerLista(new PageRequest(0,10), principal);

        Assert.assertThat(page.getTotalPages(),is(1));
        Assert.assertThat(page.getTotalElements(),is(1L));
        Assert.assertThat(page.getNumberOfElements(),is(1));
        Assert.assertThat(page.getContent().get(0).getEmail(),is("demo@demo.com"));
        Assert.assertThat(page.getContent().get(0).getDescripcion(),is("reserva 1"));
        Assert.assertThat(page.getContent().get(0).getVendedor(),is(principal.getName()));

    }

    @Test
    public void test_delete_verb_by_controller_method() throws Exception {

        reserva = new Reserva("reserva 1", "demo@demo.com", null);

        ResponseEntity<Reserva> responseEntity = reservaController.agregar(reserva, principal);
        reserva = responseEntity.getBody();

        ResponseEntity<Void> responseDelete = reservaController.borrarArticulo(reserva.getId());
        Assert.assertThat(responseDelete.getStatusCode(),is(HttpStatus.METHOD_NOT_ALLOWED));

    }

    @Test
    public void test_delete_verb_not_found_by_controller_method() throws Exception {
        ResponseEntity<Void>  responseEntity = reservaController.borrarArticulo(10L);
        Assert.assertThat(responseEntity.getStatusCode(),is(HttpStatus.METHOD_NOT_ALLOWED));
    }

}
