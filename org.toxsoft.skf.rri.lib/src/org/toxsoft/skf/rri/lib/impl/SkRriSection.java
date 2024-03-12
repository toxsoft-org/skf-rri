package org.toxsoft.skf.rri.lib.impl;

import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.core.tslib.gw.IGwHardConstants.*;
import static org.toxsoft.skf.rri.lib.impl.ISkRriServiceHardConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.events.msg.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.*;
import org.toxsoft.core.tslib.coll.helpers.*;
import org.toxsoft.core.tslib.coll.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.gw.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.legacy.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.evserv.*;
import org.toxsoft.uskat.core.api.linkserv.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.sysdescr.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.impl.dto.*;
import org.toxsoft.uskat.core.utils.*;

/**
 * {@link ISkRriSection} implementation.
 *
 * @author hazard157
 */
class SkRriSection
    implements ISkRriSection {

  private final SkRegRefInfoService rriService;
  private final ISkCoreApi          coreApi;
  private final ISkSysdescr         caCim;
  private final ISkObjectService    caOs;
  private final ISkLinkService      caLs;

  private ISkObject sectionObject;

  SkRriSection( ISkObject aSectionObject, SkRegRefInfoService aRriService ) {
    TsNullArgumentRtException.checkNulls( aSectionObject, aRriService );
    rriService = aRriService;
    coreApi = rriService.coreApi();
    caCim = coreApi.sysdescr();
    caOs = coreApi.objService();
    caLs = coreApi.linkService();
    sectionObject = aSectionObject;
  }

  // ------------------------------------------------------------------------------------
  // implementation
  //

  private String makeCompanionId( String aMasterId ) {
    StridUtils.checkValidIdPath( aMasterId );
    String cId = StridUtils.makeIdPath( sectionObject.id(), aMasterId );
    return StridUtils.makeIdPath( CLASSID_START_COMPANION, cId );
  }

  // private boolean isThisSectionCompanionClassId( String aClassId ) {
  // if( StridUtils.startsWithIdPath( aClassId, aClassId ) ) {
  // String sId = StridUtils.removeStartingIdPath( CLASSID_START_COMPANION, aClassId );
  // return StridUtils.startsWithIdPath( sectionObject.id(), sId );
  // }
  // return false;
  // }

  /**
   * Гарантирует, что для запрошенного класса существует класс-компанион со всей иерархией, начиная с корневого класса.
   * <p>
   * Внимание: корневой класс не имеет компаниона! Для него возвращается идентификатор корневого класса
   * {@link IGwHardConstants#GW_ROOT_CLASS_ID} - ведь самый "родительский" компанион наследуется от корневого класса.
   * <p>
   * Внимание: использование метода предполагает, что уже вызван {@link SkRegRefInfoService#pauseExternalValidation()}.
   *
   * @param aClassId String - идентификатор класса, чей компанион создается
   * @return String - идентификатор класса-<b>компаниона</b>
   */
  private String intenalEnsureCompanionsHierarchy( String aClassId ) {
    // корневой класс не может иметь НСИ
    if( aClassId.equals( GW_ROOT_CLASS_ID ) ) {
      return GW_ROOT_CLASS_ID;
    }
    // проверим, и если компанион существут - вернемся
    String companionId = makeCompanionId( aClassId );
    ISkClassInfo companionInfo = caCim.findClassInfo( companionId );
    if( companionInfo == null ) {
      // сначала обеспечим наличие компаниона родительского класса
      String parentId = caCim.getClassInfo( aClassId ).parentId();
      String companionParentId = intenalEnsureCompanionsHierarchy( parentId );
      // создадим класс-компанион
      IDtoClassInfo cDpu = new DtoClassInfo( companionId, companionParentId, IOptionSet.NULL );
      caCim.defineClass( cDpu );
    }
    return companionId;
  }

  ISkClassInfo ensureCompanionClass( ISkClassInfo aMaster ) {
    TsInternalErrorRtException.checkTrue( aMaster.id().equals( GW_ROOT_CLASS_ID ) );
    // проверим существование компаниона, и если его нет, создадим его
    String companionId = makeCompanionId( aMaster.id() );
    ISkClassInfo comanionInfo = caCim.findClassInfo( companionId );
    if( comanionInfo == null ) {
      try {
        rriService.pauseExternalValidation();
        intenalEnsureCompanionsHierarchy( aMaster.id() );
        comanionInfo = caCim.getClassInfo( companionId );
      }
      finally {
        rriService.resumeExternalValidation();
      }
    }
    return comanionInfo;
  }

  private ISkObject ensureCompanionObject( Skid aCompObjSkid ) {
    ISkObject sko = caOs.find( aCompObjSkid );
    if( sko != null ) {
      return sko;
    }
    DtoObject dto = new DtoObject( aCompObjSkid );
    ISkClassInfo clinf = caCim.getClassInfo( aCompObjSkid.classId() );
    for( IDtoAttrInfo ainf : clinf.attrs().listNonSys() ) {
      IAtomicValue defVal = SkHelperUtils.getConstraint( ainf, TSID_DEFAULT_VALUE );
      dto.attrs().setValue( ainf.id(), defVal );
    }
    rriService.pauseExternalValidation();
    try {
      return caOs.defineObject( dto );
    }
    finally {
      rriService.resumeExternalValidation();
    }
  }

  private void internalUnvalidatedRemoveAll( String aClassId ) {
    String compClassId = makeCompanionId( aClassId );
    ISkClassInfo compClassInfo = caCim.findClassInfo( compClassId );
    if( compClassInfo == null ) {
      return;
    }
    try {
      rriService.pauseExternalValidation();
      // remove all companion objects storing RRI parameters of this class (and subclasses)
      ISkidList toRemove = caOs.listSkids( compClassId, true );
      caOs.removeObjects( toRemove );
      // remove companion class with all subclasses: aOnlyChilds = false, aIncludeSelf = true
      IStridablesList<ISkClassInfo> compCids = compClassInfo.listSubclasses( false, true );
      for( ISkClassInfo cid : compCids ) {
        caCim.removeClass( cid.id() );
      }
      fireSectionChangeEvent();
    }
    finally {
      rriService.resumeExternalValidation();
    }
  }

  SkEvent makeAttrChangeEvent( Gwid aParamGwid, String aReason, long aTimestamp, IAtomicValue aOldValue,
      IAtomicValue aNewValue ) {
    IOptionSetEdit parvals = new OptionSet();
    parvals.setStr( EVPRMID_SECTION_ID, sectionObject.strid() );
    parvals.setStr( EVPRMID_AUTHOR_LOGIN, rriService.authorLogin() );
    parvals.setValobj( EVPRMID_PARAM_GWID, aParamGwid );
    parvals.setStr( EVPRMID_REASON, aReason );
    parvals.setBool( EVPRMID_IS_LINK, false );
    parvals.setValue( EVPRMID_OLD_VAL_ATTR, aOldValue );
    parvals.setValue( EVPRMID_NEW_VAL_ATTR, aNewValue );
    Gwid gwid = Gwid.createEvent( sectionObject.classId(), sectionObject.strid(), EVID_RRI_PARAM_CHANGE );
    return new SkEvent( aTimestamp, gwid, parvals );
  }

  SkEvent makeLinkChangeEvent( Gwid aParamGwid, String aReason, long aTimestamp, ISkidList aOldValue,
      ISkidList aNewValue ) {
    IOptionSetEdit parvals = new OptionSet();
    parvals.setStr( EVPRMID_SECTION_ID, sectionObject.strid() );
    parvals.setStr( EVPRMID_AUTHOR_LOGIN, rriService.authorLogin() );
    parvals.setValobj( EVPRMID_PARAM_GWID, aParamGwid );
    parvals.setStr( EVPRMID_REASON, aReason );
    parvals.setBool( EVPRMID_IS_LINK, true );
    parvals.setValobj( EVPRMID_OLD_VAL_LINK, aOldValue );
    parvals.setValobj( EVPRMID_NEW_VAL_LINK, aNewValue );
    Gwid gwid = Gwid.createEvent( sectionObject.classId(), sectionObject.strid(), EVID_RRI_PARAM_CHANGE );
    return new SkEvent( aTimestamp, gwid, parvals );
  }

  /**
   * Generates {@link ISkRegRefInfoServiceListener#onSectionChanged(ECrudOp, String)} event of section change.
   * <p>
   * To generate event sends the {@link BaMsgRriSectionsListChange#MSG_ID} message to backend.
   */
  private void fireSectionChangeEvent() {
    GtMessage msg = BaMsgRriSectionsListChange.BUILDER.makeMessage( ECrudOp.EDIT, id() );
    rriService.sendMessageToSiblings( msg );
  }

  // ------------------------------------------------------------------------------------
  // Package API
  //

  Skid getRriSectionObjectSkid() {
    return sectionObject.skid();
  }

  // ------------------------------------------------------------------------------------
  // IStridable
  //

  @Override
  public String id() {
    return sectionObject.strid();
  }

  @Override
  public String nmName() {
    // return sectionObject.nmName(); Sol-- Имя пустое
    return IAvMetaConstants.DDEF_NAME.getValue( params() ).asString(); // Sol++ 13.02.2024
  }

  @Override
  public String description() {
    // return sectionObject.description(); Sol-- Описание пустое
    return IAvMetaConstants.DDEF_DESCRIPTION.getValue( params() ).asString(); // Sol++ 13.02.2024
  }

  // ------------------------------------------------------------------------------------
  // IParameterized
  //

  @Override
  public IOptionSet params() {
    return sectionObject.attrs().getValobj( AINF_RRI_SECTION_PARAMS.id() );
  }

  // ------------------------------------------------------------------------------------
  // IRriSection
  //

  @Override
  public void setSectionProps( String aName, String aDescription, IOptionSet aParams ) {
    TsValidationFailedRtException
        .checkError( rriService.svs().validator().canSetSectionProps( this, aName, aDescription, aParams ) );
    IOptionSetEdit attrs = new OptionSet();
    DDEF_NAME.setValue( attrs, avStr( aName ) );
    DDEF_DESCRIPTION.setValue( attrs, avStr( aDescription ) );
    attrs.setValobj( AID_RRI_SECTION_PARAMS, aParams );
    DtoObject dtoObj =
        new DtoObject( new Skid( sectionObject.classId(), sectionObject.strid() ), attrs, IStringMap.EMPTY );
    try {
      rriService.pauseExternalValidation();
      sectionObject = caOs.defineObject( dtoObj );
    }
    finally {
      rriService.resumeExternalValidation();
    }
    fireSectionChangeEvent();
  }

  @Override
  public Skid getSectionSkid() {
    return sectionObject.skid();
  }

  @Override
  public void defineParam( String aClassId, IStridablesList<IDtoRriParamInfo> aInfos ) {
    TsNullArgumentRtException.checkNulls( aClassId, aInfos );
    ISkClassInfo ccInfo = ensureCompanionClass( caCim.getClassInfo( aClassId ) ); // create class even for empty arg
    TsValidationFailedRtException.checkError( rriService.svs().validator().canChangeParams( this, ccInfo, aInfos ) );
    if( aInfos.isEmpty() ) {
      return;
    }
    // build changed DtoClassInfo
    DtoClassInfo ccDto = DtoClassInfo.createFromSk( ccInfo, true ); // aOnlySelfProps = true
    for( IDtoRriParamInfo pinf : aInfos ) {
      if( pinf.isLink() ) {
        ccDto.linkInfos().add( pinf.linkInfo() );
      }
      else {
        ccDto.attrInfos().add( pinf.attrInfo() );
      }
    }
    // update class in Sysdescr
    rriService.pauseExternalValidation();
    try {
      caCim.defineClass( ccDto );
      fireSectionChangeEvent();
    }
    finally {
      rriService.resumeExternalValidation();
    }

  }

  @Override
  public void removeParam( String aClassId, String aParamId ) {
    TsValidationFailedRtException.checkError( rriService.svs().validator().canRemoveParam( this, aClassId, aParamId ) );
    ISkClassInfo ccInfo = ensureCompanionClass( caCim.getClassInfo( aClassId ) );
    DtoClassInfo ccDto = DtoClassInfo.createFromSk( ccInfo, true ); // aOnlySelfProps = true
    ccDto.attrInfos().removeById( aParamId );
    ccDto.linkInfos().removeById( aParamId );
    rriService.pauseExternalValidation();
    try {
      caCim.defineClass( ccDto );
      fireSectionChangeEvent();
    }
    finally {
      rriService.resumeExternalValidation();
    }
  }

  @Override
  public void removeAll( String aClassId ) {
    TsValidationFailedRtException.checkError( rriService.svs().validator().canRemoveAll( this, aClassId ) );
    internalUnvalidatedRemoveAll( aClassId );
  }

  @Override
  public void clearAll() {
    IStringList usedClassIds = listClassIds();
    for( String cid : usedClassIds ) {
      TsValidationFailedRtException.checkError( rriService.svs().validator().canRemoveAll( this, cid ) );
    }
    for( String cid : usedClassIds ) {
      internalUnvalidatedRemoveAll( cid );
    }
    fireSectionChangeEvent();
  }

  @Override
  public IStringList listClassIds() {
    IStringListEdit result = new StringLinkedBundleList();
    for( ISkClassInfo cInfo : caCim.listClasses() ) {
      String compClassId = makeCompanionId( cInfo.id() );
      if( caCim.findClassInfo( compClassId ) != null ) {
        result.add( cInfo.id() );
      }
    }
    return result;
  }

  @Override
  public IStridablesList<IDtoRriParamInfo> listParamInfoes( String aClassId ) {
    TsItemNotFoundRtException.checkNull( caCim.findClassInfo( aClassId ) );
    String compClassId = makeCompanionId( aClassId );
    ISkClassInfo compClassInfo = caCim.findClassInfo( compClassId );
    if( compClassInfo == null ) {
      return IStridablesList.EMPTY;
    }
    ISkClassInfo rootClassInfo = caCim.getClassInfo( GW_ROOT_CLASS_ID );
    IStridablesListEdit<IDtoRriParamInfo> result = new StridablesList<>();
    // all attributes except of root class
    for( IDtoAttrInfo skAinf : compClassInfo.attrs().list() ) {
      if( !rootClassInfo.attrs().list().hasKey( skAinf.id() ) ) {
        IDtoRriParamInfo pi = new DtoRriParamInfo( skAinf );
        result.add( pi );
      }
    }
    // all links except of root class
    for( IDtoLinkInfo skLinf : compClassInfo.links().list() ) {
      if( !rootClassInfo.links().list().hasKey( skLinf.id() ) ) {
        IDtoRriParamInfo pi = new DtoRriParamInfo( skLinf );
        result.add( pi );
      }
    }
    return result;
  }

  @Override
  public IAtomicValue getAttrParamValue( Skid aObjId, String aParamId ) {
    TsNullArgumentRtException.checkNulls( aObjId, aParamId );
    // проверим существование класса-компаниона (то есть, параметры НСИ для класса запрошенного объекта существуют)
    String compClassId = makeCompanionId( aObjId.classId() );
    ISkClassInfo cinf = caCim.findClassInfo( compClassId );
    TsItemNotFoundRtException.checkNull( cinf );
    // проверка существования атрибута
    IDtoAttrInfo ainf = cinf.attrs().list().getByKey( aParamId );
    // вернем значение атрибута объекта-компаниона или значение по умолчанию
    Skid compSkid = new Skid( compClassId, aObjId.strid() );
    ISkObject obj = caOs.find( compSkid );
    if( obj != null ) {
      return obj.attrs().getValue( aParamId, ainf.dataType().defaultValue() );
    }
    return ainf.dataType().defaultValue();
  }

  @Override
  public ISkidList getLinkParamValue( Skid aObjId, String aParamId ) {
    TsNullArgumentRtException.checkNulls( aObjId, aParamId );
    // проверим существование класса-компаниона (то есть, параметры НСИ для класса запрошенного объекта существуют)
    String compClassId = makeCompanionId( aObjId.classId() );
    ISkClassInfo cinf = caCim.findClassInfo( compClassId );
    TsItemNotFoundRtException.checkNull( cinf );
    // проверка существования связи
    cinf.links().list().getByKey( aParamId );
    // вернем значение связи объекта-компаниона или путсой список
    Skid compSkid = new Skid( compClassId, aObjId.strid() );
    ISkObject obj = caOs.find( compSkid );
    if( obj != null ) {
      IDtoLinkFwd lnk = caLs.getLinkFwd( compSkid, aParamId );
      return lnk.rightSkids();
    }
    return ISkidList.EMPTY;
  }

  @Override
  public ISkRriParamValues getParamValuesByObjs( ISkidList aObjIds ) {
    TsNullArgumentRtException.checkNull( aObjIds );
    IMapEdit<Skop, IAtomicValue> attrsMap = new ElemMap<>();
    IMapEdit<Skop, ISkidList> linksMap = new ElemMap<>();
    // цилк по каждому объекту из запрошенного списка для формирования незультата
    for( Skid origSkid : aObjIds ) {
      String compClassId = makeCompanionId( origSkid.classId() );
      // проверим существование класса-компаниона (то есть, параметры НСИ для класса запрошенного объекта существуют)
      ISkClassInfo cinf = caCim.findClassInfo( compClassId );
      TsItemNotFoundRtException.checkNull( cinf );
      // найдем объект-компанион, если есть
      Skid compSkid = new Skid( compClassId, origSkid.strid() );
      ISkObject compObj = caOs.find( compSkid ); // объект-компанион, может быть null
      // заполняем карту атрибутов сущствующими значениями или значениями по умолчанию
      for( IDtoAttrInfo ainf : cinf.attrs().list() ) {
        Skop skop = new Skop( origSkid, ainf.id() );
        if( compObj != null ) {
          IAtomicValue val = compObj.attrs().getValue( ainf.id() ); // существующее значение или по умолчанию
          attrsMap.put( skop, val );
        }
        else {
          IAtomicValue defVal = ainf.params().getValue( ainf.id() ); // значение по умолчанию
          attrsMap.put( skop, defVal );
        }
      }
      // запоняем карту связей сущствующими связями или пустым списком
      for( IDtoLinkInfo linf : cinf.links().list() ) {
        Skop skop = new Skop( origSkid, linf.id() );
        if( compObj != null ) {
          IDtoLinkFwd lnk = caLs.getLinkFwd( compObj.skid(), linf.id() );
          linksMap.put( skop, lnk.rightSkids() );
        }
        else {
          linksMap.put( skop, ISkidList.EMPTY );
        }
      }
    }
    return new SkRriParamValues( attrsMap, linksMap );
  }

  @Override
  public ISkRriParamValues getParamValuesByClassId( String aClassId ) {
    ISkidList objIds = caOs.listSkids( aClassId, false );
    return getParamValuesByObjs( objIds );
  }

  @Override
  public void setAttrParamValue( Skid aObjId, String aParamId, IAtomicValue aValue, String aReason ) {
    TsValidationFailedRtException
        .checkError( rriService.svs().validator().canSetAttrParamValue( this, aObjId, aParamId, aValue, aReason ) );
    String compClassId = makeCompanionId( aObjId.classId() );
    Skid compObjSkid = new Skid( compClassId, aObjId.strid() );
    ISkObject compObj = ensureCompanionObject( compObjSkid );
    IAtomicValue oldValue = compObj.attrs().getValue( aParamId );
    DtoObject dtoObj = DtoObject.createFromSk( compObj, coreApi );
    dtoObj.attrs().setValue( aParamId, aValue );
    try {
      rriService.pauseExternalValidation();
      caOs.defineObject( dtoObj );
      Gwid paramGwid = Gwid.createAttr( aObjId.classId(), aObjId.strid(), aParamId );
      SkEvent event = makeAttrChangeEvent( paramGwid, aReason, System.currentTimeMillis(), oldValue, aValue );
      coreApi.eventService().fireEvent( event );
    }
    finally {
      rriService.resumeExternalValidation();
    }
  }

  @Override
  public void setLinkParamValue( Skid aObjId, String aParamId, ISkidList aObjIds, String aReason ) {
    TsValidationFailedRtException
        .checkError( rriService.svs().validator().canSetLinkParamValue( this, aObjId, aParamId, aObjIds, aReason ) );
    String compClassId = makeCompanionId( aObjId.classId() );
    Skid compObjSkid = new Skid( compClassId, aObjId.strid() );
    ISkObject compObj = ensureCompanionObject( compObjSkid );
    try {
      rriService.pauseExternalValidation();
      ISkidList oldValue = compObj.getLinkSkids( aParamId );
      caLs.setLink( compObjSkid, aParamId, aObjIds );
      Gwid paramGwid = Gwid.createLink( aObjId.classId(), aObjId.strid(), aParamId );
      SkEvent event = makeLinkChangeEvent( paramGwid, aReason, System.currentTimeMillis(), oldValue, aObjIds );
      coreApi.eventService().fireEvent( event );
    }
    finally {
      rriService.resumeExternalValidation();
    }
  }

  @Override
  public void setParamValues( ISkRriParamValues aValues, String aReason ) {
    TsValidationFailedRtException
        .checkError( rriService.svs().validator().canSetParamValues( this, aValues, aReason ) );
    long timestamp = System.currentTimeMillis();
    try {
      rriService.pauseExternalValidation();
      IListEdit<SkEvent> events = new ElemLinkedBundleList<>();
      for( Skid origObjSkid : aValues.listObjSkids() ) {
        String compClassId = makeCompanionId( origObjSkid.classId() );
        Skid compObjSkid = new Skid( compClassId, origObjSkid.strid() );
        ISkObject oldCompObj = caOs.find( compObjSkid );
        ISkClassInfo cinf = caCim.getClassInfo( compClassId );
        //
        // задаем все атрибуты каждого объекта за один раз
        //
        IStringMap<IAtomicValue> newAttrs = aValues.getAttrParamsOfObj( origObjSkid );
        IOptionSetEdit attrs = new OptionSet();
        if( oldCompObj == null ) {
          // при создании объекта-компаниона заполним атрибуты значениями по умолчанию
          for( IDtoAttrInfo ainf : cinf.attrs().list() ) {
            IAtomicValue val = ainf.dataType().defaultValue();
            attrs.setValue( ainf.id(), val );
          }
        }
        else {
          attrs.setAll( oldCompObj.attrs() );
        }
        // занесем в атрибутах новый значения
        for( String aid : newAttrs.keys() ) {
          if( cinf.attrs().list().hasKey( aid ) ) {
            IAtomicValue oldVal = attrs.getValue( aid );
            IAtomicValue newVal = newAttrs.getByKey( aid );
            attrs.setValue( aid, newVal );
            Gwid paramGwid = Gwid.createAttr( origObjSkid.classId(), origObjSkid.strid(), aid );
            SkEvent e = makeAttrChangeEvent( paramGwid, aReason, timestamp, oldVal, newVal );
            events.add( e );
          }
        }
        // занесем новые атрибуты в объект
        DtoObject dtoObj = new DtoObject( compObjSkid, attrs, IStringMap.EMPTY );
        caOs.defineObject( dtoObj );
        //
        // теперь - связи
        //
        IStringMap<ISkidList> mapLinks = aValues.getLinkParamsOfObj( origObjSkid );
        for( String lid : mapLinks.keys() ) {
          if( !cinf.links().list().hasKey( lid ) ) {
            continue;
          }
          ISkidList oldValue = ISkidList.EMPTY;
          if( oldCompObj != null ) {
            oldValue = caLs.getLinkFwd( compObjSkid, lid ).rightSkids();
          }
          ISkidList newValue = mapLinks.getByKey( lid );
          caLs.setLink( compObjSkid, lid, newValue );
          Gwid paramGwid = Gwid.createLink( origObjSkid.classId(), origObjSkid.strid(), lid );
          SkEvent e = makeLinkChangeEvent( paramGwid, aReason, timestamp, oldValue, newValue );
          events.add( e );
        }
      }
      coreApi.eventService().fireEvents( events );
    }
    finally {
      rriService.resumeExternalValidation();
    }
  }

}
