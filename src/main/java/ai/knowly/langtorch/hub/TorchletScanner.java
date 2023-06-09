package ai.knowly.langtorch.hub;

import com.google.common.flogger.FluentLogger;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class TorchletScanner {
  private static final FluentLogger logger = FluentLogger.forEnclosingClass();
  private final ClassLoader classLoader;

  private final List<Class<?>> foundClasses;

  public TorchletScanner(ClassLoader classLoader) {
    this.classLoader = classLoader;
    this.foundClasses = new ArrayList<>();
  }

  public List<Class<?>> scanPackage(String packageName) {
    URL resource = classLoader.getResource(packageNameToPath(packageName));
    File directory = new File(resource.getFile());
    scanDirectory(directory, packageName);
    return foundClasses;
  }

  private void scanDirectory(File directory, String packageName) {
    File[] files = directory.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          scanDirectory(file, String.format("%s.%s", packageName, file.getName()));
        } else {
          registerClass(packageName, file);
        }
      }
    }
  }

  private void registerClass(String packageName, File file) {
    String className = file.getName().replace(".class", "");
    Class<?> aClass;
    try {
      aClass = classLoader.loadClass(packageName + "." + className);
      foundClasses.add(aClass);
    } catch (ClassNotFoundException e) {
      logger.atWarning().log("Class not found:%s.%s and fail to load.", packageName, className);
    }
  }

  private String packageNameToPath(String packageName) {
    return packageName.replace('.', File.separatorChar);
  }
}
