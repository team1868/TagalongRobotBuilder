/**
 * Copyright 2024 The Space Cookies : Girl Scout Troop #62868 and FRC Team #1868
 * Open Source Software; you may modify and/or share it under the terms of
 * the 3-Clause BSD License found in the root directory of this project.
 */

package tagalong;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import tagalong.utils.FileUtils;

public class TagalongSubsystemConf {
  private String _dir;
  private TagalongSubsystemSpec _subsystemSpec;
  public ArrayList<TagalongMicrosystemSpec> _microspecs;
  public String _robotVersion;
  public TagalongSubsystemConf(
      String dir, TagalongSubsystemSpec subsystemSpec, String robotVersion
  ) {
    _dir = dir;
    _subsystemSpec = subsystemSpec;
    _microspecs = subsystemSpec._microspecs;
    _robotVersion = robotVersion;
  }

  public void writeClass() {
    String fullString = getHeader() + getVars() + getMethod() + getConstructor();

    File constantsDir = new File(_dir + "/src/main/java/frc/robot/subsystems/confs/");
    if (!constantsDir.exists()) {
      constantsDir.mkdirs();
    }

    File writtenFile = new File(
        _dir + "/src/main/java/frc/robot/subsystems/confs/"
        + FileUtils.convertToCapital(_robotVersion) + _subsystemSpec._capitalName + "Conf.java"
    );
    if (!writtenFile.exists()) {
      try {
        writtenFile.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    FileUtils.writeToFile(writtenFile, fullString);
  }

  public String getHeader() {
    String header = "package frc.robot.subsystems.confs;\n";
    for (var microspec : _microspecs) {
      header += "\nimport tagalong.subsystems.micro.confs.%sConf;\n".formatted(
          microspec.getVariant().name()
      );
      header += "import frc.robot.subsystems.micro.confs."
          + FileUtils.convertToCapital(_robotVersion)
          + "%s%sConf;\n".formatted(_subsystemSpec._capitalName, microspec._capitalName);
    }
    header += "\n";
    return header;
  }

  public String getVars() {
    String classString = "";
    classString += "public class " + FileUtils.convertToCapital(_robotVersion)
        + "%sConf extends %sConf {\n".formatted(
            _subsystemSpec._capitalName, _subsystemSpec._capitalName
        );
    for (var microspec : _microspecs) {
      classString += "    public static final %sConf %sConf".formatted(
          microspec.getVariant().getCapitalName(), microspec._camelName
      );
      classString += " = " + FileUtils.convertToCapital(_robotVersion)
          + "%s%sConf.construct();\n".formatted(
              _subsystemSpec._capitalName, microspec._capitalName
          );
    }
    classString += "\n";

    return classString;
  }

  public String getMethod() {
    String classString = "";
    classString += "    public static " + FileUtils.convertToCapital(_robotVersion)
        + "%sConf construct() {\n".formatted(_subsystemSpec._capitalName);
    classString += "        return new " + FileUtils.convertToCapital(_robotVersion)
        + "%sConf (".formatted(_subsystemSpec._capitalName);
    for (var microspec : _microspecs) {
      classString += microspec._camelName + "Conf";
      if (microspec != _microspecs.get(_microspecs.size() - 1)) {
        classString += ",\n";
      }
    }
    classString += ");\n    }\n";
    return classString;
  }
  public String getConstructor() {
    String classString = "";
    classString += "    public " + FileUtils.convertToCapital(_robotVersion)
        + "%sConf(".formatted(_subsystemSpec._capitalName);
    for (var microspec : _microspecs) {
      classString += "%sConf %sConf".formatted(microspec.getVariant().name(), microspec._camelName);
      if (microspec != _microspecs.get(_microspecs.size() - 1)) {
        classString += ", ";
      }
    }
    classString += ") {\n";
    classString += "super(";
    for (var microspec : _microspecs) {
      classString += "    %sConf".formatted(microspec._camelName, microspec._camelName);
      if (microspec != _microspecs.get(_microspecs.size() - 1)) {
        classString += ", ";
      }
    }
    classString += ");\n";
    classString += "  }\n}\n";
    return classString;
  }
}
