package org.toxsoft.skf.rri.values.gui.sol;

import static org.toxsoft.core.tsgui.ved.screen.IVedScreenConstants.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;
import static org.toxsoft.skf.mnemo.mned.lite.ISkfMnemMnedLiteConstants.*;
import static org.toxsoft.skf.rri.values.gui.IRegRefInfoConstants.*;
import static org.toxsoft.skf.rri.values.gui.sol.IRriVedConstants.*;
import static org.toxsoft.skf.rri.values.gui.sol.ISkResources.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.ved.comps.*;
import org.toxsoft.core.tsgui.ved.screen.cfg.*;
import org.toxsoft.core.tsgui.ved.screen.impl.*;
import org.toxsoft.core.tsgui.ved.screen.items.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.skf.mnemo.gui.skved.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.*;
import org.toxsoft.skf.mnemo.mned.lite.rtc.impl.*;

/**
 * Однострочный текст, отображающий значение атрибута НСИ.
 *
 * @author vs
 */
public class RtcRriAttrValueView
    extends AbstractRtControl {

  /**
   * The RtControl factory ID.
   */
  public static final String FACTORY_ID = MNED_LITE + ".rtc.RriAttrValueView"; //$NON-NLS-1$

  /**
   * The IRtControl factory singleton.
   */
  public static final IRtControlFactory FACTORY = new AbstractRtControlFactory( FACTORY_ID, //
      TSID_NAME, STR_RTC_RRI_ATTR_VALUE_VIEW, //
      TSID_DESCRIPTION, STR_RTC_RRI_ATTR_VALUE_VIEW_D, //
      TSID_ICON_ID, ICONID_RTC_RRI_VALUE_VIEW, //
      PARAMID_CATEGORY, CATID_TEXT_VIEWS//
  ) {

    @Override
    protected ITinTypeInfo doCreateTypeInfo( IStridablesListEdit<ITinFieldInfo> aFields ) {
      aFields.add( TFI_RRI_ATTR_UGWI );
      aFields.add( TFI_FORMAT_STRING );

      aFields.add( TFI_TEXT );
      aFields.add( TFI_FONT );
      aFields.add( TFI_SWT_FG_COLOR );
      aFields.add( TFI_SWT_BK_FILL );
      aFields.add( TFI_SWT_BORDER_INFO );

      aFields.add( TFI_HOR_ALIGNMENT );
      aFields.add( TFI_VER_ALIGNMENT );

      aFields.add( TFI_LEFT_INDENT );
      aFields.add( TFI_TOP_INDENT );
      aFields.add( TFI_RIGHT_INDENT );
      aFields.add( TFI_BOTTOM_INDENT );

      aFields.add( TFI_X );
      aFields.add( TFI_Y );
      aFields.add( TFI_WIDTH );
      aFields.add( TFI_HEIGHT );
      return new PropertableEntitiesTinTypeInfo<>( aFields, RtcRriAttrValueView.class );
    }

    @Override
    protected void bindViselProps() {
      bindViselPropId( TFI_TEXT.id(), TFI_TEXT.id() );
      bindViselPropId( TFI_FONT.id(), TFI_FONT.id() );
      bindViselPropId( TFI_FG_COLOR.id(), TFI_FG_COLOR.id() );
      bindViselPropId( TFI_BK_FILL.id(), TFI_BK_FILL.id() );
      bindViselPropId( TFI_BORDER_INFO.id(), TFI_BORDER_INFO.id() );
      bindViselPropId( TFI_HOR_ALIGNMENT.id(), TFI_HOR_ALIGNMENT.id() );
      bindViselPropId( TFI_VER_ALIGNMENT.id(), TFI_VER_ALIGNMENT.id() );
      bindViselPropId( TFI_LEFT_INDENT.id(), TFI_LEFT_INDENT.id() );
      bindViselPropId( TFI_TOP_INDENT.id(), TFI_TOP_INDENT.id() );
      bindViselPropId( TFI_RIGHT_INDENT.id(), TFI_RIGHT_INDENT.id() );
      bindViselPropId( TFI_BOTTOM_INDENT.id(), TFI_BOTTOM_INDENT.id() );
      bindViselPropId( TFI_X.id(), TFI_X.id() );
      bindViselPropId( TFI_Y.id(), TFI_Y.id() );
      bindViselPropId( TFI_WIDTH.id(), TFI_WIDTH.id() );
      bindViselPropId( TFI_HEIGHT.id(), TFI_HEIGHT.id() );
    }

    @Override
    protected IRtControl doCreate( IRtControlCfg aCfg, VedScreen aVedScreen ) {
      VedAbstractVisel v = null;
      VedAbstractActor actor = null;

      if( aCfg.viselId().isBlank() ) { // создание с нуля
        // IVedViselFactory f = viselFactory( ViselLabel.FACTORY_ID, aVedScreen );
        // VedItemCfg viselCfg = aVedScreen.model().visels().prepareFromTemplate( f.paletteEntries().first().itemCfg()
        // );

        VedItemCfg viselCfg = createViselCfg( ViselLabel.FACTORY_ID, aVedScreen, "RriAttrValueView" ); //$NON-NLS-1$
        viselCfg.propValues().setDouble( PROPID_X, aCfg.params().getDouble( PROPID_X ) );
        viselCfg.propValues().setDouble( PROPID_Y, aCfg.params().getDouble( PROPID_Y ) );
        v = aVedScreen.model().visels().create( viselCfg );

        IVedActorFactory af = actorFactory( SkActorAttrText.FACTORY_ID, aVedScreen );
        VedItemCfg actorCfg = aVedScreen.model().actors().prepareFromTemplate( af.paletteEntries().first().itemCfg() );
        actorCfg.propValues().setStr( PROPID_VISEL_ID, v.id() );
        actorCfg.propValues().setStr( PROPID_VISEL_PROP_ID, PROPID_TEXT );
        actor = aVedScreen.model().actors().create( actorCfg );
      }
      else {
        v = aVedScreen.model().visels().list().getByKey( aCfg.viselId() );
        actor = aVedScreen.model().actors().list().getByKey( aCfg.actorIds().first() );
      }

      if( v != null && actor != null ) {
        IOptionSetEdit params = new OptionSet();
        params.setDouble( PROPID_X, v.props().getDouble( PROPID_X ) );
        params.setDouble( PROPID_Y, v.props().getDouble( PROPID_Y ) );
        params.setStr( PROPID_VISEL_ID, v.id() );

        StringArrayList actorIds = new StringArrayList();
        actorIds.add( actor.id() );
        params.setValobj( IRtControlCfg.PROPID_ACTORS_IDS, actorIds );
        RtControlCfg cfg = new RtControlCfg( v.id(), FACTORY_ID, params );

        return new RtcRriAttrValueView( cfg, propDefs(), aVedScreen );
      }
      return null;
    }
  };

  protected RtcRriAttrValueView( IRtControlCfg aConfig, IStridablesList<IDataDef> aPropDefs, VedScreen aVedScreen ) {
    super( aConfig, aPropDefs, aVedScreen );
  }

  @Override
  protected void bindActorProps() {
    VedAbstractActor actor = actors().first();
    bindActorPropId( actor.id(), TFI_RRI_ATTR_UGWI.id(), TFI_RRI_ATTR_UGWI.id() );
  }
}
