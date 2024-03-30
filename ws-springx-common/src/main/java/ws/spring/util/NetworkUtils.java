package ws.spring.util;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Objects;

/**
 * @author WindShadow
 * @version 2024-09-29.
 */
public class NetworkUtils {

    public static boolean isNetworkAvailable(String address, int timeout) {

        try {
            return InetAddress.getByName(Objects.requireNonNull(address)).isReachable(timeout);
        } catch (IOException e) {
            return false;
        }
    }
}
