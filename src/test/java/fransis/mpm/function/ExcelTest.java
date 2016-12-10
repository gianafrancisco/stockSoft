/*
 * Copyright (C) 2016-2016 Francisco Giana <gianafrancisco@gmail.com>
 *
 */

package fransis.mpm.function;

import fransis.mpm.config.MemoryDBConfig;
import fransis.mpm.model.*;
import fransis.mpm.repository.ArticuloRepository;
import fransis.mpm.repository.ItemRepository;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.nullValue;

/**
 * Created by francisco on 12/10/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ComponentScan("fransis.mpm")
@ContextConfiguration(classes = {MemoryDBConfig.class, ArticuloRepository.class, Excel.class })
public class ExcelTest {
    @Autowired
    private ArticuloRepository articuloRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ExportarService exportarService;

    @Before
    public void setUp() throws Exception {
        Articulo art = new Articulo("1234","articulo 1");
        art = articuloRepository.saveAndFlush(art);
        Item item = new Item();
        item.setEstado(Estado.DISPONIBLE);
        item.setArticulo(art);
        item.setTipo(Tipo.FISICO);
        itemRepository.saveAndFlush(item);
        art = new Articulo("1234","articulo 2");
        art.setMoneda(Moneda.EURO);
        art= articuloRepository.saveAndFlush(art);
        item = new Item();
        item.setEstado(Estado.DISPONIBLE);
        item.setArticulo(art);
        item.setTipo(Tipo.VIRTUAL);
        itemRepository.saveAndFlush(item);
        art = new Articulo("1234","articulo 3");
        art.setMoneda(Moneda.PESO);
        art = articuloRepository.saveAndFlush(art);
        item = new Item();
        item.setEstado(Estado.RESERVADO);
        item.setTipo(Tipo.FISICO);
        item.setArticulo(art);
        itemRepository.saveAndFlush(item);
    }


    @Test
    public void create_excel() throws Exception {
        exportarService.exportar("test.xls");
        File file = new File("test.xls");
        Assert.assertThat(file.exists(), is(true));
        FileInputStream f = new FileInputStream(file);
        HSSFWorkbook workbook = new HSSFWorkbook(f);
        HSSFSheet articulos = workbook.getSheet("articulos");
        Assert.assertThat(articulos, is(not(nullValue())));
        Assert.assertThat(articulos.getLastRowNum(), is(2));
        Row title = articulos.getRow(0);
        Assert.assertThat(title.getCell(0).getStringCellValue(), is("Codigo"));
        Assert.assertThat(title.getCell(1).getStringCellValue(), is("Descripcion"));
        Assert.assertThat(title.getCell(2).getStringCellValue(), is("Moneda"));
        Assert.assertThat(title.getCell(3).getStringCellValue(), is("Stock fisico"));
        Assert.assertThat(title.getCell(4).getStringCellValue(), is("Stock fisico reservado"));
        Assert.assertThat(title.getCell(5).getStringCellValue(), is("Stock total"));

        Row row1 = articulos.getRow(1);
        Assert.assertThat(row1.getCell(0).getStringCellValue(), is("1234"));
        Assert.assertThat(row1.getCell(1).getStringCellValue(), is("articulo 1"));
        Assert.assertThat(row1.getCell(2).getStringCellValue(), is("DOLAR"));
        Assert.assertThat(row1.getCell(3).getNumericCellValue(), is(1.0));
        Assert.assertThat(row1.getCell(4).getNumericCellValue(), is(0.0));
        Assert.assertThat(row1.getCell(5).getNumericCellValue(), is(1.0));

        row1 = articulos.getRow(2);
        Assert.assertThat(row1.getCell(0).getStringCellValue(), is("1234"));
        Assert.assertThat(row1.getCell(1).getStringCellValue(), is("articulo 3"));
        Assert.assertThat(row1.getCell(2).getStringCellValue(), is("PESO"));
        Assert.assertThat(row1.getCell(3).getNumericCellValue(), is(0.0));
        Assert.assertThat(row1.getCell(4).getNumericCellValue(), is(1.0));
        Assert.assertThat(row1.getCell(5).getNumericCellValue(), is(1.0));

    }



    @After
    public void tearDown() throws Exception {
        itemRepository.deleteAll();
        articuloRepository.deleteAll();
        File file = new File("test.xls");
        file.delete();
    }
}

