package pl.edu.pwr.student.actions_feed.dao

import androidx.room.*

@Entity(tableName = "repository")
data class Repository(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val path: String,
)

@Dao
interface RepositoryDao {
    @Insert
    fun addRepository(repository: Repository)
    @Query("delete from repository where id = :id")
    fun deleteRepository(id: Long)
    @Query("select path from repository order by id asc")
    fun getAll(): Array<String>
}

@Database(entities = [Repository::class], version = 1)
abstract class RepositoryDatabase : RoomDatabase() {
    abstract fun repositoryDao(): RepositoryDao
}
