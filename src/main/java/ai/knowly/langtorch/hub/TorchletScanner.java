package ai.knowly.langtorch.hub;

import ai.knowly.langtorch.hub.annotation.LangtorchHubApplication;
import ai.knowly.langtorch.hub.exception.AnnotationNotFoundException;
import com.google.common.flogger.FluentLogger;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class TorchletScanner {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final ClassLoader classLoader;

  public TorchletScanner(ClassLoader classLoader) {
    this.classLoader = classLoader;
  }

  private String getPackageNameToScan(Class<?> torchHubClass) {
    Optional<LangtorchHubApplication> torchHubAnnotation =
        Optional.ofNullable(torchHubClass.getAnnotation(LangtorchHubApplication.class));

    if (!torchHubAnnotation.isPresent()) {
      throw new AnnotationNotFoundException(
          String.format(
              "Class %s is not annotated with @TorchHub.", torchHubClass.getCanonicalName()));
    }

    return torchHubAnnotation
        .map(LangtorchHubApplication::value)
        .orElse(torchHubClass.getPackage().getName());
  }

  public List<Class<?>> scan(Class<?> torchHubClass) {
    String packageName = getPackageNameToScan(torchHubClass);
    return scanPackage(packageName);
  }

  private List<Class<?>> scanPackage(String packageName) {
    URL resource = classLoader.getResource(packageNameToPath(packageName));
    if (resource == null) {
      logger.atWarning().log("Package not found: %s.", packageName);
      return Collections.emptyList();
    }

    File directory = new File(resource.getFile());
    return scanDirectory(directory, packageName);
  }

  private List<Class<?>> scanDirectory(File directory, String packageName) {
    File[] files = directory.listFiles();

    if (files == null) {
      logger.atWarning().log("No files in directory: %s.", directory.getPath());
      return Collections.emptyList();
    }

    List<Class<?>> classes = new ArrayList<>();
    for (File file : files) {
      if (file.isDirectory()) {
        classes.addAll(scanDirectory(file, String.format("%s.%s", packageName, file.getName())));
      } else {
        registerClass(packageName, file).ifPresent(classes::add);
      }
    }
    return classes;
  }

  private Optional<Class<?>> registerClass(String packageName, File file) {
    if (!file.getName().endsWith(".class")) {
      return Optional.empty();
    }
    String className = file.getName().replace(".class", "");
    try {
      return Optional.of(classLoader.loadClass(packageName + "." + className));
    } catch (ClassNotFoundException e) {
      logger.atWarning().log("Class not found: %s.%s. Failed to load.", packageName, className);
    }
    return Optional.empty();
  }

  private String packageNameToPath(String packageName) {
    return packageName.replace('.', File.separatorChar);
  }
}
