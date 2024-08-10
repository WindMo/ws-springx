package ws.spring.util.lambda;

/**
 * 表示错误的lambda 实现方法的方法签名
 *
 * @author WindShadow
 * @version 2024-10-12.
 */
public class IllegalMethodSignatureException extends RuntimeException {

    IllegalMethodSignatureException(String message) {
        super(message);
    }

    IllegalMethodSignatureException(String message, Throwable cause) {
        super(message, cause);
    }

    static void toAssert(boolean state, String message) {

        if (!state) {
            throw new IllegalMethodSignatureException(message);
        }
    }
}
