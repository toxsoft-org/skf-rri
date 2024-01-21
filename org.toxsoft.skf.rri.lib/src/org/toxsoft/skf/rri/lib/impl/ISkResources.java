package org.toxsoft.skf.rri.lib.impl;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
@SuppressWarnings( "nls" )
interface ISkResources {

  String FMT_ERR_CLASS_IS_REGREF_OWNED     = "Класс %s управлентся службой НСИ";
  String FMT_ERR_SECTION_ID_ALREADY_ESISTS = "Раздел с идентификатором %s уже существует";
  String FMT_ERR_INV_SECTION_ID            = "Раздел с идентификатором %s уже существует";
  String FMT_ERR_EMPTY_NAME                = "Нужно задать название раздела";
  String FMT_ERR_SECTION_ID_NOT_ESISTS     = "Раздел с идентификатором %s не существует";
  String STR_LINK_RRI_COMPANION_MASTER     = "Мастер";
  String STR_LINK_RRI_COMPANION_MASTER_D   = "Мастер-объект для которого сформировано НСИ";
  String STR_ATTR_RRI_SECTION_PARAMS       = "Параметры";
  String STR_ATTR_RRI_SECTION_PARAMS_D     = "Набор параметров раздела НСИ и их значений";
  String STR_TYPE_OPRION_SET               = "Опции";
  String STR_TYPE_OPRION_SET_D             = "Тип данных, хранящийнабор опции (идентифицированных атомарных значений";

  String STR_EVENT_RRI_EDIT       = "Правка НСИ";
  String STR_EVENT_RRI_EDIT_D     = "Событие редактирования значения параметра НСИ";
  String STR_EVPRM_REASON         = "Причина";
  String STR_EVPRM_REASON_D       = "Причина изменения параметров НСИ";
  String STR_EVPRM_AUTHOR_LOGIN   = "Автор";
  String STR_EVPRM_AUTHOR_LOGIN_D = "Автор (точнее, логин, имя входа) изменения параметров НСИ";
  String STR_EVPRM_SECTION_ID     = "Раздел";
  String STR_EVPRM_SECTION_ID_D   = "Идентификатор раздела НСИ";
  String STR_EVPRM_PARAM_ID       = "Параметр";
  String STR_EVPRM_PARAM_ID_D     = "Идентификатор параметра НСИ";
  String STR_EVPRM_IS_LINK        = "Связь?";
  String STR_EVPRM_IS_LINK_D      = "Признак, что параметр НСИ связь, а не атрибут объекта";
  String STR_EVPRM_OLD_VAL_ATTR   = "Предыдущее";
  String STR_EVPRM_OLD_VAL_ATTR_D = "Предыдущее значение параметра НСИ";
  String STR_EVPRM_OLD_VAL_LINK   = "Предыдущее";
  String STR_EVPRM_OLD_VAL_LINK_D = "Предыдущее значение параметра НСИ";
  String STR_EVPRM_NEW_VAL_ATTR   = "Новое";
  String STR_EVPRM_NEW_VAL_ATTR_D = "Новое значение параметра НСИ";
  String STR_EVPRM_NEW_VAL_LINK   = "Новое";
  String STR_EVPRM_NEW_VAL_LINK_D = "Новое значение параметра НСИ";

}
