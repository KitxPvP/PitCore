package com.kitx.permanent;

import com.kitx.permanent.impl.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum PerkLoader {
    INSTANCE;

    private final List<Perk> perkList = new ArrayList<>();

    public void init() {
        perkList.add(new LavaPerk());
        perkList.add(new FishingRodPerk());
        perkList.add(new GoldenHeadPerk());
        perkList.add(new StrengthChaining());
        perkList.add(new SafetyFirst());
        perkList.add(new Mineman());
        perkList.add(new EndlessQuiver());
        perkList.add(new Vampire());
        perkList.add(new TrickleDown());
        perkList.add(new Speedster());

    }

    public Perk findItem(String name) {
        for(Perk perk : perkList) {
            if(perk.getName().equalsIgnoreCase(name))
                return perk;
        }
        return null;
    }
}
