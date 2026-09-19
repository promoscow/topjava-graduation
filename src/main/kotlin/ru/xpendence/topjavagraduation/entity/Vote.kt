package ru.xpendence.topjavagraduation.entity

import java.time.LocalDate

/**
 * Голос.
 */
data class Vote(
    val id: Long? = null,
    val date: LocalDate,
    val user: User,
    val restaurant: Restaurant,
) {
    companion object {
        @JvmStatic
        fun enrichForUpdate(forUpdate: Vote, stored: Vote): Vote =
            stored.copy(
                date = LocalDate.now(),
                restaurant = forUpdate.restaurant,
            )
    }
}
