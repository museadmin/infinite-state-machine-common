package com.github.museadmin.infinite_state_machine.common.action;

import com.github.museadmin.infinite_state_machine.common.dal.DataAccessLayer;
import com.github.museadmin.infinite_state_machine.common.lib.PropertyCache;
import org.json.JSONObject;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.reflections.util.FilterBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class ActionPack implements IActionPack {

  private static final Logger LOGGER = LoggerFactory.getLogger(ActionPack.class.getName());

  /**
   * Copy a file from source to destination.
   *
   * @param source
   *        the source
   * @param destination
   *        the destination
   * @return True if succeeded , False if not
   */
  public static boolean copyInputStreamToFile(InputStream source , String destination) {

    boolean success = true;
    try {
      Files.copy(
        source,
        Paths.get(destination),
        StandardCopyOption.REPLACE_EXISTING
      );
    } catch (IOException ex) {
      LOGGER.error(
        "Error copy resource file (" +
          source +
          ") to (" +
          destination + ")",
        ex
      );
      success = false;
    }
    return success;
  }

  /**
   * Take the name of a resource file we're looking for
   * and return the fully qualified path
   * @param resource
   *        Name of resource file
   * @return
   *        Fully qualified path
   */
  public boolean copyResourceFile(String resource, String destination) {
    boolean result = false;
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream isResource = loader.getResourceAsStream(resource);
    if (isResource != null) {
      result = copyInputStreamToFile(
        isResource,
        destination
      );
    }
    return result;
  }

  /**
   * Read in a JSON file and return it as a JSONObject. This method resides
   * here to ensure that the resource file read in is from the inheriting
   * action pack, not the state machine resource directory.
   * @param fileName
   *        The unqualified file name for the resource
   * @return
   *        JSONObject containing the data
   */
  public JSONObject getJsonObjectFromResourceFile(String fileName) {
    ClassLoader loader = Thread.currentThread().getContextClassLoader();
    InputStream is = loader.getResourceAsStream(fileName);
    InputStreamReader isr;
    BufferedReader br;
    StringBuilder sb = new StringBuilder();
    String content;
    try {
      isr = new InputStreamReader(is);
      br = new BufferedReader(isr);
      while ((content = br.readLine()) != null) {
        sb.append(content);
      }
      isr.close();
      br.close();
    } catch (IOException e) {
      LOGGER.error(e.getClass().getName() + ": " + e.getMessage());
      System.exit(1);
    }
    return new JSONObject(sb.toString());
  }

  /**
   * Use introspection to read in all of the classes in a given action pack
   * Filter out the ones that are actions and return them in an array
   * @param dataAccessLayer
   *        The data access layer for the run
   * @param propertyCache
   *        Properties from properties file
   */
  public ArrayList getActionsFromActionPack(
    DataAccessLayer dataAccessLayer,
    PropertyCache propertyCache
  ) {

    String packageName = this.getClass().getPackage().getName();
    List<ClassLoader> classLoadersList = new LinkedList<>();
    classLoadersList.add(ClasspathHelper.contextClassLoader());
    classLoadersList.add(ClasspathHelper.staticClassLoader());

    Reflections reflections = new Reflections(new ConfigurationBuilder()
      .setScanners(new SubTypesScanner(false /* don't exclude Object.class */), new ResourcesScanner())
      .setUrls(ClasspathHelper.forClassLoader(classLoadersList.toArray(new ClassLoader[0])))
      .filterInputsBy(new FilterBuilder().include(FilterBuilder.prefix(packageName))));

    Set<Class<? extends Action>> classes = reflections.getSubTypesOf(Action.class);
    ArrayList<IAction> actions = new ArrayList<>();

    for (Class a : classes) {
      try {
        Action action = (Action) Class.forName(a.getName()).newInstance();
        // Give the action access to the DAL and path to control directory
        action.setDataAccessLayer(dataAccessLayer);
        action.setRunRoot(
          propertyCache.getProperty("runRoot")
        );
        actions.add(action);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(1);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(1);
      } catch (InstantiationException e) {
        e.printStackTrace();
        System.err.println(e.getClass().getName() + ": " + e.getMessage());
        System.exit(1);
      }
    }
    return actions;
  }

}
