package subsystem_c_anticheat;

public class MatchSession {
    public enum Status { SCANNING, SESSION_ACTIVE, SECURITY_ABORTED }

    private final String partyId;
    private final boolean hasCheats;
    private Status status;

    public MatchSession(String partyId, boolean hasCheats) {
        this.partyId = partyId;
        this.hasCheats = hasCheats;
        this.status = Status.SCANNING;
    }

    public String getPartyId() { return partyId; }
    public boolean hasCheats() { return hasCheats; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
}