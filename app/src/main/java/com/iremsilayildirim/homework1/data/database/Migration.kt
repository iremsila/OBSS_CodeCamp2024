import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_2_3 = object : Migration(2, 3) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Drop the existing table if it exists
        database.execSQL("DROP TABLE IF EXISTS favorite_movies")

        // Create the new table with the correct schema
        database.execSQL(
            """
            CREATE TABLE IF NOT EXISTS favorite_movies (
                id INTEGER PRIMARY KEY NOT NULL,
                title TEXT NOT NULL,
                posterPath TEXT NOT NULL,
                releaseDate TEXT NOT NULL,
                overview TEXT NOT NULL
            )
            """.trimIndent()
        )
    }
}
