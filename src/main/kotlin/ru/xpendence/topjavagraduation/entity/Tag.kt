package ru.xpendence.topjavagraduation.entity

/**
 * Тег.
 */
data class Tag(
    val id: Long? = null,
    val name: String,
) {
    companion object {
        @JvmStatic
        fun enrichForUpdate(forUpdate: Tag, stored: Tag): Tag =
            stored.copy(name = forUpdate.name)
    }
}
