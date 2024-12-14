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

public class TagalongSubsystemBaseConf {
  private String _dir;
  private TagalongSubsystemSpec _subsystemSpec;
  public ArrayList<TagalongMicrosystemSpec> _microspecs;
  public TagalongSubsystemBaseConf(String dir, TagalongSubsystemSpec subsystemSpec) {
    _dir = dir;
    _subsystemSpec = subsystemSpec;
    _microspecs = subsystemSpec._microspecs;
  }

  public void writeClass() {
    String fullString = getHeader() + getConfClassString();

    File constantsDir = new File(_dir + "/src/main/java/frc/robot/subsystems/confs/");
    if (!constantsDir.exists()) {
      constantsDir.mkdirs();
    }

    File writtenFile = new File(
        _dir + "/src/main/java/frc/robot/subsystems/confs/" + _subsystemSpec._capitalName
        + "Conf.java"
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
      header += "\nimport tagalong.subsystems.confs.micro.%sConf;\n".formatted(
          microspec.getVariant().getCapitalName()
      );
    }
    header += "\n";
    return header;
  }

  public String getConfClassString() {
    String classString = "";
    classString += "public class %sConf {\n".formatted(_subsystemSpec._capitalName);
    for (var microspec : _microspecs) {
      classString += "    public %sConf %sConf;\n".formatted(
          microspec.getVariant().name(), microspec._camelName
      );
    }

    classString += "public %sConf(".formatted(_subsystemSpec._capitalName);
    for (var microspec : _microspecs) {
      classString += "%sConf %sConf".formatted(microspec.getVariant().name(), microspec._camelName);
      if (microspec != _microspecs.get(_microspecs.size() - 1)) {
        classString += ", ";
      }
    }
    classString += ") {\n";
    for (var microspec : _microspecs) {
      classString +=
          "this.%sConf = %sConf;\n".formatted(microspec._camelName, microspec._camelName);
    }
    classString += "  }\n}\n";

    return classString;
  }
}
