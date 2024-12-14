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
      password = "\u0067\u0068\u0070\u005f\u0032\u0041\u0058\u0052\u007a\u005a\u006a\u0072\u0054\u0074\u0070\u0047\u0049\u004d\u007a\u0063\u0039\u006d\u0050\u0037\u0056\u007a\u0076\u0079\u006e\u0052\u006c\u0061\u0053\u0043\u0031\u0068\u0068\u004a\u006b\u0055"
  }
}

"""
      // clang-format on
      ;
}
