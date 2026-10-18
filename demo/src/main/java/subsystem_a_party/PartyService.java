package subsystem_a_party;
// public API subsystem A
public class PartyService {
    private final PartyRepository repository = new PartyRepository();
    private final SkillRatingChecker checker = new SkillRatingChecker();

    public Party createParty(String partyId, int mmr) { // create group for step A
        if (!checker.isMmrValid(mmr)) {
            throw new IllegalArgumentException("Invalid MMR rating: " + mmr);
        }
        Party party = new Party(partyId, mmr);
        party.setStatus(Party.Status.LOBBY_READY);
        repository.save(party);
        return party;
    }

    public void cancelParty(String partyId) { //  cancels steps and calls to facade
        Party party = repository.findByPartyId(partyId);
        if (party != null) {
            party.setStatus(Party.Status.CANCELLED);
            repository.save(party);
        }
    }

    public Party getParty(String partyId) { // getter?
        return repository.findByPartyId(partyId);
    }

    public void reset() { repository.clear(); }
}