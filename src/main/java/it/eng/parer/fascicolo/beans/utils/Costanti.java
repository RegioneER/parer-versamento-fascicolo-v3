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

/*
 * To change this template, choose Tools | Templates and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.utils;

import java.util.stream.Stream;

public class Costanti {

    //
    /**
     * Si reperiscono da DB (parametri applicativi)
     */
    public static final String WS_VERS_FASCICOLO_VRSN = "3.0";
    /**
     * Si reperiscono da DB (parametri applicativi)
     */
    public static final String WS_VERS_FASCICOLO_NOME = "VersamentoFascicoloMach";

    public static final int COLLEGAMENTO_DESC_MAX_SIZE = 256;
    public static final String COLLEGAMENTO_DESC_SEP = ";";
    public static final int MAXLEN_DATOSPEC = 4000;
    //
    public static final int DS_INDICE_CLASS_MAX_LEN = 254;
    public static final int DS_NOTA_MAX_LEN = 4000;
    public static final int DS_OGGETTO_FASC_MAX_LEN = 4000;
    //
    public static final String SOG_COD_IDENT_PARERLOCAL = "ParerLocal";
    public static final String SOG_IDENT_PARERLOCAL_SOGGETTO = "Soggetto";
    public static final String SOG_IDENT_PARERLOCAL_FMT = "{0}_{1}_{2}_{3}";
    //
    public static final String PNORM_TIPO_AGGREG_FASCICOLO = "Fascicolo";
    //
    public static final String NOME_FASCICOLO_SCONOSCIUTO = "Tipo fascicolo sconosciuto";
    //
    public static final String TO_STRING_FIELD_SEPARATOR = ", ";
    public static final String SOGG_DENOMINAZIONE_DEFAULT = "Soggetto non indicato in fase di versamento";
    //
    public static final String TMP_FILE_SUFFIX = "-fascxml.tmp";
    //
    public static final String CLCR_REGEXP = "[\\u0000-\\u001F]";
    public static final String DESCKEY_FASCICOLO_DUMMY = "dummy";

    public class UrnFormatter {

	private UrnFormatter() {
	    throw new IllegalStateException("Utility class");
	}

	public static final char URN_STD_SEPARATOR = ':';
	public static final String VERS_FMT_STRING = "{0}:{1}:{2}";
	public static final String UD_FMT_STRING = "{0}-{1}-{2}";
	public static final String DOC_FMT_STRING = "{0}-{1}";
	public static final String SPATH_COMP_FMT_STRING = "{0}-{1}-{2}-{3}";

	//
	// FASCICOLI
	//
	public static final String FASC_FMT_STRING = "{0}-{1}";

	public static final String CHIAVE_FASC_FMT_STRING = "{0}:{1}:{2}:{3}-{4}";

	public static final String URN_INDICE_SIP_FASC_FMT_STRING = "urn:IndiceSIP:{0}"; // CHIAVE_FASC_FMT_STRING
	public static final String URN_RAPP_VERS_FASC_FMT_STRING = "urn:RapportoVersamento:{0}"; // CHIAVE_FASC_FMT_STRING
	public static final String URN_RAPP_NEG_FASC_FMT_STRING = "urn:RapportoNegativoVersamento:{0}"; // CHIAVE_FASC_FMT_STRING

	// NEW URN FMT
	public static final String URN_INDICE_SIP_V2 = "urn:{0}:IndiceSIP"; //
	public static final String URN_RAPP_VERS_V2 = "urn:{0}:RdV"; //
	public static final String URN_PI_SIP_V2 = "urn:{0}:PISIP"; //
	public static final String URN_ESITO_VERS_V2 = "urn:{0}:EdV"; //
	//

	// MEV#25288
	public static final String URN_SIP_FASC = "urn:{0}:SIP-FA"; //
	// end MEV#25288
	//
	public static final String PAD5DIGITS_FMT = "%05d";
	public static final String PAD2DIGITS_FMT = "%02d";
	public static final String PADNODIGITS_FMT = "%00d";

    }

    // AWS
    // MAC#37280
    public class AwsFormatter {

	private AwsFormatter() {
	    throw new IllegalStateException("AwsFormatter Utility class");
	}

	// ente_normalizzato/struttura_normalizzata, es.: Ente_test_di_carico/test_di_carico_00
	public static final String VERS_FMT_STRING_KEY_OS = "{0}/{1}";
	// anno-numero_normalizzato, es.: 2023-Test44_1675443130871_9446
	public static final String FASC_FMT_STRING_KEY_OS = "{0}-{1}";
	// ente_normalizzato/struttura_normalizzata/anno-numero_normalizzato
	public static final String CHIAVE_FASC_FMT_STRING_KEY_OS = "{0}/{1}";

    }
    // end MAC#37280

    //
    public class AwsConstants {

	private AwsConstants() {
	    throw new IllegalStateException("AwsS3Constants Utility class");
	}

	// custom tags
	public static final String TAG_KEY_VRSOBJ_TYPE = "vrs-object-type";
	public static final String TAG_VALUE_VRSOBJ_METADATI_FASC_ERR_KO = "xml_metadati_fasc_err_ko";

	// custom metadata
	public static final String MEATADATA_INGEST_NODE = "ingest-node";
	public static final String MEATADATA_INGEST_TIME = "ingest-time";

    }

    //
    public enum ModificatoriWS {

    }

    public enum EsitoServizio {

	OK, KO, WARN
    }

    public enum TipiWSPerControlli {
	VERSAMENTO_FASCICOLO
    }

    /**
     * Versione WS_SIP supportata dal backend
     *
     */
    public enum VersioneWS {

	V_EMPTY(""), V3_0("3.0");

	private String version;

	private VersioneWS(String version) {
	    this.version = version;
	}

	/**
	 * @return the version
	 */
	public String getVersion() {
	    return version;
	}

	public static VersioneWS calculate(String versione) {
	    return Stream.of(values()).filter(v -> v.getVersion().equalsIgnoreCase(versione))
		    .findAny().orElse(V_EMPTY);
	}

	public static boolean issupported(String versione) {
	    return Stream.of(values()).anyMatch(v -> v.getVersion().equalsIgnoreCase(versione));
	}
    }

    public enum TipiGestioneFascAnnullati {

	CARICA, CONSIDERA_ASSENTE
    }

    public enum TipiGestioneUDAnnullate {

	CARICA, CONSIDERA_ASSENTE
    }

    public enum ErrorCategory {

	INTERNAL_ERROR, USER_ERROR, VALIDATION_ERROR, PERSISTENCE;
    }

}
