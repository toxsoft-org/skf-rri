package org.toxsoft.skf.rri.values.gui.sol;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.uskat.core.*;

/**
 * Набор вспомогательных методов для работы с sk-сущностями в Ved'e.
 * <p>
 *
 * @author vs
 */
public class SkVedUtils {

  /**
   * Возвращает Ugwi для атрибута НСИ по ИДу из набора значений.<br>
   *
   * @param aParamId String - ИД значения
   * @param aValues {@link IOptionSet} - набор значений
   * @return {@link Ugwi} - Ugwi для атрибута НСИ
   */
  public static Ugwi getRriAttributeUgwi( String aParamId, IOptionSet aValues ) {
    if( aValues.hasKey( aParamId ) ) {
      IAtomicValue av = aValues.getValue( aParamId );
      if( av.isAssigned() && av.asValobj() != null && av.asValobj() != IAtomicValue.NULL ) {
        return av.asValobj();
      }
    }
    return null;
  }

  public static IAtomicValue getRriValue( Ugwi aUgwi, ISkCoreApi aCoreApi ) {
    IAtomicValue av = null;
    String sectId = UgwiKindRriAttr.getSectionId( aUgwi );
    ISkRriSection section;
    section = ((ISkRegRefInfoService)aCoreApi.getService( ISkRegRefInfoService.SERVICE_ID )).findSection( sectId );
    Gwid gwid = UgwiKindRriAttr.INSTANCE.getGwid( aUgwi );
    av = section.getAttrParamValue( gwid.skid(), gwid.propId() );
    return av;
  }

  private SkVedUtils() {
    // запрет на создание экземпляров
  }
}
