package com.example.questfromrpg.model;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.questfromrpg.viewmodel.Characteristic;

public class SharedCharacteristic {
    Context context;
    public SharedCharacteristic(Context context){
        context = this.context;
    }
    //Прочитать характеристику
    public Characteristic readCharacteristic(String name){
        SharedPreferences prefs = context.getSharedPreferences("characteristicsDataForRPG0", MODE_PRIVATE);
        Characteristic characteristic = new Characteristic();
        characteristic.setName(name);
        characteristic.setLevel(prefs.getInt(name + "level", 1));
        characteristic.setDescription(prefs.getString(name + "description", ""));
        return characteristic;
    }

    //Сохранить изменения
    public void saveCharacteristic(Characteristic characteristic){
        // Запись данных
        SharedPreferences.Editor editor = context.getSharedPreferences("characteristicsDataForRPG0", MODE_PRIVATE).edit();
        editor.putInt(characteristic.getName() + "level", characteristic.getLevel());
        editor.putString(characteristic.getName() + "description", characteristic.getDescription());
        editor.apply();
    }
}
