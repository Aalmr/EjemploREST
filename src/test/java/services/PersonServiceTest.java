package services;

/**
 * Created by oscar on 20/01/17.
 */
import data.Person;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

public class PersonServiceTest {
    private final static String targeUrl = "http://localhost:8080/rest/people";
    private static WebTarget target;

    @BeforeClass
    public static void setUp() {
        target = ClientBuilder.newClient().target(targeUrl);
    }

    @Test
    public void retreiveAllTest() {
        Response response = target
                .request(MediaType.APPLICATION_XML)
                .get();
        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
    }

    @Test
    public void retrieveNotFoundTest() {
        Response response = target
                .path("DoesNotExists")
                .request(MediaType.APPLICATION_XML)
                .get();
        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }

    @Test
    public void createTest() {
        String nif = "123";
        // Primero lo borramos
        target.path(nif)
                .request()
                .delete();

        Person person = new Person("Óscar", "Belmonte", "123");
        Entity<Person> entity = Entity.entity(person, MediaType.APPLICATION_XML_TYPE);
        Response response = target
                .request(MediaType.APPLICATION_XML)
                .post(entity);

        assertThat(response.getStatus(), is(Response.Status.CREATED.getStatusCode()));
        assertThat(response.readEntity(Person.class), is(person));
    }

    @Test
    public void retrieveTest() {
        // Primero nos aseguramos que existe
        String nif = "123";
        Person person = new Person("Óscar", "Belmonte", nif);
        Entity<Person> entity = Entity.entity(person, MediaType.APPLICATION_XML_TYPE);
        Response response = target
                .request(MediaType.APPLICATION_XML)
                .post(entity);

        response = target
                .path(nif)
                .request()
                .get();

        assertThat(response.getStatus(), is(Response.Status.OK.getStatusCode()));
        assertThat(response.readEntity(Person.class), is(person));
    }

    @Test
    public void updateTest() {
        // Primero nos aseguramos que existe
        String nif = "123";
        Person person = new Person("Óscar", "Belmonte", nif);
        Entity<Person> entity = Entity.entity(person, MediaType.APPLICATION_XML_TYPE);
        Response response = target
                .request()
                .post(entity);

        Person updatedPerson = new Person("Óscar", "Belmonte Fernández", nif);
        entity = Entity.entity(updatedPerson, MediaType.APPLICATION_XML_TYPE);
        response = target
                .path(nif)
                .request()
                .put(entity);

        assertThat(response.getStatus(), is(Response.Status.NO_CONTENT.getStatusCode()));
    }

    @Test
    public void updateNotFoundTest() {
        String nif = "123";
        // Primero lo borramos
        target.path(nif)
                .request()
                .delete();

        Person updatedPerson = new Person("Óscar", "Belmonte Fernández", nif);
        Entity<Person> entity = Entity.entity(updatedPerson, MediaType.APPLICATION_XML_TYPE);
        Response response = target
                .path(nif)
                .request()
                .put(entity);

        assertThat(response.getStatus(), is(Response.Status.NOT_FOUND.getStatusCode()));
    }
}