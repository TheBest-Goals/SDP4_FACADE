package subsystem_a_party;
// check mmr around 0-5000
// and this business logic in 4 asikkk u said that we must use a one class for storage?
public class SkillRatingChecker {
    public boolean isMmrValid(int mmr){
        return mmr >= 0 && mmr <= 5000;
    }
}
