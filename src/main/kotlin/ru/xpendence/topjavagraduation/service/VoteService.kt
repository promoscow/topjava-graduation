package ru.xpendence.topjavagraduation.service

import ru.xpendence.topjavagraduation.entity.Vote
import java.time.LocalDate

/**
 * Операции над голосами пользователей за рестораны.
 */
interface VoteService {

    /**
     * Сохраняет новый голос. Голосование доступно только до 11:00 текущего дня.
     *
     * @param vote новый голос с идентификаторами пользователя и ресторана
     * @return сохранённый голос с присвоенным идентификатором
     * @throws IllegalArgumentException если голосование уже закрыто на сегодня
     * @throws java.util.NoSuchElementException если пользователь или ресторан не найдены
     */
    fun create(vote: Vote): Vote

    /**
     * Обновляет существующий голос — меняет выбранный ресторан.
     *
     * @param vote голос с заполненным идентификатором и новым рестораном
     * @throws IllegalArgumentException если идентификатор не задан
     * @throws java.util.NoSuchElementException если голос или ресторан не найдены
     */
    fun update(vote: Vote)

    /**
     * Возвращает голос по идентификатору вместе со связанными пользователем и рестораном.
     *
     * @param id идентификатор голоса
     * @return найденный голос
     * @throws java.util.NoSuchElementException если голос не найден
     */
    fun getById(id: Long): Vote

    /**
     * Возвращает голос пользователя за указанную дату.
     *
     * @param userId идентификатор пользователя
     * @param date дата голосования
     * @return найденный голос
     * @throws java.util.NoSuchElementException если голос не найден
     */
    fun getByUserId(userId: Long, date: LocalDate): Vote
}
