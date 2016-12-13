/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.function;

import fransis.mpm.model.*;
import fransis.mpm.repository.ArticuloRepository;
import fransis.mpm.repository.ItemRepository;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by francisco on 12/10/16.
 */
@Component("exportarService")
public class Excel implements ExportarService {

    final static Logger logger = Logger.getLogger(Excel.class);

    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Override
    public void exportar(OutputStream output) throws IOException {
        Workbook wb = new HSSFWorkbook();
        Sheet sheet = wb.createSheet("articulos");
        List<Articulo> articuloList = articuloRepository.findAll();
        Integer rowIndex = 0;
        Row title = sheet.createRow(rowIndex);

        title.createCell(0).setCellValue("Codigo");
        title.createCell(1).setCellValue("Descripcion");
        title.createCell(2).setCellValue("Moneda");
        title.createCell(3).setCellValue("Precio");
        title.createCell(4).setCellValue("Stock fisico");
        title.createCell(5).setCellValue("Stock fisico reservado");
        title.createCell(6).setCellValue("Stock total");
        for(Articulo articulo: articuloList) {
            populateStock(articulo);
            long totalStockFisico = articulo.getStockFisico() + articulo.getStockFisicoReservado();
            if(totalStockFisico > 0) {
                rowIndex++;
                Row row = sheet.createRow(rowIndex);
                row.createCell(0).setCellValue(articulo.getCodigo());
                row.createCell(1).setCellValue(articulo.getDescripcion());
                Moneda moneda = articulo.getMoneda();
                String sMoneda = "";
                if(moneda != null){
                    sMoneda = moneda.toString();
                }
                row.createCell(2).setCellValue(sMoneda);
                row.createCell(3).setCellValue(articulo.getPrecioDeInventario());
                row.createCell(4).setCellValue(articulo.getStockFisico());
                row.createCell(5).setCellValue(articulo.getStockFisicoReservado());
                row.createCell(6).setCellValue(totalStockFisico);
            }
        }
        sheet.autoSizeColumn(0);
        sheet.autoSizeColumn(1);
        sheet.autoSizeColumn(2);
        sheet.autoSizeColumn(3);
        sheet.autoSizeColumn(4);
        wb.write(output);
    }

    private void populateStock(Articulo articulo) {
        List<Item> items = itemRepository.findByArticuloAndEstadoOrderByTipoDesc(articulo, Estado.DISPONIBLE);
        long virtual = items.stream().filter(item -> item.getTipo() == Tipo.VIRTUAL).count();
        long fisico = items.stream().filter(item -> item.getTipo() == Tipo.FISICO).count();
        items = itemRepository.findByArticuloAndEstadoOrderByTipoDesc(articulo, Estado.RESERVADO);
        long fisicoReservados = items.stream().filter(item -> item.getTipo() == Tipo.FISICO).count();
        long virtualReservados = items.stream().filter(item -> item.getTipo() == Tipo.VIRTUAL).count();
        articulo.setStockVirtual(virtual);
        articulo.setStockFisico(fisico);
        articulo.setStockVirtualReservado(virtualReservados);
        articulo.setStockFisicoReservado(fisicoReservados);
    }

}
