package distributed.systems;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * LeaderElection class that implements the Leader Election with the help of Apache ZooKeeper.
 *
 * In this algorithm we first connect to ZooKeeper server, then volunteer for leadership, and finally determine if it is the leader or not.
 */
public class LeaderElection implements Watcher {
    private static final String ZOOKEEPER_ADDRESS = "localhost:2181";
    private static final int SESSION_TIMEOUT = 3000;
    private static final String ELECTION_NAMESPACE = "/election";
    private ZooKeeper zooKeeper;
    private String currentZnodeName;

    /**
     * Connecting to the ZooKeeper server.
     *
     * @throws IOException if there is an occurence of an Input Output error while connecting to the ZooKeeper server.
     */
    public void connectToZooKeeper() throws IOException {
        this.zooKeeper = new ZooKeeper(ZOOKEEPER_ADDRESS, SESSION_TIMEOUT, this);
    }

    /**
     * In this method it volunteers for leadership by creation of an ephemeral sequential znode in the election namespace.
     *
     * @throws KeeperException if a failure occurs in a ZooKeeper operation.
     * @throws InterruptedException if there is interruption in the thread while waiting for the ZooKeeper operation to complete.
     */
    public void volunteerForLeadership() throws KeeperException, InterruptedException {
        String znodePrefix = ELECTION_NAMESPACE + "/c_";
        String znodeFullPath = zooKeeper.create(
                znodePrefix,
                new byte[]{},
                ZooDefs.Ids.OPEN_ACL_UNSAFE,
                CreateMode.EPHEMERAL_SEQUENTIAL
        );
        System.out.println("znode name " + znodeFullPath);
        this.currentZnodeName = znodeFullPath.replace("/election/", "");
    }

    /**
     * If the current znode is the smallest among the znodes at the election mamespace, it declares itself as the leader; else, it prints that it is not leader declares smallest as the leader.
     *
     * @throws KeeperException if a failure occurs in a ZooKeeper operation.
     * @throws InterruptedException if there is interruption in the thread while waiting for the ZooKeeper operation to complete.
     */
    public void electLeader() throws KeeperException, InterruptedException {
        List<String> children = zooKeeper.getChildren(ELECTION_NAMESPACE, false);
        Collections.sort(children);

        String smallestChild = children.get(0);

        if (currentZnodeName.equals(smallestChild)) {
            System.out.println("I am the leader.");
            return;
        }
        System.out.println("I am not the leader, " + smallestChild + " is the leader.");
    }

    /**
     * Method responsible for running the leader election process and waits until it is notified.
     *
     * @throws InterruptedException if there is interruption while waiting
     */
    public void run() throws InterruptedException {
        synchronized (zooKeeper) {
            zooKeeper.wait();
        }
    }

    /**
     * This method is responsible for Closing the connection to the ZooKeeper server.
     *
     * @throws InterruptedException if there is an interruption while the connection is being closed.
     */
    public void close() throws InterruptedException {
        zooKeeper.close();
    }

    /**
     * This method is responsible for processing the ZooKeeper events.
     *
     * @param event is the ZooKeeper event that is going to be processed.
     */
    @Override
    public void process(WatchedEvent event) {
        switch (event.getType()) {
            case None:
                if (event.getState() == Event.KeeperState.SyncConnected) {
                    System.out.println("Successfully connected to ZooKeeper");
                } else {
                    synchronized (zooKeeper) {
                        System.out.println("Disconnected from the ZooKeeper server.");
                        zooKeeper.notifyAll();
                    }
                }
        }
    }
}
