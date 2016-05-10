import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import yporque.model.Articulo;

import javax.persistence.EntityManager;
import javax.persistence.Persistence;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;

/**
 * Created by francisco on 21/11/15.
 */

/*
public class ModelTest {

    private EntityManager entityManager = Persistence.createEntityManagerFactory("test").createEntityManager();


    @Test
    public void test_add_new_Articulo_with_zero_item(){

        Long id = 0L;
        entityManager.getTransaction().begin();
        Articulo art = new Articulo(1.0,1.0,1.0,1.0,1.0,"articulo1");
        entityManager.persist(art);
        entityManager.getTransaction().commit();
        id = art.getArticuloId();
        Articulo artPersisted = entityManager.find(Articulo.class,id);
        entityManager.close();

        Assert.assertThat(artPersisted.getDescripcion(),is("Prueba"));
        Assert.assertThat(artPersisted.getItems().size(),is(0));

    }


    @Test
    public void test_add_new_Articulo_with_items(){

        Long id = 0L;
        entityManager.getTransaction().begin();
        Articulo art = new Articulo(1.0,1.0,1.0,1.0,1.0,"articulo1");
        List<Item> list = new ArrayList<>();
        list.add(new Item("123456789",1));
        list.add(new Item("987654321",1));
        list.add(new Item("132547698",1));
        art.setItems(list);

        entityManager.persist(art);
        entityManager.getTransaction().commit();
        id=art.getArticuloId();
        Articulo artPersisted = entityManager.find(Articulo.class,id);
        entityManager.close();

        Assert.assertThat(artPersisted.getDescripcion(),is("Prueba"));
        Assert.assertThat(artPersisted.getItems().size(),is(3));
        Assert.assertThat(artPersisted.getItems().get(0).getCodigo(),is("123456789"));
        Assert.assertThat(artPersisted.getItems().get(0).getCantidad(),is(1));

    }

    @Test
    @Ignore
    public void test_add_new_item_to_Articulo(){

        Long id = 0L;
        entityManager.getTransaction().begin();
        Articulo art = new Articulo(1.0,1.0,1.0,1.0,1.0,"articulo1");
        entityManager.persist(art);
        entityManager.getTransaction().commit();
        id=art.getArticuloId();

        Item item = new Item("123456789", 1);
        //item.setArticulo(art);

        entityManager.getTransaction().begin();
        entityManager.persist(item);
        entityManager.flush();
        entityManager.getTransaction().commit();

        Articulo artPersisted = entityManager.find(Articulo.class,id);
        entityManager.close();

        Assert.assertThat(artPersisted.getDescripcion(),is("Prueba"));
        Assert.assertThat(artPersisted.getItems().size(),is(1));
        Assert.assertThat(artPersisted.getItems().get(0).getCodigo(),is("123456789"));
        Assert.assertThat(artPersisted.getItems().get(0).getCantidad(),is(1));

    }

}
*/
