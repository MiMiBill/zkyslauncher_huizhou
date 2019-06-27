package com.muju.note.launcher.app.home.db;

import org.litepal.crud.LitePalSupport;

import java.util.List;

public class HomeMenuDao extends LitePalSupport {


    /**
     * tab : 11
     * name : 医院介绍
     * id : 1
     * parentId : 0
     * child : []
     */

    private String tab;
    private String name;
    private int id;
    private int parentId;
    private String icon;
    private int menuId;
    private List<HomeMenuSubDao> child;

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public String getTab() {
        return tab;
    }

    public void setTab(String tab) {
        this.tab = tab;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public List<HomeMenuSubDao> getChild() {
        return child;
    }

    public void setChild(List<HomeMenuSubDao> child) {
        this.child = child;
    }
}
