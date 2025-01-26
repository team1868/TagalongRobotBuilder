/**
 * Copyright 2024 The Space Cookies : Girl Scout Troop #62868 and FRC Team #1868
 * Open Source Software; you may modify and/or share it under the terms of
 * the 3-Clause BSD License found in the root directory of this project.
 */

package tagalong;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;
import tagalong.enums.MicrosystemTypes;
import tagalong.enums.MicrosystemVariants;
import tagalong.microsystemConfs.TagalongElevatorConf;
import tagalong.microsystemConfs.TagalongPivotConf;
import tagalong.microsystemConfs.TagalongRollerConf;
import tagalong.utils.FileUtils;

public class TagalongRobotBuilder {
  // private String _robotContainer;
  // private File _file;
  // private static String _dir;

  public TagalongRobotBuilder(String dir) {
    // _robotContainer = dir + "RobotContainer.java";
    // _file = new File(_robotContainer);
    // _dir = dir;
  }

  // TODO: update version of tagalonglib so no error in generated code
  public static void main(String args[]) {
    MicrosystemTypes type = MicrosystemTypes.Default;
    MicrosystemVariants variant = MicrosystemVariants.Default;
    int micro;
    int variantNum;
    String projPath = "";
    String robotVersion = "";
    String subsystemName;
    TagalongSubsystemSpec subsystemSpec;
    ArrayList<TagalongMicrosystemSpec> microSpecs = new ArrayList<>();
    TagalongMicrosystemSpec microSpec;
    TagalongMicrosystemConf conf;
    ArrayList<String> motorTypes = new ArrayList<>();
    String canBus = "";
    ArrayList<String> motorDeviceIDs = new ArrayList<String>();

    Scanner scan = new Scanner(System.in);
    switch (args.length) {
      case 0:
        // Query and verify project path
        System.out.print("Please enter the absolute path to the project repo: ");
        projPath = scan.next();
        while (!TagalongProjectFinder.verifyProjectPath(projPath)) {
          System.out.print("Please enter the absolute path to the project repo: ");
          projPath = scan.next();
        }
        break;
      case 2:
        // json mode
        System.err.println("json based config not supported yet");
        System.exit(1);
      default:
        System.err.println("unsupported number of arguments received: " + args.length);
        System.exit(1);
    }

    scan.nextLine();

    System.out.println("What robot version is this for? (no spaces)");
    robotVersion = scan.nextLine();

    // Name of subsystem
    System.out.println("What is the name of the subsystem would you like to add? ");
    subsystemName = scan.next();

    // Check to make sure the subsystem does not already exist in the subsystem
    // directory

    File f1Dir = new File(projPath + "/src/main/java/frc/robot/subsystems");
    if (!f1Dir.exists()) {
      f1Dir.mkdir();
    }
    File f1 = new File(f1Dir, FileUtils.convertToCapital(subsystemName + ".java"));
    if (!f1.exists()) {
      try {
        f1.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    if (f1.exists() && !f1.isDirectory()) {
      char continues = 'Y';
      while (continues == 'Y') {
        // First micro system
        while (type == MicrosystemTypes.Default) {
          System.out.println(
              "What type of microsystem would you like to add? Please enter the corresponding "
              + "number\n"
              + "0: " + MicrosystemTypes.Linear.name() + " 1: " + MicrosystemTypes.Rotational.name()
          );
          // this has to be a roller or linear or ___not yet defined___
          try {
            micro = scan.nextInt();
          } catch (Exception e) {
            System.err.println(e);
            break;
          }
          switch (micro) {
            case 0:
              type = MicrosystemTypes.Linear;
              break;
            case 1:
              type = MicrosystemTypes.Rotational;
              break;
            default:
              System.out.println("invalid microsystem type #");
              break;
          }
        }

        while (variant == MicrosystemVariants.Default) {
          // Select micro system variant
          System.out.println(
              "What variant of microsystem would you like to add? Please enter the "
              + "corresponding number"
          );
          for (int i = 0; i < type.variants.length; i++) {
            System.out.print(i + ": " + type.variants[i].name() + " ");
          }
          System.out.println("");
          try {
            variantNum = scan.nextInt();
            scan.nextLine();
          } catch (Exception e) {
            System.err.println(e);
            break;
          }

          for (int i = 0; i < type.variants.length; i++) {
            if (variantNum == i) {
              variant = type.variants[i];
            }
          }
          if (variant == MicrosystemVariants.Default) {
            System.out.println("invalid microsystem variant #");
          }
        }
        // Name of micro system
        System.out.println("What do you want to name your microsystem?");
        String microName = scan.nextLine();

        System.out.println("What is the name of the CAN bus? Use `rio` for the roborio");
        canBus = scan.nextLine();

        int encoderID = 0;
        if (variant == MicrosystemVariants.PivotFused || variant == MicrosystemVariants.PivotUnfused
            || variant == MicrosystemVariants.PivotNoCancoder) {
          System.out.println("What is the encoder id?");
          encoderID = Integer.valueOf(scan.nextLine());
        }

        // motor agnostic
        System.out.println(
            "What type of motor are you using?\n1: Kraken_X60 2: Kraken_X60 FOC 3: "
            + "Falcon 500 4: Falcon 500 FOC 5: Kraken_x44 6: Kraken_x44 FOC "
        );
        switch (scan.nextInt()) {
          case 1:
            motorTypes.add("Motors.KRAKEN_X60");
            break;
          case 2:
            motorTypes.add("Motors.KRAKEN_X60_FOC");
            break;
          case 3:
            motorTypes.add("Motors.FALCON500");
            break;
          case 4:
            motorTypes.add("Motors.FALCON500_FOC");
            break;
          case 5:
            motorTypes.add("Motors.KRAKEN_X44");
            break;
          case 6:
            motorTypes.add("Motors.KRAKEN_X44_FOC");
            break;
        }

        char next = 'Y';
        while (next == 'Y') {
          // motor specific
          System.out.println("What is the motor id?");
          motorDeviceIDs.add(scan.next());
          char nextMotor = '0';
          while (nextMotor != 'Y' && nextMotor != 'N') {
            System.out.println("Would you like to add another motor for the microsystem? Y/N");
            nextMotor = Character.toUpperCase(scan.next().charAt(0));
            next = nextMotor;
          }
        }

        switch (variant) {
          case Elevator:
            conf = new TagalongElevatorConf(
                projPath,
                microName,
                subsystemName,
                robotVersion,
                variant,
                motorTypes,
                motorDeviceIDs,
                canBus
            );
            break;
          case PivotFused:
          case PivotUnfused:
          case PivotNoCancoder:
            conf = new TagalongPivotConf(
                projPath,
                microName,
                subsystemName,
                robotVersion,
                variant,
                motorTypes,
                motorDeviceIDs,
                canBus,
                encoderID
            );
            break;
          case Roller:
            conf = new TagalongRollerConf(
                projPath,
                microName,
                subsystemName,
                robotVersion,
                variant,
                motorTypes,
                motorDeviceIDs,
                canBus
            );
            break;
          default:
            conf = new TagalongMicrosystemConf(
                projPath,
                microName,
                subsystemName,
                robotVersion,
                variant,
                motorTypes,
                motorDeviceIDs,
                canBus
            );
        }
        // Run generation actions
        conf.writeClass();

        microSpec = new TagalongMicrosystemSpec(microName, variant, motorTypes.size());

        // Additional micro system (Y/N) -- repeat from step 4 if additional
        continues = '0';
        while (continues != 'Y' && continues != 'N') {
          System.out.println(
              "Would you like to add any additional microsystems to this subsystem? Y/N"
          );
          continues = Character.toUpperCase(scan.next().charAt(0));
          scan.nextLine();
          variant = MicrosystemVariants.Default;
          type = MicrosystemTypes.Default;
          motorTypes = new ArrayList<>();
          canBus = "";
          motorDeviceIDs = new ArrayList<String>();
        }
        microSpecs.add(microSpec);
      }
      subsystemSpec = new TagalongSubsystemSpec(projPath, subsystemName, microSpecs);

      subsystemSpec.writeClass();
      new TagalongRobotVersions(projPath, subsystemSpec, robotVersion).writeClass();
      new TagalongRobotConfiguration(projPath).writeClass();
      new TagalongRobotContainerConfiguration(projPath, subsystemSpec._camelName).writeClass();
      new TagalongSubsystemBaseConf(projPath, subsystemSpec).writeClass();
      new TagalongSubsystemConf(projPath, subsystemSpec, robotVersion).writeClass();
      System.out.println("Tagalong subsystem generation complete!");
    }
    scan.close();
    new TagalongGradleBuildConfiguration(projPath).writeConf();
    TagalongVendorDep.addVendorDep(projPath);
    try {
      String osName = System.getProperty("os.name").toLowerCase();
      if (osName.contains("mac")) {
        Process p = Runtime.getRuntime().exec(new String[] {
            "bash",
            "-c",
            "find " + projPath + "/src"
                + " -iname '*.java'| xargs clang-format -i -style=./.clang-format"
        });
        p.waitFor(15, TimeUnit.SECONDS);
      } else if (osName.contains("win")) {
        Process p = Runtime.getRuntime().exec(new String[] {
            "cmd.exe",
            "/c",
            "FOR /R \"" + projPath
                + ("\\src\" %I IN (*.java) DO clang-format \"%I\" -i "
                   + "-style=file:.\\.clang-format")
        });
        p.waitFor(15, TimeUnit.SECONDS);
        System.out.println("it ran");
      }
    } catch (IOException e) {
      e.printStackTrace();
      System.err.println("Code formatting failed, subsystems will work fine");
    } catch (InterruptedException e) {
      e.printStackTrace();
      System.err.println("Code formatting interrupted, subsystems will work fine");
    }
  }

  public static String addIfDNE(String fileContent, String methodHeader) {
    if (fileContent.contains(methodHeader)) {
      return fileContent;
    } else {
      return fileContent.replaceAll("^(" + methodHeader + ")", "$1\n" + methodHeader + " {}");
    }
  }

  public static String addSpecificSubsystemMethodIfDNE(
      String fileContent, String methodHeader, String methodName, String subsystemName
  ) {
    String returnVal = "";

    String find = "((?<=" + methodHeader + ".*\\(.*\\)*\\n).*)";
    String replace = "$1\n    " + subsystemName + "." + methodName + "();";

    if (!fileContent.contains(methodHeader)) {
      returnVal = fileContent.replaceAll("^(" + methodHeader + ")", "$1\n" + methodHeader + " {}");
    }

    returnVal = fileContent.replaceAll(find, replace);
    return returnVal;
  }

  public static File checkExists(String dir, String fileName) {
    File fileDir = new File(dir);
    if (!fileDir.exists() && !fileDir.isDirectory()) {
      fileDir.mkdirs();
    }
    File file = new File(fileDir, fileName);
    if (!file.exists()) {
      try {
        file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return file;
  }

  public static String getFileContent(String fileName) throws IOException {
    List<String> lines = Files.readAllLines(Paths.get(fileName), StandardCharsets.UTF_8);
    String fileContent = String.join("\n", lines);
    return fileContent;
  }

  public static void updateRobot(String fileName, String methodHeader) throws IOException {
    // In Robot.java, check if method exists and if not, add a function that does

    String fileContent = getFileContent(fileName);
    String updatedContent = addIfDNE(fileContent, methodHeader);
    FileUtils.writeToFile(fileName, updatedContent);
  }

  public static void updateRobotContainer(
      String fileName, String methodHeader, String methodName, String subsystemName
  ) throws IOException {
    // In RobotContainer.java, given "coolSubsystem" add lines in simulationInit() for that
    // subsystem

    String fileContent = getFileContent(fileName);
    String updatedContent =
        addSpecificSubsystemMethodIfDNE(fileContent, methodHeader, methodName, subsystemName);

    FileUtils.writeToFile(fileName, updatedContent);
  }
}
