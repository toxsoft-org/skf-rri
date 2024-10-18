package org.toxsoft.skf.rri.values.gui.utils;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.skf.rri.values.gui.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Утилитный класс, предназначенный для выделения из кода некарсивых частей, которые следует заменить универсальными
 * решениями.
 *
 * @author max
 */
public class RriUtils {

  /**
   * Проводит поиск подходящего для параметра редактора (фабрики редактора)
   *
   * @param aAttrInfo IAttrInfo - описание параметра
   * @return IValedControlFactory - фабрика редактора параметра.
   */
  public static IValedControlFactory searchValedControlFactory( IDtoAttrInfo aAttrInfo ) {
    IValedControlFactory result = ValedControlUtils.getDefaultFactory( aAttrInfo.dataType().atomicType() );
    // TODO - регистрировать заранее и
    // изменить формат в скрипте
    if( aAttrInfo.params().hasValue( "timeFormat" ) ) { // Dima, 03.06.19
      if( aAttrInfo.params().getStr( "timeFormat" ).equals( "HH_mm_ss" ) ) {
        // return ValedAvIntHhmmss.FACTORY;
        return ValedDurationText.FACTORY;
      }
      if( aAttrInfo.params().getStr( "timeFormat" ).equals( "mm_ss" ) ) {
        return ValedDurationText.FACTORY;
      }
    }
    return result;
  }

  /**
   * Возвращает идентификатор текущего раздела НСИ
   *
   * @param aContext ITsGuiContext - контекст.
   * @return String - идентификатор раздела НСИ или пустая строка, если идентификатор текущего раздела НСИ не найден.
   */
  public static String getRriSectionId( ITsGuiContext aContext ) {
    Object result = aContext.find( IRegRefInfoConstants.REG_REF_INFO_DEFAULT_SECTION_ID.id() );
    return result != null ? result.toString() : TsLibUtils.EMPTY_STRING;
  }

  /**
   * Проверяет наличие прав на редактирование
   *
   * @param aContext ITsGuiContext - контекст.
   * @return true - права на редактирование есть, false - прав нет.
   */
  public static boolean canEdit( ITsGuiContext aContext ) {
    if( aContext.hasKey( IRegRefInfoConstants.REG_REF_INFO_EDITABLE.id() ) ) {
      return ((IAtomicValue)aContext.get( IRegRefInfoConstants.REG_REF_INFO_EDITABLE.id() )).asBool();
    }
    return true;
  }

  /**
   * Проверяет наличие прав на печать
   *
   * @param aContext ITsGuiContext - контекст.
   * @return true - права на печать есть, false - прав нет.
   */
  public static boolean canPrint( ITsGuiContext aContext ) {
    if( aContext.hasKey( IRegRefInfoConstants.REG_REF_INFO_PRINTABLE.id() ) ) {
      return ((IAtomicValue)aContext.get( IRegRefInfoConstants.REG_REF_INFO_PRINTABLE.id() )).asBool();
    }
    return false;
  }
}
