package nikola.lab3.data.database;

import android.arch.persistence.room.Room;
import android.content.Context;

public class DatabaseSingleton {
    public static final String DATABASE_NAME = "places";
    private static PlacesDatabase database;

    public synchronized static PlacesDatabase getDatabase(Context context){
        if (database == null){
            database = Room.databaseBuilder(context,PlacesDatabase.class, DATABASE_NAME)
                    .addMigrations(PlacesDatabase.MIGRATION_1_2)
                    .build();
        }
        return database;
    }
}
