package ru.xpendence.topjavagraduation.entity

import ru.xpendence.topjavagraduation.entity.type.RoleType

/**
 * Роль.
 */
data class Role(
    val id: Long? = null,
    val name: RoleType? = null,
)
