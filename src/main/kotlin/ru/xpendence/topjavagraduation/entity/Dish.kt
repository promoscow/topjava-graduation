package ru.xpendence.topjavagraduation.entity

import java.math.BigDecimal

/**
 * Блюдо.
 */
data class Dish(
    val id: Long? = null,
    val name: String? = null,
    val price: BigDecimal? = null,
    val active: Boolean? = null,
    val restaurant: Restaurant,
) {
    companion object {
        @JvmStatic
        fun enrichForUpdate(forUpdate: Dish, stored: Dish): Dish =
            stored.copy(
                name = forUpdate.name,
                price = forUpdate.price,
                active = forUpdate.active,
            )
    }
}
