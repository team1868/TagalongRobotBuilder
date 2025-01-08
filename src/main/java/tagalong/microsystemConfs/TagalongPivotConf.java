/**
 * Copyright 2024 The Space Cookies : Girl Scout Troop #62868 and FRC Team #1868
 * Open Source Software; you may modify and/or share it under the terms of
 * the 3-Clause BSD License found in the root directory of this project.
 */

package tagalong.microsystemConfs;

import java.util.ArrayList;

import tagalong.TagalongMicrosystemConf;
import tagalong.enums.MicrosystemVariants;

public class TagalongPivotConf extends TagalongMicrosystemConf {
  private int _encoderDeviceID;
  public TagalongPivotConf(
      String dir,
      String name,
      String subsystemName,
      String robotVersion,
      MicrosystemVariants variant,
      ArrayList<String> motorTypes,
      ArrayList<String> motorDeviceIDs,
      String canBus,
      int encoderDeviceID
  ) {
    super(dir, name, subsystemName, robotVersion, variant, motorTypes, motorDeviceIDs, canBus);
    _encoderDeviceID = encoderDeviceID;
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
  import tagalong.devices.Encoders;
  import tagalong.devices.Motors;
  import tagalong.controls.FeedforwardConstants;
  import tagalong.controls.PIDSGVAConstants;
  import tagalong.units.AccelerationUnits;
  import tagalong.units.DistanceUnits;
  import tagalong.units.VelocityUnits;
  import tagalong.subsystems.micro.confs.PivotConf;
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
  public static final int[][] motorToPivotRatio = {{1, 1}};
  public static final int[][] encoderToPivotRatio = {{1, 1}};

  public static final boolean motorCurrentLimitStatorEnableLimit = true;
  public static final int motorCurrentLimitStatorPeakLimit = 80;
  public static final boolean motorCurrentLimitSupplyEnableLimit = true;
  public static final int motorCurrentLimitSupplyPeakLimit = 80;
  public static final int motorCurrentLimitSupplyContinuousLimit = 80;
  public static final double motorCurrentLimitPeakDuration = 0.8;

  public static final Encoders encoderTypes = Encoders.CANCODER;
  public static final int encoderDeviceID = %s;
  public static final String encoderCanBus = "%s";

  public static final boolean encoderConfigZeroToOne = true;
  public static final boolean encoderConfigClockwisePositive = true;
  public static final DistanceUnits encoderConfigMagnetOffsetUnit = DistanceUnits.ROTATION;
  public static final double encoderConfigMagnetOffsetValue = 0.0;

"""
        // clang-format on
        .formatted(_encoderDeviceID, _canBus);
  }

  @Override
  public String getRequiredTuning() {
    return
        // clang-format off
"""
  /* ----- Required Tuning ------ */
  private static final DistanceUnits trapezoidalLimitsUnits = DistanceUnits.ROTATION;
  private static final VelocityUnits trapezoidalVelocityUnits = VelocityUnits.ROTATIONS_PER_SECOND;
  private static final AccelerationUnits trapezoidalAccelerationUnits = AccelerationUnits.ROTATIONS_PER_SECOND2;
  private static final double trapezoidalLimitsVelocity = 0.0;
  private static final double trapezoidalLimitsAcceleration = 0.0;

  public static final DistanceUnits positionalLimitsUnits = DistanceUnits.ROTATION;
  public static final double positionalLimitsMin = 0.0;
  public static final double positionalLimitsMax = 0.0;
  public static final DistanceUnits defaultTolerancesUnit = DistanceUnits.ROTATION;
  public static final double defaultLowerTolerance = 0.0;
  public static final double defaultUpperTolerance = 0.0;

""";
    // clang-format on
  }

  @Override
  public String getControlConstants() {
    return
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

  public static final DistanceUnits ffOffsetUnit = DistanceUnits.DEGREE;
  public static final double ffOffsetValue = 0.0;

  public static final DistanceUnits profileOffsetUnit = DistanceUnits.DEGREE;
  public static final double profileOffsetValue = 0.0;

  public static final double pivotMOI = 0.0;
  public static final double pivotLengthM = 0.0;

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

""";
    // clang-format on
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
        encoderTypes,
        encoderDeviceID,
        encoderCanBus,
        encoderConfigZeroToOne,
        encoderConfigClockwisePositive,
        encoderConfigMagnetOffsetUnit,
        encoderConfigMagnetOffsetValue,
        motorEnabledBrakeMode,
        motorDisabledBrakeMode,
        motorToPivotRatio,
        encoderToPivotRatio,
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
            .withSupplyCurrentLowerLimit(motorCurrentLimitSupplyContinuousLimit)
            .withSupplyCurrentLowerTime(motorCurrentLimitPeakDuration)
            .withStatorCurrentLimitEnable(motorCurrentLimitStatorEnableLimit)
            .withSupplyCurrentLimitEnable(motorCurrentLimitSupplyEnableLimit),
        slot0,
        slot1,
        slot2,
        simSlot0,
        simSlot1,
        simSlot2,
        closedLoopConfigsContinuousWrap,
        ffOffsetUnit,
        ffOffsetValue,
        profileOffsetUnit,
        profileOffsetValue,
        pivotMOI,
        pivotLengthM
    );
    }
    """ .formatted(_className, _className)
    // clang-format off
    ;

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
  Encoders encoderType,
  int encoderDeviceID,
  String encoderCanBus,
  boolean encoderConfigZeroToOne,
  boolean encoderConfigClockwisePositive,
  DistanceUnits encoderConfigMagnetOffsetUnit,
  double encoderConfigMagnetOffsetValue,
  NeutralModeValue[] motorEnabledBrakeMode,
  NeutralModeValue[] motorDisabledBrakeMode,
  int[][] motorToPivotRatio,
  int[][] encoderToPivotRatio,
  DistanceUnits rotationalLimitsUnits,
  double rotationalMin,
  double rotationalMax,
  DistanceUnits trapezoidalLimitsUnits,
  VelocityUnits trapezoidalVelocityUnits,
  double trapezoidalLimitsVelocity,
  AccelerationUnits trapezoidalAccelerationUnits,
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
  PIDSGVAConstants simSlot0,
  PIDSGVAConstants simSlot1,
  PIDSGVAConstants simSlot2,
  boolean closedLoopConfigsContinuousWrap,
  DistanceUnits ffOffsetUnit,
  double ffOffsetValue,
  DistanceUnits profileOffsetUnit,
  double profileOffsetValue,
  double pivotMOI,
  double pivotLengthM) {
  super(
    name,
    motorTypes,
    motorDeviceIDs,
    motorCanBus,
    motorDirection,
    encoderTypes,
    encoderDeviceID,
    encoderCanBus,
    encoderConfigZeroToOne,
    encoderConfigClockwisePositive,
    encoderConfigMagnetOffsetUnit,
    encoderConfigMagnetOffsetValue,
    motorEnabledBrakeMode,
    motorDisabledBrakeMode,
    motorToPivotRatio,
    encoderToPivotRatio,
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
        .withSupplyCurrentLowerLimit(motorCurrentLimitSupplyContinuousLimit)
        .withSupplyCurrentLowerTime(motorCurrentLimitPeakDuration)
        .withStatorCurrentLimitEnable(motorCurrentLimitStatorEnableLimit)
        .withSupplyCurrentLimitEnable(motorCurrentLimitSupplyEnableLimit),
    slot0,
    slot1,
    slot2,
    simSlot0,
    simSlot1,
    simSlot2,
    closedLoopConfigsContinuousWrap,
    ffOffsetUnit,
    ffOffsetValue,
    profileOffsetUnit,
    profileOffsetValue,
    pivotMOI,
    pivotLengthM
);
}
"""
        // clang-format on
        ;
    return baseString;
  }
}
