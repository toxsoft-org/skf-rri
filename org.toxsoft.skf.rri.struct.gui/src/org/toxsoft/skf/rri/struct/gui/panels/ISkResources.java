package org.toxsoft.skf.rri.struct.gui.panels;

/**
 * Локализуемые ресурсы.
 *
 * @author max
 */
@SuppressWarnings( "nls" )
interface ISkResources {

  // String FMT_ASK_REMOVE_RRI_CLASS = "Дейтсвительно удалить все НСИ класса %s";
  // String MSG_NO_CLASSES_TO_ADD = "Все возможные классы уже имеют параметры НСИ";

  //
  // ----------------------------------------
  // SelectRriSectionToolbarComposite

  String STR_N_SELECT_RRI_SECTION  = Messages.getString( "STR_N_SELECT_RRI_SECTION" );// "Выбрать раздел НСИ";
  String STR_D_SELECT_RRI_SECTION  = Messages.getString( "STR_D_SELECT_RRI_SECTION" );//"Выбрать раздел НСИ для редактирования";
  String STR_NOT_SELECTED          = Messages.getString( "STR_NOT_SELECTED" );//"не выбран";
  String STR_SELECTION_RRI_SECTION = Messages.getString( "STR_SELECTION_RRI_SECTION" );// "Выбор раздела НСИ";
  String STR_RRI_SECTION           = Messages.getString( "STR_RRI_SECTION" );//"Раздел НСИ: ";

  //
  // --------------------------------------
  // PanelRriSectionStructEditor

  String IT_NEEDS_TO_CREATE_AT_LEAST_ONE_RRI_ATTR = Messages.getString( "IT_NEEDS_TO_CREATE_AT_LEAST_ONE_RRI_ATTR" );//"Необходимо создать хотя бы один атрибут НСИ для нового класса";
  String STR_RRI_ATTR_CREATION                    = Messages.getString( "STR_RRI_ATTR_CREATION" );//"Создание атрибута НСИ";
  String STR_SELECTION_CLASS_TO_ADD_INTO_RRI      = Messages.getString( "STR_SELECTION_CLASS_TO_ADD_INTO_RRI" );// "Выбор класса для НСИ";
  String STR_RRI_CLASSES                          = Messages.getString( "STR_RRI_CLASSES" );// "Классы НСИ: ";
  String STR_RRI_ATTRS                            = Messages.getString( "STR_RRI_ATTRS" );// "Атрибуты НСИ: ";
  String STR_RRI_LINKS                            = Messages.getString( "STR_RRI_LINKS" );// "Связи НСИ: ";
}
