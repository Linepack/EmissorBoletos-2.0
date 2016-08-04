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
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import org.jrimum.bopepo.Boleto;
import org.jrimum.bopepo.view.BoletoViewer;
import org.linepack.dao.TituloDAO;
import org.linepack.main.EmissorBoleto;
import org.linepack.model.Titulo;

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
            Titulo tituloModel = new Titulo();
            TituloDAO tituloDAO = new TituloDAO();
            tituloModel = tituloDAO.getByID(tituloId);

            byte[] pdfAsBytes = null;
            if (tituloModel != null) {
                pdfAsBytes = tituloModel.getBoleto();
            } else {
                EmissorBoleto emissorBoleto = new EmissorBoleto();
                Boleto boleto = emissorBoleto.getBoletoStream(tituloId);
                BoletoViewer viewer = new BoletoViewer(boleto);
                pdfAsBytes = viewer.getPdfAsByteArray();
            }
            
            return Response.ok(pdfAsBytes)
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
        try {
            Titulo tituloModel = new Titulo();
            TituloDAO tituloDAO = new TituloDAO();
            tituloModel = tituloDAO.getByID(tituloId);
            return Response.ok(tituloModel.getBoleto())
                    .header("Content-Disposition", "inline; filename=boleto.pdf")
                    .build();
        } catch (Exception e) {
            ResponseBuilder rBuild = Response.status(Response.Status.BAD_REQUEST);
            return rBuild.type(MediaType.TEXT_PLAIN)
                    .entity("Erro ao abrir Boleto: " + e.toString())
                    .build();
        }
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
