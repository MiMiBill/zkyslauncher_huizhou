package com.muju.note.launcher.app.hostipal.bean;

import android.content.Intent;

public class Entity {
    int icon;
    String name;
    int color;
    Intent intent;

    public Entity(int icon, Intent intent) {
        this.icon = icon;
        this.intent = intent;
    }

    public Entity(int icon, String name, int color, Intent intent) {
        this.icon = icon;
        this.name = name;
        this.color = color;
        this.intent = intent;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
