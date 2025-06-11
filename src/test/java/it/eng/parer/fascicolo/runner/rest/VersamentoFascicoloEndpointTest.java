/*
 * Engineering Ingegneria Informatica S.p.A.
 *
 * Copyright (C) 2023 Regione Emilia-Romagna <p/> This program is free software: you can
 * redistribute it and/or modify it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the License, or (at your option)
 * any later version. <p/> This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Affero General Public License for more details. <p/> You should
 * have received a copy of the GNU Affero General Public License along with this program. If not,
 * see <https://www.gnu.org/licenses/>.
 */

package it.eng.parer.fascicolo.runner.rest;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle.FAS_CONFIG_002_003;
import static it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle.FAS_CONFIG_003_002;
import static it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle.FAS_XSD_001_002;
import static it.eng.parer.fascicolo.beans.utils.messages.MessaggiWSBundle.WS_CHECK;
import static it.eng.parer.fascicolo.runner.util.EndPointCostants.URL_FASCICOLO_BASE;
import static it.eng.parer.fascicolo.runner.util.EndPointCostants.URL_PUBLIC_FASCICOLO_V3;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;

import com.google.common.net.HttpHeaders;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.TestProfile;
import io.quarkus.test.security.TestSecurity;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import it.eng.parer.Profiles;
import jakarta.ws.rs.core.MediaType;

@QuarkusTest
@TestProfile(Profiles.EndToEnd.class)
class VersamentoFascicoloEndpointTest {

    @Test
    @TestSecurity(authorizationEnabled = false)
    void wrongXmlRequest_fails() {
	given().config(RestAssured.config().encoderConfig(
		encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA)
		.urlEncodingEnabled(true).multiPart("LOGINNAME", "fake", MediaType.TEXT_PLAIN)
		.multiPart("VERSIONE", "9999", MediaType.TEXT_PLAIN)
		.multiPart("PASSWORD", "fake", MediaType.TEXT_PLAIN)
		.multiPart("XMLSIP", "<IndiceSIPFascicolo></IndiceSIPFascicolo>",
			MediaType.TEXT_XML)
		.when().post(URL_FASCICOLO_BASE + URL_PUBLIC_FASCICOLO_V3).then().statusCode(400)
		.body("EsitoVersamentoFascicolo.EsitoVersamentoNegativo.EsitoGenerale.CodiceEsito",
			is("NEGATIVO"))
		.body("EsitoVersamentoFascicolo.EsitoVersamentoNegativo.EsitoGenerale.CodiceErrore",
			is(FAS_XSD_001_002));
    }

    @Test
    @TestSecurity(authorizationEnabled = false)
    void missingParams_fails() {
	given().config(RestAssured.config().encoderConfig(
		encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA)
		.urlEncodingEnabled(true).when().post(URL_FASCICOLO_BASE + URL_PUBLIC_FASCICOLO_V3)
		.then().statusCode(400)
		.body("EsitoVersamentoFascicolo.EsitoVersamentoNegativo.EsitoGenerale.CodiceEsito",
			is("NEGATIVO"))
		.body("EsitoVersamentoFascicolo.EsitoVersamentoNegativo.EsitoGenerale.CodiceErrore",
			is(WS_CHECK));
    }

    @Test
    @TestSecurity(authorizationEnabled = false)
    void wrongCredentials_fails() {
	given().config(RestAssured.config().encoderConfig(
		encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA)
		.urlEncodingEnabled(true).multiPart("LOGINNAME", "not_exists", MediaType.TEXT_PLAIN)
		.multiPart("VERSIONE", "3.0", MediaType.TEXT_PLAIN)
		.multiPart("PASSWORD", "password", MediaType.TEXT_PLAIN)
		.multiPart("XMLSIP", xmlSip(), MediaType.TEXT_XML).when()
		.post(URL_FASCICOLO_BASE + URL_PUBLIC_FASCICOLO_V3).then().statusCode(200)
		.body("EsitoVersamentoFascicolo.EsitoVersamentoNegativo.EsitoGenerale.CodiceEsito",
			is("NEGATIVO"))
		.body("EsitoVersamentoFascicolo.EsitoVersamentoNegativo.EsitoGenerale.CodiceErrore",
			is(FAS_CONFIG_002_003));
    }

    @Test
    @TestSecurity(authorizationEnabled = false)
    void wrongVersion_fails() {
	given().config(RestAssured.config().encoderConfig(
		encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA)
		.urlEncodingEnabled(true)
		.multiPart("LOGINNAME", "admin_generale", MediaType.TEXT_PLAIN)
		.multiPart("VERSIONE", "9999999", MediaType.TEXT_PLAIN)
		.multiPart("PASSWORD", "password", MediaType.TEXT_PLAIN)
		.multiPart("XMLSIP", xmlSip(), MediaType.TEXT_XML).when()
		.post(URL_FASCICOLO_BASE + URL_PUBLIC_FASCICOLO_V3).then().statusCode(200)
		.body("EsitoVersamentoFascicolo.EsitoVersamentoNegativo.EsitoGenerale.CodiceEsito",
			is("NEGATIVO"))
		.body("EsitoVersamentoFascicolo.EsitoVersamentoNegativo.EsitoGenerale.CodiceErrore",
			is(FAS_CONFIG_003_002));
    }

    @Test
    @TestSecurity(authorizationEnabled = false)
    void success() {
	given().config(RestAssured.config().encoderConfig(
		encoderConfig().encodeContentTypeAs("multipart/form-data", ContentType.TEXT)))
		.header(HttpHeaders.CONTENT_TYPE, MediaType.MULTIPART_FORM_DATA)
		.urlEncodingEnabled(true)
		.multiPart("LOGINNAME", "admin_generale", MediaType.TEXT_PLAIN)
		.multiPart("VERSIONE", "3.0", MediaType.TEXT_PLAIN)
		.multiPart("PASSWORD", "password", MediaType.TEXT_PLAIN)
		.multiPart("XMLSIP", xmlSip(), MediaType.TEXT_XML).when()
		.post(URL_FASCICOLO_BASE + URL_PUBLIC_FASCICOLO_V3).then().statusCode(200)
		.body("EsitoVersamentoFascicolo.EsitoVersamentoNegativo.EsitoGenerale.CodiceEsito.text()",
			emptyString())
		.body("EsitoVersamentoFascicolo.RapportoVersamentoFascicolo.EsitoGenerale.CodiceEsito",
			is(not("NEGATIVO")));
    }

    private String xmlSip() {
	try {
	    return IOUtils.toString(this.getClass().getResourceAsStream("/fascicoloCorretto.xml"),
		    StandardCharsets.UTF_8);
	} catch (IOException e) {
	    e.printStackTrace();
	    return "";
	}
    }
}
