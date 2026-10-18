package subsystem_c_anticheat;

public class AnticheatService {
    private final AnticheatRepository repository = new AnticheatRepository();
    private final AnticheatScanner scanner = new AnticheatScanner();

    public MatchSession startSession(String partyId, boolean simulatedCheatFlag) {
        MatchSession session = new MatchSession(partyId, simulatedCheatFlag);

        if (!scanner.isClean(simulatedCheatFlag)) {
            session.setStatus(MatchSession.Status.SECURITY_ABORTED);
            repository.save(session);
            throw new SecurityException("Anti-cheat violation detected for party: " + partyId);
        }

        session.setStatus(MatchSession.Status.SESSION_ACTIVE);
        repository.save(session);
        return session;
    }

    public MatchSession getSession(String partyId) {
        return repository.findByPartyId(partyId);
    }

    public void reset() { repository.clear(); }
}