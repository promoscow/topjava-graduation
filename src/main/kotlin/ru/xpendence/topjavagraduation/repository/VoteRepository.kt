package ru.xpendence.topjavagraduation.repository

import ru.xpendence.topjavagraduation.entity.Vote
import java.time.LocalDate
import java.util.*

/**
 * Доступ к голосам через JDBC.
 */
interface VoteRepository {

    /**
     * Сохраняет голос: вставляет новую запись или обновляет дату и ресторан существующей по [Vote.id].
     *
     * @param vote голос; для вставки — без id, с user.id и restaurant.id
     * @return сохранённый голос (с присвоенным id при вставке)
     */
    fun save(vote: Vote): Vote

    /**
     * Возвращает голос по идентификатору без полной загрузки связанных сущностей.
     *
     * @param id идентификатор голоса
     * @return голос или empty, если не найден
     */
    fun findById(id: Long): Optional<Vote>

    /**
     * Возвращает голос по идентификатору с загруженными пользователем и рестораном.
     *
     * @param id идентификатор голоса
     * @return голос с деталями или empty, если не найден
     */
    fun findByIdWithDetails(id: Long): Optional<Vote>

    /**
     * Возвращает голос пользователя на указанную дату с загруженными пользователем и рестораном.
     *
     * @param userId идентификатор пользователя
     * @param date дата голосования
     * @return голос или empty, если не найден
     */
    fun findByUserIdAndDate(userId: Long, date: LocalDate): Optional<Vote>
}
