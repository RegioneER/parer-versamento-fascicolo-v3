/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package it.eng.parer.fascicolo.runner.util;

public class EndPointCostants {

    private EndPointCostants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String URL_ADMIN_BASE = "/admin";

    public static final String URL_FASCICOLO_BASE = "/api";
    private static final String URL_CTX_PUBLIC = "/public";
    private static final String URL_CTX_OAUTH2 = "/oauth2";

    public static final String RESOURCE_FASCICOLO_V3 = "/fascicolo";

    public static final String RESOURCE_INFOS = "/infos";

    public static final String URL_GET_INFOS = URL_ADMIN_BASE + RESOURCE_INFOS;
    public static final String URL_PUBLIC_FASCICOLO_V3 = URL_CTX_PUBLIC + RESOURCE_FASCICOLO_V3;
    public static final String URL_OAUTH_2_FASCICOLO_V3 = URL_CTX_OAUTH2 + RESOURCE_FASCICOLO_V3;
    public static final String URL_GET_STATUS = "/status";

}
