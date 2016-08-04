/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.linepack.rest;

import com.sun.xml.internal.ws.util.ByteArrayBuffer;
import java.io.OutputStream;
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
    @Produces("application/pdf")
    public Response getPDF(@PathParam("tituloId") Integer id) {
        try {
            EmissorBoleto emissorBoleto = new EmissorBoleto();
            Boleto boleto = emissorBoleto.getBoletoStream(id);
            BoletoViewer viewer = new BoletoViewer(boleto);
            byte[] pdfAsBytes = viewer.getPdfAsByteArray();
            return Response.ok(pdfAsBytes)
                    .header("Content-Disposition", "attachment; filename=boleto.pdf")
                    .build();
        } catch (Exception e) {
            ResponseBuilder rBuild = Response.status(Response.Status.BAD_REQUEST);
            return rBuild.type(MediaType.TEXT_PLAIN)
                    .entity("Erro ao gerar Boleto: " + e.toString())
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
