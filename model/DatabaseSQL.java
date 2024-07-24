package com.example.questfromrpg.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.questfromrpg.viewmodel.Quest;
import com.example.questfromrpg.viewmodel.Skill;

import java.util.ArrayList;

public class DatabaseSQL extends SQLiteOpenHelper {
    private static final String DB_NAME = "my_database";
    private static final int DB_VERSION = 1;

    private SQLiteDatabase db;
    public DatabaseSQL(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    //Таблица skills хранит навыки
    //
    //Таблица quests хранит задания. codeCharacteristic представляет из себя строку из 5 цифр, которые обозначают,
    //есть ли связь задания с одной из пяти характеристик. Также quest содержит levelQ который показывает редкость(значимость) задания
    //LevelQ: 1-обычный, 2-редкий, 3-эпический, 4-легендарный.
    //
    //Таблица skillsForQuests хранит айди навыков, привязанных к заданию.
    //
    //Таблица completeQuests содержит завершенные задания
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE skills (_id INTEGER PRIMARY KEY, name TEXT, description TEXT, level INTEGER)");
        db.execSQL("CREATE TABLE quests (_id INTEGER PRIMARY KEY, name TEXT, description TEXT, codeCharacteristic TEXT, levelQ INTEGER)");
        db.execSQL("CREATE TABLE skillsForQuests (idQuest INTEGER, idSkill INTEGER)");
        db.execSQL("CREATE TABLE completeQuests (_id INTEGER PRIMARY KEY, name TEXT, description TEXT, codeCharacteristic TEXT, levelQ INTEGER)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    //*****************************************ЧТЕНИЕ ИНФОРМАЦИИ***********************************************************
    public ArrayList<Skill> getAllSkills(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM skills" ,null);
        ArrayList<Skill> skillMassiv = new ArrayList<>();
        while(cursor.moveToNext()){
            Skill skill = new Skill();
            skill.setId(cursor.getInt(0));
            skill.setName(cursor.getString(1));
            skill.setDescription(cursor.getString(2));
            skill.setLevel(cursor.getInt(3));
            skillMassiv.add(skill);
        }
        db.close();
        cursor.close();
        return skillMassiv;
    }

    public ArrayList<Quest> getAllActiveQuests(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM quests" ,null);
        ArrayList<Quest> questMassiv = new ArrayList<>();
        while(cursor.moveToNext()){
            Quest quest = new Quest();
            quest.setId(cursor.getInt(0));
            quest.setName(cursor.getString(1));
            quest.setDescription(cursor.getString(2));
            quest.setCharacteristicsCode(cursor.getString(3));
            quest.setLevelQ(cursor.getInt(4));
            quest.setSkills(getSkillsFromQuest(quest.getId()));
            questMassiv.add(quest);
        }
        db.close();
        cursor.close();
        return questMassiv;
    }

    public ArrayList<Quest> getAllCompleteQuests(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM completeQuests" ,null);
        ArrayList<Quest> questMassiv = new ArrayList<>();
        while(cursor.moveToNext()){
            Quest quest = new Quest();
            quest.setId(cursor.getInt(0));
            quest.setName(cursor.getString(1));
            quest.setDescription(cursor.getString(2));
            quest.setCharacteristicsCode(cursor.getString(3));
            quest.setLevelQ(cursor.getInt(4));
            quest.setSkills(getSkillsFromQuest(quest.getId()));
            questMassiv.add(quest);
        }
        db.close();
        cursor.close();
        return questMassiv;
    }
    public Skill getSkill(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("skills",
                new String[] {"_id","name","description", "level"},
                "_id = ?", new String[] {String.valueOf(id)}, null, null,null);
        if(cursor.moveToFirst()){
            Skill skill = new Skill();
            skill.setId(cursor.getInt(0));
            skill.setName(cursor.getString(1));
            skill.setDescription(cursor.getString(2));
            skill.setLevel(cursor.getInt(3));
            cursor.close();
            db.close();
            return skill;
        }
        cursor.close();
        db.close();
        return null;
    }
    //Получить связанные с заданием навыки, принимает на вход _id от конкретного задания
    public ArrayList<Skill> getSkillsFromQuest(int idQuest){
        ArrayList<Skill> skillMassiv = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("skillsForQuests", new String[] {"idSkill"}, "idQuest = ?", new String[]{String.valueOf(idQuest)},
                null, null, null);
        while(cursor.moveToNext()){
            skillMassiv.add(getSkill(cursor.getInt(0)));
        }
        db.close();
        cursor.close();
        return skillMassiv;
    }
    //********************************ДОБАВЛЕНИЕ ИНФОРМАЦИИ*****************************************************************
    public boolean addSkill(Skill skill){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", skill.getName());
        contentValues.put("description", skill.getDescription());
        contentValues.put("level", skill.getLevel());
        long result = db.insert("skills", null, contentValues);
        db.close();
        return result != -1;
    }
    public boolean addQuest(Quest quest){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", quest.getName());
        contentValues.put("description", quest.getDescription());
        contentValues.put("codeCharacteristic", quest.getCodeCharacteristics());
        contentValues.put("levelQ", quest.getLevelQ());
        long id = db.insert("quests", null, contentValues);
        //добавляем навыки в таблицу skillsFromQuest
        if(id != -1){
            ArrayList<Skill> skillArrayList = quest.getSkills();
            boolean f1 = true;
            for(Skill skill : skillArrayList){
                ContentValues contentValuesSkills = new ContentValues();
                contentValuesSkills.put("idQuest", id);
                contentValuesSkills.put("idSkill", skill.getId());
                long res = db.insert("skillsForQuests", null, contentValuesSkills);
                if(res == -1){
                    f1 = false;
                }
            }
            db.close();
            return f1;
        }
        else{
            db.close();
            return false;
        }
    }
    public boolean addCompleteQuest(Quest quest){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", quest.getName());
        contentValues.put("description", quest.getDescription());
        contentValues.put("codeCharacteristic", quest.getCodeCharacteristics());
        contentValues.put("levelQ", quest.getLevelQ());
        long id = db.insert("completeQuests", null, contentValues);
        //добавляем навыки в таблицу skillsFromQuest
        if(id != -1){
            ArrayList<Skill> skillArrayList = quest.getSkills();
            boolean f1 = true;
            for(Skill skill : skillArrayList){
                ContentValues contentValuesSkills = new ContentValues();
                contentValuesSkills.put("idQuest", id);
                contentValuesSkills.put("idSkill", skill.getId());
                long res = db.insert("skillsForQuests", null, contentValuesSkills);
                if(res == -1){
                    f1 = false;
                }
            }
            db.close();
            return f1;
        }
        else{
            db.close();
            return false;
        }
    }
    //********************************ИЗМЕНЕНИЕ Информации******************************************************************
    public boolean changeSkill(Skill skill){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", skill.getId());
        contentValues.put("name", skill.getName());
        contentValues.put("description", skill.getDescription());
        contentValues.put("level", skill.getLevel());
        int result = db.update("skills", contentValues, "_id = ?", new String[]{String.valueOf(skill.getId())});
        db.close();
        return result == 1;
    }

    public boolean changeQuest(Quest quest){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("_id", quest.getId());
        contentValues.put("name", quest.getName());
        contentValues.put("description", quest.getDescription());
        contentValues.put("levelQ", quest.getLevelQ());
        contentValues.put("codeCharacteristic", quest.getCodeCharacteristics());
        int result = db.update("quests", contentValues, "_id = ?", new String[]{String.valueOf(quest.getId())});
        // Изменить skillsForQuests
        deleteSkillsForQuests(quest.getId());

        ArrayList<Skill> skillArrayList = quest.getSkills();
        boolean f1 = true;
        for(Skill skill : skillArrayList) {
            ContentValues contentValuesSkills = new ContentValues();
            contentValuesSkills.put("idQuest", quest.getId());
            contentValuesSkills.put("idSkill", skill.getId());
            long res = db.insert("skillsForQuests", null, contentValuesSkills);
        }
        db.close();
        return result == 1;
    }
    //********************************УДАЛЕНИЕ ИНФОРМАЦИИ*******************************************************************
    public boolean deleteSkill(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("skills", "_id = ?", new String[]{String.valueOf(id)});
        db.close();
        return result == 1;
    }
    public boolean deleteActiveQuest(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("quests", "_id = ?", new String[]{String.valueOf(id)});
        int res2 = db.delete("skillsForQuests", "idQuest = ?", new String[]{String.valueOf(id)});
        db.close();
        return result == 1;
    }
    public boolean deleteCompleteQuest(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("completeQuests", "_id = ?", new String[]{String.valueOf(id)});
        int res2 = db.delete("skillsForQuests", "idQuest = ?", new String[]{String.valueOf(id)});
        db.close();
        return result == 1;
    }
    private void deleteSkillsForQuests(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete("skillsForQuests", "idQuest = ?", new String[]{String.valueOf(id)});
        db.close();
    }
}