package com.example.demo;
import facade.AdminFacade;
import facade.MatchmakingFacade;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import persistance.ChainStateRepository;
import persistance.ChainStatus;
import subsystem_a_party.Party;
import subsystem_a_party.PartyService;
import subsystem_b_server.ServerAllocation;
import subsystem_b_server.ServerService;
import subsystem_c_anticheat.AnticheatService;
import subsystem_c_anticheat.MatchSession;

import static org.junit.jupiter.api.Assertions.*;

public class MatchmakingFacadeTest {

    private MatchmakingFacade clientFacade;
    private AdminFacade adminFacade;
    private PartyService partyService;
    private ServerService serverService;
    private AnticheatService anticheatService;
    private ChainStateRepository chainRepo;

    @BeforeEach
    void setUp() { // after every runs file .txt will clean because about this said in FIRST repeatable and independent
        clientFacade = new MatchmakingFacade();
        adminFacade = new AdminFacade();
        partyService = new PartyService();
        serverService = new ServerService();
        anticheatService = new AnticheatService();
        chainRepo = new ChainStateRepository();

        adminFacade.resetAllData();
    }

    @AfterEach
    void tearDown() { // checking disk after finished tests
        adminFacade.resetAllData();
    }

    //DSL domen-specific testing language
    //also we used clean code 9  build-operate-check

    private void assertChainStatusOnDisk(String partyId, ChainStatus expectedStatus) {
        assertEquals(expectedStatus, chainRepo.getStatus(partyId),
                "Chain status on disk should be " + expectedStatus);
    }

    private void assertPartyStatus(String partyId, Party.Status expectedStatus) {
        Party party = partyService.getParty(partyId);
        assertNotNull(party, "Party should exist");
        assertEquals(expectedStatus, party.getStatus(), "Party status mismatch");
    }

    private void assertServerStatus(String partyId, ServerAllocation.Status expectedStatus) {
        ServerAllocation alloc = serverService.getAllocation(partyId);
        assertNotNull(alloc, "Server allocation should exist");
        assertEquals(expectedStatus, alloc.getStatus(), "Server status mismatch");
    }

    // =========================================================================
    // MANDATORY TEST CASES
    // =========================================================================

    @Test
    @DisplayName("Happy Path: Full chain A -> B -> C completes successfully")
    void testHappyPathCompleteChain() {
        String partyId = "PARTY_HAPPY";

        boolean result = clientFacade.startMatchmaking(partyId, 2000, 50, false);
        //transmitting data 2000mmr 50ping and have not cheat
        // can say perfectly options

        assertTrue(result, "Matchmaking should succeed");
        assertChainStatusOnDisk(partyId, ChainStatus.COMPLETED);
        assertPartyStatus(partyId, Party.Status.LOBBY_READY);
        assertServerStatus(partyId, ServerAllocation.Status.SERVER_RESERVED);
        assertEquals(MatchSession.Status.SESSION_ACTIVE, anticheatService.getSession(partyId).getStatus());
    }

    @Test
    @DisplayName("Compensation Depth 1: Step B fails (High Ping) -> Rollback Step A")
    void testStepBFailureTriggersLevel1Compensation() {
        String partyId = "PARTY_PING_FAIL";

        boolean result = clientFacade.startMatchmaking(partyId, 2000, 300, false);
        // same, but only have ping 300
        //and I wanna notice u if player have 300ms he's can't start game and connect with server like cs2
        assertFalse(result, "Matchmaking should fail");
        assertChainStatusOnDisk(partyId, ChainStatus.COMPENSATED_A);

        // Assert Subsystem A was cancelled (compensated)
        assertPartyStatus(partyId, Party.Status.CANCELLED);
        // Assert Subsystem B was never reserved
        assertNull(serverService.getAllocation(partyId));
    }

    @Test
    @DisplayName("Compensation Depth 2: Step C fails (Cheater Detected) -> Rollback B then A")
    void testStepCFailureTriggersLevel2Compensation() {
        String partyId = "PARTY_CHEAT_FAIL";

        boolean result = clientFacade.startMatchmaking(partyId, 2000, 45, true);
        //ohhhh this option cheaters we call them weaklings because without soft/cheat they are nothing

        assertFalse(result, "Matchmaking should fail");
        assertChainStatusOnDisk(partyId, ChainStatus.COMPENSATED_AB);

        // Assert Subsystem B was released
        assertServerStatus(partyId, ServerAllocation.Status.RELEASED);
        // Assert Subsystem A was cancelled
        assertPartyStatus(partyId, Party.Status.CANCELLED);
    }



    @Test
    @DisplayName("Persistence Reload Test: State is correctly restored from disk after reload")
    void testPersistenceReloadFromDisk() {
        String partyId = "PARTY_PERSIST";

        // Trigger Depth 1 compensation
        clientFacade.startMatchmaking(partyId, 2000, 300, false);

        // Simulate application restart by creating brand new repository instances
        ChainStateRepository newChainRepo = new ChainStateRepository();
        PartyService newPartyService = new PartyService();

        // Read directly from disk
        assertEquals(ChainStatus.COMPENSATED_A, newChainRepo.getStatus(partyId));
        assertEquals(Party.Status.CANCELLED, newPartyService.getParty(partyId).getStatus());
    }
}