package org.toxsoft.skf.rri.lib.checkers;

/**
 * Localizable resources.
 *
 * @author hazard157
 */
interface ISkResources {

  /**
   * {@link AlertCheckerRtdataVsRriType}
   */
  String STR_RTD_VC_RRI       = Messages.getString( "STR_RTD_VC_RRI" );       //$NON-NLS-1$
  String STR_RTD_VC_RRI_D     = Messages.getString( "STR_RTD_VC_RRI_D" );     //$NON-NLS-1$
  String STR_RTDVC_RRI_GWID   = Messages.getString( "STR_RTDVC_RRI_GWID" );   //$NON-NLS-1$
  String STR_RTDVC_RRI_GWID_D = Messages.getString( "STR_RTDVC_RRI_GWID_D" ); //$NON-NLS-1$

  /**
   * {@link AlertCheckerRriTypeGtZero}
   */
  String STR_RRI_GT_ZERO   = "RRI value > 0";//$NON-NLS-1$
  String STR_RRI_GT_ZERO_D = "RRI value > 0";//$NON-NLS-1$

  // ------------------------------------------------------------------------------------
  // Log messages, no need to localize

  String FMT_WARN_CANT_FIND_RRI        = "Can find RRI attribute for GWID '%s'"; //$NON-NLS-1$
  String FMT_ERR_INVALID_RRI_ATTR_TYPE = "Invalid type for that checker '%s'";   //$NON-NLS-1$

}
