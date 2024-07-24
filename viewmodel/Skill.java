package com.example.questfromrpg.viewmodel;

import android.content.Context;

import com.example.questfromrpg.model.DatabaseSQL;

//Класс для навыков
public class Skill {
    private String name;
    private String description;
    private int level;
    private int id;
    public Skill(){
        level = 1;
    }
    public Skill(String name, String description){
        this.name = name;
        this.description = description;
        level = 1;
    }
    public Skill(String name, String description, int level){
        this.name = name;
        this.description = description;
        this.level = level;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getLevel() {
        return level;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    //Сохранить навык в базе данных, если удалось сохранить, возвращает true
    public boolean save(Context context){
        DatabaseSQL db = new DatabaseSQL(context);
        boolean b = db.addSkill(this);
        db.close();
        return b;
    }

    //Изменить содержимое навыка если изменилась одна запись, возвращает true
    public boolean change(Context context){
        DatabaseSQL db = new DatabaseSQL(context);
        boolean b = db.changeSkill(this);
        db.close();
        return b;
    }

    //Удалить навык, если удалилась одна запись, возвращет true
    public boolean delete(Context context){
        DatabaseSQL db = new DatabaseSQL(context);
        boolean b = db.deleteSkill(id);
        db.close();
        return b;
    }

    //Увеличить уровень навыка на how пунктов
    public void increase(Context context, int how){
        level = level + how;
        change(context);
    }

    //Уменьшить уровень навыка
    public void decrease(Context context, int how){
        level = level - how;
        change(context);
    }
}
