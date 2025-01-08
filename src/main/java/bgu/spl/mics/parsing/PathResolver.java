package bgu.spl.mics.parsing;

import java.nio.file.Path;
import java.nio.file.Paths;

public class PathResolver {

    /**
     * Resolves a relative path to an absolute path based on an absolute file's path in the same directory.
     *
     * @param relativePath The relative path to resolve.
     * @param absoluteFilePath The absolute path of a file in the same directory as the relative file.
     * @return The absolute path of the relative file.
     */
    public static String resolveRelativePath(String relativePath, String absoluteFilePath) {
        // Convert the absolute file path to a Path object
        Path absolutePath = Paths.get(absoluteFilePath);

        // Get the directory where the absolute file is located
        Path directoryPath = absolutePath.getParent();

        // Resolve the relative path against the directory path
        Path resolvedPath = directoryPath.resolve(relativePath);

        // Return the absolute path of the resolved file
        return resolvedPath.toAbsolutePath().toString();
    }

    public static String getDirectoryPath(String filePath){
        // Convert the absolute file path to a Path object
        Path path = Paths.get(filePath);
        // Get the directory where the absolute file is located
        Path directoryPath = path.getParent();
        return directoryPath.toString();
    }

}

