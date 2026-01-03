package org.toxsoft.skf.rri.lib.checkers;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.utils.gui.ITsLibInnerSharedConstants.*;
import static org.toxsoft.skf.rri.lib.checkers.ISkResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;
import static org.toxsoft.uskat.core.inner.ISkCoreGuiInnerSharedConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.math.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.math.cond.checker.*;
import org.toxsoft.skf.alarms.lib.checkers.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.ugwis.*;

/**
 * Alert checker type: compares specified RTdata current value to the RRI attribute value constant.
 * <p>
 * {@link ITsSingleCondType} implementation of condition formula: <i><b> RtGwid </i></b> {@link Gwid} <b><i>OP</i></b>
 * {@link EAvCompareOp} <b><i>RRI value</i></b> {@link Ugwi}.
 *
 * @author dima
 */
public class AlertCheckerRtdataVsRriType
    extends AlertCheckerRtdataVsXxxBaseType {

  /**
   * The type ID.
   */
  public static final String TYPE_ID = USKAT_FULL_ID + ".alert_checker.RtdataVsRri"; //$NON-NLS-1$

  /**
   * {@link ITsSingleCondInfo#params()} option: The Ugwi of the RRI attribute.<br>
   * Type: {@link Ugwi}
   */
  public static final IDataDef OPDEF_RRI_UGWI = DataDef.create( "RriUgwi", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_RTDVC_RRI_GWID, //
      TSID_DESCRIPTION, STR_RTDVC_RRI_GWID_D, //
      TSID_KEEPER_ID, Ugwi.KEEPER_ID, //
      TSLIB_VCC_EDITOR_FACTORY_NAME, SKCGC_VALED_AV_VALOBJ_UGWI_SELECTOR_TEXT_AND_BUTTON, //
      SKCGC_VALED_UGWI_SELECTOR_OPID_SINGLE_UGWI_KIND_ID, avStr( UgwiKindRriAttr.KIND_ID ), //
      TSID_DEFAULT_VALUE, avValobj( UgwiKindRriAttr.makeUgwi( NONE_ID, Gwid.NONE_CONCR_ATTR ) ) );

  /**
   * Implementation of {@link AbstractTsSingleChecker} created in by this type.
   *
   * @author dima
   */
  static class Checker
      extends AbstractChecker {

    private final Ugwi        ugwi;
    private final ISkUgwiKind ugwiKind;

    public Checker( ISkCoreApi aEnviron, IOptionSet aParams ) {
      super( aEnviron, aParams );
      ugwi = params().getValobj( AlertCheckerRriTypeGtZero.OPDEF_RRI_UGWI );
      ugwiKind = coreApi().ugwiService().listKinds().getByKey( ugwi.kindId() );
    }

    @Override
    protected IAtomicValue doGetXxxValue() {
      return ugwiKind.getAtomicValue( ugwi );
    }

  }

  /**
   * Constructor.
   */
  public AlertCheckerRtdataVsRriType() {
    super( TYPE_ID, //
        OptionSetUtils.createOpSet( //
            TSID_NAME, STR_RTD_VC_RRI, //
            TSID_DESCRIPTION, STR_RTD_VC_RRI_D //
        ), //
        new StridablesList<>( //
            OPDEF_RTDATA_GWID, //
            OPDEF_COMPARE_OP, //
            OPDEF_RRI_UGWI //
        ) );
  }

  // ------------------------------------------------------------------------------------
  // AlertCheckerRtdataVsXxxBaseType
  //

  @Override
  protected AbstractTsSingleChecker<ISkCoreApi> doCreateChecker( ISkCoreApi aEnviron, IOptionSet aParams ) {
    return new Checker( aEnviron, aParams );
  }

  @Override
  protected IAtomicValue getXxxValue( IOptionSet aCondParams ) {
    Gwid attrGwid = aCondParams.getValobj( AlertCheckerRtdataVsRriType.OPDEF_RRI_UGWI );
    return AvUtils.avStr( attrGwid.canonicalString() );
  }

}
