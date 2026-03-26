package utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class FileUtils {

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 1️Create folder if it does NOT exist
     * If folder exists → do nothing
     */
    public static String createFolderIfNotExists(String basePath) {
        try {
            LocalDate today = LocalDate.now();
            String folderName = today.format(formatter);
            Path folderPath = Paths.get(basePath+ File.separator+ folderName);

            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
                System.out.println("Folder created: " + folderPath);
            } else {
                System.out.println();
            }

            return folderPath.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     Delete folders older than 'days' compared to current date
     **/
    public static void deleteOldFolders(String basePath, String day) {

        Path baseDir = Paths.get(basePath);
        if (!Files.exists(baseDir)) return;
        int days = Integer.parseInt(day);
        LocalDate cutoffDate = LocalDate.now().minusDays(days);

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(baseDir)) {

            for (Path folder : stream) {

                if (Files.isDirectory(folder)) {
                    String folderName = folder.getFileName().toString();

                    try {
                        LocalDate folderDate = LocalDate.parse(folderName, formatter);

                        if (folderDate.isBefore(cutoffDate)) {
                            deleteRecursively(folder);
                            System.out.println("Deleted folder: " + folder);
                        }

                    } catch (Exception e) {
                        // Folder name not a date → skip
                        continue;
                    }
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Helper: delete folder recursively
     */
    private static void deleteRecursively(Path path) throws IOException {
        Files.walkFileTree(path, new SimpleFileVisitor<>() {

            @Override
            public FileVisitResult visitFile(Path file,
                                             BasicFileAttributes attrs) throws IOException {
                Files.delete(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir,
                                                      IOException exc) throws IOException {
                Files.delete(dir);
                return FileVisitResult.CONTINUE;
            }
        });
    }

}
