package org.toxsoft.skf.rri.struct.skide;

/**
 * Localizable resources.
 *
 * @author max
 */
@SuppressWarnings( "javadoc" )
public interface ISkidePluginRriStructSharedResources {

  String STR_SKIDE_PLUGIN_TEMPLATE   = Messages.getString( "STR_SKIDE_PLUGIN_TEMPLATE" );   //$NON-NLS-1$
  String STR_SKIDE_PLUGIN_TEMPLATE_D = Messages.getString( "STR_SKIDE_PLUGIN_TEMPLATE_D" ); //$NON-NLS-1$
  String STR_SKIDE_TEMPLATE_UNIT_1   = Messages.getString( "STR_SKIDE_TEMPLATE_UNIT_1" );   //$NON-NLS-1$
  String STR_SKIDE_TEMPLATE_UNIT_1_D = Messages.getString( "STR_SKIDE_TEMPLATE_UNIT_1_D" ); //$NON-NLS-1$
  String STR_SKIDE_TEMPLATE_UNIT_2   = Messages.getString( "STR_SKIDE_TEMPLATE_UNIT_2" );   //$NON-NLS-1$
  String STR_SKIDE_TEMPLATE_UNIT_2_D = Messages.getString( "STR_SKIDE_TEMPLATE_UNIT_2_D" ); //$NON-NLS-1$

  String MSG_RRI_VALUES_UPLOAD        = Messages.getString( "MSG_RRI_VALUES_UPLOAD" );        //$NON-NLS-1$
  String FMT_RRI_VALUES_UPLOADED      = Messages.getString( "FMT_RRI_VALUES_UPLOADED" );      //$NON-NLS-1$
  String STR_REASON_IMPORT_FROM_SKIDE = Messages.getString( "STR_REASON_IMPORT_FROM_SKIDE" ); //$NON-NLS-1$

  String MSG_RRI_VALUES_DOWNLOAD   = "Выгрузка НСИ с сервера в SkIDE";
  String FMT_RRI_VALUES_DOWNLOADED = "Классов НСИ параметры объектов которых выгружены с сервера в SkIDE: %d";
  String STR_RRI_VALUES_DOWNLOAD   = "выгрузить с сервера в SkIDE";
  String STR_RRI_VALUES_DOWNLOAD_D = "Выгрузить с выбранного сервера в SkIDE uskat-system.textual";

  String FMT_RRI_STRUCT_UPLOADED = Messages.getString( "FMT_RRI_STRUCT_UPLOADED" ); //$NON-NLS-1$
  String MSG_RRI_STRUCT_UPLOAD   = Messages.getString( "MSG_RRI_STRUCT_UPLOAD" );   //$NON-NLS-1$
}
