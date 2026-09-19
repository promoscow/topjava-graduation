package ru.xpendence.topjavagraduation.entity

import java.time.LocalDate

/**
 * Отзыв.
 */
data class Review(
    val id: Long? = null,
    val rating: Int,
    val text: String? = null,
    val date: LocalDate,
    val user: User,
    val restaurant: Restaurant,
) {
    companion object {
        @JvmStatic
        fun enrichForUpdate(forUpdate: Review, stored: Review): Review =
            stored.copy(
                rating = forUpdate.rating,
                text = forUpdate.text,
            )
    }
}
