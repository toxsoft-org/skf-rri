package org.toxsoft.skf.rri.values.gui.km5;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Значение конкретного параметра НСИ с признаком, что нет однозначной величины параметра.
 *
 * @author max
 */
public class AttrParam {

  private IDtoAttrInfo attrInfo;

  private IAtomicValue attrValue;

  private boolean different = false;

  /**
   * Конструктор значения конкретного параметра НСИ.
   *
   * @param aAttrInfo ISkAttrInfo - описание параметра.
   * @param aAttrValue IAtomicValue - значение параметра.
   * @param aDifferent boolean - признак того, что нет однозначной величины параметра.
   */
  public AttrParam( IDtoAttrInfo aAttrInfo, IAtomicValue aAttrValue, boolean aDifferent ) {
    super();
    attrInfo = aAttrInfo;
    attrValue = aAttrValue;
    different = aDifferent;
  }

  /**
   * Возвращает значение параметра.
   *
   * @return IAtomicValue - значение параметра.
   */
  public IAtomicValue getAttrValue() {
    return attrValue;
  }

  /**
   * Возвращает описание параметра.
   *
   * @return ISkAttrInfo - описание параметра.
   */
  public IDtoAttrInfo getAttrInfo() {
    return attrInfo;
  }

  /**
   * Признак того, что нет однозначной величины параметра.
   *
   * @return boolean true - нет однозначной величины параметра, false - величина параметра однозначна.
   */
  public boolean isDifferent() {
    return different;
  }

}
