package database

import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.mongodb.User
import io.realm.kotlin.mongodb.sync.SyncConfiguration
import io.realm.kotlin.query.RealmResults
import io.realm.kotlin.query.Sort
import io.realm.kotlin.types.RealmInstant
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transform
import model.Product

class Database private constructor(
    private val user: User
) {
    private val realm: Realm by lazy {
        val configuration = SyncConfiguration.Builder(user, schema = setOf(Product::class))
            .name("sampleDB")
            .initialSubscriptions { realm ->
                this.add(realm.query<Product>(), name = "Products")
            }
            .waitForInitialRemoteData()
            .build()

        Realm.open(configuration)
    }

    private suspend fun write(product: Product) {
        realm.write {
            this.copyToRealm(instance = product)
        }
    }

    suspend fun addProduct1() {
        write(product = Product().apply {
            description = "Carne Bovina ${RealmInstant.now().epochSeconds}"
            brand = "Frisa"
        })
    }

    suspend fun addProduct2() {
        write(product = Product().apply {
            description = "Carne Bovina Alcatra"
            brand = "Friboi"
        })
    }

    suspend fun addProduct3() {
        write(product = Product().apply {
            description = "Carne Bovina Picanha Fatiada"
            brand = "Friboi"
        })
    }

    suspend fun addProduct4() {
        write(product = Product().apply {
            description = "Carne Suína Pernil"
            brand = "Corella"
        })
    }

    fun findProducts(): Flow<RealmResults<Product>> {
        return realm.query<Product>("description TEXT $0", "bovina -paleta")
            .sort("description" to Sort.ASCENDING, "brand" to Sort.ASCENDING)
            .asFlow()
            .transform { results ->
                emit(results.list)
            }
    }

    fun close() = realm.close()

    companion object {
        @Volatile
        private var INSTANCE: Database? = null

        fun create(user: User): Database {
            return INSTANCE ?: synchronized(this) {
                val instance = Database(user)
                INSTANCE = instance
                instance
            }
        }
    }
}