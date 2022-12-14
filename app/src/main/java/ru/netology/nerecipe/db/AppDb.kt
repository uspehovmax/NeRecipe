package ru.netology.nerecipe.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [RecipeEntity::class],
    version = 1
)
//@TypeConverters(Converter::class)
abstract class AppDb : RoomDatabase() {
    abstract val recipeDao: RecipeDao

    companion object {
        @Volatile
        private var instance: AppDb? = null

        fun getInstance(context: Context): AppDb {
            return instance ?: synchronized(this) {
                instance ?: buildDatabase(context)
                    .also { instance = it }
            }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context, AppDb::class.java, "appRecipe.db" // имя файла, куда созданная БД сохранится
            ).allowMainThreadQueries() // разрешить обрабатывать запросы на основном потоке
                .build()
    }
}