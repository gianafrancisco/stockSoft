/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.repository;

import fransis.mpm.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by francisco on 6/20/16.
 */
public interface ItemRepository extends JpaRepository<Item, Long> {
}
