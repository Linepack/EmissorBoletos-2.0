/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import org.linepack.main.EmissorBoleto;

/**
 * REST Web Service
 *
 * @author Leandro
 */
@Path("boleto")
public class BoletoResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of GenericResource
     */
    public BoletoResource() {
    }

    /**
     * Retrieves representation of an instance of
     * org.linepack.rest.BoletoResource
     *
     * @param id
     * @return an instance of java.lang.String
     */
    @GET
    @Path("response/{tituloId}")
    public Response getRespons(@PathParam("tituloId") Integer id) {
        EmissorBoleto emissorBoleto = new EmissorBoleto();
        String stream = emissorBoleto.getBoletoStream(id);
        return Response.status(200).entity(stream).build();
    }

    /**
     * PUT method for updating or creating an instance of BoletoResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.TEXT_HTML)
    public void putHtml(String content) {
    }
}
