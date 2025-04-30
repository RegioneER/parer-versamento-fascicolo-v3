package it.eng.parer;

public class TestUtils {

    public static boolean exceptionMessageContains(Exception e, String... messages) {
        for (String m : messages) {
            final String message = e.getMessage() != null ? e.getMessage() : e.getClass().getSimpleName();
            if (message.contains(m)) {
                return true;
            }
        }
        return false;
    }
}
