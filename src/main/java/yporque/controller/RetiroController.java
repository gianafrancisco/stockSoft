package yporque.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yporque.model.*;
import yporque.repository.RetiroRepository;

import java.time.*;

/**
 * Created by francisco on 23/12/15.
 */
@RestController
@Component("retiroController")
public class RetiroController {

    @Autowired
    private RetiroRepository retiroRepository;

    @RequestMapping("/retiro/agregar")
    public Retiro agregar(@RequestBody Retiro retiro){
        retiro.setFecha(LocalDateTime.now().toInstant(ZoneOffset.UTC));
        retiroRepository.saveAndFlush(retiro);
        return retiro;
    }

    @RequestMapping("/retiro/today")
    public Page<Retiro> obtenerListado(Pageable pageRequest) {

        LocalDateTime startTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime stopTime = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);

        return retiroRepository.findByFechaBetween(startTime.toInstant(ZoneOffset.UTC), stopTime.toInstant(ZoneOffset.UTC), pageRequest);
    }


}

