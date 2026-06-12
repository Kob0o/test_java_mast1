package com.example;

import java.util.ArrayList;
import java.util.List;

public class Frame {
    private int score;
    private final boolean lastFrame;
    private final IGenerateur generateur;
    private final List<Roll> rolls;

    public Frame(IGenerateur generateur, boolean lastFrame) {
        this.lastFrame = lastFrame;
        this.generateur = generateur;
        this.rolls = new ArrayList<>();
    }

    public boolean makeRoll() {
        if (!canRoll()) {
            return false;
        }
        int pins = generateur.randomPin(maxPinsForNextRoll());
        rolls.add(new Roll(pins));
        score += pins;
        return true;
    }

    public int getScore() {
        return score;
    }

    private boolean canRoll() {
        if (rolls.isEmpty()) {
            return true;
        }
        if (!lastFrame) {
            if (isStrike(rolls.getFirst())) {
                return false;
            }
            return rolls.size() < 2;
        }
        if (rolls.size() >= 3) {
            return false;
        }
        if (rolls.size() == 2 && !isStrike(rolls.getFirst()) && !isSpare()) {
            return false;
        }
        return true;
    }

    private int maxPinsForNextRoll() {
        if (rolls.isEmpty()) {
            return 10;
        }
        if (!lastFrame || isStrike(rolls.getFirst()) || rolls.size() >= 2) {
            if (rolls.size() == 1 && !lastFrame) {
                return 10 - rolls.getFirst().getPins();
            }
            return 10;
        }
        return 10 - rolls.getFirst().getPins();
    }

    private boolean isStrike(Roll roll) {
        return roll.getPins() == 10;
    }

    private boolean isSpare() {
        return rolls.size() >= 2
                && rolls.get(0).getPins() + rolls.get(1).getPins() == 10;
    }
}
