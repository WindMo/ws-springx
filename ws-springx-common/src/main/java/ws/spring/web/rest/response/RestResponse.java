package ws.spring.web.rest.response;

/**
 * restful请求的响应
 *
 * @author WindShadow
 * @version 2021-12-20.
 */


public class RestResponse<T> {

    private String code;
    private String message;
    private T data;

    public RestResponse() {
    }

    public RestResponse(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "{code=" + this.getCode() + ", message=" + this.getMessage() + ", data=" + this.getData() + "}";
    }

    public static <E> RestResponse<E> of(String code, String message) {

        return new RestResponse<>(code, message, null);
    }

    public static <E> RestResponse<E> of(String code, String message, E data) {

        return new RestResponse<>(code, message, data);
    }

    public static <E> RestResponse<E> of(RestCodeSupplier codeSupplier, String message) {

        return new RestResponse<>(codeSupplier.getCode(), message, null);
    }

    public static <E> RestResponse<E> of(RestCodeSupplier codeSupplier, String message, E data) {

        return new RestResponse<>(codeSupplier.getCode(), message, data);
    }
}
