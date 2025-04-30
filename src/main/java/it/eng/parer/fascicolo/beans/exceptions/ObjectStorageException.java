package it.eng.parer.fascicolo.beans.exceptions;

import java.text.MessageFormat;

public class ObjectStorageException extends Exception {

    private static final long serialVersionUID = -2311721715615582399L;

    private final String bucket;
    private final String key;
    private final String message;
    private final Throwable cause;
    private final String eTag;

    private ObjectStorageException(ObjectStorageExceptionBuilder builder) {
        super(builder.message, builder.cause);
        this.bucket = builder.bucket;
        this.key = builder.key;
        this.message = builder.message;
        this.cause = builder.cause;
        this.eTag = builder.eTag;
    }

    public static ObjectStorageExceptionBuilder builder() {
        return new ObjectStorageExceptionBuilder();
    }

    public String bucket() {
        return bucket;
    }

    public String key() {
        return key;
    }

    public String message() {
        return message;
    }

    public Throwable cause() {
        return cause;
    }

    public String eTag() {
        return eTag;
    }

    public static class ObjectStorageExceptionBuilder {

        private String bucket;
        private String key;
        private String eTag;
        private String message;
        private Throwable cause;

        public void setBucket(String bucket) {
            this.bucket = bucket;
        }

        public String getBucket() {
            return bucket;
        }

        public ObjectStorageExceptionBuilder bucket(String bucket) {
            this.setBucket(bucket);
            return this;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public ObjectStorageExceptionBuilder key(String key) {
            this.setKey(key);
            return this;
        }

        public String geteTag() {
            return eTag;
        }

        public void seteTag(String eTag) {
            this.eTag = eTag;
        }

        public ObjectStorageExceptionBuilder eTag(String eTag) {
            this.seteTag(eTag);
            return this;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public ObjectStorageExceptionBuilder message(String message) {
            this.setMessage(message);
            return this;
        }

        public ObjectStorageExceptionBuilder message(String messageToFormat, Object... args) {
            this.setMessage(MessageFormat.format(messageToFormat, args));
            return this;
        }

        public Throwable getCause() {
            return cause;
        }

        public void setCause(Throwable cause) {
            this.cause = cause;
        }

        public ObjectStorageExceptionBuilder cause(Throwable cause) {
            this.setCause(cause);
            return this;
        }

        public ObjectStorageException build() {
            return new ObjectStorageException(this);
        }
    }
}
