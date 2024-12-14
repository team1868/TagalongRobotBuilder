/**
 * Copyright 2024 The Space Cookies : Girl Scout Troop #62868 and FRC Team #1868
 * Open Source Software; you may modify and/or share it under the terms of
 * the 3-Clause BSD License found in the root directory of this project.
 */

package tagalong;

import java.io.File;
import java.util.Scanner;

public class TagalongProjectFinder {
  public static final String WPILIB_YEAR = "2024";
  public static final String WPILIB_FORMATTED_YEAR = "\"" + WPILIB_YEAR + "\",";
  // {
  // "enableCppIntellisense": false,
  // "currentLanguage": "java",
  // "projectYear": "2024",
  // "teamNumber": 1868
  // }

  public static boolean verifyProjectPath(String projPath) {
    // verify the path and project is valid
    File projFile = new File(projPath);
    if (!projFile.exists() || !projFile.isDirectory() || !projFile.isAbsolute()) {
      System.out.println("Invalid project directory: \"" + projPath + "\"");
      return false;
    }

    // check for robot and robot container files
    File robotFile = new File(projPath + "/src/main/java/frc/robot/Robot.java");
    File robotContainerFile = new File(projPath + "/src/main/java/frc/robot/RobotContainer.java");
    if (!robotFile.exists()) {
      System.out.println(
          "Invalid project directory: \"" + projPath + "\""
          + " robotfile DNE"
      );
    } else if (!robotContainerFile.exists()) {
      System.out.println(
          "Invalid project directory: \"" + projPath + "\""
          + " robotContainerFile DNE"
      );
    } else if (robotFile.isDirectory()) {
      System.out.println(
          "Invalid project directory: \"" + projPath + "\""
          + " robotFile isn't directory"
      );
    } else if (robotContainerFile.isDirectory()) {
      System.out.println(
          "Invalid project directory: \"" + projPath + "\""
          + " robotContainerFile isn't directory"
      );
    }

    // verify .wpilib/wpilib_preferences.json is the correct year
    File wpilibFile = new File(projPath + "/.wpilib/wpilib_preferences.json");
    if (!wpilibFile.exists() || wpilibFile.isDirectory()) {
      System.err.println("Invalid project directory, no wpilib preferences");
      return false;
    }

    try {
      Scanner prefScanner = new Scanner(wpilibFile);
      boolean year = false;
      boolean language = false;
      while (prefScanner.hasNext()) {
        String value = prefScanner.next();
        if (value.matches(".*:")) {
          String prefValue = prefScanner.next();
          // System.out.println(prefValue);
          year = year || prefValue.equalsIgnoreCase(WPILIB_FORMATTED_YEAR);
          language = language || prefValue.equalsIgnoreCase("\"java\",");
        }
      }

      prefScanner.close();
      if (!year) {
        System.out.println("wpilib version year mismatch expected " + WPILIB_YEAR + " but got ");
        return false;
      }
      if (!language) {
        System.out.println("wpilib project language mismatch");
        return false;
      }
    } catch (Exception e) {
      System.err.println(e);
      return false;
    }
    return true;
  }
}
