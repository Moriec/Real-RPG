package com.example.questfromrpg.viewmodel;

import android.content.Context;

import com.example.questfromrpg.model.DatabaseSQL;

public class ActiveQuest extends Quest{
    //Сохраняет задание в базе данных. Если запись сохранилась, возвращает true
    public boolean save(Context context){
        DatabaseSQL db = new DatabaseSQL(context);
        boolean b = db.addQuest(this);
        db.close();
        return b;
    }

    //Изменить задание, если изменилось одно задание, возвращает true
    public boolean change(Context context){
        DatabaseSQL db = new DatabaseSQL(context);
        boolean b = db.changeQuest(this);
        db.close();
        return b;
    }

    //Удаляет задание, если удалиась одна запись, возвращает true
    public boolean delete(Context context){
        DatabaseSQL db = new DatabaseSQL(context);
        boolean b = db.deleteActiveQuest(id);
        db.close();
        return b;
    }

    //Выполнить задание,
    public boolean done(Context context){
        // how - то, насколько должны увеличиться уровни навыков
        int how;
        switch (levelQ){
            case (1):
                how = 1;
                break;
            case (2):
                how = 3;
                break;
            case (3):
                how = 5;
                break;
            case (4):
                how = 10;
                break;
            case (5):
                how = 20;
                break;
            default:
                how = -1000;
                break;
        }
        //Увеличение уровней навыков
        for(Skill skill : skills){
            skill.increase(context, how);
        }

        //Изменение характеристик
        // howC - то, насколько должны увеличиться уровни навыков
        int howC;
        switch (levelQ){
            case (1):
                howC = 1;
                break;
            case (2):
                howC = 3;
                break;
            case (3):
                howC = 5;
                break;
            case (4):
                howC = 10;
                break;
            case (5):
                howC = 20;
                break;
            default:
                howC = -1000;
                break;
        }
        //Увеличение уровня характеристик
        for(Characteristic characteristic : characteristics){
            characteristic.increase(context, howC);
        }
        DatabaseSQL db = new DatabaseSQL(context);
        db.addCompleteQuest(this);
        db.deleteActiveQuest(id);
        db.close();
        return true;
    }
}
