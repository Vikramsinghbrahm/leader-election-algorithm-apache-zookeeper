package distributed.systems;

import org.apache.zookeeper.KeeperException;
import java.io.IOException;

/**
 * Main is the entry point in this application that demonstrates the leader election algorithm by using the Apache ZooKeeper.
 */
public class Main {
    /**
     * The main method that runs the leader election process.
     * @param args the command line arguments (unused in this application).
     * @throws InterruptedException if the thread is interrupted while waiting.
     * @throws KeeperException if there is a failure in a zookeeper operation.
     * @throws IOException if there is an Input Output error while connecting to ZooKeeper server.
     */
    public static void main(String[] args) throws InterruptedException, KeeperException, IOException {
        LeaderElection leaderElection = new LeaderElection();
        leaderElection.connectToZooKeeper();
        leaderElection.volunteerForLeadership();
        leaderElection.electLeader();
        leaderElection.run();
        leaderElection.close();
        System.out.println("Disconnected from ZooKeeper, exiting application");
    }
}
