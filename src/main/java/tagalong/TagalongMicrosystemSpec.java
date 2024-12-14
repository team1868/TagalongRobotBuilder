/**
 * Copyright 2024 The Space Cookies : Girl Scout Troop #62868 and FRC Team #1868
 * Open Source Software; you may modify and/or share it under the terms of
 * the 3-Clause BSD License found in the root directory of this project.
 */

package tagalong;

import tagalong.enums.MicrosystemVariants;
import tagalong.utils.FileUtils;

public class TagalongMicrosystemSpec {
  private final String _name;
  public final String _camelName;
  public final String _capitalName;
  protected final MicrosystemVariants _variant;
  private final int _numMotors;

  public TagalongMicrosystemSpec(String name, MicrosystemVariants variant, int numMotors) {
    if (name == null) {
      System.out.println("Invalid Microsystem Name");
      System.exit(0);
    }
    if (variant == null) {
      System.out.println("Invalid Microsystem Type.");
      System.exit(0);
    }
    if (numMotors <= 0) {
      System.out.println("Invalid Motor Type");
      System.exit(0);
    }
    _name = name;
    _camelName = FileUtils.convertToCamel(_name);
    _capitalName = FileUtils.convertToCapital(_name);
    _variant = variant;
    _numMotors = numMotors;
  }

  public String getName() {
    return _name;
  }
  public String getCapitalName() {
    return _capitalName;
  }

  public String toString() {
    return _name;
  }

  public int getNumMotors() {
    return _numMotors;
  }

  public MicrosystemVariants getVariant() {
    return _variant;
  }
}
