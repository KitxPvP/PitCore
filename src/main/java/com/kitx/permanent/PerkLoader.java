package com.kitx.permanent;

import com.kitx.permanent.impl.FishingRodPerk;
import com.kitx.permanent.impl.GoldenHeadPerk;
import com.kitx.permanent.impl.LavaPerk;
import com.kitx.utils.ClassUtils;
import lombok.Getter;
import lombok.experimental.UtilityClass;

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
    }

    public Perk findItem(String name) {
        for(Perk perk : perkList) {
            if(perk.getName().equalsIgnoreCase(name))
                return perk;
        }
        return null;
    }
}
