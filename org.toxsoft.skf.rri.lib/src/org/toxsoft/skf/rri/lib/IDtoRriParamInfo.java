package org.toxsoft.skf.rri.lib;

import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;

/**
 * Information about RRI parameter.
 * <p>
 * RRI parameter may be:
 * <ul>
 * <li>attribute - is created based on {@link IDtoAttrInfo}, use {@link #attrInfo()} to get RRI parameter info;</li>
 * <li>link - is created based on {@link IDtoLinkInfo}, use {@link #linkInfo()} to get RRI parameter info.</li>
 * </ul>
 * This interface extends {@link IStridableParameterized} returning the same values as {@link #attrInfo()} or
 * {@link #linkInfo()} depending on the value returned by {@link #isLink()}.
 *
 * @author hazard157
 */
public interface IDtoRriParamInfo
    extends IStridableParameterized {

  /**
   * Determines if this RRI parameter is either an attribute or a link.
   *
   * @return boolean - sign of the link<br>
   *         <b>true</b> - parameter is an attribute, use {@link #attrInfo()} to get RRI parameter info;<br>
   *         <b>false</b> - parameter is a link, use {@link #linkInfo()} to get RRI parameter info.
   */
  boolean isLink();

  /**
   * Returns information about attribute parameter.
   *
   * @return {@link IDtoAttrInfo} - attribute description
   * @throws TsUnsupportedFeatureRtException {@link #isLink()} = <code>true</code>
   */
  IDtoAttrInfo attrInfo();

  /**
   * Returns information about link parameter.
   *
   * @return {@link IDtoLinkInfo} - link description
   * @throws TsUnsupportedFeatureRtException {@link #isLink()} = <code>false</code>
   */
  IDtoLinkInfo linkInfo();

}
