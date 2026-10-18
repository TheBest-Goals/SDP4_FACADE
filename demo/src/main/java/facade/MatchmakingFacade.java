package facade;

import persistance.ChainStatus;
import persistance.ChainStateRepository;
import subsystem_a_party.PartyService;
import subsystem_b_server.ServerService;
import subsystem_c_anticheat.AnticheatService;

public class MatchmakingFacade {
    private final PartyService partyService = new PartyService();
    private final ServerService serverService = new ServerService();
    private final AnticheatService anticheatService = new AnticheatService();
    private final ChainStateRepository chainRepo = new ChainStateRepository();

    public boolean startMatchmaking(String partyId, int mmr, int pingMs, boolean simulateCheat) {
        chainRepo.saveStatus(partyId, ChainStatus.STARTED);

        // step A: this is just lobby when u invite ur friends or start the game
        try {
            partyService.createParty(partyId, mmr);
            chainRepo.saveStatus(partyId, ChainStatus.A_COMPLETED);
        } catch (Exception e) {
            System.out.println("[Facade] Step A failed: " + e.getMessage());
            return false;
        }

        // step B
        try {
            serverService.allocateServer(partyId, pingMs);
            chainRepo.saveStatus(partyId, ChainStatus.B_COMPLETED);
        } catch (Exception e) {
            System.out.println("[Facade] Step B failed: " + e.getMessage() + ". Triggering Level 1 Compensation (Undo A)...");
            // ГЛУБИНА КОМПЕНСАЦИИ 1: Отмена A
            partyService.cancelParty(partyId);
            chainRepo.saveStatus(partyId, ChainStatus.COMPENSATED_A);
            return false;
        }

        // step C anticheat
        try {
            anticheatService.startSession(partyId, simulateCheat);
            chainRepo.saveStatus(partyId, ChainStatus.COMPLETED);
            return true;
        } catch (Exception e) {
            System.out.println("[Facade] Step C failed: " + e.getMessage() + ". Triggering Level 2 Compensation (Undo B -> Undo A)...");
            // ГЛУБИНА КОМПЕНСАЦИИ 2: Последовательная отмена B, затем A
            serverService.releaseServer(partyId);
            partyService.cancelParty(partyId);
            chainRepo.saveStatus(partyId, ChainStatus.COMPENSATED_AB);
            return false;
        }
    }
}