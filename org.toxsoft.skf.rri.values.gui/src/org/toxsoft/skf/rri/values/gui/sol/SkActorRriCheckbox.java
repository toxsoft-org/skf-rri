package org.toxsoft.skf.rri.values.gui.sol;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.ISkMnemoGuiConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;
import static org.toxsoft.skf.mnemo.lib.ISkMnemosServiceHardConstants.*;
import static org.toxsoft.skf.rri.values.gui.sol.IRriVedConstants.*;
import static org.toxsoft.skf.rri.values.gui.sol.ISkResources.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.dialogs.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * Обработчик checkbox'a для чения/записи значения булевого параметра НСИ.
 * <p>
 *
 * @author vs
 */
public class SkActorRriCheckbox
    extends AbstractSkVedButtonActor {

  /**
   * The actor factor ID.
   */
  public static final String FACTORY_ID = SKVED_ID + ".actor.RriCheckbox"; //$NON-NLS-1$

  /**
   * The VISEL factory singleton.
   */
  public static final IVedActorFactory FACTORY = new VedAbstractActorFactory( FACTORY_ID, //
      TSID_NAME, STR_ACTOR_RRI_CHECKBOX, //
      TSID_DESCRIPTION, STR_ACTOR_RRI_CHECKBOX_D, //
      TSID_ICON_ID, ICONID_VED_RRI_CHECK_ACTOR //
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo() {
      IStridablesListEdit<ITinFieldInfo> fields = new StridablesList<>();
      fields.add( TFI_IS_ACTIVE );
      fields.add( TFI_NAME );
      fields.add( TFI_DESCRIPTION );
      fields.add( TFI_VISEL_ID );
      fields.add( TFI_VISEL_PROP_ID );
      fields.add( TFI_RRI_ATTR_UGWI );
      return new PropertableEntitiesTinTypeInfo<>( fields, SkActorRriCheckbox.class );
    }

    @Override
    protected VedAbstractActor doCreate( IVedItemCfg aCfg, VedScreen aVedScreen ) {
      return new SkActorRriCheckbox( aCfg, propDefs(), aVedScreen );
    }

  };

  private Ugwi   ugwi   = null;
  private String fmtStr = null;

  private IAtomicValue lastValue = IAtomicValue.NULL;

  private ISkRriSection section = null;

  protected SkActorRriCheckbox( IVedItemCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
    IButtonClickHandler buttonHandler = aVisel -> {

      ISkVedEnvironment vedEnv = aVedScreen.tsContext().get( ISkVedEnvironment.class );
      ISkCoreApi coreApi = vedEnv.skConn().coreApi();
      if( !coreApi.userService().abilityManager().isAbilityAllowed( ABILITYID_MNEMO_EDIT_PARAMS ) ) {
        TsDialogUtils.warn( getShell(), ERR_STR_OPERATION_NOT_ALLOWED );
        return;
      }

      VedAbstractVisel visel = getVisel( props().getStr( PROPID_VISEL_ID ) );
      visel.props().setValobj( ViselButton.PROPID_STATE, EButtonViselState.WORKING );
      IAtomicValue value = getRriAttrValue();
      boolean bVal = false;
      if( value.isAssigned() ) {
        bVal = value.asBool();
      }
      setRriAttrValue( !bVal );
      if( visel.props().hasKey( PROPID_ON_OFF_STATE ) ) {
        visel.props().setBool( PROPID_ON_OFF_STATE, !bVal );
      }
    };
    setButtonClickHandler( buttonHandler );
  }

  // ------------------------------------------------------------------------------------
  // VedAbstractActor
  //

  @Override
  protected void doUpdateCachesAfterPropsChange( IOptionSet aChangedValues ) {
    if( aChangedValues.hasKey( TFI_RRI_ATTR_UGWI.id() ) ) {
      IAtomicValue av = aChangedValues.getValue( TFI_RRI_ATTR_UGWI.id() );
      if( av.isAssigned() && av.asValobj() != null && av.asValobj() != IAtomicValue.NULL
          && av.asValobj() != Ugwi.NONE ) {
        ugwi = av.asValobj();
        String sectId = UgwiKindRriAttr.getSectionId( ugwi );
        section = ((ISkRegRefInfoService)coreApi().getService( ISkRegRefInfoService.SERVICE_ID )).findSection( sectId );
      }
    }
    if( aChangedValues.hasKey( TFI_FORMAT_STRING.id() ) ) {
      fmtStr = props().getStr( TFI_FORMAT_STRING.id() );
      if( fmtStr.isBlank() ) {
        fmtStr = null;
        if( ugwi != null && ugwi != Ugwi.NONE ) {
          Gwid gwid = UgwiKindRriAttr.INSTANCE.getGwid( ugwi );
          ISkClassInfo classInfo = skSysdescr().findClassInfo( gwid.classId() );
          if( classInfo != null ) {
            IDtoAttrInfo attrInfo = classInfo.attrs().list().findByKey( gwid.propId() );
            if( attrInfo != null ) {
              IAtomicValue avFmtStr = SkHelperUtils.getConstraint( attrInfo, TSID_FORMAT_STRING );
              if( avFmtStr != null ) {
                fmtStr = avFmtStr.asString();
              }
            }
          }
        }
      }
      if( fmtStr != null && fmtStr.isBlank() ) {
        fmtStr = null;
      }
    }
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkVedActor
  //

  @Override
  protected IGwidList doListUsedGwids() {
    return IGwidList.EMPTY;
  }

  // ------------------------------------------------------------------------------------
  // IRealTimeSensitive
  //

  @Override
  public void whenRealTimePassed( long aRtTime ) {
    IAtomicValue newValue = getRriAttrValue();
    if( !newValue.equals( lastValue ) ) {
      // String text = AvUtils.printAv( fmtStr, newValue );
      setStdViselPropValue( newValue );
      lastValue = newValue;
    }
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  IAtomicValue getRriAttrValue() {
    if( ugwi == null || ugwi == Ugwi.NONE ) {
      return IAtomicValue.NULL;
    }
    Gwid gwid = UgwiKindRriAttr.INSTANCE.getGwid( ugwi );
    return section.getAttrParamValue( gwid.skid(), gwid.propId() );
  }

  void setRriAttrValue( boolean aValue ) {
    if( ugwi == null || ugwi == Ugwi.NONE ) {
      LoggerUtils.errorLogger().error( "Attempt to set RRI attribute for null or NONE Ugwi" ); //$NON-NLS-1$
      return;
    }
    Gwid gwid = UgwiKindRriAttr.INSTANCE.getGwid( ugwi );
    section.setAttrParamValue( gwid.skid(), gwid.propId(), avBool( aValue ), TsLibUtils.EMPTY_STRING );
  }

}
