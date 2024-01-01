# Distributed Leader Election Using Apache ZooKeeper

## Overview

This Maven project implements a basic leader election algorithm using Apache ZooKeeper. The application demonstrates the process of electing a leader among multiple instances using ZooKeeper's distributed coordination capabilities.

## Project Structure

### 1. Main Class

The `Main` class initializes and executes the leader election process by creating a `LeaderElection` object, connecting it to ZooKeeper, volunteering for leadership, electing a leader, and finally disconnecting from ZooKeeper.

### 2. LeaderElection Class

The `LeaderElection` class implements the Watcher interface from Apache ZooKeeper. It provides methods to connect to ZooKeeper, volunteer for leadership, elect a leader, and disconnect from ZooKeeper. This class plays a crucial role in the leader election process.

## Requirements

- Maven: Ensure that Maven is installed on your system.
- Apache ZooKeeper: Follow the installation instructions and troubleshoot any issues to ensure the smooth running of your program.

## Getting Started

1. **Clone the Repository:**
   ```bash
   git clone https://github.com/your-username/your-project.git
   cd your-project
   ```
2. **Build the Project:**
   ```bash
   mvn clean package
   ```
3. **Run Multiple Instances:**
   ```bash
   java -jar target/your-project.jar
   ```
4. **Verify Leader Election**
  Check the terminal outputs to verify that one instance becomes the leader while others correctly identify themselves as non-leaders and recognize the leader.
## Troubleshooting
If you encounter any issues during the execution of the program, refer to the Apache ZooKeeper installation and troubleshooting instructions.

## Acknowledgements
Apache ZooKeeper: Official Documentation
Maven: Official Documentation
