package fransis.mpm.backend;

import fransis.mpm.repository.ItemRepository;
import fransis.mpm.repository.ReservaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * Created by francisco on 22/08/2016.
 */
@Component("scheduleTask")
public class ScheduleTask {

    private static final Logger log = LoggerFactory.getLogger(ScheduleTask.class);
    public static final int DAYS = 7;

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private ReservaRepository reservaRepository;

    public ScheduleTask() {
    }

    public ScheduleTask(ItemRepository itemRepository, ReservaRepository reservaRepository) {
        this.itemRepository = itemRepository;
        this.reservaRepository = reservaRepository;
    }

    @Scheduled(fixedRate = 60000)
    public void cerrarReserva(){
        TaskReserva.cerrarReserva(reservaRepository, itemRepository, DAYS);
        log.info("Cerrando Reservas pendientes.");
    }

}
