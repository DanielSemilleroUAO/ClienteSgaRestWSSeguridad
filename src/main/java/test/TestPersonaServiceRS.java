package test;


import domain.Persona;
import java.util.List;
import javax.ws.rs.client.*;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author adseglocdom
 */
public class TestPersonaServiceRS {

    private static final String URL_BASE = "http://localhost:8080/sga-jee-web-rest-ws/webservice";
    private static Client client;
    private static WebTarget webTarget;
    private static Persona persona;
    private static List<Persona> personas;
    private static Invocation.Builder invocationBuilder;
    private static Response response;

    public static void main(String[] args) {
        client = ClientBuilder.newClient();

        webTarget = client.target(URL_BASE).path("/personas");
        persona = webTarget.path("/1").request(MediaType.APPLICATION_XML).get(Persona.class);
        System.out.println("Persona recuperada:" + persona);
        
        personas = webTarget.request(MediaType.APPLICATION_XML).get(Response.class).readEntity(new GenericType<List<Persona>>(){});
        System.out.println("Personas recuperadas");
        imprimirPersonas(personas);
        
        Persona nuevaPersona = new Persona("Pedro", "Sanchez", "ps@gmail.com", "99887766");
        invocationBuilder = webTarget.request(MediaType.APPLICATION_XML);
        response = invocationBuilder.post(Entity.entity(nuevaPersona, MediaType.APPLICATION_XML));
        System.out.println(response.getStatus());
        
        Persona personaRecuperada = response.readEntity(Persona.class);
        System.out.println("Persona agregada: " + personaRecuperada);
        
        Persona personaModificar = personaRecuperada;
        personaModificar.setApellido("Munoz");
        String pathId = "/"  + personaModificar.getIdPersona();
        invocationBuilder = webTarget.path(pathId).request(MediaType.APPLICATION_XML);
        response = invocationBuilder.put(Entity.entity(personaModificar, MediaType.APPLICATION_XML));
        System.out.println(response.getStatus());
        System.out.println("Persona modificada" + response.readEntity(Persona.class));
        
        Persona personaEliminar = personaRecuperada;
        String pathEliminarId = "/" + personaEliminar.getIdPersona();
        invocationBuilder = webTarget.path(pathEliminarId).request(MediaType.APPLICATION_XML);
        response = invocationBuilder.delete();
        System.out.println(response.getStatus());
        System.out.println("Persona eliminada" + personaEliminar);
        
        personas = webTarget.request(MediaType.APPLICATION_XML).get(Response.class).readEntity(new GenericType<List<Persona>>(){});
        System.out.println("Personas recuperadas");
        imprimirPersonas(personas);
    }

    private static void imprimirPersonas(List<Persona> personas) {
        for (Persona persona1 : personas) {
            System.out.println(persona1);
        }
    }

}
