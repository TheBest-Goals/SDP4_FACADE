package subsystem_a_party;

import java.io.*;

public class PartyRepository {
    private final File file = new File("party_store.txt");

    public void save(Party party) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
            writer.println(party.getPartyId() + ";" + party.getAverageMmr() + ";" + party.getStatus().name());
        } catch (IOException e) {
            throw new RuntimeException("Failed to save party to disk", e);
        }
    }

    public Party findByPartyId(String partyId) {
        if (!file.exists()) return null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(";");
                if (parts.length >= 3 && parts[0].trim().equals(partyId.trim())) {
                    Party party = new Party(parts[0].trim(), Integer.parseInt(parts[1].trim()));
                    party.setStatus(Party.Status.valueOf(parts[2].trim()));
                    return party;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clear() {
        if (file.exists()) {
            file.delete();
        }
    }
}