## Getting Started with Tagalong RobotBuilder

This tool augments existing FRC Robot projects with generated subsystems utilizing [TagalongLib](https://github.com/team1868/TagalongLib). We encourage all Tagalong RobotBuilder users to read about TagalongLib and learn about the underlying architecture, functionality, and tools that enable the generated subsystems.

### Requirements

- [Apache Maven](https://maven.apache.org/)  
  - If you are using macOS, we encourage using [brew](https://brew.sh/) for package installation and management  
- An existing WPILib robot project

	If you are starting from scratch, we recommend using VS-Code to generate a skeleton project, use the [CTRE Swerve Project Generator](https://v6.docs.ctr-electronics.com/en/stable/docs/tuner/tuner-swerve/index.html), or use one of the [AdvantageKit example projects](https://github.com/Mechanical-Advantage/AdvantageKit/releases) as a starting point.

### Downloading

Clone this repository using git and then follow the Compilation and Execution instructions

### Compilation and Execution
```
mvn package exec:java  
```

### Arguments and Customization

As part of execution we ask for the following fields:

* Absolute path to the robot code project directory   
* Robot version (compbot, practicebot, field specific, etc)  
* Subsystem names  
* Microsystem types (Linear: Elevator, Rotational: Roller, Pivot)  
* Microsystem names  
* CAN bus name  
* Motor type  
* Device CAN IDs

### Verification and File Structure

Once generation completes, look for the following files in your robot repository that correspond to your new subsystem.  
constants folder: 

* RobotVersions.java with all unique robot versions from past generations.

subsystems folder: 

* confs folder:  
  * Subsystem file (naming convention: `(Robot version name)(Subsystem name)Conf.java`) with conf objects for each microsystem in the subsystem.  
* confs/micro folder:  
  * Microsystem file (naming convention: `(Robot version name)(Microsystem name)Conf.java`) with all inputted configurations & default values   
* Subsystem.java with base subsystem functions 

### Next Steps and Advanced Usage

Congratulations\! You now have a Tagalong subsystem ready for tuning and command composition\!

#### Microsystem Tuning

Guides for tuning your microsystems are located [here in the TagalongLib repository](https://github.com/team1868/TagalongLib/tree/main/resources/guides/README.md). There are detailed guides on how to tune each microsystem and the different modes of each system that will assist in tuning and using the system effectively.

#### Base Commands

Base commands are basic functionalities for specific microsystems. These are powerful building blocks that have lots of configurability and safety features included in the commands. See here for a full list of these base commands and detailed information on them.

#### Command Composition

There is no need to wait for your robot to be up and running with base commands to start composing commands and writing robot actions\!

As soon as you know which microsystems will be in your subsystems you can use RobotBuilder to generate the subsystem code and start composing robot actions. We encourage you to start on the subsystem level by composing multiple base commands into cohesive and coordinated subsystem actions.

> WARNING: Tagalong base commands require the subsystem containing the microsystem being controlled. You **MUST** use the tagalong `anonymize()` decorator when combining multiple base commands controlling any number of microsystems inside the same Tagalong subsystems.

### Recommended Tools

There are a variety of tools and configurations we encourage everyone to add to FRC projects and git repositories. You can find a [full list with implementation details here](https://github.com/team1868/TagalongLib/tree/main/resources/TOOLING.md).

<!-- 
### Variables

### Examples
Coming soon!

### FAQ
Coming soon!
-->
