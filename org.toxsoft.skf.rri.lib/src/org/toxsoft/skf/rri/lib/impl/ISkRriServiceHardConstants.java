package org.toxsoft.skf.rri.lib.impl;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.rri.lib.impl.ISkResources.*;
import static org.toxsoft.skf.rri.lib.l10n.ISkRriLibSharedResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.users.ability.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * Service constants with unmodifiable values.
 *
 * @author hazard157
 */
@SuppressWarnings( "javadoc" )
public interface ISkRriServiceHardConstants {

  String SERVICE_ID = SK_SYSEXT_SERVICE_ID_PREFIX + ".RegRefService"; //$NON-NLS-1$

  // Identifier prefix of all classes owned by this service.
  String CLASSID_PREFIX_OWNED = ISkHardConstants.SK_SYSEXT_SERVICE_ID_PREFIX + ".regref."; //$NON-NLS-1$

  // Идентификатор класса объектов, соответствующих разделам {@link ISkRriSection}.
  String CLASSID_RRI_SECTION = CLASSID_PREFIX_OWNED + "Section"; //$NON-NLS-1$

  // Base class for all companion classes of all sections.
  String CLASSID_RRI_COMPANION_BASE = CLASSID_PREFIX_OWNED + "CompBase"; //$NON-NLS-1$

  // Префикс идентификаторов классов объектов-компаньенов.
  String CLASSID_START_COMPANION = CLASSID_PREFIX_OWNED + "comp"; //$NON-NLS-1$

  // Связь связи от объекта компаньена к местер-объекту.
  String LID_COMPANION_MASTER = "RriMasterObject"; //$NON-NLS-1$

  DtoLinkInfo LINFO_COMPANION_MASTER = DtoLinkInfo.create1( //
      LID_COMPANION_MASTER, //
      new SingleStringList( IGwHardConstants.GW_ROOT_CLASS_ID ), //
      new CollConstraint( 1, true, true, false ), //
      OptionSetUtils.createOpSet( //
          DDEF_NAME, STR_LINK_RRI_COMPANION_MASTER, //
          DDEF_DESCRIPTION, STR_LINK_RRI_COMPANION_MASTER //
      ) );

  // Атрибут объекта раздела, который хранит параметры ISkRriSection.params()
  String AID_RRI_SECTION_PARAMS = "Params"; //$NON-NLS-1$

  IDtoAttrInfo AINF_RRI_SECTION_PARAMS = DtoAttrInfo.create1( AID_RRI_SECTION_PARAMS, DataType.create( VALOBJ, //
      TSID_NAME, STR_ATTR_RRI_SECTION_PARAMS, //
      TSID_DESCRIPTION, STR_ATTR_RRI_SECTION_PARAMS, //
      TSID_KEEPER_ID, OptionSetKeeper.KEEPER_ID, //
      TSID_IS_NULL_ALLOWED, AV_FALSE, //
      TSID_DEFAULT_VALUE, avValobj( new OptionSet() ) //
  ), IOptionSet.NULL );

  /**
   * The identifier off the event {@link #EVDTO_RRI_PARAM_CHANGE}.
   */
  String EVID_RRI_PARAM_CHANGE = "RriParamsChange"; //$NON-NLS-1$

  String EVPRMID_REASON       = "reason";      //$NON-NLS-1$
  String EVPRMID_AUTHOR_LOGIN = "authorLogin"; //$NON-NLS-1$
  String EVPRMID_SECTION_ID   = "sectionId";   //$NON-NLS-1$
  String EVPRMID_PARAM_GWID   = "paramGwid";   //$NON-NLS-1$
  String EVPRMID_IS_LINK      = "isLink";      //$NON-NLS-1$
  String EVPRMID_OLD_VAL_ATTR = "oldAttr";     //$NON-NLS-1$
  String EVPRMID_NEW_VAL_ATTR = "newAttr";     //$NON-NLS-1$
  String EVPRMID_OLD_VAL_LINK = "oldLink";     //$NON-NLS-1$
  String EVPRMID_NEW_VAL_LINK = "newLink";     //$NON-NLS-1$

  IDataDef SDD_EVPRM_REASON = DataDef.create( EVPRMID_REASON, STRING, //
      TSID_NAME, STR_EVPRM_REASON, //
      TSID_DESCRIPTION, STR_EVPRM_REASON_D //
  );

