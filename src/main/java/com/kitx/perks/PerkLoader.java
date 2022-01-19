package com.kitx.perks;

import com.kitx.PitCore;
import com.kitx.perks.impl.*;
import lombok.Getter;
import org.bukkit.Bukkit;

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
        perkList.add(new StrengthChainingPerk());
        perkList.add(new SafetyFirstPerk());
        perkList.add(new MinemanPerk());
        perkList.add(new EndlessQuiverPerk());
        perkList.add(new VampirePerk());
        perkList.add(new TrickleDownPerk());
        perkList.add(new SpeedsterPerk());
        perkList.add(new MedicPerk());
        perkList.add(new DirtyPerk());
        perkList.add(new JanitorPerk());
    }

    public Perk findPerk(String name) {
        for(Perk perk : perkList) {
            if(perk.getName().equalsIgnoreCase(name))
                return perk;
        }
        return null;
    }
}
