/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.emissorboleto.rest;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.container.AsyncResponse;
import javax.ws.rs.container.Suspended;
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
    private ExecutorService executorService = java.util.concurrent.Executors.newCachedThreadPool();

    /**
     * Creates a new instance of GenericResource
     */
    public BoletoResource() {
    }

    @GET
    @Path(value = "create/{tituloId}")
    @Produces(value = "application/pdf")
    public void createBoletoPDF(
            @Suspended final AsyncResponse asyncResponse, 
            @PathParam(value = "tituloId") final Integer tituloId) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doCreateBoletoPDF(tituloId));
            }
        });
    }

    private Response doCreateBoletoPDF(@PathParam("tituloId") Integer tituloId) {
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
     * @param asyncResponse
     * @param tituloId
     */
    @GET
    @Path("open/{tituloId}")
    @Produces("application/pdf")
    public void openBoletoPDF(@Suspended final AsyncResponse asyncResponse,
            @PathParam("tituloId") final Integer tituloId) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                asyncResponse.resume(doOpenBoletoPDF(tituloId));
            }
        });
    }

    private Response doOpenBoletoPDF(@PathParam("tituloId") Integer tituloId) {
        return this.doCreateBoletoPDF(tituloId);
    }

    /**
     *
     * @param asyncResponse
     * @param tituloId
     */
    @GET
    @Path("getPath/{tituloId}")
    @Produces(MediaType.TEXT_PLAIN)
    public void getPathBoleto(@Suspended final AsyncResponse asyncResponse,
            @PathParam("tituloId") final Integer tituloId) {
        executorService.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    asyncResponse.resume(doGetPathBoleto(tituloId));
                } catch (IOException ex) {
                    Logger.getLogger(BoletoResource.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private String doGetPathBoleto(@PathParam("tituloId") Integer tituloId) throws IOException {
        EmissorBoleto emissorBoleto = new EmissorBoleto();
        return emissorBoleto.getBoletoByPath(tituloId);
    }
}
