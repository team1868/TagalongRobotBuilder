package tagalong;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import tagalong.utils.FileUtils;

public class TagalongGradleBuildConfiguration {
  private static File _dir;

  public TagalongGradleBuildConfiguration(String dirString) {
    _dir = new File(dirString);
  }

  public void writeConf() {
    File buildFile = new File(_dir, "/build.gradle");
    String fileString = "";

    try {
      boolean containsRepositories = false;
      BufferedReader readFile = new BufferedReader(new FileReader(buildFile));
      String line;
      while ((line = readFile.readLine()) != null) {
        if (line.contains("\"space-cookies-bot\"")) {
          return;
        } else if (line.contains("repositories {")) {
          containsRepositories = true;
          fileString += line;
          fileString += mavenRepoString;
        } else {
          fileString += line;
          fileString += "\n";
        }
      }
      if (!containsRepositories) {
        fileString = fileString.replace(
            "def ROBOT_MAIN_CLASS",
            "\nrepositories {\n" + mavenRepoString + "}\ndef ROBOT_MAIN_CLASS"
        );
      }
      readFile.close();
      FileUtils.writeToFile(buildFile, fileString);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static final String mavenRepoString =
      // clang-format off
"""

maven {
  url = uri("https://maven.pkg.github.com/team1868/TagalongLib")
  credentials {
      username = "space-cookies-bot"
      password = "\\u0067\\u0069\\u0074\\u0068\\u0075\\u0062\\u005f\\u0070\\u0061\\u0074\\u005f\\u0031\\u0031\\u0042\\u004e\\u0057\\u0049\\u0045\\u0042\\u0041\\u0030\\u0035\\u006b\\u0053\\u006c\\u0052\\u0049\\u0032\\u0059\\u0077\\u006d\\u0047\\u0072\\u005f\\u006c\\u004e\\u0056\\u0039\\u004b\\u0049\\u0076\\u006f\\u0077\\u0050\\u004b\\u0065\\u0030\\u0046\\u0045\\u0031\\u004e\\u0077\\u0053\\u0073\\u0047\\u004c\\u0055\\u0033\\u0035\\u0064\\u0070\\u0074\\u0079\\u0079\\u0057\\u0043\\u0031\\u0074\\u004c\\u0049\\u0036\\u0072\\u0039\\u0071\\u0064\\u007a\\u0036\\u0044\\u0037\\u004b\\u0056\\u004c\\u005a\\u0056\\u004c\\u006d\\u006e\\u0073\\u0038\\u004a\\u0035\\u0056\\u0051"
  }
}

"""
      // clang-format on
      ;
}
