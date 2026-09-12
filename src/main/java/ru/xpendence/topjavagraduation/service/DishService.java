package ru.xpendence.topjavagraduation.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.xpendence.topjavagraduation.entity.Dish;

/**
 * Операции над блюдами ресторанов.
 */
public interface DishService {

    /**
     * Сохраняет новое блюдо.
     *
     * @param dish новое блюдо
     * @return сохранённое блюдо с присвоенным идентификатором
     */
    Dish create(Dish dish);

    /**
     * Обновляет название, цену и признак активности существующего блюда.
     * Остальные поля, включая привязку к ресторану, остаются прежними.
     *
     * @param dish блюдо с заполненным идентификатором и новыми значениями полей
     * @throws IllegalArgumentException если идентификатор не задан
     * @throws java.util.NoSuchElementException если блюдо с таким идентификатором не найдено
     */
    void update(Dish dish);

    /**
     * Снимает с меню все блюда ресторана, проставляя им признак неактивности.
     * Блюда не удаляются и могут быть возвращены в меню повторно.
     *
     * @param restaurantId идентификатор ресторана
     * @throws java.util.NoSuchElementException если у ресторана нет ни одного блюда
     */
    void resetMenu(Long restaurantId);

    /**
     * Возвращает блюдо по идентификатору.
     *
     * @param id идентификатор блюда
     * @return найденное блюдо
     * @throws java.util.NoSuchElementException если блюдо не найдено
     */
    Dish getById(Long id);

    /**
     * Возвращает активное блюдо по идентификатору.
     *
     * @param id идентификатор блюда
     * @return найденное активное блюдо
     * @throws java.util.NoSuchElementException если блюдо не найдено или неактивно
     */
    Dish getActiveById(Long id);

    /**
     * Возвращает страницу блюд ресторана — как активных, так и снятых с меню.
     *
     * @param restaurantId идентификатор ресторана
     * @param pageable     параметры страницы
     * @return страница блюд ресторана
     */
    Page<Dish> getAllByRestaurantId(Long restaurantId, Pageable pageable);

    /**
     * Возвращает страницу только активных блюд ресторана.
     *
     * @param restaurantId идентификатор ресторана
     * @param pageable     параметры страницы
     * @return страница активных блюд ресторана
     */
    Page<Dish> getAllActiveByRestaurantId(Long restaurantId, Pageable pageable);

    /**
     * Безвозвратно удаляет блюдо из базы. Чтобы временно убрать блюдо из меню,
     * достаточно снять признак активности через {@link #update(Dish)}.
     *
     * @param id идентификатор блюда
     */
    void delete(Long id);
}
