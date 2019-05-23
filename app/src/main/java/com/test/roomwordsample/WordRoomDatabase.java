package com.test.roomwordsample;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

@Database(entities = {Word.class},version = 1)
public abstract class WordRoomDatabase extends RoomDatabase {

    private static volatile WordRoomDatabase INSTANCE;

    public static synchronized WordRoomDatabase getDatabase(final Context context){
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    WordRoomDatabase.class,"word_database")
                    .addCallback(roomDatabaseCallback)
                    .build();
        }

        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new populateDatabaseAsyncTask(INSTANCE).execute();
        }
    };

    private static class populateDatabaseAsyncTask extends AsyncTask<Void,Void,Void>{

        private WordDao wordDao;
        public populateDatabaseAsyncTask(WordRoomDatabase wordRoomDatabase) {
            wordDao = wordRoomDatabase.wordDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAll();
          /*  Word word = new Word("First Word");
            wordDao.insert(word);
            word = new Word("Second Word");
            wordDao.insert(word);*/
            return null;
        }
    }

    public abstract WordDao wordDao();
}