  IDataDef SDD_EVPRM_AUTHOR_LOGIN = DataDef.create( EVPRMID_AUTHOR_LOGIN, STRING, //
      TSID_NAME, STR_EVPRM_AUTHOR_LOGIN, //
      TSID_DESCRIPTION, STR_EVPRM_AUTHOR_LOGIN_D //
  );

  IDataDef SDD_EVPRM_SECTION_ID = DataDef.create( EVPRMID_SECTION_ID, STRING, //
      TSID_NAME, STR_EVPRM_SECTION_ID, //
      TSID_DESCRIPTION, STR_EVPRM_SECTION_ID_D //
  );

  IDataDef SDD_EVPRM_PARAM_GWID = DataDef.create( EVPRMID_PARAM_GWID, VALOBJ, //
      TSID_NAME, STR_EVPRM_PARAM_ID, //
      TSID_DESCRIPTION, STR_EVPRM_PARAM_ID_D //
  );

  IDataDef SDD_EVPRM_IS_LINK = DataDef.create( EVPRMID_IS_LINK, BOOLEAN, //
      TSID_NAME, STR_EVPRM_IS_LINK, //
      TSID_DESCRIPTION, STR_EVPRM_IS_LINK_D //
  );

  IDataDef SDD_EVPRM_OLD_VAL_ATTR = DataDef.create( EVPRMID_OLD_VAL_ATTR, NONE, //
      TSID_NAME, STR_EVPRM_OLD_VAL_ATTR, //
      TSID_DESCRIPTION, STR_EVPRM_OLD_VAL_ATTR_D //
  );

  IDataDef SDD_EVPRM_NEW_VAL_ATTR = DataDef.create( EVPRMID_NEW_VAL_ATTR, NONE, //
      TSID_NAME, STR_EVPRM_NEW_VAL_ATTR, //
      TSID_DESCRIPTION, STR_EVPRM_NEW_VAL_ATTR_D //
  );

  IDataDef SDD_EVPRM_OLD_VAL_LINK = DataDef.create( EVPRMID_OLD_VAL_LINK, VALOBJ, //
      TSID_NAME, STR_EVPRM_OLD_VAL_LINK, //
      TSID_DESCRIPTION, STR_EVPRM_OLD_VAL_LINK //
  );

  IDataDef SDD_EVPRM_NEW_VAL_LINK = DataDef.create( EVPRMID_NEW_VAL_LINK, VALOBJ, //
      TSID_NAME, STR_EVPRM_NEW_VAL_LINK, //
      TSID_DESCRIPTION, STR_EVPRM_NEW_VAL_LINK_D //
  );

  /**
   * Definition of the event: "RRI parameter value changed".
   * <p>
   * This event is generated for RRI section object {@link ISkRriSection#getSectionSkid()}. That is for a object with
   * class ID {@link #CLASSID_RRI_SECTION} and STRID equal to the user-defined section ID.
   */
  IDtoEventInfo EVDTO_RRI_PARAM_CHANGE = DtoEventInfo.create1( EVID_RRI_PARAM_CHANGE, true, // aIsHist = true
      new StridablesList<>( //
          SDD_EVPRM_SECTION_ID, //
          SDD_EVPRM_PARAM_GWID, //
          SDD_EVPRM_AUTHOR_LOGIN, //
          SDD_EVPRM_IS_LINK, //
          SDD_EVPRM_REASON, //
          SDD_EVPRM_OLD_VAL_ATTR, //
          SDD_EVPRM_NEW_VAL_ATTR, //
          SDD_EVPRM_OLD_VAL_LINK, //
          SDD_EVPRM_NEW_VAL_LINK //
      ), //
      OptionSetUtils.createOpSet( //
          IAvMetaConstants.TSID_NAME, STR_EVENT_RRI_EDIT, //
          IAvMetaConstants.TSID_DESCRIPTION, STR_EVENT_RRI_EDIT_D //
      ) );

  String ABKINDID_RRI = SERVICE_ID + ".abkind.rri"; //$NON-NLS-1$

  String ABILITYID_EDIT_RRI = SERVICE_ID + ".ability.edit_rri"; //$NON-NLS-1$

  IDtoSkAbilityKind ABKIND_RRI = DtoSkAbilityKind.create( ABKINDID_RRI, STR_ABKIND_RRI, STR_ABKIND_RRI_D );

  IDtoSkAbility ABILITY_EDIT_RRI =
      DtoSkAbility.create( ABILITYID_EDIT_RRI, ABKINDID_RRI, STR_ABILITY_EDIT_RRI, STR_ABILITY_EDIT_RRI_D );

}
