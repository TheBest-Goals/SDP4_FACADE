package subsystem_b_server;

public class ServerService {
    private final ServerRepository repository = new ServerRepository();
    private final Checker latencyChecker = new Checker();

    public ServerAllocation allocateServer(String partyId, int pingMs) {
        if (!latencyChecker.isLatencyAcceptable(pingMs)) {
            throw new ServerAllocationException("Latency too high for matchmaking: " + pingMs + "ms");
        }
        ServerAllocation alloc = new ServerAllocation(partyId, pingMs);
        alloc.setStatus(ServerAllocation.Status.SERVER_RESERVED);
        repository.save(alloc);
        return alloc;
    }

    public void releaseServer(String partyId) {
        ServerAllocation alloc = repository.findByPartyId(partyId);
        if (alloc != null) {
            alloc.setStatus(ServerAllocation.Status.RELEASED);
            repository.save(alloc);
        }
    }

    public ServerAllocation getAllocation(String partyId) {
        return repository.findByPartyId(partyId);
    }

    public void reset() { repository.clear(); }
}