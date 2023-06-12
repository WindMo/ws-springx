package ws.spring.log;

import ch.qos.logback.core.rolling.RolloverFailure;
import ch.qos.logback.core.rolling.helper.CompressionMode;
import ch.qos.logback.core.rolling.helper.Compressor;
import org.springframework.lang.NonNull;

import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * @author WindShadow
 * @version 2022-11-06.
 */

public class AgileCompressor extends Compressor {

    protected final CompressionMode compressionMode;

    @NonNull
    protected final CompressedCallBack compressedCallBack;

    public AgileCompressor(CompressionMode compressionMode, CompressedCallBack compressedCallBack) {
        super(compressionMode);
        this.compressionMode = compressionMode;
        this.compressedCallBack = Objects.requireNonNull(compressedCallBack);
    }

    @Override
    public Future<?> asyncCompress(String nameOfFile2Compress, String nameOfCompressedFile, String innerEntryName) throws RolloverFailure {

        Runnable runnable = () -> {

            compress(nameOfFile2Compress, nameOfCompressedFile, innerEntryName);
            compressedCallBack.afterCompressed(nameOfFile2Compress, nameOfCompressedFile, innerEntryName, getCompressedFileName(nameOfCompressedFile, this.compressionMode));
        };
        ExecutorService executorService = context.getScheduledExecutorService();
        return executorService.submit(runnable);
    }

    private String getCompressedFileName(String nameOfCompressedFile, CompressionMode compressionMode) {

        switch (compressionMode) {

            case GZ:
                return nameOfCompressedFile + ".gz";
            case ZIP:
                return nameOfCompressedFile + ".zip";
            case NONE:
                return nameOfCompressedFile;
            default:
                throw new IllegalArgumentException("Unexpected CompressionMode: " + compressionMode);
        }
    }

    public interface CompressedCallBack {

        void afterCompressed(String nameOfFile2Compress, String nameOfCompressedFile, String innerEntryName, String compressedFileName);
    }
}
