package services

import io.realm.kotlin.mongodb.App
import io.realm.kotlin.mongodb.Credentials
import io.realm.kotlin.mongodb.User

class AppServices private constructor() {
    private val app: App by lazy {
        App.create("APP_ID") // Replace with existing APP_ID
    }

    val currentUser: User?
        get() = app.currentUser

    val isUserLoggedIn: Boolean
        get() = (app.currentUser != null)

    suspend fun login(): User {
        val credentials = Credentials.anonymous()
        return app.login(credentials)
    }

    companion object {
        @Volatile
        private var INSTANCE: AppServices? = null

        fun create(): AppServices {
            return INSTANCE ?: synchronized(this) {
                val instance = AppServices()
                INSTANCE = instance
                instance
            }
        }
    }
}
