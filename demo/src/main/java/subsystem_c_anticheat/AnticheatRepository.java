package subsystem_c_anticheat;

import java.io.*;

public class AnticheatRepository {
    private final File file = new File("anticheat_store.txt");

    public void save(MatchSession session) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) { // false mode overwrite
            writer.println(session.getPartyId() + ";" + session.hasCheats() + ";" + session.getStatus().name()); // method println unites every data like String, enum via ";"
        } catch (IOException e) { // input/output exception errors inside code like don't have memory or don't have file
            throw new RuntimeException("Failed to persist match session", e); // output
        }
    }

    public MatchSession findByPartyId(String partyId) {
        if (!file.exists()) return null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) { // different bufferedReader with fileReader buffered reader just faster
            String line = reader.readLine(); // readline reading with rows
            if (line != null && line.startsWith(partyId)) { // starts with party id?
                String[] parts = line.split(";"); //split on rows via this ";" symbol
                MatchSession session = new MatchSession(parts[0], Boolean.parseBoolean(parts[1]));
                session.setStatus(MatchSession.Status.valueOf(parts[2]));
                return session;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clear() { if (file.exists()) file.delete(); }
}