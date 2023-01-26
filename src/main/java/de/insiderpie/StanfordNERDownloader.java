/**
 * Download the Stanford NER version 4.2.0 and unzip it locally into the ./lib directory.
 * This file is used in the Dockerfile, but you can also run it locally if you want
 * a local copy of the NER.
 */

package de.insiderpie;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class StanfordNERDownloader {
    public static void main(String[] args) throws IOException {
        downloadAndUnzip(
            "https://nlp.stanford.edu/software/stanford-ner-4.2.0.zip",
            "./lib/stanford-ner-4.2.0"
        );
    }

    private static void downloadAndUnzip(String url, String outDir) throws IOException {
        try (InputStream input = downloadFromURL(url)) {
            unzip(input, outDir);
        }
    }

    private static InputStream downloadFromURL(String urlString) throws IOException {
        URL url = new URL(urlString);
        URLConnection connection = url.openConnection();
        connection.setDoOutput(true);
        return connection.getInputStream();
    }

    static void unzip(InputStream is, String targetDirString) throws IOException {
        Path targetDir = Path.of(targetDirString).toAbsolutePath();
        try (ZipInputStream zipIn = new ZipInputStream(is)) {
            ZipEntry ze;
            while ((ze = zipIn.getNextEntry()) != null) {
                Path zePath = Path.of(ze.getName());
                if (isRootDirectory(zePath)) {
                    continue;
                }
                Path resolvedPath = resolvePathAndSkipRootDirectory(targetDir, zePath);
                ensurePathIsLegal(ze, targetDir, resolvedPath);
                createFileOrDirectory(zipIn, ze, resolvedPath);
            }
        }
    }

    private static boolean isRootDirectory(Path zePath) {
        return zePath.getNameCount() == 1;
    }

    private static Path resolvePathAndSkipRootDirectory(Path targetDir, Path zePath) {
        try {
            Path relativePath = zePath.subpath(1, zePath.getNameCount());
            return targetDir.resolve(relativePath);
        } catch (IllegalArgumentException e) {
            System.out.println(zePath);
            System.out.println(zePath.getNameCount());
            throw e;
        }
    }

    private static void ensurePathIsLegal(ZipEntry ze, Path targetDir, Path resolvedPath) {
        if (!resolvedPath.startsWith(targetDir)) {
            // see: https://snyk.io/research/zip-slip-vulnerability
            throw new RuntimeException("Entry with an illegal path: "
                    + ze.getName());
        }
    }

    private static void createFileOrDirectory(ZipInputStream zipIn, ZipEntry ze, Path resolvedPath) throws IOException {
        if (ze.isDirectory()) {
            Files.createDirectories(resolvedPath);
        } else {
            Files.createDirectories(resolvedPath.getParent());
            Files.copy(zipIn, resolvedPath);
        }
    }

}
