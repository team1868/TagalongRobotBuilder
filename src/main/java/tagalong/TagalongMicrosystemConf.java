/**
 * Copyright 2024 The Space Cookies : Girl Scout Troop #62868 and FRC Team #1868
 * Open Source Software; you may modify and/or share it under the terms of
 * the 3-Clause BSD License found in the root directory of this project.
 */

package tagalong;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import tagalong.enums.MicrosystemVariants;
import tagalong.utils.FileUtils;

public class TagalongMicrosystemConf {
  protected String _dir;
  protected String _microName;
  protected String _subsystemName;
  protected String _className;
  protected File _file;
  protected final MicrosystemVariants _variant;
  protected ArrayList<String> _motorTypes;
  protected ArrayList<String> _motorDeviceIDs;
  protected String _canBus;
  protected String _encoderType;
  public TagalongMicrosystemConf(
      String dir,
      String microName,
      String subsystemName,
      String robotVersion,
      MicrosystemVariants variant,
      ArrayList<String> motorTypes,
      ArrayList<String> motorDeviceIDs,
      String canBus,
      String encoderType
  ) {
    if (microName == null) {
      System.out.println("Invalid Microsystem Name.");
      System.exit(0);
    }
    _dir = dir;
    _microName = microName;
    _subsystemName = subsystemName;
    _className = FileUtils.convertToCapital(robotVersion)
        + FileUtils.convertToCapital(subsystemName) + FileUtils.convertToCapital(microName)
        + "Conf";
    _variant = variant;
    _motorTypes = motorTypes;
    _motorDeviceIDs = motorDeviceIDs;
    _canBus = canBus;
    _encoderType = encoderType;
  }

  public TagalongMicrosystemConf(
      String dir,
      String microName,
      String subsystemName,
      String robotVersion,
      MicrosystemVariants variant,
      ArrayList<String> motorTypes,
      ArrayList<String> motorDeviceIDs,
      String canBus
  ) {
    this(
        dir,
        microName,
        subsystemName,
        robotVersion,
        variant,
        motorTypes,
        motorDeviceIDs,
        canBus,
        "Fused"
    );
  }

  public void writeClass() {
    _file = TagalongRobotBuilder.checkExists(
        _dir + "/src/main/java/frc/robot/subsystems/confs/micro/",
        FileUtils.convertToCapital(_className + ".java")
    );
    FileUtils.writeToFile(
        _file,
        getFileHeader() + getClassHeader() + getRequiredMotorConfigs() + getControlConstants()
            + getRequiredTuning() + getMethods() + getConversions() + getConstructor() + "\n}"
    );
  }

  public String getFileHeader() {
    return "";
  }

  public String getClassHeader() {
    String baseString = "";
    baseString += "\n\npublic class " + _className
        + " extends %s".formatted(_variant.getCapitalName()) + "Conf"
        + " {\n ";
    return baseString;
  }

  public String arrayToString(ArrayList<String> elements) {
    String baseString = "";
    for (int i = 0; i < elements.size(); i++) {
      baseString += elements.get(i);
      if (i != _motorDeviceIDs.size() - 1) {
        baseString += ", ";
      }
    }
    return baseString;
  }

  public String getRequiredMotorConfigs() {
    return "";
  }

  protected String getRequiredMotorConfigsHeader() {
    return
        // clang-format off
"""
  public static final String name = \"%s\";
  public static final Motors[] motorTypes = {%s};
  public static final int[] motorDeviceIDs = {%s};
  public static final String[] motorCanBus = {"%s"};

  public static final InvertedValue[] motorDirection = {InvertedValue.CounterClockwise_Positive};
  public static final NeutralModeValue[] motorEnabledBrakeMode = {NeutralModeValue.Coast};
  public static final NeutralModeValue[] motorDisabledBrakeMode = {NeutralModeValue.Coast};

"""
        // clang-format on
        ;
  }

  public String getControlConstants() {
    return "";
  }

  public String getRequiredTuning() {
    return "";
  }

  public String getMethods() {
    // clang-format off
    return "";
  }

  public String getConversions() {
    return "";
  }

  public String getConstructor() {
    return "";
  }

  protected String motorArrayToString(List<String> motors) {
    String result = "";
    for(int i = 0; i < motors.size()-1; i++) {
      result += motors.get(i) + ", ";
    }
    result += motors.get(motors.size()-1);
    return result;
  }
}
