package com.kitx.permanent;

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
        try {
            for(Class<?> classes : ClassUtils.getClassesForPackage("com.kitx.permanent.impl")) {
                try {
                    Perk perk = (Perk) classes.newInstance();
                    perkList.add(perk);
                } catch (InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
