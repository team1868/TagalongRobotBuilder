/**
 * Copyright 2024 The Space Cookies : Girl Scout Troop #62868 and FRC Team #1868
 * Open Source Software; you may modify and/or share it under the terms of
 * the 3-Clause BSD License found in the root directory of this project.
 */

package tagalong.enums;

public enum MicrosystemTypes {
  Default(),
  Rotational(new MicrosystemVariants[] {
      MicrosystemVariants.Roller,
      MicrosystemVariants.PivotFused,
      MicrosystemVariants.PivotUnfused,
      MicrosystemVariants.PivotNoCancoder
      // MicrosystemVariants.Flywheel,
      // MicrosystemVariants.PivotFused,
      // MicrosystemVariants.PivotNoEncoder,
      // MicrosystemVariants.PivotNoFused
  }),
  Linear(new MicrosystemVariants[] {
      MicrosystemVariants.Elevator
      // MicrosystemVariants.ElevatorTelescoping, MicrosystemVariants.TensionTelescoping
  });

  public MicrosystemVariants[] variants;

  MicrosystemTypes() {}

  MicrosystemTypes(MicrosystemVariants[] variants) {
    this.variants = variants;
  }
}
