package subsystem_b_server;

import java.io.*;

public class ServerRepository {
    private final File file = new File("server_store.txt");

    public void save(ServerAllocation allocation) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
            writer.println(allocation.getPartyId() + ";" + allocation.getPingMs() + ";" + allocation.getStatus().name());
        } catch (IOException e) {
            throw new RuntimeException("Failed to persist server allocation", e);
        }
    }

    public ServerAllocation findByPartyId(String partyId) {
        if (!file.exists()) return null;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line != null && line.startsWith(partyId)) {
                String[] parts = line.split(";");
                ServerAllocation alloc = new ServerAllocation(parts[0], Integer.parseInt(parts[1]));
                alloc.setStatus(ServerAllocation.Status.valueOf(parts[2]));
                return alloc;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clear() { if (file.exists()) file.delete(); }
}