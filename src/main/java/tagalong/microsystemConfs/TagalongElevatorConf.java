/**
 * Copyright 2024 The Space Cookies : Girl Scout Troop #62868 and FRC Team #1868
 * Open Source Software; you may modify and/or share it under the terms of
 * the 3-Clause BSD License found in the root directory of this project.
 */

package tagalong.microsystemConfs;

import java.util.ArrayList;

import tagalong.TagalongMicrosystemConf;
import tagalong.enums.MicrosystemVariants;

public class TagalongElevatorConf extends TagalongMicrosystemConf {
  public TagalongElevatorConf(
      String dir,
      String name,
      String subsystemName,
      String robotVersion,
      MicrosystemVariants variant,
      ArrayList<String> motorTypes,
      ArrayList<String> motorDeviceIDs,
      String canBus
  ) {
    super(dir, name, subsystemName, robotVersion, variant, motorTypes, motorDeviceIDs, canBus);
  }

  @Override
  public void writeClass() {
    super.writeClass();
  }

  @Override
  public String getFileHeader() {
    return
        // clang-format off
"""
  package frc.robot.subsystems.confs.micro;

  import com.ctre.phoenix6.configs.CurrentLimitsConfigs;
  import com.ctre.phoenix6.signals.InvertedValue;
  import com.ctre.phoenix6.signals.NeutralModeValue;
  import tagalong.devices.Motors;
  import tagalong.controls.FeedforwardConstants;
  import tagalong.controls.PIDSGVAConstants;
  import tagalong.units.AccelerationUnits;
  import tagalong.units.DistanceUnits;
  import tagalong.units.MassUnits;
  import tagalong.units.VelocityUnits;
  import tagalong.subsystems.confs.micro.ElevatorConf;
""";
    // clang-format on
  }

  @Override
  public String getRequiredMotorConfigs() {
    String baseString = getRequiredMotorConfigsHeader().formatted(
        _microName, motorArrayToString(_motorTypes), arrayToString(_motorDeviceIDs), _canBus
    );
    return baseString +
        // clang-format off
"""
  public static final int[][] gearRatio = {{1, 1}};

  public static final boolean motorCurrentLimitStatorEnableLimit = true;
  public static final int motorCurrentLimitStatorPeakLimit = 80;
  public static final boolean motorCurrentLimitSupplyEnableLimit = true;
  public static final int motorCurrentLimitSupplyPeakLimit = 80;
  public static final int motorCurrentLimitSupplyContinuousLimit = 80;
  public static final double motorCurrentLimitPeakDuration = 0.8;

"""
        // clang-format on
        ;
  }

  @Override
  public String getControlConstants() {
    String baseString = "";
    baseString +=
        // clang-format off
"""
  public static final FeedforwardConstants feedForward =
      new FeedforwardConstants(0.0, 0.0, 0.0, 0.0);

  /* -------- Positional -------- */
  public static final PIDSGVAConstants slot0 =
      new PIDSGVAConstants(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
  /* -------- Velocity -------- */
  public static final PIDSGVAConstants slot1 =
      new PIDSGVAConstants(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
  /* -------- Current -------- */
  public static final PIDSGVAConstants slot2 =
      new PIDSGVAConstants(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

  public static final boolean closedLoopConfigsContinuousWrap = true;

  public static final DistanceUnits ffOffsetUnit = DistanceUnits.METER;
  public static final double ffOffsetValue = 0.0;

  public static final DistanceUnits profileOffsetUnit = DistanceUnits.METER;
  public static final double profileOffsetValue = 0.0;

  /* -------- Simulation Specific Control -------- */
  public static final FeedforwardConstants simFeedForward =
      new FeedforwardConstants(0.0, 0.0, 0.0, 0.0);
  /* -------- Positional -------- */
  public static final PIDSGVAConstants simSlot0 =
      new PIDSGVAConstants(1.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
  /* -------- Velocity -------- */
  public static final PIDSGVAConstants simSlot1 =
      new PIDSGVAConstants(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
  /* -------- Current -------- */
  public static final PIDSGVAConstants simSlot2 =
      new PIDSGVAConstants(0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

  public static final MassUnits carriageMassUnit = MassUnits.KILOGRAMS;
  public static final double carriageMassValue = 0.0;
  public static final int mech2dDim = 0;
  public static final int lineLength = 0;
  public static final int angle = 0;
  public static final DistanceUnits drumDiameterUnits = DistanceUnits.METER;
  public static final double drumDiameter = 0.0;

    """;
        // clang-format on
        return baseString;
  }
  @Override
  public String getRequiredTuning() {
    return
        // clang-format off
"""
  /* ----- Required Tuning ------ */
  private static final DistanceUnits trapezoidalLimitsUnits = DistanceUnits.METER;
  private static final VelocityUnits trapezoidalVelocityUnits = VelocityUnits.METERS_PER_SECOND;
  private static final AccelerationUnits trapezoidalAccelerationUnits = AccelerationUnits.METERS_PER_SECOND2;
  private static final double trapezoidalLimitsVelocity = 0.0;
  private static final double trapezoidalLimitsAcceleration = 0.0;

  public static final DistanceUnits positionalLimitsUnits = DistanceUnits.METER;
  public static final double positionalLimitsMin = 0.0;
  public static final double positionalLimitsMax = 0.0;
  public static final double maxScopeRot = 1.0;
  public static final DistanceUnits defaultTolerancesUnit = DistanceUnits.METER;
  public static final double defaultLowerTolerance = 0.0;
  public static final double defaultUpperTolerance = 0.0;

""";
    // clang-format on
  }

