package org.toxsoft.skf.rri.lib.impl;

import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * {@link IDtoRriParamInfo} implementation.
 *
 * @author mvk
 */
public class DtoRriParamInfo
    extends StridableParameterizedSer
    implements IDtoRriParamInfo {

  private static final long serialVersionUID = 4997500209361567648L;

  private final IDtoAttrInfo attrInfo;
  private final IDtoLinkInfo linkInfo;

  /**
   * Constructor for the attribute parameter.
   *
   * @param aAttrInfo {@link IDtoAttrInfo} - attribute description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public DtoRriParamInfo( IDtoAttrInfo aAttrInfo ) {
    super( aAttrInfo.id(), aAttrInfo.params() );
    attrInfo = TsNullArgumentRtException.checkNull( aAttrInfo );
    linkInfo = null;
  }

  /**
   * Constructor for the link parameter.
   *
   * @param aLinkInfo {@link IDtoLinkInfo} - the link description
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public DtoRriParamInfo( IDtoLinkInfo aLinkInfo ) {
    super( aLinkInfo.id(), aLinkInfo.params() );
    attrInfo = null;
    linkInfo = TsNullArgumentRtException.checkNull( aLinkInfo );
  }

  // ------------------------------------------------------------------------------------
  // IDtoRriParamInfo
  //

  @Override
  public boolean isLink() {
    return (linkInfo != null);
  }

  @Override
  public IDtoAttrInfo attrInfo() {
    TsUnsupportedFeatureRtException.checkTrue( isLink() );
    return attrInfo;
  }

  @Override
  public IDtoLinkInfo linkInfo() {
    TsUnsupportedFeatureRtException.checkFalse( isLink() );
    return linkInfo;
  }

}
