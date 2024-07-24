package com.example.questfromrpg.viewmodel;

import android.content.Context;

import com.example.questfromrpg.model.SharedCharacteristic;

import java.util.ArrayList;

public class Quest {
    protected int id;
    protected String name;
    protected String description;
    protected ArrayList<Characteristic> characteristics;
    protected ArrayList<Skill> skills;
    protected int levelQ;
    protected  String code;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getDescription(){
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Characteristic> getCharacteristics() {
        return characteristics;
    }

    public void setCharacteristics(ArrayList<Characteristic> characteristics) {
        this.characteristics = characteristics;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }
    public void setCharacteristicsCode(String code){
        this.code = code;
    }
    public void setCharacteristics(Context context){
        String codeCharacteristic = code;
        String[] nameCharacteristics = { "Сила", "Ловкость", "Интеллект", "Выносливость" };
        SharedCharacteristic sc = new SharedCharacteristic(context);
        //Нужно прописать
        for(int i = 0; i < codeCharacteristic.length(); i++){
            char achar = codeCharacteristic.charAt(i);
            if(achar == '1'){
                String name = nameCharacteristics[i];
                Characteristic characteristic = sc.readCharacteristic(name);
                characteristics.add(characteristic);
            }
        }
    }
    public String getCodeCharacteristics(){
        //Дописать
        return "";
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLevelQ(int levelQ) {
        this.levelQ = levelQ;
    }

    public int getLevelQ() {
        return levelQ;
    }
}
