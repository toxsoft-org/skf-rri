package org.toxsoft.skf.rri.lib;

import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.uskat.core.api.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * The RRI (Regulatory and Reference Information) service.
 * <p>
 * <b>Regulatory and Reference Information</b> - is a <i><b>RRI parameters</b></i>, bind to the Green World object.
 * <p>
 * TODO TRANSLATE
 * <p>
 * Параметр НСИ имеет <i><b>описание</b></i>, связанное с опаределенным классом системы, и <i><b>значения</b></i> для
 * каждого объекта класса. Таким образом, каждый объект системы кроме свойств своего класса (атрибуты, данные, связи,
 * команды, события) получает набор параметров НСИ. Параметры НСИ содержатся (сгруппированы) в тематических
 * <i><b>разделах</b></i>. Разбиение на разделы определяется разработчиком системы (например, по разным ИУСам, или по
 * нормативным документам).
 * <p>
 * Внимание: служба работает только с теми объектами, которые имеют уникальный строковый идентификатор
 * {@link ISkObject#strid()}. Служба <b>не</b> позволяет получить лоступ к НСИ объектов по <code>long</code>
 * идентификатору.
 *
 * @author hazard157
 */
public interface ISkRegRefInfoService
    extends ISkService {

  /**
   * The service ID.
   */
  String SERVICE_ID = ISkRriServiceHardConstants.SERVICE_ID;

  /**
   * Возвращает все разделы, существующие в системе.
   * <p>
   * Ссылки на разделы не меняются за время работы службы (если раздел не был удален).
   *
   * @return {@link IStridablesList}&lt;{@link ISkRriSection}&gt; - список всех разделов
   */
  IStridablesList<ISkRriSection> listSections();

  /**
   * Находит раздел.
   *
   * @param aSectionId String - идентификатор раздела
   * @return {@link ISkRriSection} - раздел или <code>null</code>
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   */
  ISkRriSection findSection( String aSectionId );

  /**
   * Возвращает существующий раздел.
   *
   * @param aSectionId String - идентификатор раздела
   * @return {@link ISkRriSection} - раздел
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsItemNotFoundRtException нет такого раздела
   */
  ISkRriSection getSection( String aSectionId );

  /**
   * Создает раздел НСИ.
   *
   * @param aId String - идентификатор (ИД-путь) раздела
   * @param aName String - название раздела
   * @param aDescription String - описание раздела
   * @param aParams {@link IOptionSet} - значения параметров {@link ISkRriSection#params()}
   * @return {@link ISkRriSection} - созданный раздел
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsValidationFailedRtException не прошла проверка
   *           {@link ISkRegRefInfoServiceValidator#canCreateSection(String, String, String, IOptionSet)}
   */
  ISkRriSection createSection( String aId, String aName, String aDescription, IOptionSet aParams );

  /**
   * Удаялет раздел НСИ со всеми описаниями и значениями параметров.
   *
   * @param aSectionId String - идентификатор раздела НСИ
   * @throws TsNullArgumentRtException любой аргумент = <code>null</code>
   * @throws TsValidationFailedRtException не прошла проверка
   *           {@link ISkRegRefInfoServiceValidator#canRemoveSection(String)}
   */
  void removeSection( String aSectionId );

  /**
   * Returns the history of the RRI parameter values changes.
   *
   * @return {@link ISkRriHistory} - the RRI history
   */
  ISkRriHistory history();

  /**
   * Returns the service mutator methods pre-conditions validation helper.
   *
   * @return {@link ITsValidationSupport} - service changes validation support
   */
  ITsValidationSupport<ISkRegRefInfoServiceValidator> svs();

  /**
   * Returns the service changes event firing helper.
   *
   * @return {@link ITsEventer} - event firing and listening helper
   */
  ITsEventer<ISkRegRefInfoServiceListener> eventer();

}
