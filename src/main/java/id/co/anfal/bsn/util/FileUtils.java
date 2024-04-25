package id.co.anfal.bsn.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

public class FileUtils {

    private static final Logger logger = (Logger) LoggerFactory.getLogger(FileUtils.class);

    // Private constructor to hide the implicit public one
    private FileUtils() {
        throw new IllegalStateException("Utility class");
    }

    public static byte[] readFileFromLocation(String fileUrl) {
        if (StringUtils.isBlank(fileUrl)) {
            logger.warning("File URL is blank or null");
            return new byte[0];
        }
        try {
            Path filePath = new File(fileUrl).toPath();
            return Files.readAllBytes(filePath);
        } catch (IOException e) {
            logger.warning("Failed to read file from the path");
        }
        return new byte[0]; //  cara mengonversi string kosong menjadi byte array. Menggunakan StandardCharsets.UTF_8 untuk konversi yang lebih aman dan jelas.
    }
}
