package org.toxsoft.skf.rri.values.gui.sol;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.mnemo.gui.skved.ISkVedConstants.*;
import static org.toxsoft.skf.rri.values.gui.sol.ISkResources.*;

import org.toxsoft.core.tsgui.bricks.tin.*;
import org.toxsoft.core.tsgui.bricks.tin.impl.*;
import org.toxsoft.core.tsgui.bricks.tin.tti.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.skf.rri.lib.ugwi.*;
import org.toxsoft.uskat.core.gui.valed.ugwi.*;

/**
 * The package constants.
 *
 * @author hazard157, vs
 */
@SuppressWarnings( "javadoc" )
public interface IRriVedConstants {

  String PROPID_RRI_ID = SKVED_ID + ".prop.RriId"; //$NON-NLS-1$

  IDataDef PROP_RRI_ID = DataDef.create( PROPID_RRI_ID, VALOBJ, //
      TSID_NAME, STR_PROP_RRI_ID, //
      TSID_DESCRIPTION, STR_PROP_RRI_ID_D, //
      TSID_KEEPER_ID, RriId.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvValobjRriIdEditor.FACTORY_NAME, //
      TSID_DEFAULT_VALUE, avValobj( RriId.NONE ) //
  );

  ITinTypeInfo  TTI_RRI_ID = new TinAtomicTypeInfo.TtiValobj<>( PROP_RRI_ID, RriId.class );
  ITinFieldInfo TFI_RRI_ID = new TinFieldInfo( PROP_RRI_ID, TTI_RRI_ID );

  // ------------------------------------------------------------------------------------
  // UGWI support
  //

  String PROPID_RRI_ATTR_UGWI = SKVED_ID + ".prop.RriAttrUgwi"; //$NON-NLS-1$

  IDataDef PROP_RRI_ATTR_UGWI = DataDef.create( PROPID_RRI_ATTR_UGWI, VALOBJ, //
      TSID_NAME, STR_PROP_RRI_ATTR_UGWI, //
      TSID_DESCRIPTION, STR_PROP_RRI_ATTR_UGWI_D, //
      PROPID_UGWI_KIND, avStr( UgwiKindRriAttr.KIND_ID ), //
      TSID_KEEPER_ID, Ugwi.KEEPER_ID, //
      OPDEF_EDITOR_FACTORY_NAME, ValedAvUgwiSelector.FACTORY_NAME, //
      ValedUgwiSelector.OPDEF_SINGLE_UGWI_KIND_ID, avStr( UgwiKindRriAttr.KIND_ID ), //
      OPDEF_IS_SINGLE_LINE_UI, AV_TRUE, //
      TSID_DEFAULT_VALUE, IAtomicValue.NULL //
  );

  ITinTypeInfo  TTI_RRI_ATTR_UGWI = new TinAtomicTypeInfo.TtiValobj<>( PROP_RRI_ATTR_UGWI, Ugwi.class );
  ITinFieldInfo TFI_RRI_ATTR_UGWI = new TinFieldInfo( PROP_RRI_ATTR_UGWI, TTI_RRI_ATTR_UGWI );
}
