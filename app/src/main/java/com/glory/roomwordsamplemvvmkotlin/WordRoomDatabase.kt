package com.glory.roomwordsamplemvvmkotlin

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

// Annotates class to be a Room Database with a table (entity) of the Word class
@Database(entities = arrayOf(Word::class), version = 1)
 abstract class WordRoomDatabase : RoomDatabase(){
     abstract fun wordDao() : WordDao

     companion object{
         // Singleton prevents multiple instances of database opening at the
         // same time.
         @Volatile
         private var INSTANCE: WordRoomDatabase? = null

         fun getDatabase(context: Context,
                        // To launch a coroutine we need a CoroutineScope.
                         scope: CoroutineScope
         ) : WordRoomDatabase{
             val tempInstance = INSTANCE
             if(tempInstance != null){
                 return tempInstance
             }

             synchronized(this){
                 val instance = Room.databaseBuilder(
                     context.applicationContext,
                     WordRoomDatabase::class.java,
                     "word_database"
                 )
                     .build()
                 INSTANCE = instance
                 return instance
             }
         }
     }


    //To delete all content and repopulate the database whenever the app is started,
    // you create a RoomDatabase.Callback and override onOpen().
    // Because you cannot do Room database operations on the UI thread,
    // onOpen() launches a coroutine on the IO Dispatcher.

  /*  private class WordDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.wordDao())
                }
            }
        }

        suspend fun populateDatabase(wordDao: WordDao) {
            // Delete all content here.
            wordDao.deleteAll()

            // Add sample words.
            var word = Word("Hello")
            wordDao.insert(word)
            word = Word("World!")
            wordDao.insert(word)

            // TODO: Add your own words!
        }
    }*/
}
