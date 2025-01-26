/**
 * Copyright 2024 The Space Cookies : Girl Scout Troop #62868 and FRC Team #1868
 * Open Source Software; you may modify and/or share it under the terms of
 * the 3-Clause BSD License found in the root directory of this project.
 */

package tagalong.enums;

import tagalong.utils.FileUtils;

public enum MicrosystemVariants {
  Default(""),
  // Flywheel(),
  Elevator("Elevator"),
  Roller("Roller"),
  PivotFused("Pivot"),
  PivotUnfused("Pivot"),
  PivotNoCancoder("Pivot");
  // Turret(),
  // ElevatorTelescoping(),
  // TensionTelescoping();

  private String _name;
  private String _camelName;
  private String _capitalName;

  MicrosystemVariants(String name) {
    _name = name;
  }

  public String getFullName() {
    return this.name();
  }

  public String getCamelName() {
    if (_camelName == null) {
      _camelName = FileUtils.convertToCamel(_name);
    }
    return _camelName;
  }

  public String getCapitalName() {
    if (_capitalName == null) {
      _capitalName = FileUtils.convertToCapital(_name);
    }
    return _capitalName;
  }
}
