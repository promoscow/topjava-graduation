package ru.xpendence.topjavagraduation.entity

/**
 * Пользователь.
 */
data class User(
    val id: Long? = null,
    val username: String,
    val password: String,
) {
    val roles: MutableList<Role> = mutableListOf()

    companion object {
        @JvmStatic
        fun enrichForUpdate(forUpdate: User, stored: User): User =
            stored.copy(
                username = forUpdate.username,
                password = forUpdate.password,
            ).also { copy ->
                copy.roles.addAll(stored.roles)
            }
    }
}
