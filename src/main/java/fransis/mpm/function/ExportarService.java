/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.function;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by francisco on 12/10/16.
 */
public interface ExportarService {
    void exportar(OutputStream filename) throws IOException;
}
