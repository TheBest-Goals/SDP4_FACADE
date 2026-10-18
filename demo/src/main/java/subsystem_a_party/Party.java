package subsystem_a_party;

// this is product getters and setter
// constructors and clean code (private final)
// stores ID, status, mmr

public class Party {
    public enum Status{NEW, LOBBY_READY, CANCELLED}

    private final String partyId;
    private final int averageMmr;
    private Status status;

    public Party(String partyId, int averageMmr){
        this.partyId = partyId;
        this.averageMmr = averageMmr;
        this.status = Status.NEW;
    }

    public String getPartyId() {return partyId;}

    public int getAverageMmr() {return averageMmr;}

    public Status getStatus() {return status;}

    public void setStatus(Status status) {this.status = status;}


}
