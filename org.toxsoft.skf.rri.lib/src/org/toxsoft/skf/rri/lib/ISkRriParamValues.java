package org.toxsoft.skf.rri.lib;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.skf.legacy.Skop;

/**
 * Набор значений (произвольное подмножество) параметров НСИ одного раздела.
 *
 * @author hazard157
 */
public interface ISkRriParamValues {

  // ------------------------------------------------------------------------------------
  // Значения в наборе

  /**
   * Возвращает значения параметров-атрибутов.
   *
   * @return {@link IMap}&lt;{@link Skid},{@link IAtomicValue}&gt; - карта "объект/параметр" - "значение параметра"
   */
  IMap<Skop, IAtomicValue> attrParams();

  /**
   * Возвращает значения параметров-связей.
   *
   * @return {@link IMap}&lt;{@link Skid},{@link ISkidList}&gt; - карта "объект/параметр" - "ИДы связанных объектов"
   */
  IMap<Skop, ISkidList> linkParams();

  // ------------------------------------------------------------------------------------
  // Выборки из вышеприведенных данных

  /**
   * Возвращает список всех объектов, которые присутствуют в наборе.
   *
   * @return {@link ISkidList} - список идентификаторов всех объектов набора
   */
  ISkidList listObjSkids();

  /**
   * Возвращает значения тех атрибутов, которые есть в набору для запрошенного объекта.
   *
   * @param aObjId {@link Skid} - запрашиваемый объект
   * @return {@link IStringMap}&lt;{@link ISkidList}&gt; - карта "ИД атрибута" - "значение атрибута"
   */
  IStringMap<IAtomicValue> getAttrParamsOfObj( Skid aObjId );

  /**
   * Возвращает те связи, которые есть в набору для запрошенного объекта.
   *
   * @param aObjId {@link Skid} - запрашиваемый объект
   * @return {@link IStringMap}&lt;{@link ISkidList}&gt; - карта "ИД связи" - "связанные объекты"
   */
  IStringMap<ISkidList> getLinkParamsOfObj( Skid aObjId );

}
