package hu.rpg.service;

import hu.rpg.domain.Fighter;

import java.util.ArrayList;
import java.util.List;

public class CombatEngine {

    public String executeTurn(Fighter attacker, Fighter defender) throws InvalidActionException {
        if (attacker == defender) {
            throw new InvalidActionException("Nem választhatod ugyanazt a karaktert támadónak és védőnek!");
        }
        int hpBefore = defender.getHp();
        int damageDealt = attacker.attack(defender);
        int actualLost = hpBefore - defender.getHp();

        if (actualLost == 0 && damageDealt > 0) {
            return String.format("%s támadott, de %s kitért a csapás elől! (HP: %d/%d)",
                    attacker.getName(), defender.getName(), defender.getHp(), defender.getMaxHp());
        }

        return String.format("%s megtámadta %s harcost (%d sebzés, tényleges HP veszteség: %d). %s HP: %d/%d",
                attacker.getName(),
                defender.getName(),
                damageDealt,
                actualLost,
                defender.getName(),
                defender.getHp(),
                defender.getMaxHp());
    }

    public List<String> simulateDuel(Fighter first, Fighter second) throws InvalidActionException {
        if (first == second) {
            throw new InvalidActionException("Egy karakter nem párbajozhat saját magával!");
        }
        List<String> log = new ArrayList<>();
        int round = 1;

        while (first.isAlive() && second.isAlive() && round <= 50) {
            log.add("--- " + round + ". KÖR ---");
            log.add(executeTurn(first, second));
            if (!second.isAlive()) {
                log.add("GYŐZTES: " + first.getName() + "!");
                break;
            }
            log.add(executeTurn(second, first));
            if (!first.isAlive()) {
                log.add("GYŐZTES: " + second.getName() + "!");
                break;
            }
            round++;
        }
        return log;
    }
}
