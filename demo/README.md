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
   