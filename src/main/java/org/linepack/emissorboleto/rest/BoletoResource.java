/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.emissorboleto.rest;

import java.io.IOException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.linepack.emissorboleto.service.EmissorBoleto;

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
     * @param tituloId
     * @return an instance of java.lang.String
     */
    @GET
    @Path("create/{tituloId}")
    @Produces("application/pdf")
    public Response createBoletoPDF(@PathParam("tituloId") Integer tituloId) {
        try {
            EmissorBoleto emissorBoleto = new EmissorBoleto();
            byte[] pdf = emissorBoleto.getBoletoBytes(tituloId);
            return Response.ok(pdf)
                    .header("Content-Disposition", "inline; filename=boleto.pdf")
                    .build();
        } catch (Exception e) {
            ResponseBuilder rBuild = Response.status(Response.Status.BAD_REQUEST);
            return rBuild.type(MediaType.TEXT_PLAIN)
                    .entity("Erro ao criar Boleto: " + e.toString())
                    .build();
        }
    }

    /**
     *
     * @param tituloId
     * @return
     */
    @GET
    @Path("open/{tituloId}")
    @Produces("application/pdf")
    public Response openBoletoPDF(@PathParam("tituloId") Integer tituloId) {
        return this.createBoletoPDF(tituloId);
    }

    /**
     *
     * @param tituloId
     * @return
     */
    @GET
    @Path("getPath/{tituloId}")
    @Produces(MediaType.TEXT_PLAIN)
    public String getPathBoleto(@PathParam("tituloId") Integer tituloId) throws IOException {
        EmissorBoleto emissorBoleto = new EmissorBoleto();
        return emissorBoleto.getBoletoByPath(tituloId);
    }    
}
