package com.example.questfromrpg.viewmodel;

import android.content.Context;

import com.example.questfromrpg.model.DatabaseSQL;

public class CompleteQuest extends Quest{
    //Сохраняет завершенное задание в базе данных. Если запись сохранилась, возвращает true
    public boolean save(Context context){
        DatabaseSQL db = new DatabaseSQL(context);
        boolean b = db.addCompleteQuest(this);
        db.close();
        return b;
    }

    //Удаляет выполненное задание, откатывая навыки до прежнего состояния, при этом, если удалиась одна запись, возвращает true
    public boolean delete(Context context){
        // how - то, насколько должны уменьшится уровни навыков
        int how;
        switch (levelQ){
            case (1):
                how = 1;
                break;
            case (2):
                how = 5;
                break;
            case (3):
                how = 10;
                break;
            case (4):
                how = 20;
                break;
            case (5):
                how = 50;
                break;
            default:
                how = -1000;
                break;
        }
        //Увеличение уровней навыков
        for(Skill skill : skills){
            skill.decrease(context, how);
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
            characteristic.decrease(context, howC);
        }
        DatabaseSQL db = new DatabaseSQL(context);
        db.deleteCompleteQuest(id);
        db.close();
        return true;
    }
}
