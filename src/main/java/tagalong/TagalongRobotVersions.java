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

public class TagalongRobotVersions {
  public static boolean _redundantSubsystem = false;

  private String _dir;
  private TagalongSubsystemSpec _spec;
  private String _robotVersion; // compbot;

  public TagalongRobotVersions(String dir, TagalongSubsystemSpec spec, String robotVersion) {
    _dir = dir;
    _robotVersion = robotVersion;
    _spec = spec;
  }

  public void writeClass() {
    File constantsDir = new File(_dir, "/src/main/java/frc/robot/constants/");
    if (!constantsDir.exists()) {
      constantsDir.mkdirs();
    }

    // writes RobotVersions in constants file
    File constantsFile = new File(_dir, "/src/main/java/frc/robot/Constants.java");
    String constString = "";
    String prevLine = "";
    boolean isChanged = false;
    try {
      if (!constantsFile.exists()) {
        constantsFile.createNewFile();
        String defaultConstantsFile =
            // clang-format off
"""
package frc.robot;

import frc.robot.constants.RobotVersions;
public final class Constants{
  public static final RobotVersions curRobot = RobotVersions.%s;
}
"""
            // clang-format on
            .formatted(_robotVersion.toUpperCase());
        FileUtils.writeToFile(constantsFile, defaultConstantsFile);
      } else {
        BufferedReader readFile = new BufferedReader(new FileReader(constantsFile));
        String line;
        while ((line = readFile.readLine()) != null) {
          String tempString = line;
          if (line.contains("RobotVersions")) {
            break;
          } else if (prevLine.contains("package frc.robot;")) {
            constString += "\nimport frc.robot.constants.RobotVersions;";
            constString += tempString;
            constString += "\n";
          } else if (line.contains("}")) {
            isChanged = true;
            constString += tempString;
            constString += "\n";
          } else {
            constString += tempString.replaceAll(
                "(class Constants)  *\\{",
                ("class Constants{\n  public static final RobotVersions curRobot = "
                 + "RobotVersions.%s;\n")
                    .formatted(_robotVersion.toUpperCase())
            );
            constString += "\n";
          }
          prevLine = line;
        }
        readFile.close();
        if (isChanged) {
          FileUtils.writeToFile(constantsFile, constString);
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    // Actually handle RobotVersions.java file
    String robotVersionsString = "";
    File robotVersFile = new File(_dir, "/src/main/java/frc/robot/constants/RobotVersions.java");
    // File doesn't exist, write the RobotVersions.java file from scratch
    if (!robotVersFile.exists()) {
      try {
        robotVersFile.createNewFile();
        robotVersionsString = getHeader() + getVars() + getConstructor();
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      try {
        // Check if version already exists
        BufferedReader readFile = new BufferedReader(new FileReader(robotVersFile));
        String line;

        // Parse out the system
        boolean startOfEnums = false;
        boolean endOfEnums = false;
        String allEnums = "";
        String allSubsystems = "";
        while ((line = readFile.readLine()) != null) {
          if (endOfEnums) {
            if (line.matches(".*public final.+Conf\s+.+Conf;"))
              allSubsystems += line;
          } else if (startOfEnums) {
            allEnums += line;
            if (line.contains(";")) {
              endOfEnums = true;
            }
          } else if (line.contains("public enum RobotVersions {")) {
            startOfEnums = true;
          }
        }
        readFile.close();

        String[] versionNames = allEnums.split("\\)\\),|\\)\\);");
        for (int i = 0; i < versionNames.length; i++) {
          versionNames[i] = versionNames[i].split("\\(")[0].replace(" ", "").replace("\t", "");
          versionNames[i] = versionNames[i].substring(0, 1).toUpperCase()
              + versionNames[i].substring(1).toLowerCase();
        }

        String[] capitalSubsystemNames = allSubsystems.split(";");
        String[] camelSubsystemNames = new String[capitalSubsystemNames.length];
        for (int i = 0; i < capitalSubsystemNames.length; i++) {
          capitalSubsystemNames[i] = capitalSubsystemNames[i]
                                         .replace("public ", "")
                                         .replace("final ", "")
                                         .replace("\t", "")
                                         .replaceAll("\\s+[a-z]+Conf", "")
                                         .replace(" ", "")
                                         .replace("Conf", "");
          camelSubsystemNames[i] = FileUtils.convertToCamel(capitalSubsystemNames[i]);
        }

        // readFile = new BufferedReader(new FileReader(robotVersFile));
        // line = "";
        // robotVersionsString = "";

        // // Add new version
        // if (!doesVersionExist) {
        //   // Create new confs for existing subsystems and their microsystems for robotVersion
        //   // Add a line into
        // }

        String newMemberVariables = "";
        String newConstructorArgs = "";
        String newConstructorAssignment = "";
        for (int i = 0; i < camelSubsystemNames.length; i++) {
          newMemberVariables += "\n  public final %sConf %sConf;".formatted(
              capitalSubsystemNames[i], camelSubsystemNames[i]
          );
          newConstructorArgs +=
              "%sConf %sConf".formatted(capitalSubsystemNames[i], camelSubsystemNames[i]);
          if (i < (camelSubsystemNames.length - 1)) {
            newConstructorArgs += ",";
          }
          newConstructorAssignment += "\n    this.%sConf = %sConf;".formatted(
              camelSubsystemNames[i], camelSubsystemNames[i]
          );
          if (capitalSubsystemNames[i].equals(_spec._capitalName)) {
            _redundantSubsystem = true;
          }
        }
        if (!_redundantSubsystem) {
          newMemberVariables +=
              "\n  public final %sConf %sConf;".formatted(_spec._capitalName, _spec._camelName);
          newConstructorArgs += ", %sConf %sConf".formatted(_spec._capitalName, _spec._camelName);
          newConstructorAssignment +=
              "\n    this.%sConf = %sConf;".formatted(_spec._camelName, _spec._camelName);
        }
        boolean isNewRobotVersion = true;
        String[] newVersionEnums = new String[versionNames.length];
        for (int i = 0; i < versionNames.length; i++) {
          newVersionEnums[i] = versionNames[i].toUpperCase() + "(";
          for (int j = 0; j < capitalSubsystemNames.length; j++) {
            newVersionEnums[i] +=
                "%s%sConf.construct()".formatted(versionNames[i], capitalSubsystemNames[j]);
            if (j < capitalSubsystemNames.length - 1) {
              newVersionEnums[i] += ", ";
            }
          }
          // TODO add conf file here, the filename is printed to standard out
          System.out.println(
              versionNames[i] + _spec._capitalName + ".java and associated microsystem confs"
          );
          if (!_redundantSubsystem) {
            // newVersionEnums[i] +=
            //     ", %s%sConf.construct()".formatted(versionNames[i], _spec._capitalName);
            newVersionEnums[i] += ", null".formatted(versionNames[i], _spec._capitalName);
          }
          newVersionEnums[i] += ")";
          if (_robotVersion.equals(versionNames[i])) {
            isNewRobotVersion = false;
          }
        }

        String fullEnumString = "";
        if (isNewRobotVersion) {
          for (int i = 0; i < newVersionEnums.length; i++) {
            fullEnumString += newVersionEnums[i] + ",\n";
          }

          fullEnumString += _robotVersion.toUpperCase() + "(";
          for (int i = 0; i < capitalSubsystemNames.length; i++) {
            // TODO add conf file for each subsystem here
            System.out.println(
                _robotVersion + capitalSubsystemNames[i] + ".java and associated microsystem confs"
            );
            // fullEnumString +=
            //     "%s%sConf.construct()".formatted(_robotVersion, capitalSubsystemNames[i]);
            fullEnumString += "null";
            if (i < (capitalSubsystemNames.length - 1)) {
              fullEnumString += ",";
            }
          }

          // TODO add conf file for the singular new robot version +new subsystem combo here
          if (!_redundantSubsystem) {
            fullEnumString +=
                ", %s%sConf.construct())".formatted(_robotVersion, _spec._capitalName);
          } else {
            fullEnumString += ") ";
          }
        } else {
          for (int i = 0; i < newVersionEnums.length; i++) {
            fullEnumString += newVersionEnums[i];
          }
        }

        // Full RobotVersions file
        robotVersionsString =
            // clang-format off
"""
package frc.robot.constants;

import frc.robot.subsystems.confs.*;

public enum RobotVersions {
%s;

%s

  RobotVersions(%s) {
%s
  }
}

"""
            // clang-format on
            .formatted(
                fullEnumString, newMemberVariables, newConstructorArgs, newConstructorAssignment
            );

        readFile.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    FileUtils.writeToFile(robotVersFile, robotVersionsString);
  }

  private String getHeader() {
    String header = "package frc.robot.constants;\n";
    header += "\nimport frc.robot.subsystems.confs.%sConf;\n".formatted(
        _robotVersion + _spec._capitalName
    );
    header += "import frc.robot.subsystems.confs.%sConf;".formatted(_spec._capitalName);
    header += "\n";
    return header;
  }

  private String getVars() {
    String classString = "";
    classString += "public enum RobotVersions {\n";
    classString += "  " + _robotVersion.toUpperCase() + "(";
    classString += "%s%sConf.construct()".formatted(_robotVersion, _spec._capitalName);
    classString += ");\n";

    classString +=
        "  public final %sConf %sConf;\n".formatted(_spec._capitalName, _spec._camelName);

    return classString;
  }

  private String getConstructor() {
    String classString = "";
    classString += "  RobotVersions(";
    classString += "%sConf %sConf".formatted(_spec._capitalName, _spec._camelName);
    classString += ") {\n";
    classString += "    this.%sConf = %sConf;\n".formatted(_spec._camelName, _spec._camelName);
    classString += "  }\n}\n";
    return classString;
  }
}
