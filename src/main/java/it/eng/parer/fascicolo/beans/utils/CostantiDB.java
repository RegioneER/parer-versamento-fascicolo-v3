/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.beans.utils;

public class CostantiDB {

    //
    public enum TipiXmlDati {

        INDICE_FILE, RICHIESTA, RISPOSTA, RAPP_VERS
    }

    //
    public enum TipiUsoDatiSpec {

        MIGRAZ, VERS
    }

    public enum TiUsoModelloXsd {
        MIGRAZ, VERS
    }

    public enum TiModelloXsd {
        PROFILO_GENERALE_FASCICOLO, PROFILO_ARCHIVISTICO_FASCICOLO, PROFILO_SPECIFICO_FASCICOLO, FASCICOLO,
        SELF_DESCRIPTION_MORE_INFO,
        // FILE_GROUP_FILE_MORE_INFO,
        UNISYNCRO, PROFILO_NORMATIVO_FASCICOLO
    }

    public enum StatoConservazioneUnitaDoc {

        ANNULLATA, AIP_DA_GENERARE, AIP_GENERATO, AIP_IN_AGGIORNAMENTO, IN_ARCHIVIO, IN_CUSTODIA,
        IN_VOLUME_DI_CONSERVAZIONE, PRESA_IN_CARICO, VERSAMENTO_IN_ARCHIVIO, AIP_FIRMATO
    }

    public enum TipoGravitaErrore {

        ERRORE, WARNING
    }

    public enum TipiEsitoVerificaHash {

        POSITIVO, NEGATIVO, DISABILITATO, // nel caso la verifica hash non fosse da fare o nel caso di mancata
        // identificazione dell'algoritmo
        NON_EFFETTUATO // nel caso di hash forzato nel versamento MM
    }

    //
    public enum StatoVrsFascicoloKo {

        NON_VERIFICATO, VERIFICATO, NON_RISOLUBILE, RISOLTO
    }

    public enum TipiHash {

        SCONOSCIUTO("SCONOSCIUTO", -1), MD5("MD5", 16), SHA_1("SHA-1", 20), SHA_224("SHA-224", 28),
        SHA_256("SHA-256", 32), SHA_384("SHA-384", 48), SHA_512("SHA-512", 64);

        private String desc;
        private int lenght;

        private TipiHash(String ds, int ln) {
            desc = ds;
            lenght = ln;
        }

        public String descrivi() {
            return desc;
        }

        public int lunghezza() {
            return lenght;
        }

        public static TipiHash evaluateByLenght(int lenght) {
            for (TipiHash hash : values()) {
                if (hash.lunghezza() == lenght) {
                    return hash;
                }
            }
            return SCONOSCIUTO;
        }

        public static TipiHash evaluateByDesc(String desc) {
            for (TipiHash hash : values()) {
                if (hash.descrivi().equals(desc)) {
                    return hash;
                }
            }
            return SCONOSCIUTO;
        }
    }

    public enum TipiEncBinari {

        SCONOSCIUTO("SCONOSCIUTO"), HEX_BINARY("hexBinary"), BASE64("Base64");

        private String desc;

        private TipiEncBinari(String ds) {
            desc = ds;
        }

        public String descrivi() {
            return desc;
        }

        public static TipiEncBinari evaluateByDesc(String desc) {
            for (TipiEncBinari bin : values()) {
                /*
                 * equalsIgnoreCase = dato che non esiste una codifica "forte" il chiamante (e.g. ping che utilizza
                 * BASE64 e non Base64 come sui ws, ma non ha importanza la sintassi quanto la semantica ...)
                 */
                if (bin.descrivi().equalsIgnoreCase(desc)) {
                    return bin;
                }
            }
            return TipiEncBinari.SCONOSCIUTO;
        }
    }

    //
    public class Flag {

        private Flag() {
            throw new IllegalStateException("Utility class");
        }

        public static final String TRUE = "1";
        public static final String FALSE = "0";
    }

}
