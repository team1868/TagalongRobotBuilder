/**
 * Copyright 2024 The Space Cookies : Girl Scout Troop #62868 and FRC Team #1868
 * Open Source Software; you may modify and/or share it under the terms of
 * the 3-Clause BSD License found in the root directory of this project.
 */

package tagalong;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import tagalong.enums.MicrosystemVariants;
import tagalong.utils.FileUtils;

public class TagalongSubsystemSpec {
  private String _name;
  public String _camelName;
  public String _capitalName;
  private String _fileName;
  public ArrayList<TagalongMicrosystemSpec> _microspecs;
  private String _dir;
  private File _file;

  /* String "Constants" */
  private String header = "public ";
  private String paren = "() ";
  private String indent = "   ";
  private String ifDisabled = "if (_isSubsystemDisabled) { \n  return;\n    }\n";
  // private String ifEnabled = "if (!_isSubsystemDisabled) { \n  return;\n    }\n";
  private String _str = "";

  public TagalongSubsystemSpec(
      String dir, String name, ArrayList<TagalongMicrosystemSpec> microspecs
  ) {
    if (name == null) {
      System.out.println("Invalid Subsystem Name");
      System.exit(0);
    }
    if (microspecs == null) {
      System.out.println("Invalid Microspecs.");
      System.exit(0);
    }

    Set<String> nameSet = new HashSet<>();
    for (TagalongMicrosystemSpec microspec : microspecs) {
      String microname = microspec.getName();
      if (!nameSet.add(microname)) {
        System.out.println("There are duplicate microsystem names.");
        System.exit(0);
        break;
      }
    }
    _dir = dir;
    _name = name;
    _camelName = FileUtils.convertToCamel(name);
    _capitalName = FileUtils.convertToCapital(name);
    _microspecs = microspecs;
    _fileName = FileUtils.convertToCapital(name + ".java");
  }

  public void writeImports() {
    _str +=
        // clang-format off
"""
package frc.robot.subsystems;
import edu.wpi.first.wpilibj.Filesystem;

"""

        // clang-format on
        ;

    for (TagalongMicrosystemSpec microspec : _microspecs) {
      String microspecImports =
          // clang-format off
"""
import tagalong.subsystems.micro.%s;
import tagalong.subsystems.micro.augments.%sAugment;
import frc.robot.subsystems.confs.%sConf;
"""
        // clang-format on;
;
          _str += String.format(
              microspecImports,
              microspec.getVariant().getCapitalName(),
              microspec.getVariant().getCapitalName(),
              FileUtils.convertToCapital(_name)
          );
    }

    String baseString =
        // clang-format off
"""
import tagalong.subsystems.TagalongSubsystemBase;

"""
        // clang-format on
        ;
    _str += String.format(baseString, _camelName, _camelName);
  }

