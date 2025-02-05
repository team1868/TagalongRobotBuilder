/**
 * Copyright 2024 The Space Cookies : Girl Scout Troop #62868 and FRC Team #1868
 * Open Source Software; you may modify and/or share it under the terms of
 * the 3-Clause BSD License found in the root directory of this project.
 */

package tagalong;

import java.io.File;
import java.io.IOException;
import tagalong.utils.FileUtils;

public class TagalongVendorDep {
  public static void addVendorDep(String dir) {
    String tagalongLibVersion = "2025.21.0204";
    String vendorDep =
        // clang-format off
"""
{
  "fileName": "TagalongLib.json",
  "name": "TagalongLib",
  "version": "%s",
  "frcYear": "2025",
  "uuid": "32b609c1-86c7-4c61-a0f7-7debd9d77017",
  "mavenUrls": ["https://maven.pkg.github.com/Team1868/TagalongLib"],
  "jsonUrl": "https://github.com/team1868/TagalongLib/releases/latest/download/TagalongLib.json",
  "javaDependencies": [
    {
      "groupId": "com.tagalong.lib",
      "artifactId": "tagalonglib-java",
      "version": "%s"
    }
  ],
  "jniDependencies": [],
  "cppDependencies": []
}

""" 
        // clang-format on
        .formatted(tagalongLibVersion, tagalongLibVersion);

    File vendorDir = new File((dir + "/vendordeps/"));
    if (!vendorDir.exists() && !vendorDir.isDirectory()) {
      vendorDir.mkdirs();
    }
    File vendorDepsFile = new File(vendorDir + "/TagalongLib.json");
    if (!vendorDepsFile.exists()) {
      try {
        vendorDepsFile.createNewFile();
        FileUtils.writeToFile(vendorDepsFile, String.format(vendorDep, tagalongLibVersion));
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
