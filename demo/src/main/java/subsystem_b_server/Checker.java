package subsystem_b_server;

public class Checker {
    public boolean isLatencyAcceptable(int pingMs) {
        //if ping higher  200ms server freezing
        return pingMs <= 200;
    }
}