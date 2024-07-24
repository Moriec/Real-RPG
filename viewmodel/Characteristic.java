package com.example.questfromrpg.viewmodel;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.questfromrpg.model.SharedCharacteristic;

//характеристика имеет имя, описание и уровень
public class Characteristic {
    private String id;
    private String name;
    private String description;
    private int level;
    public Characteristic(){
        level = 1;
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
    public void save(Context context){
        // Запись данных
        SharedCharacteristic sc = new SharedCharacteristic(context);
        sc.saveCharacteristic(this);
    }

    //Увеличить характеристику
    public void increase(Context context, int how){
        level = level + how;
        save(context);
    }

    //Уменьшить характеристику
    public  void decrease(Context context, int how){
        level = level - how;
        save(context);
    }
}
