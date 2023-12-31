package org.toxsoft.skf.rri.values.gui.km5;

import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Значение конкретной связи НСИ с признаком, что нет однозначной величины связи.
 *
 * @author max
 */
public class LinkParam {

  private IDtoLinkInfo linkInfo;

  private ISkidList value;

  private boolean different = false;

  /**
   * Конструктор значения конкретной связи НСИ.
   *
   * @param aLinkInfo ISkLinkInfo - описание связи.
   * @param aValue ISkidList - значение связи.
   * @param aDifferent boolean - признак того, что нет однозначной величины связи.
   */
  public LinkParam( IDtoLinkInfo aLinkInfo, ISkidList aValue, boolean aDifferent ) {
    super();
    linkInfo = aLinkInfo;
    value = aValue;
    different = aDifferent;
  }

  /**
   * Возвращает описание связи.
   *
   * @return ISkLinkInfo - описание связи.
   */
  public IDtoLinkInfo getName() {
    return linkInfo;
  }

  /**
   * Возвращает значение связи.
   *
   * @return ISkidList - значение связи.
   */
  public ISkidList getValue() {
    return value;
  }

  /**
   * Признак того, что нет однозначной величины связи.
   *
   * @return boolean true - нет однозначной величины связи, false - величина связи однозначна.
   */
  public boolean isDifferent() {
    return different;
  }

}
