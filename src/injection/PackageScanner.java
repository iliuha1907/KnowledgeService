package injection;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PackageScanner {

    public static List<Class<?>> findClasses(String path) {
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(path);
        if (scannedUrl == null) {
            throw new IncorrectInitializationException("Could not scan package");
        }
        File scannedDir = new File(scannedUrl.getFile());
        File[] listFiles = scannedDir.listFiles();
        if (listFiles == null) {
            throw new IncorrectInitializationException("Could not scan package");
        }
        List<Class<?>> classes = new ArrayList<>();
        for (File file : listFiles) {
            classes.addAll(findClasses(file, path.replace("/", ".")));
        }
        return classes;
    }

    private static List<Class<?>> findClasses(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        String resource = scannedPackage + "." + file.getName();
        if (file.isDirectory()) {
            if (file.listFiles() == null) {
                throw new IncorrectInitializationException("Could not scan package");
            }
            for (File child : file.listFiles()) {
                classes.addAll(findClasses(child, resource));
            }
        } else if (resource.endsWith(".class")) {
            try {
                String replacement = resource.replace(".class", "");
                classes.add(Class.forName(replacement));
            } catch (ClassNotFoundException ex) {
                throw new IncorrectInitializationException("Could not scan package");
            }
        }
        return classes;
    }
}

