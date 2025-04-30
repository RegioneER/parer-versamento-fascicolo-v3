package it.eng.parer.fascicolo.beans.utils.xml;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import it.eng.parer.fascicolo.beans.exceptions.AppGenericRuntimeException;
import it.eng.parer.fascicolo.beans.utils.Costanti.ErrorCategory;

import static it.eng.parer.fascicolo.beans.utils.converter.DateUtilsConverter.convert;

import java.time.ZonedDateTime;

public class XmlDateUtility {

    private static final String LOG_ERR_CONVERSION = "Exception in conversion of Date to XMLGregorianCalendar";

    private XmlDateUtility() {
        throw new IllegalStateException("Utility class");
    }

    /**
     *
     * @param date
     *            data da convertire
     * @param zone
     *            timezone
     *
     * @return restituisce XMLGregorianCalendar
     */
    public static XMLGregorianCalendar dateToXMLGregorianCalendar(Date date, TimeZone zone) {
        XMLGregorianCalendar xmlGregorianCalendar = null;
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        gregorianCalendar.setTime(date);
        gregorianCalendar.setTimeZone(zone);
        try {
            DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();
            xmlGregorianCalendar = dataTypeFactory.newXMLGregorianCalendar(gregorianCalendar);
        } catch (Exception e) {
            throw new AppGenericRuntimeException(LOG_ERR_CONVERSION, e, ErrorCategory.VALIDATION_ERROR);
        }

        return xmlGregorianCalendar;
    }

    /**
     *
     * @param date
     *            da convertire
     *
     * @return restituisce XMLGregorianCalendar
     *
     */
    public static XMLGregorianCalendar dateToXMLGregorianCalendarOrNull(Date date) {
        if (date != null) {
            XMLGregorianCalendar xmlGregorianCalendar = null;
            GregorianCalendar gregorianCalendar = new GregorianCalendar();
            gregorianCalendar.setTime(date);
            try {
                DatatypeFactory dataTypeFactory = DatatypeFactory.newInstance();
                xmlGregorianCalendar = dataTypeFactory.newXMLGregorianCalendar(gregorianCalendar);
            } catch (Exception e) {
                throw new AppGenericRuntimeException(LOG_ERR_CONVERSION, e, ErrorCategory.VALIDATION_ERROR);
            }
            return xmlGregorianCalendar;
        } else {
            return null;
        }
    }

    public static XMLGregorianCalendar dateToXMLGregorianCalendarOrNull(ZonedDateTime zonedDateTime) {
        if (zonedDateTime != null) {
            XMLGregorianCalendar xmlGregorianCalendar = null;
            try {
                GregorianCalendar gregorianCalendar = GregorianCalendar.from(zonedDateTime);
                xmlGregorianCalendar = DatatypeFactory.newInstance().newXMLGregorianCalendar(gregorianCalendar);
            } catch (Exception e) {
                throw new AppGenericRuntimeException(LOG_ERR_CONVERSION, e, ErrorCategory.VALIDATION_ERROR);
            }
            return xmlGregorianCalendar;
        } else {
            return null;
        }

    }

    public static Date xmlGregorianCalendarToDateOrNull(XMLGregorianCalendar xmlGregorianCalendar) {
        if (xmlGregorianCalendar != null) {
            ZonedDateTime zdt = xmlGregorianCalendar.toGregorianCalendar().toZonedDateTime();
            return convert(zdt);
        } else {
            return null;
        }
    }

}
