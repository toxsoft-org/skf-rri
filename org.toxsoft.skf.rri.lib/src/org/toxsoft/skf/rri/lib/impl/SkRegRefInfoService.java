package org.toxsoft.skf.rri.lib.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.skf.rri.lib.impl.ISkResources.*;
import static org.toxsoft.skf.rri.lib.impl.ISkRriServiceHardConstants.*;
import static org.toxsoft.skf.rri.lib.l10n.ISkRriLibSharedResources.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.ctx.*;
import org.toxsoft.core.tslib.bricks.events.*;
import org.toxsoft.core.tslib.bricks.events.msg.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.core.tslib.utils.logs.impl.*;
import org.toxsoft.core.tslib.utils.txtmatch.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.linkserv.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.devapi.*;
import org.toxsoft.uskat.core.impl.*;
import org.toxsoft.uskat.core.impl.dto.*;

/**
 * {@link ISkRegRefInfoService} implementation.
 *
 * @author hazard157
 */
public class SkRegRefInfoService
    extends AbstractSkService
    implements ISkRegRefInfoService {

  /**
   * The service creator singleton.
   */
  public static final ISkServiceCreator<SkRegRefInfoService> CREATOR = SkRegRefInfoService::new;

  /**
   * Class for {@link ISkRegRefInfoService#eventer()} instance implementation.
   *
   * @author hazard157
   */
  class Eventer
      extends AbstractTsEventer<ISkRegRefInfoServiceListener> {

    private IStringMapEdit<ECrudOp> sectOpMap = new StringMap<>();

    @Override
    protected void doClearPendingEvents() {
      sectOpMap.getClass();
    }

    @Override
    protected void doFirePendingEvents() {
      for( String sectId : sectOpMap.keys() ) {
        ECrudOp op = sectOpMap.getByKey( sectId );
        reallyFireSectionEvent( op, sectId );
      }
    }

    @Override
    protected boolean doIsPendingEvents() {
      return !sectOpMap.isEmpty();
    }

    private void reallyFireSectionEvent( ECrudOp aOp, String aSectionId ) {
      for( ISkRegRefInfoServiceListener l : listeners() ) {
        try {
          l.onSectionChanged( aOp, aSectionId );
        }
        catch( Exception ex ) {
          LoggerUtils.errorLogger().error( ex );
        }
      }
    }

    void fireSectionEvent( ECrudOp aOp, String aSectionId ) {
      TsNullArgumentRtException.checkNulls( aOp, aSectionId );
      TsIllegalArgumentRtException.checkTrue( aOp == ECrudOp.LIST );
      if( isFiringPaused() ) {
        sectOpMap.put( aSectionId, aOp );
      }
      else {
        reallyFireSectionEvent( aOp, aSectionId );
      }
    }

  }

  /**
   * Class for {@link ISkRegRefInfoService#svs()} instance implementation.
   *
   * @author hazard157
   */
  class ValidationSupport
      extends AbstractTsValidationSupport<ISkRegRefInfoServiceValidator>
      implements ISkRegRefInfoServiceValidator {

    @Override
    public ISkRegRefInfoServiceValidator validator() {
      return this;
    }

    @Override
    public ValidationResult canCreateSection( String aSectionId, String aName, String aDescription,
        IOptionSet aParams ) {
      TsNullArgumentRtException.checkNulls( aSectionId, aName, aDescription, aParams );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkRegRefInfoServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canCreateSection( aSectionId, aName, aDescription, aParams ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canRemoveSection( String aSectionId ) {
      TsNullArgumentRtException.checkNull( aSectionId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkRegRefInfoServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canRemoveSection( aSectionId ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canSetSectionProps( ISkRriSection aSection, String aName, String aDescription,
        IOptionSet aParams ) {
      TsNullArgumentRtException.checkNulls( aSection, aName, aDescription, aParams );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkRegRefInfoServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canSetSectionProps( aSection, aName, aDescription, aParams ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canChangeParams( ISkRriSection aSection, ISkClassInfo aCompanionClassInfo,
        IStridablesList<IDtoRriParamInfo> aParamInfos ) {
      TsNullArgumentRtException.checkNulls( aSection, aCompanionClassInfo, aParamInfos );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkRegRefInfoServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canChangeParams( aSection, aCompanionClassInfo, aParamInfos ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canRemoveParam( ISkRriSection aSection, String aClassId, String aRriParamId ) {
      TsNullArgumentRtException.checkNulls( aSection, aClassId, aRriParamId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkRegRefInfoServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canRemoveParam( aSection, aClassId, aRriParamId ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canRemoveAll( ISkRriSection aSection, String aClassId ) {
      TsNullArgumentRtException.checkNulls( aSection, aClassId );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkRegRefInfoServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canRemoveAll( aSection, aClassId ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canSetAttrParamValue( ISkRriSection aSection, Skid aObjId, String aParamId,
        IAtomicValue aValue, String aReason ) {
      TsNullArgumentRtException.checkNulls( aSection, aObjId, aParamId, aValue, aReason );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkRegRefInfoServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canSetAttrParamValue( aSection, aObjId, aParamId, aValue, aReason ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canSetLinkParamValue( ISkRriSection aSection, Skid aObjId, String aParamId,
        ISkidList aObjIds, String aReason ) {
      TsNullArgumentRtException.checkNulls( aSection, aObjId, aParamId, aObjIds, aReason );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkRegRefInfoServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canSetLinkParamValue( aSection, aObjId, aParamId, aObjIds, aReason ) );
      }
      return vr;
    }

    @Override
    public ValidationResult canSetParamValues( ISkRriSection aSection, ISkRriParamValues aValues, String aReason ) {
      TsNullArgumentRtException.checkNulls( aSection, aValues, aReason );
      ValidationResult vr = ValidationResult.SUCCESS;
      for( ISkRegRefInfoServiceValidator v : validatorsList() ) {
        vr = ValidationResult.firstNonOk( vr, v.canSetParamValues( aSection, aValues, aReason ) );
      }
      return vr;
    }

  }

  /**
   * Builtin validation rules.
   */
  private final ISkRegRefInfoServiceValidator builtinValidator = new ISkRegRefInfoServiceValidator() {

    @Override
    public ValidationResult canCreateSection( String aSectionId, String aName, String aDescription,
        IOptionSet aParams ) {
      if( !userService().abilityManager().isAbilityAllowed( ABILITYID_EDIT_RRI ) ) { // check editing is enabled
        return ValidationResult.error( MSG_ERR_RRI_EDITING_NOT_ALLOWED );
      }
      if( listSections().hasKey( aSectionId ) ) {
        return ValidationResult.error( FMT_ERR_SECTION_ID_ALREADY_ESISTS, aSectionId );
      }
      if( aName.isEmpty() ) {
        return ValidationResult.error( FMT_ERR_EMPTY_NAME );
      }
      if( !StridUtils.isValidIdPath( aSectionId ) ) {
        return ValidationResult.error( FMT_ERR_INV_SECTION_ID, aSectionId );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canRemoveSection( String aSectionId ) {
      TsNullArgumentRtException.checkNull( aSectionId );
      if( !userService().abilityManager().isAbilityAllowed( ABILITYID_EDIT_RRI ) ) { // check editing is enabled
        return ValidationResult.error( MSG_ERR_RRI_EDITING_NOT_ALLOWED );
      }
      if( !listSections().hasKey( aSectionId ) ) {
        return ValidationResult.error( FMT_ERR_SECTION_ID_NOT_ESISTS, aSectionId );
      }
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canSetSectionProps( ISkRriSection aSection, String aName, String aDescription,
        IOptionSet aParams ) {
      if( !userService().abilityManager().isAbilityAllowed( ABILITYID_EDIT_RRI ) ) { // check editing is enabled
        return ValidationResult.error( MSG_ERR_RRI_EDITING_NOT_ALLOWED );
      }
      // TODO Auto-generated method stub
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canChangeParams( ISkRriSection aSection, ISkClassInfo aCompanionClassInfo,
        IStridablesList<IDtoRriParamInfo> aParamInfos ) {
      if( !userService().abilityManager().isAbilityAllowed( ABILITYID_EDIT_RRI ) ) { // check editing is enabled
        return ValidationResult.error( MSG_ERR_RRI_EDITING_NOT_ALLOWED );
      }
      // TODO ensure that no attribute and link has the same ID

      // TODO Auto-generated method stub
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canRemoveParam( ISkRriSection aSection, String aClassId, String aRriParamId ) {
      if( !userService().abilityManager().isAbilityAllowed( ABILITYID_EDIT_RRI ) ) { // check editing is enabled
        return ValidationResult.error( MSG_ERR_RRI_EDITING_NOT_ALLOWED );
      }
      // TODO Auto-generated method stub
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canRemoveAll( ISkRriSection aSection, String aClassId ) {
      if( !userService().abilityManager().isAbilityAllowed( ABILITYID_EDIT_RRI ) ) { // check editing is enabled
        return ValidationResult.error( MSG_ERR_RRI_EDITING_NOT_ALLOWED );
      }
      // TODO Auto-generated method stub
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canSetAttrParamValue( ISkRriSection aSection, Skid aObjId, String aParamId,
        IAtomicValue aValue, String aReason ) {
      if( !userService().abilityManager().isAbilityAllowed( ABILITYID_EDIT_RRI ) ) { // check editing is enabled
        return ValidationResult.error( MSG_ERR_RRI_EDITING_NOT_ALLOWED );
      }
      // TODO Auto-generated method stub
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canSetLinkParamValue( ISkRriSection aSection, Skid aObjId, String aParamId,
        ISkidList aObjIds, String aReason ) {
      if( !userService().abilityManager().isAbilityAllowed( ABILITYID_EDIT_RRI ) ) { // check editing is enabled
        return ValidationResult.error( MSG_ERR_RRI_EDITING_NOT_ALLOWED );
      }
      // TODO warn if reason is a blank string

      // TODO Auto-generated method stub
      return ValidationResult.SUCCESS;
    }

    @Override
    public ValidationResult canSetParamValues( ISkRriSection aSection, ISkRriParamValues aValues, String aReason ) {

      // TODO SkRegRefInfoService.builtinValidator.new ISkRegRefInfoServiceValidator() {...}.canSetParamValues()

      if( !userService().abilityManager().isAbilityAllowed( ABILITYID_EDIT_RRI ) ) { // check editing is enabled
        return ValidationResult.error( MSG_ERR_RRI_EDITING_NOT_ALLOWED );
      }
      // TODO warn if reason is a blank string

      // TODO Auto-generated method stub
      return ValidationResult.SUCCESS;
    }
  };

  /**
   * Prevents {@link ISkSysdescr} to work with refbook service owned classes.
   */
  private final ISkSysdescrValidator classInfoManagerValidator = new ISkSysdescrValidator() {

    @Override
    public ValidationResult canCreateClass( IDtoClassInfo aDpuClassInfo ) {
      return validateClassIsManagedByThisService( aDpuClassInfo.id() );
    }

    @Override
    public ValidationResult canEditClass( IDtoClassInfo aDpuClassInfo, ISkClassInfo aOldClassInfo ) {
      return validateClassIsManagedByThisService( aDpuClassInfo.id() );
    }

    @Override
    public ValidationResult canRemoveClass( String aClassId ) {
      return validateClassIsManagedByThisService( aClassId );
    }
  };

  /**
   * Prevents {@link ISkObjectService} to work with refbook service owned objects.
   */
  private final ISkObjectServiceValidator objectServiceValidator = new ISkObjectServiceValidator() {

    @Override
    public ValidationResult canCreateObject( IDtoObject aDpuObject ) {
      return validateClassIsManagedByThisService( aDpuObject.skid().classId() );
    }

    @Override
    public ValidationResult canEditObject( IDtoObject aDpuObject, ISkObject aOldObject ) {
      return validateClassIsManagedByThisService( aOldObject.classId() );
    }

    @Override
    public ValidationResult canRemoveObject( Skid aSkid ) {
      return validateClassIsManagedByThisService( aSkid.classId() );
    }
  };

  /**
   * Prevents {@link ISkLinkService} to work with refbook service owned objects.
   */
  private final ISkLinkServiceValidator linkServiceValidator = //
      ( aOldLink, aNewLink ) -> validateClassIsManagedByThisService( aNewLink.leftSkid().classId() );

  /**
   * The rule to check class ID is claimed by this service.
   */
  private static final TextMatcher THIS_SERVICE_CLASS_ID_MATCHER =
      new TextMatcher( ETextMatchMode.STARTS, ISkRriServiceHardConstants.CLASSID_PREFIX_OWNED, true );

  final Eventer           eventer           = new Eventer();
  final ValidationSupport validationSupport = new ValidationSupport();
  final SkRriHistory      history;

  private final IStridablesListEdit<SkRriSection> sectsList   = new StridablesList<>();
  private String                                  authorLogin = TsLibUtils.EMPTY_STRING;

  /**
   * The constructor.
   *
   * @param aCoreApi {@link IDevCoreApi} - core API for service developers
   */
  public SkRegRefInfoService( IDevCoreApi aCoreApi ) {
    super( SERVICE_ID, aCoreApi );
    history = new SkRriHistory( this );
    validationSupport.addValidator( builtinValidator );
  }

  // ------------------------------------------------------------------------------------
  // Implementation
  //

  private static ValidationResult validateClassIsManagedByThisService( String aClassId ) {
    if( aClassId.startsWith( CLASSID_PREFIX_OWNED ) ) {
      return ValidationResult.error( FMT_ERR_CLASS_IS_REGREF_OWNED, aClassId );
    }
    return ValidationResult.SUCCESS;
  }

  // ------------------------------------------------------------------------------------
  // package API
  //

  void pauseExternalValidation() {
    coreApi().sysdescr().svs().pauseValidator( classInfoManagerValidator );
    coreApi().objService().svs().pauseValidator( objectServiceValidator );
    coreApi().linkService().svs().pauseValidator( linkServiceValidator );
  }

  void resumeExternalValidation() {
    coreApi().sysdescr().svs().resumeValidator( classInfoManagerValidator );
    coreApi().objService().svs().resumeValidator( objectServiceValidator );
    coreApi().linkService().svs().resumeValidator( linkServiceValidator );
  }

  String authorLogin() {
    return authorLogin;
  }

  // ------------------------------------------------------------------------------------
  // AbstractSkService
  //

  @Override
  protected void doInit( ITsContextRo aArgs ) {
    // ensure RRI section class exists
    DtoClassInfo rriSectionClass =
        new DtoClassInfo( CLASSID_RRI_SECTION, IGwHardConstants.GW_ROOT_CLASS_ID, IOptionSet.NULL );
    rriSectionClass.eventInfos().add( EVDTO_RRI_PARAM_CHANGE );
    rriSectionClass.attrInfos().add( AINF_RRI_SECTION_PARAMS );
    rriSectionClass.params().setBool( ISkHardConstants.OPDEF_SK_IS_SOURCE_CODE_DEFINED_CLASS, true );
    sysdescr().defineClass( rriSectionClass );
    // service validators
    sysdescr().svs().addValidator( classInfoManagerValidator );
    objServ().svs().addValidator( objectServiceValidator );
    linkService().svs().addValidator( linkServiceValidator );
    // load sections
    IList<ISkObject> rsObjs = coreApi().objService().listObjs( CLASSID_RRI_SECTION, true );
    for( ISkObject sko : rsObjs ) {
      sectsList.add( new SkRriSection( sko, this ) );
    }
    authorLogin = coreApi().getCurrentUserInfo().userSkid().strid();
    // register builtin abilities
    userService().abilityManager().defineKind( ABKIND_RRI );
    userService().abilityManager().defineAbility( ABILITY_EDIT_RRI );
  }

  @Override
  protected boolean onBackendMessage( GenericMessage aMessage ) {
    switch( aMessage.messageId() ) {
      case BaMsgRriSectionsListChange.MSG_ID: {
        ECrudOp op = BaMsgRriSectionsListChange.BUILDER.getCrudOp( aMessage );
        String sectionId = BaMsgRriSectionsListChange.BUILDER.getSectionId( aMessage );
        eventer.fireSectionEvent( op, sectionId );
        break;
      }
      default:
        return false;
    }
    return true;
  }

  @Override
  protected boolean doIsClassClaimedByService( String aClassId ) {
    return THIS_SERVICE_CLASS_ID_MATCHER.accept( aClassId );
  }

  @Override
  protected void doClose() {
    // nop
  }

  // ------------------------------------------------------------------------------------
  // ISkRegRefInfoService
  //

  @SuppressWarnings( { "unchecked", "rawtypes" } )
  @Override
  public IStridablesList<ISkRriSection> listSections() {
    return (IStridablesList)sectsList;
  }

  @Override
  public ISkRriSection findSection( String aSectionId ) {
    return sectsList.findByKey( aSectionId );
  }

  @Override
  public ISkRriSection getSection( String aSectionId ) {
    return sectsList.getByKey( aSectionId );
  }

  @Override
  public ISkRriSection createSection( String aId, String aName, String aDescription, IOptionSet aParams ) {
    TsValidationFailedRtException.checkError( svs().validator().canCreateSection( aId, aName, aDescription, aParams ) );
    IOptionSetEdit attrs = new OptionSet();
    IAvMetaConstants.DDEF_IDNAME.setValue( attrs, avStr( aName ) );
    IAvMetaConstants.DDEF_DESCRIPTION.setValue( attrs, avStr( aDescription ) );
    attrs.setValobj( AID_RRI_SECTION_PARAMS, aParams );
    DtoObject dtoObj = new DtoObject( new Skid( CLASSID_RRI_SECTION, aId ), attrs, IStringMap.EMPTY );
    SkRriSection sect = null;
    try {
      pauseExternalValidation();
      ISkObject sko = coreApi().objService().defineObject( dtoObj );
      sect = new SkRriSection( sko, this );
      sectsList.add( sect );
    }
    finally {
      resumeExternalValidation();
    }
    GtMessage msg = BaMsgRriSectionsListChange.BUILDER.makeMessage( ECrudOp.CREATE, aId );
    sendMessageToSiblings( msg );
    return sect;
  }

  @Override
  public void removeSection( String aSectionId ) {
    TsValidationFailedRtException.checkError( svs().validator().canRemoveSection( aSectionId ) );
    try {
      pauseExternalValidation();
      SkRriSection sect = sectsList.getByKey( aSectionId );
      sect.removeAll( aSectionId );
      coreApi().objService().removeObject( sect.getRriSectionObjectSkid() );
      sectsList.removeById( aSectionId );
    }
    finally {
      resumeExternalValidation();
    }
    GtMessage msg = BaMsgRriSectionsListChange.BUILDER.makeMessage( ECrudOp.REMOVE, aSectionId );
    sendMessageToSiblings( msg );
  }

  @Override
  public ISkRriHistory history() {
    return history;
  }

  @Override
  public ITsValidationSupport<ISkRegRefInfoServiceValidator> svs() {
    return validationSupport;
  }

  @Override
  public ITsEventer<ISkRegRefInfoServiceListener> eventer() {
    return eventer;
  }

}
