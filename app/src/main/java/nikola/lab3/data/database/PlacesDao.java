package nikola.lab3.data.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PlacesDao {

    @Query("SELECT * FROM place")
    LiveData<List<Place>> getAll();

    @Insert
    void insertAll(Place... places);

    @Query("DELETE FROM place")
    void deleteAll();

    @Insert
    void insert(Place place);


}
