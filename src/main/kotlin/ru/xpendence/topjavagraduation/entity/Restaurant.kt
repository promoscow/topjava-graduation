package ru.xpendence.topjavagraduation.entity

/**
 * Ресторан.
 */
data class Restaurant(
    val id: Long? = null,
    val name: String? = null,
) {
    val dishes: MutableSet<Dish> = hashSetOf()
    val votes: MutableSet<Vote> = hashSetOf()

    companion object {
        @JvmStatic
        fun enrichForUpdate(forUpdate: Restaurant, stored: Restaurant): Restaurant =
            stored.copy(name = forUpdate.name).also { copy ->
                copy.dishes.addAll(stored.dishes)
                copy.votes.addAll(stored.votes)
            }
    }
}