  @Override
  public String getConstructor() {
    String baseString = "";
    baseString += "public " + _className +
        // clang-format off
"""
(
  String name,
  Motors[] motorTypes,
  int[] motorDeviceIDs,
  String[] motorCanBus,
  InvertedValue[] motorDirection,
  NeutralModeValue[] motorEnabledBrakeMode,
  NeutralModeValue[] motorDisabledBrakeMode,
  int[][] gearRatio,
  DistanceUnits positionalLimitsUnits,
  double positionalMin,
  double positionalMax,
  DistanceUnits trapezoidalLengthUnit,
  VelocityUnits trapezoidalVelocityUnit,
  double trapezoidalLimitsVelocity,
  AccelerationUnits trapezoidalAccelerationUnit,
  double trapezoidalLimitsAcceleration,
  DistanceUnits defaultTolerancesUnit,
  double defaultLowerTolerance,
  double defaultUpperTolerance,
  FeedforwardConstants feedForward,
  FeedforwardConstants simFeedForward,
  CurrentLimitsConfigs currentLimitsConfigs,
  PIDSGVAConstants slot0,
  PIDSGVAConstants slot1,
  PIDSGVAConstants slot2,
  MassUnits carriageMassUnit,
  double carriageMassValue,
  int mech2dDim,
  int lineLength,
  int angle,
  PIDSGVAConstants simSlot0,
  PIDSGVAConstants simSlot1,
  PIDSGVAConstants simSlot2,
  DistanceUnits drumDiameterUnits,
  double drumDiameter) {
  super(
    name,
    motorTypes,
    motorDeviceIDs,
    motorCanBus,
    motorDirection,
    motorEnabledBrakeMode,
    motorDisabledBrakeMode,
    gearRatio,
    positionalLimitsUnits,
    positionalMin,
    positionalMax,
    trapezoidalLimitsUnits,
    trapezoidalVelocityUnits,
    trapezoidalLimitsVelocity,
    trapezoidalAccelerationUnits,
    trapezoidalLimitsAcceleration,
    defaultTolerancesUnit,
    defaultLowerTolerance,
    defaultUpperTolerance,
    feedForward,
    simFeedForward,
    new CurrentLimitsConfigs()
        .withStatorCurrentLimit(motorCurrentLimitStatorPeakLimit)
        .withSupplyCurrentLimit(motorCurrentLimitSupplyPeakLimit)
        .withSupplyCurrentThreshold(motorCurrentLimitSupplyContinuousLimit)
        .withSupplyTimeThreshold(motorCurrentLimitPeakDuration)
        .withStatorCurrentLimitEnable(motorCurrentLimitStatorEnableLimit)
        .withSupplyCurrentLimitEnable(motorCurrentLimitSupplyEnableLimit),
    slot0,
    slot1,
    slot2,
    carriageMassUnit,
    carriageMassValue,
    mech2dDim,
    lineLength,
    angle,
    simSlot0,
    simSlot1,
    simSlot2,
    drumDiameterUnits,
    drumDiameter);
  }
"""
        // clang-format on
        ;
    return baseString;
  }

  @Override
  public String getMethods() {
    // clang-format off
    return
  """
        public static %s
        construct() {
      // placeholder
      return new %s(
          name,
          motorTypes,
          motorDeviceIDs,
          motorCanBus,
          motorDirection,
          motorEnabledBrakeMode,
          motorDisabledBrakeMode,
          gearRatio,
          positionalLimitsUnits,
          positionalLimitsMin,
          positionalLimitsMax,
          trapezoidalLimitsUnits,
          trapezoidalVelocityUnits,
          trapezoidalLimitsVelocity,
          trapezoidalAccelerationUnits,
          trapezoidalLimitsAcceleration,
          defaultTolerancesUnit,
          defaultLowerTolerance,
          defaultUpperTolerance,
          feedForward,
          simFeedForward,
          new CurrentLimitsConfigs()
              .withStatorCurrentLimit(motorCurrentLimitStatorPeakLimit)
              .withSupplyCurrentLimit(motorCurrentLimitSupplyPeakLimit)
              .withSupplyCurrentThreshold(motorCurrentLimitSupplyContinuousLimit)
              .withSupplyTimeThreshold(motorCurrentLimitPeakDuration)
              .withStatorCurrentLimitEnable(motorCurrentLimitStatorEnableLimit)
              .withSupplyCurrentLimitEnable(motorCurrentLimitSupplyEnableLimit),
          slot0,
          slot1,
          slot2,
          carriageMassUnit,
          carriageMassValue,
          mech2dDim,
          lineLength,
          angle,
          simSlot0,
          simSlot1,
          simSlot2,
          drumDiameterUnits,
          drumDiameter
      );
    }
    """ .formatted(_className, _className)
    // clang-format off
    ;

  }
}
