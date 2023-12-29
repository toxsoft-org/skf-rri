package org.toxsoft.skf.rri.lib;

import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.uskat.core.api.evserv.*;

/**
 * {@link ISkRegRefInfoService} changes listener.
 *
 * @author hazard157
 */
public interface ISkRriSectionListener {

  /**
   * Извещение об изменений свойств раздела.
   * <p>
   * К изменениям свойст относится изменение имени {@link ISkRriSection#nmName()}, описания
   * {@link ISkRriSection#description()} или любого параметра {@link ISkRriSection#params()} раздела.
   *
   * @param aSource {@link ISkRriSection} - раздел, источник сообщения
   */
  void onSectionPropsChanged( ISkRriSection aSource );

  /**
   * Извещение об изменении описания параметры НСИ указанного класса.
   * <p>
   * Обратите внимание, что изменение параметров НСИ класса приводит к изменениям во всех наследниках. Поэтому,
   * слушатели должны провреять, является ли измененный класс родителем отслеживаемого класса.
   *
   * @param aSource {@link ISkRriSection} - раздел, источник сообщения
   * @param aClassId String - класс, у которого изменился состав или описания параметров НСИ
   */
  void onClassParamInfosChanged( ISkRriSection aSource, String aClassId );

  /**
   * Извещает об изменении значений параметров НСИ.
   *
   * @param aSource {@link ISkRriSection} - раздел, источник сообщения
   * @param aEvents {@link IList}&lt;{@link SkEvent}&gt; - события изменения НСИ, параметры события:<br>
   *          <ul>
   *          <li>EVPRMID_SECTION_ID - String in format of strid</li>
   *          <li>EVPRMID_AUTHOR_LOGIN - String</li>
   *          <li>EVPRMID_PARAM_GWID - Gwid</li>
   *          <li>EVPRMID_REASON - String</li>
   *          <li>EVPRMID_IS_LINK - Boolean</li>
   *          <li>EVPRMID_OLD_VAL_ATTR - AtomicValue</li>
   *          <li>EVPRMID_NEW_VAL_ATTR</li>
   *          </ul>
   *          see SkRriSection::fireEventAttrChange()
   */
  void onParamValuesChanged( ISkRriSection aSource, IList<SkEvent> aEvents );

}
