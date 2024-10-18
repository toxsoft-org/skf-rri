package org.toxsoft.skf.rri.struct.gui.km5;

/**
 * Локализуемые ресурсы.
 *
 * @author max
 */
@SuppressWarnings( "nls" )
interface ISkResources {

  //
  // ----------------------------------------
  // AttributeModel

  String STR_N_ATTR_ID          = Messages.getString( "STR_N_ATTR_ID" );         // "Идентификатор";
  String STR_D_ATTR_ID          = Messages.getString( "STR_D_ATTR_ID" );         // "Идентификатор атрибута";
  String STR_D_ATTR_TYPE        = Messages.getString( "STR_D_ATTR_TYPE" );       // "Тип атрибута";
  String STR_N_ATTR_TYPE        = Messages.getString( "STR_N_ATTR_TYPE" );       // "Тип";
  String STR_N_ATTR_NAME        = Messages.getString( "STR_N_ATTR_NAME" );       // "Наименование";
  String STR_D_ATTR_NAME        = Messages.getString( "STR_D_ATTR_NAME" );       // "Наименование атрибута";
  String STR_D_ATTR_DESCRIPTION = Messages.getString( "STR_D_ATTR_DESCRIPTION" );// "Описание атрибута";
  String STR_N_ATTR_DESCRIPTION = Messages.getString( "STR_N_ATTR_DESCRIPTION" );// "Описание";
  String STR_N_ATTRIBUTES_LIST  = Messages.getString( "STR_N_ATTRIBUTES_LIST" ); // "Список атрибутов";
  String STR_D_ATTRIBUTES_LIST  = Messages.getString( "STR_D_ATTRIBUTES_LIST" ); // "Список НСИ атрибутов класса";

  //
  // ----------------------------------------
  // LinkModel

  String STR_N_LINKS_LIST          = Messages.getString( "STR_N_LINKS_LIST" );         // "Список связей";
  String STR_D_LINKS_LIST          = Messages.getString( "STR_D_LINKS_LIST" );         // "Список НСИ связей класса";
  String STR_N_LINK_CLASS_IDS      = Messages.getString( "STR_N_LINK_CLASS_IDS" );     // "Классы связи";
  String STR_D_LINK_CLASS_IDS      = Messages.getString( "STR_D_LINK_CLASS_IDS" );     // "Возможные классы связи";
  String STR_N_LINK_IS_EXACT_COUNT = Messages.getString( "STR_N_LINK_IS_EXACT_COUNT" );// "Кол-во связей точное";
  String STR_D_LINK_IS_EXACT_COUNT = Messages.getString( "STR_D_LINK_IS_EXACT_COUNT" );// "Кол-во связей точное";
  String STR_N_LINK_MAX_COUNT      = Messages.getString( "STR_N_LINK_MAX_COUNT" );     // "Макс. кол-во связей";
  String STR_D_LINK_MAX_COUNT      = Messages.getString( "STR_D_LINK_MAX_COUNT" );     // "Максимальное кол-во связей";
  String STR_D_LINK_DESCRIPTION    = Messages.getString( "STR_D_LINK_DESCRIPTION" );   // "Описание связи";
  String STR_N_LINK_DESCRIPTION    = Messages.getString( "STR_N_LINK_DESCRIPTION" );   // "Описание";
  String STR_N_LINK_NAME           = Messages.getString( "STR_N_LINK_NAME" );          // "Наименование";
  String STR_D_LINK_NAME           = Messages.getString( "STR_D_LINK_NAME" );          // "Наименование связи";
  String STR_D_LINK_ID             = Messages.getString( "STR_D_LINK_ID" );            // "Идентификатор связи";
  String STR_N_LINK_ID             = Messages.getString( "STR_N_LINK_ID" );            // "Идентификатор";

  //
  // --------------------------------------
  // RriSectionModel

  String STR_N_RRI_SECTIONS_LIST = Messages.getString( "STR_N_RRI_SECTIONS_LIST" );// "RRI sections: ";
  String STR_D_RRI_SECTIONS_LIST = Messages.getString( "STR_D_RRI_SECTIONS_LIST" );// "Reg Ref Info sections: ";
}
