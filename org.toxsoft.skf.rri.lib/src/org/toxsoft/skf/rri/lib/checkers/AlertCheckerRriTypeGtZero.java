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
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.math.cond.*;
import org.toxsoft.core.tslib.math.cond.checker.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.alarms.lib.checkers.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.ugwis.*;

/**
 * Alert checker type: checks that specified RRI attribute value is > 0.
 * <p>
 * {@link ITsSingleCondType} implementation of condition formula: <b><i>RRI value</i></b> {@link Ugwi} == true.
 *
 * @author dima
 */
public class AlertCheckerRriTypeGtZero
    extends AbstractTsSingleCheckerType<ISkCoreApi> {

  /**
   * The type ID.
   */
  public static final String TYPE_ID = USKAT_FULL_ID + ".alert_checker.Rri"; //$NON-NLS-1$

  /**
   * {@link ITsSingleCondInfo#params()} option: The Ugwi of the RRI attribute.<br>
   * Type: {@link Ugwi}
   */
  public static final IDataDef OPDEF_RRI_UGWI = DataDef.create( "RriUgwi", VALOBJ, //$NON-NLS-1$
      TSID_NAME, STR_RTDVC_RRI_GWID, //
      TSID_DESCRIPTION, STR_RTDVC_RRI_GWID_D, //
      TSID_KEEPER_ID, Ugwi.KEEPER_ID, //
      TSLIB_VCC_EDITOR_FACTORY_NAME, SKCGC_VALED_AV_UGWI_SELECTOR_TEXT_AND_BUTTON, //
      SKCGC_VALED_UGWI_SELECTOR_OPID_SINGLE_UGWI_KIND_ID, avStr( UgwiKindRriAttr.KIND_ID ), //
      TSID_DEFAULT_VALUE, avValobj( UgwiKindRriAttr.makeUgwi( NONE_ID, Gwid.NONE_CONCR_ATTR ) ) );

  /**
   * Implementation of {@link AbstractTsSingleChecker} created in by this type.
   *
   * @author dima
   */
  static class Checker
      extends AbstractAlertChecker {

    private final Ugwi        ugwi;
    private final ISkUgwiKind ugwiKind;

    public Checker( ISkCoreApi aEnviron, IOptionSet aParams ) {
      super( aEnviron, aParams );
      ugwi = params().getValobj( AlertCheckerRriTypeGtZero.OPDEF_RRI_UGWI );
      ugwiKind = coreApi().ugwiService().listKinds().getByKey( ugwi.kindId() );
    }

    protected IAtomicValue doGetXxxValue() {
      return ugwiKind.getAtomicValue( ugwi );
    }

    // ------------------------------------------------------------------------------------
    // AbstractAlertChecker
    //

    @Override
    public boolean checkCondition() {
      boolean retVal = false;
      IAtomicValue val = doGetXxxValue();
      retVal = switch( val.atomicType() ) {
        case BOOLEAN -> val.asBool();
        case FLOATING -> val.asFloat() > 0;
        case INTEGER -> val.asInt() > 0;
        case NONE -> false; // none mean unknown value
        case STRING, TIMESTAMP, VALOBJ -> throw new TsIllegalArgumentRtException( FMT_ERR_INVALID_RRI_ATTR_TYPE,
            val.atomicType().name() );
      };
      return retVal;
    }

    @Override
    public void close() {
      // nop
    }

  }

  /**
   * Constructor.
   */
  public AlertCheckerRriTypeGtZero() {
    super( TYPE_ID, //
        OptionSetUtils.createOpSet( //
            TSID_NAME, STR_RRI_GT_ZERO, //
            TSID_DESCRIPTION, STR_RRI_GT_ZERO_D //
        ), //
        new StridablesList<>( //
            OPDEF_RRI_UGWI //
        ) );
  }

  @Override
  protected AbstractTsSingleChecker<ISkCoreApi> doCreateChecker( ISkCoreApi aEnviron, IOptionSet aParams ) {
    return new Checker( aEnviron, aParams );
  }

}
