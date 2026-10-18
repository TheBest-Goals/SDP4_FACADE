package subsystem_b_server;

public class ServerAllocation {
    public enum Status { REQUESTED, SERVER_RESERVED, RELEASED }

    private final String partyId;
    private final int pingMs;
    private Status status;

    public ServerAllocation(String partyId, int pingMs) {
        this.partyId = partyId;
        this.pingMs = pingMs;
        this.status = Status.REQUESTED;
    }

    public String getPartyId() { return partyId; }
    public int getPingMs() { return pingMs; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}