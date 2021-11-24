package com.kitx.permanent;

import org.bukkit.Material;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface PerkInfo {
    String name();

    String desc();

    int cost();

    Material icon();

    int requiredPrestige() default 0;
}
