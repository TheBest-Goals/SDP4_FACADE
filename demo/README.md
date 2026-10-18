# Video Game Matchmaking System (Assignment 4 — Facade Pattern)

## Domain
**Variant 16: Video Game Matchmaking**
- **Subsystem A (Intake / Reservation):** Party Formation & Skill Rating Check (`subsystem_a_party`)
- **Subsystem B (Processing / Execution):** Server Allocation & Latency Check (`subsystem_b_server`)
- **Subsystem C (Finalization / Confirmation):** Anti-Cheat Scanner & Match Session Start (`subsystem_c_anticheat`)

---

## How to Build & Run
1. **Requirements:** Java 17+ / Java 21.
2. **Run Application:** Execute `Main.java` in your IDE or via terminal:
   ```bash
   javac -d out $(find src/main/java -name "*.java")
   java -cp out Main

   ---

## Principle of Least Knowledge (Law of Demeter)
The client interacts exclusively with `MatchmakingFacade` through a single method (`startMatchmaking`), having zero knowledge of underlying subsystems or rollback logic.

---

## Reflection: Facade vs. Adapter

1. **Facade vs. Adapter:**
   - **Facade:** Hides a complex multi-step system behind one simple operation.
   - **Adapter:** Makes two incompatible interfaces work together.

2. **Why Facade?**
   We are joining 3 subsystems (Party, Server, Anticheat) with automatic rollback logic into a single method call: `startMatchmaking()`. Since we are hiding workflow complexity rather than converting interfaces, Facade is the correct pattern.

---

## UML Class Diagram

```mermaid
classDiagram
    class Main {
        +main(String[] args)
    }

    class MatchmakingFacade {
        -PartyService partyService
        -ServerService serverService
        -AnticheatService anticheatService
        -ChainStateRepository chainRepo
        +startMatchmaking(partyId, mmr, pingMs, simulateCheat) boolean
    }

    class AdminFacade {
        +inspectState(partyId) void
        +resetAllData() void
    }

    Main --> MatchmakingFacade
    Main --> AdminFacade

   
