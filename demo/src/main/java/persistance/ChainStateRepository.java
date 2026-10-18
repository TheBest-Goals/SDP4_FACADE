package persistance;

import java.io.*;
// read the current status in file
//if classes B and C return an error, this class will write to disk
public class ChainStateRepository {
    private final File file = new File("chain_state.txt");

    public void saveStatus(String sessionCode, ChainStatus status){
        try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))){
            writer.println(sessionCode + ";" + status.name());
        }catch (IOException e){
            throw new RuntimeException("Failed tp persist chain state", e);
        }
    }

    public ChainStatus getStatus(String sessionCode){
        if (!file.exists()) return ChainStatus.NOT_STARTED;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))){
            String line = reader.readLine();
            if (line != null && line.startsWith(sessionCode)){
                return ChainStatus.valueOf(line.split(";")[1]);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return ChainStatus.NOT_STARTED;
    }

    public void clear() {
        if (file.exists()) {
            file.delete();
        }
    }

}
