package injection;

import injection.exception.IncorrectInitializationException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PackageScanner {

    private static final Logger LOGGER = LogManager.getLogger(PackageScanner.class);

    public static List<Class<?>> findClasses(final String path) {
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(path);
        if (scannedUrl == null) {
            LOGGER.error("Error at finding classes: Could not scan package");
            throw new IncorrectInitializationException("Could not scan package");
        }
        File scannedDir = new File(scannedUrl.getFile());
        File[] listFiles = scannedDir.listFiles();
        if (listFiles == null) {
            LOGGER.error("Error at finding classes: Could not scan package");
            throw new IncorrectInitializationException("Could not scan package");
        }
        List<Class<?>> classes = new ArrayList<>();
        for (File file : listFiles) {
            classes.addAll(findClasses(file, path.replace("/", ".")));
        }
        return classes;
    }

    private static List<Class<?>> findClasses(final File file, final String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        String resource = scannedPackage + "." + file.getName();
        if (file.isDirectory()) {
            File[] listFiles = file.listFiles();
            if (listFiles == null) {
                LOGGER.error("Error at finding classes: Could not scan package");
                throw new IncorrectInitializationException("Could not scan package");
            }
            for (File child : listFiles) {
                classes.addAll(findClasses(child, resource));
            }
        } else if (resource.endsWith(".class")) {
            try {
                String replacement = resource.replace(".class", "");
                classes.add(Class.forName(replacement));
            } catch (ClassNotFoundException ex) {
                LOGGER.error("Error at finding classes: " + ex.getMessage());
                throw new IncorrectInitializationException("Could not scan package");
            }
        }
        return classes;
    }
}

