package utils.db.repos

import models.entities.Entity
import play.api.db.slick.Config.driver.simple._

///**
// * Базовый класс для всех репозиториев доменных данных
// * @tparam T доменный тип
// */
//trait Repository[T <: Entity] {
//  /**
//   * Создать новый объект
//   * @param entity данные создаваемого объекта
//   * @param session сессия к БД
//   * @return id созданного объекта
//   */
//  def create(entity: T)(implicit session: Session): Int
//
//  /**
//   * Вернуть отфильтрованных пользователей
//   * @param session сессия к БД
//   * @return список пользователей, попавших под фильтр
//   */
//  def read(implicit session: Session): List[T]
//
//  /**
//   * Обновить данные пользователя
//   * @param entity обновленные данные
//   * @param session сессия к БД
//   * @return true, если объект был найден, и данные обновлены
//   */
//  def update(entity: T)(implicit session: Session): Boolean
//
//  /**
//   * Удалить пользователя
//   * @param id идентификатор удаляемого пользователя
//   * @param session сессия к БД
//   * @return true, если пользователь был найден и удален
//   */
//  def delete(id: Int)(implicit session: Session): Boolean
//}