  public void writeClass() {
    File subsystemsDir = new File(_dir + "/src/main/java/frc/robot/subsystems/");
    if (!subsystemsDir.exists() && !subsystemsDir.isDirectory()) {
      subsystemsDir.mkdirs();
    }
    _file = new File(subsystemsDir, _fileName);
    if (!_file.exists()) {
      try {
        _file.createNewFile();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    /* Imports */
    writeImports();
    /* Header */
    String classHeader = "public class " + _capitalName + " extends TagalongSubsystemBase"
        + " implements ";
    for (int i = 0; i < _microspecs.size(); i++) {
      classHeader += _microspecs.get(i).getVariant().getCapitalName() + "Augment";
      if (i < _microspecs.size() - 1) {
        classHeader += ", ";
      }
    }
    classHeader += " {";
    _str += classHeader;
    /* Variables */
    writeIdentifiers();
    writeInstanceVariables();
    /* Methods */
    getMicrosystemFetchers();

    writeConstructor();
    enabledMethods("onEnable", "void");
    enabledMethods("onDisable", "void");
    enabledMethods("periodic", "void");
    disabledMethods("simulationInit", "void");
    disabledMethods("simulationPeriodic", "void");
    disabledMethods("updateShuffleboard", "void");
    disabledMethods("configShuffleboard", "void");
    checkInitStatus();
    FileUtils.writeToFile(_file, _str + "}\n");
  }

  private void writeIdentifiers() {
    for (var variant : MicrosystemVariants.values()) {
      int counter = 0;
      for (var micro : _microspecs) {
        if (micro.getVariant() == variant) {
          _str +=
              "\n  public static final int %s_ID = %d;\n".formatted(micro._capitalName, counter++);
        }
      }
    }
  }

  private void writeInstanceVariables() {
    for (TagalongMicrosystemSpec microSpec : _microspecs) {
      _str += "\n  private final %s".formatted(microSpec.getVariant().getCapitalName());
      _str += " _%s;\n".formatted(microSpec._camelName);
    }
    _str += "\n";
    _str += "  public final %sConf _%sConf;\n\n".formatted(_capitalName, _name.toLowerCase());
    logging();
  }

  public void enabledMethods(String methodName, String returnType) {
    _str += "  @Override \n";
    _str += "  " + header + returnType + " " + methodName + " " + paren + "{\n";
    _str += indent + ifDisabled;

    for (TagalongMicrosystemSpec microSpec : _microspecs) {
      _str += "%s%s_%s.%s();\n".formatted(indent, indent, microSpec._camelName, methodName);
    }

    if (methodName == "periodic") {
      _str +=
          //  "    // Logging\n"
          // + "    _io.updateInputs(_inputs);\n"
          // + "    Logger.processInputs(\"" + _name + "\", _inputs);\n"
          "    updateShuffleboard();\n";
    }

    _str += "  } \n\n";
  }

  public void disabledMethods(String methodName, String returnType) {
    _str += " %s%s %s (){\n".formatted(header, returnType, methodName);
    for (TagalongMicrosystemSpec microSpec : _microspecs) {
      _str += "%s%s_%s.%s();\n".formatted(indent, indent, microSpec._camelName, methodName);
      // assuming microspec is one word and camelCase is unnecessary ^
    }

    _str += "  } \n\n";
  }

  public void checkInitStatus() {
    _str += header + "boolean"
        + " "
        + "checkInitStatus" + paren + "{\n  return";

    for (int i = 0; i < _microspecs.size(); i++) {
      _str += "  _%s.checkInitStatus()".formatted(_microspecs.get(i).getName().toLowerCase());
      if (i < _microspecs.size() - 1) {
        _str += " && ";
      } else {
        _str += ";";
      }
    }

    _str += "} \n\n";
  }

  public void logging() {
    String baseString =
        // clang-format off
"""
//   /* -------- Logging: utilities and configs -------- */
//   private final %sIOTalonFX _io;
//   private final %sIOInputsAutoLogged _inputs = new %sIOInputsAutoLogged();

"""
        // clang-format on
        ;
    _str += String.format(baseString, _name, _name, _name);
  }

  public void getMicrosystemFetchers() {
    for (var variant : MicrosystemVariants.values()) {
      boolean unused = true;
      for (var spec : _microspecs) {
        if (unused && spec.getVariant() == variant) {
          unused = false;
          // write default fetcher
          _str += "  @Override\n  public %s get%s() {\n".formatted(
              spec.getVariant().getCapitalName(), spec.getVariant().getCapitalName()
          );
          _str += "    return _%s;\n  }\n\n".formatted(spec._camelName);

          getMultiFetcher(spec);
        }
      }
    }
  }

  public void getMultiFetcher(TagalongMicrosystemSpec spec) {
    _str += "  @Override\n  public %s get%s(int id) {\n    switch (id) {\n".formatted(
        spec.getVariant().getCapitalName(), spec.getVariant().getCapitalName()
    );

    int variantCounter = 1;
    for (var microspec : _microspecs) {
      if (microspec.getVariant() == spec.getVariant() && microspec._camelName != spec._camelName) {
        _str +=
            "      case %d:\n        return _%s;".formatted(variantCounter++, microspec._camelName);
      }
    }

    _str +=
        // clang-format off
"""
      case 0:
      default:
        return _%s;
    }
  }
""".formatted(spec._camelName)
    // clang-format on
    ;
}

public void writeConstructor() {
  _str += "  public %s(%sConf %sConf) {\n".formatted(_capitalName, _capitalName, _camelName);
  _str += "    super(%sConf);\n".formatted(FileUtils.convertToCamel(_name));
  for (TagalongMicrosystemSpec microSpec : _microspecs) {
    _str += "    _" + FileUtils.convertToCamel(microSpec.getName());
    _str += " = new %s( %sConf!= null ? %sConf.%sConf : null);\n".formatted(
        FileUtils.convertToCapital(microSpec.getVariant().name()),
        FileUtils.convertToCamel(_name),
        FileUtils.convertToCamel(_name),
        microSpec._camelName
    );
  }

  _str += "  if (";
  for (int i = 0; i < _microspecs.size(); i++) {
    _str += "_" + FileUtils.convertToCamel(_microspecs.get(i).getName())
        + "._configuredMicrosystemDisable";
    if (i < _microspecs.size() - 1) {
      _str += " || ";
    }
  }
  _str += ") {\n";
  // _str += "    _io = null; \n";
  _str +=
      "      _%sConf = null;\n      return;\n    }\n".formatted(FileUtils.convertToCamel(_name));

  _str += "    _%sConf = %sConf;\n ".formatted(
      FileUtils.convertToCamel(_name), FileUtils.convertToCamel(_name)
  );
  // for (TagalongMicrosystemSpec microSpec : _microspecs) {
  //   _str += "  _" + microSpec.getName().toLowerCase() + " = new "
  //       + microSpec.getVariant().getCapitalName();
  //   _str += "(_conf." + microSpec.getVariant().getCamelName() + "Conf);";
  //   _str += "\n";
  // }
  _str +=
      // clang-format off
"""

  int counter = 0;
  while (!checkInitStatus() && counter < 100) {
    System.out.println("Waiting for %s");
  }

  if (counter >= 100) {
    System.out.println("failed to init %s");
  }

  configShuffleboard();
}

"""
    // clang-format on
    .formatted(_name, _name);

// _str += "  _io = new " + FileUtils.convertToCamel(_name) + "IOTalonFX(this);\n";
}

public void writeConfClass() {
  _str += "public class %sConf {\n}".formatted(FileUtils.convertToCamel(_name));
  for (TagalongMicrosystemSpec microSpec : _microspecs) {
    _str += " public %sConf %sConf".formatted(
        FileUtils.convertToCamel(microSpec.getName()), microSpec.getName().toLowerCase()
    );
  }
}
}
