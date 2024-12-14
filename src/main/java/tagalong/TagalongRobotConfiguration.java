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

public class TagalongRobotConfiguration {
  private static File _dir;

  public TagalongRobotConfiguration(String dirString) {
    _dir = new File(dirString);
  }

  public void writeClass() {
    File robotFile = new File(_dir, "/src/main/java/frc/robot/Robot.java");

    // adds shuffleboardMicrosystems, ffTuningMicrosystems, pidTuningMicrosystems
    String fileString = "";
    boolean needsRobotInitEdit = true;
    boolean needsDisabledInitEdit = true;
    boolean needsDisabledExitEdit = true;

    try {
      BufferedReader readFile = new BufferedReader(new FileReader(robotFile));
      String line;
      while ((line = readFile.readLine()) != null) {
        if (line.contains("TagalongConfiguration")) {
          needsRobotInitEdit = false;
        } else if (line.contains("_robotContainer.onDisable()")) {
          needsDisabledInitEdit = false;
        } else if (line.contains("_robotContainer.disabledExit()")) {
          needsDisabledExitEdit = false;
        }
      }
      // close and reopen the file
      readFile.close();
      readFile = new BufferedReader(new FileReader(robotFile));
      while ((line = readFile.readLine()) != null) {
        boolean lineModified = false;
        if (needsRobotInitEdit) {
          if (line.contains("package frc.robot;")) {
            lineModified = true;
            fileString += line;
            fileString += "\nimport tagalong.TagalongConfiguration;\n";
          } else if (line.contains("robotInit()")) {
            lineModified = true;
            fileString += line;
            fileString +=
                // clang-format off
"""

    TagalongConfiguration.LOOP_PERIOD_S = this.getPeriod();
    // Add microsystem name(s) for shuffleboard logging
    // TagalongConfiguration.shuffleboardMicrosystems.add("your microsystem name here");
    // Add microsystem name(s) for feedforward tuning
    // TagalongConfiguration.ffTuningMicrosystems.add("your microsystem name here");
    // Add microsystem name(s) for PID tuning
    // TagalongConfiguration.pidTuningMicrosystems.add("your microsystem name here");


"""
                // clang-format on
                ;
            if (line.contains("{}")) {
              fileString += "  }\n";
            }
          }
        }

        if (needsDisabledInitEdit) {
          if (line.contains("public void disabledInit() {")) {
            lineModified = true;
            fileString +=
                // clang-format off
"""
  public void disabledInit() {
    m_robotContainer.onDisable();

"""
                  // clang-format on
                  ;
              if (line.contains("{}")) {
                fileString += "  }\n";
              }
            }
          }

          if (needsDisabledExitEdit) {
            if (line.contains("public void disabledExit() {")) {
              lineModified = true;
              fileString +=
                  // clang-format off
"""
  public void disabledExit() {
    m_robotContainer.onEnable();

"""
                    // clang-format on
                    ;
                if (line.contains("{}")) {
                  fileString += "  }\n";
                }
              }
            }

            if (!lineModified) {
              fileString += line;
              fileString += "\n";
            }
          }
          readFile.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
        if (needsRobotInitEdit || needsDisabledInitEdit || needsDisabledExitEdit) {
          FileUtils.writeToFile(robotFile, fileString);
        }
      }
    }
