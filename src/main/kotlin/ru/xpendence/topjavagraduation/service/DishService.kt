package ru.xpendence.topjavagraduation.service

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.xpendence.topjavagraduation.entity.Dish

/**
 * Операции над блюдами ресторанов.
 */
interface DishService {

    /**
     * Сохраняет новое блюдо.
     *
     * @param dish новое блюдо
     * @return сохранённое блюдо с присвоенным идентификатором
     */
    fun create(dish: Dish): Dish

    /**
     * Обновляет название, цену и признак активности существующего блюда.
     * Остальные поля, включая привязку к ресторану, остаются прежними.
     *
     * @param dish блюдо с заполненным идентификатором и новыми значениями полей
     * @throws IllegalArgumentException если идентификатор не задан
     * @throws java.util.NoSuchElementException если блюдо с таким идентификатором не найдено
     */
    fun update(dish: Dish)

    /**
     * Снимает с меню все блюда ресторана, проставляя им признак неактивности.
     * Блюда не удаляются и могут быть возвращены в меню повторно.
     *
     * @param restaurantId идентификатор ресторана
     * @throws java.util.NoSuchElementException если у ресторана нет ни одного блюда
     */
    fun resetMenu(restaurantId: Long)

    /**
     * Возвращает блюдо по идентификатору.
     *
     * @param id идентификатор блюда
     * @return найденное блюдо
     * @throws java.util.NoSuchElementException если блюдо не найдено
     */
    fun getById(id: Long): Dish

    /**
     * Возвращает активное блюдо по идентификатору.
     *
     * @param id идентификатор блюда
     * @return найденное активное блюдо
     * @throws java.util.NoSuchElementException если блюдо не найдено или неактивно
     */
    fun getActiveById(id: Long): Dish

    /**
     * Возвращает страницу блюд ресторана — как активных, так и снятых с меню.
     *
     * @param restaurantId идентификатор ресторана
     * @param pageable параметры страницы
     * @return страница блюд ресторана
     */
    fun getAllByRestaurantId(restaurantId: Long, pageable: Pageable): Page<Dish>

    /**
     * Возвращает страницу только активных блюд ресторана.
     *
     * @param restaurantId идентификатор ресторана
     * @param pageable параметры страницы
     * @return страница активных блюд ресторана
     */
    fun getAllActiveByRestaurantId(restaurantId: Long, pageable: Pageable): Page<Dish>

    /**
     * Безвозвратно удаляет блюдо из базы. Чтобы временно убрать блюдо из меню,
     * достаточно снять признак активности через [update].
     *
     * @param id идентификатор блюда
     */
    fun delete(id: Long)
}
