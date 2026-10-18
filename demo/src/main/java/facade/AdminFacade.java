package facade;

import persistance.ChainStateRepository;
import persistance.ChainStatus;
import subsystem_a_party.PartyService;
import subsystem_b_server.ServerService;
import subsystem_c_anticheat.AnticheatService;

public class AdminFacade {
    private final PartyService partyService = new PartyService();
    private final ServerService serverService = new ServerService();
    private final AnticheatService anticheatService = new AnticheatService();
    private final ChainStateRepository chainRepo = new ChainStateRepository();

    public void inspectState(String partyId) {
        System.out.println("=== SYSTEM STATE INSPECTION FOR: " + partyId + " ===");
        System.out.println("Chain Persisted Status: " + chainRepo.getStatus(partyId));
        System.out.println("Party (Subsystem A): " + (partyService.getParty(partyId) != null ? partyService.getParty(partyId).getStatus() : "N/A"));
        System.out.println("Server (Subsystem B): " + (serverService.getAllocation(partyId) != null ? serverService.getAllocation(partyId).getStatus() : "N/A"));
        System.out.println("Session (Subsystem C): " + (anticheatService.getSession(partyId) != null ? anticheatService.getSession(partyId).getStatus() : "N/A"));
        System.out.println("=========================================");
    }

    public void resetAllData() {
        partyService.reset();
        serverService.reset();
        anticheatService.reset();
        chainRepo.clear();
        System.out.println("[AdminFacade] All durable state cleared from disk.");
    }
}