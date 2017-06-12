package model;

import java.util.Objects;

public class Error {

    private String code;
    private String message;

    public Error() {
    }

    public Error(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
        return code.hashCode() * message.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Error
                && obj.hashCode() == this.hashCode()
                && !(!Objects.equals(((Error) obj).getCode(), this.getCode())
                || !Objects.equals(((Error) obj).getMessage(), this.getMessage())
        );
    }
}
