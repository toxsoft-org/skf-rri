package org.toxsoft.skf.rri.values.gui;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.impl.DataDef.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;

/**
 * Описание настроечных констант редактора НСИ.
 *
 * @author max
 */
public interface IRegRefInfoConstants {

  /**
   * Описание параметра редактируемости НСИ параметров для графика.
   */
  IDataDef REG_REF_INFO_EDITABLE = create( "reg.ref.info.editable", BOOLEAN, //
      TSID_NAME, "RegRefInfoEditable", //
      TSID_DESCRIPTION, "Reg ref info editable", //
      TSID_DEFAULT_VALUE, AV_TRUE, //
      TSID_IS_MANDATORY, AV_FALSE );

  /**
   * Описание параметра возможности печати НСИ параметров для графика.
   */
  IDataDef REG_REF_INFO_PRINTABLE = create( "reg.ref.info.printable", BOOLEAN, //
      TSID_NAME, "RegRefInfoPrintable", //
      TSID_DESCRIPTION, "Reg ref info printable", //
      TSID_DEFAULT_VALUE, AV_TRUE, //
      TSID_IS_MANDATORY, AV_FALSE );

  /**
   * Идентификатор раздела НСИ.
   */
  IDataDef REG_REF_INFO_DEFAULT_SECTION_ID = create( "rri.default.section.id", STRING, //
      TSID_NAME, "RegRefInfoDefaultSection", //
      TSID_DESCRIPTION, "Reg ref info default section identifier", //
      TSID_DEFAULT_VALUE, AvUtils.AV_STR_EMPTY, //
      TSID_IS_MANDATORY, AV_FALSE );

  /**
   * Идентификатор связи НСИ в системном описании.
   */
  IDataDef REG_REF_INFO_OBJS_TREE_LINK_ID = create( "rri.objs.tree.link.id", STRING, //
      TSID_NAME, "RegRefInfoObjsTreeLinkId", //
      TSID_DESCRIPTION, "Reg ref info objects tree link identifier", //
      TSID_DEFAULT_VALUE, avStr( "rri" ), //
      TSID_IS_MANDATORY, AV_FALSE );

  /**
   * Идентификатор топового (корневого) объекта НСИ (например линиия или расписание).
   */
  IDataDef RRI_ROOT_OBJECT_ID = create( "rri.root.object.id", STRING, //
      TSID_NAME, "RegRefInfoRootObjectId", //
      TSID_DESCRIPTION, "Reg ref info root object  identifier", //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL, //
      TSID_IS_MANDATORY, AV_FALSE );

  /**
   * Идентификатор класса топового (корневого) объекта НСИ (например линиия или расписание).
   */
  IDataDef RRI_ROOT_CLASS_ID = create( "rri.root.class.id", STRING, //
      TSID_NAME, "RegRefInfoRootClassId", //
      TSID_DESCRIPTION, "Reg ref info root class  identifier", //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL, //
      TSID_IS_MANDATORY, AV_FALSE );

  /**
   * Топовый (корневой) объект НСИ (например линиия или расписание).
   */
  String RRI_CLASSES_OBJECTS_TREE_MAKER = "rri.classes.objects.tree.maker";

}
