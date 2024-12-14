/**
 * Copyright 2024 The Space Cookies : Girl Scout Troop #62868 and FRC Team #1868
 * Open Source Software; you may modify and/or share it under the terms of
 * the 3-Clause BSD License found in the root directory of this project.
 */

package tagalong.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileUtils {
  public static void writeToFile(String fileName, String str) {
    try {
      FileWriter _fileWriter = new FileWriter(fileName, true);
      _fileWriter.write(str);
      _fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void writeToFile(File file, String str) {
    try {
      BufferedWriter _fileWriter = new BufferedWriter(new FileWriter(file));
      _fileWriter.write(str);
      _fileWriter.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String convertToCamel(String str) {
    return str.substring(0, 1).toLowerCase() + str.substring(1);
  }

  public static String convertToCapital(String str) {
    return str.substring(0, 1).toUpperCase() + str.substring(1);
  }
}
