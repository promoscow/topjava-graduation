package ru.xpendence.topjavagraduation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.xpendence.topjavagraduation.entity.Restaurant;

/**
 * Операции над ресторанами.
 */
public interface RestaurantService {

    /**
     * Сохраняет новый ресторан.
     *
     * @param restaurant новый ресторан
     * @return сохранённый ресторан с присвоенным идентификатором
     */
    Restaurant create(Restaurant restaurant);

    /**
     * Обновляет существующий ресторан.
     *
     * @param restaurant ресторан с заполненным идентификатором и новыми значениями полей
     * @throws IllegalArgumentException если идентификатор не задан
     * @throws java.util.NoSuchElementException если ресторан с таким идентификатором не найден
     */
    void update(Restaurant restaurant);

    /**
     * Возвращает ресторан по идентификатору.
     *
     * @param id идентификатор ресторана
     * @return найденный ресторан
     * @throws java.util.NoSuchElementException если ресторан не найден
     */
    Restaurant getById(Long id);

    /**
     * Возвращает ресторан, к которому привязано блюдо.
     *
     * @param dishId идентификатор блюда
     * @return найденный ресторан
     * @throws java.util.NoSuchElementException если ресторан по блюду не найден
     */
    Restaurant getByDishId(Long dishId);

    /**
     * Возвращает страницу всех ресторанов.
     *
     * @param pageable параметры страницы
     * @return страница ресторанов
     */
    Page<Restaurant> getAll(Pageable pageable);

    /**
     * Возвращает ресторан, набравший наибольшее число голосов на текущую дату.
     *
     * @return выбранный ресторан дня
     * @throws java.util.NoSuchElementException если на сегодня нет ни одного голоса
     */
    Restaurant getChosen();

    /**
     * Безвозвратно удаляет ресторан из базы.
     *
     * @param id идентификатор ресторана
     */
    void delete(Long id);
}
