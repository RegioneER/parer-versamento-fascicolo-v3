/**
 *
 */
package it.eng.parer.fascicolo.runner.rest.input;

import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.media.SchemaProperty;
import org.jboss.resteasy.reactive.PartType;

import it.eng.parer.fascicolo.runner.util.IVersamentoFascicoloMultipartForm;
import it.eng.parer.ws.xml.versfascicoloV3.IndiceSIPFascicolo;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.core.MediaType;

/*
 * Wrapper multipart-form con dati inviati da client. Presenti meccanismi di validazione da javax.validation che
 * verificano la presenza o meno dei campi all'interno del multipart scambiato.
 *
 * Campi: VERSIONE -> campo testo con versione ws/sip LOGINNAME -> campo testo con login utente PASSWORD -> campo testo
 * con password utente XMLSIP -> campo testo contenente l'xml (questo campo viene sia portato su tipo String che su
 * oggetto serializzato via JAXB)
 *
 *
 */
public class VersamentoFascicoloStdMultipartForm implements IVersamentoFascicoloMultipartForm {

    public VersamentoFascicoloStdMultipartForm() {
        super();
    }

    @Schema(type = SchemaType.STRING, required = true, example = "3.0", description = "Versione dell'xml versato (versione SIP)")
    @FormParam("VERSIONE")
    @PartType(MediaType.TEXT_PLAIN)
    @NotEmpty(message = "Necessario indicare il campo VERSIONE")
    public String VERSIONE;

    @Schema(type = SchemaType.STRING, required = true, description = "Userid dell'utente versatore", example = "login")
    @FormParam("LOGINNAME")
    @PartType(MediaType.TEXT_PLAIN)
    @NotEmpty(message = "Necessario indicare il campo LOGINNAME")
    public String LOGINNAME;

    @Schema(type = SchemaType.STRING, format = "password", required = true, description = "Password dell'utente versatore", example = "password")
    @FormParam("PASSWORD")
    @PartType(MediaType.TEXT_PLAIN)
    @NotEmpty(message = "Necessario indicare il campo PASSWORD")
    public String PASSWORD;

    @Schema(hidden = true)
    @FormParam("XMLSIP")
    @PartType(MediaType.APPLICATION_XML)
    public IndiceSIPFascicolo indiceSIPFascicolo;

    @Schema(type = SchemaType.OBJECT, properties = {
            @SchemaProperty(name = "IndiceSIPFascicolo", minLength = 1, example = "<IndiceSIPFascicolo></IndiceSIPFascicolo>") }, required = true, description = "Xml versato")
    @FormParam("XMLSIP")
    @PartType(MediaType.TEXT_XML)
    @NotNull(message = "Necessario indicare il campo XMLSIP")
    public String XMLSIP;

}
