/**
 * Copyright 2024 The Space Cookies : Girl Scout Troop #62868 and FRC Team #1868
 * Open Source Software; you may modify and/or share it under the terms of
 * the 3-Clause BSD License found in the root directory of this project.
 */

package tagalong;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import tagalong.utils.FileUtils;

public class TagalongRobotContainerConfiguration {
  private final File _dir;
  private final String _camelSubsystemName;
  private final String _capitalSubsystemName;

  public TagalongRobotContainerConfiguration(String dirString, String newSubsystemVariableName) {
    _dir = new File(dirString);
    _camelSubsystemName = FileUtils.convertToCamel(newSubsystemVariableName);
    _capitalSubsystemName = FileUtils.convertToCapital(newSubsystemVariableName);
  }

  public void writeClass() {
    File robotFile = new File(_dir, "/src/main/java/frc/robot/RobotContainer.java");

    String fileString = "";
    boolean needsImportInjection = true;
    boolean onEnableExists = false;
    boolean onDisableExists = false;

    try {
      BufferedReader readFile = new BufferedReader(new FileReader(robotFile));
      String line;
      while ((line = readFile.readLine()) != null) {
        if (line.contains("Constants;")) {
          needsImportInjection = false;
        }
        if (line.contains("onEnable();")) {
          onEnableExists = true;
        }
        if (line.contains("onDisable();")) {
          onDisableExists = true;
        }
      }

      // close and reopen the file
      readFile.close();
      readFile = new BufferedReader(new FileReader(robotFile));

      while ((line = readFile.readLine()) != null) {
        boolean lineModified = false;

        if (needsImportInjection && line.contains("package frc.robot;")) {
          lineModified = true;
          fileString += line;
          fileString += "\n\nimport frc.robot.Constants;";
          fileString += "\nimport frc.robot.subsystems.*;\n";
        }

        if (line.contains("public class RobotContainer")) {
          lineModified = true;
          fileString += line;
          fileString += "\n  public final %s _%s = new %s(Constants.curRobot.%sConf);\n".formatted(
              _capitalSubsystemName, _camelSubsystemName, _capitalSubsystemName, _camelSubsystemName
          );
        }

        if (line.contains("public void onEnable() {")) {
          lineModified = true;
          fileString += line;
          fileString += "\n\n    _%s.onEnable();\n".formatted(_camelSubsystemName);
        }

        if (line.contains("public void onDisable() {")) {
          lineModified = true;
          fileString += line;
          fileString += "\n\n    _%s.onDisable();\n".formatted(_camelSubsystemName);
        }

        if (!lineModified) {
          fileString += line;
          fileString += "\n";
        }
      }
      readFile.close();

      // Add on enable and on disable functions
      if (!onEnableExists) {
        fileString = fileString.substring(0, fileString.length() - 3) +
            // clang-format off
"""
  public void onEnable() {
    _%s.onEnable();
  }
}

"""
              //clang-format on
              .formatted(_camelSubsystemName);
      }
      if (!onDisableExists) {
        fileString = fileString.substring(0, fileString.length() - 3) +
            // clang-format off
"""
  public void onDisable() {
    _%s.onDisable();
  }
}

"""
              //clang-format on
              .formatted(_camelSubsystemName);
      }

      FileUtils.writeToFile(robotFile, fileString);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
