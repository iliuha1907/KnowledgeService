package com.senla.training.hoteladmin.util;

import com.senla.training.hoteladmin.exception.IncorrectWorkException;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class PackageScanner {

    public static List<Class<?>> find(String path) {
        URL scannedUrl = Thread.currentThread().getContextClassLoader().getResource(path);
        if (scannedUrl == null) {
            throw new IncorrectWorkException("Could not scan package");
        }
        File scannedDir = new File(scannedUrl.getFile());
        List<Class<?>> classes = new ArrayList<>();
        for (File file : scannedDir.listFiles()) {
            classes.addAll(find(file, path.replaceAll("/", ".")));
        }
        return classes;
    }

    private static List<Class<?>> find(File file, String scannedPackage) {
        List<Class<?>> classes = new ArrayList<>();
        String resource = scannedPackage + "." + file.getName();
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                classes.addAll(find(child, resource));
            }
        } else if (resource.endsWith(".class")) {
            try {
                String replacement = resource.replace(".class", "");
                classes.add(Class.forName(replacement));
            } catch (ClassNotFoundException ex) {
                throw new IncorrectWorkException("Could not scan package");
            }
        }
        return classes;
    }
}

