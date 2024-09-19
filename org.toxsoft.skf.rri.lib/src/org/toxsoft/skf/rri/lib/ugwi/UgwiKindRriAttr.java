package org.toxsoft.skf.rri.lib.ugwi;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.rri.lib.ugwi.ISkResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.metainfo.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * UGWI kind: RRI (Regulatory Reference Info) attribute value.
 * <p>
 * Format is 4-branches {@link IdChain}: "rriSectionId/classId/objStrid/attrId".
 * <p>
 * {@link ISkRriSection#getAttrParamValue(Skid, String)} is used to retrieve RRI attribute value.
 *
 * @author hazard157
 */
public class UgwiKindRriAttr
    extends AbstractUgwiKind<IAtomicValue> {

  /**
   * The index of the RRI section ID in the {@link IdChain} made from {@link Ugwi#essence()}.
   */
  public static final int IDX_SECTION_ID = 0;

  /**
   * The index of the class ID in the {@link IdChain} made from {@link Ugwi#essence()}.
   */
  public static final int IDX_CLASS_ID = 1;

  /**
   * The index of the object STRID in the {@link IdChain} made from {@link Ugwi#essence()}.
   */
  public static final int IDX_OBJ_STRID = 2;

  /**
   * The index of the attribute ID in the {@link IdChain} made from {@link Ugwi#essence()}.
   */
  public static final int IDX_ATTR_ID = 3;

  /**
   * Number of branches in {@link IdChain}.
   */
  private static final int NUM_BRANCHES = 4;

  /**
   * {@link ISkUgwiKind} implementation.
   *
   * @author hazard157
   */
  public static class RriAttrKind
      extends AbstractSkUgwiKind<IAtomicValue> {

    RriAttrKind( AbstractUgwiKind<IAtomicValue> aRegistrator, ISkCoreApi aCoreApi ) {
      super( aRegistrator, aCoreApi );
    }

    @Override
    protected IAtomicValue doFindContent( Ugwi aUgwi ) {
      IdChain chain = IdChain.of( aUgwi.essence() );
      String sectId = chain.get( IDX_SECTION_ID );
      ISkRegRefInfoService rriServ = (ISkRegRefInfoService)coreApi().getService( ISkRegRefInfoService.SERVICE_ID );
      ISkRriSection rriSect = rriServ.findSection( sectId );
      if( rriSect != null ) {
        Skid skid = new Skid( chain.get( IDX_CLASS_ID ), chain.get( IDX_OBJ_STRID ) );
        return rriSect.getAttrParamValue( skid, chain.get( IDX_ATTR_ID ) );
      }
      return IAtomicValue.NULL;
    }

    @Override
    protected boolean doCanRegister( SkCoreServUgwis aUgwiService ) {
      return coreApi().services().hasKey( ISkRegRefInfoService.SERVICE_ID );
    }

    @Override
    protected boolean doIsNaturalAtomicValue( Ugwi aUgwi ) {
      return true;
    }

    @Override
    protected IAtomicValue doFindAtomicValue( Ugwi aUgwi ) {
      return doFindAtomicValue( aUgwi );
    }

    @Override
    protected IDataType doGetAtomicValueDataType( Ugwi aUgwi ) {
      IdChain chain = IdChain.of( aUgwi.essence() );
      String sectId = chain.get( IDX_SECTION_ID );
      ISkRegRefInfoService rriServ = (ISkRegRefInfoService)coreApi().getService( ISkRegRefInfoService.SERVICE_ID );
      ISkRriSection rriSect = rriServ.findSection( sectId );
      if( rriSect != null ) {
        String classId = chain.get( IDX_CLASS_ID );
        if( rriSect.listClassIds().hasElem( classId ) ) {
          String attrId = chain.get( IDX_ATTR_ID );
          IDtoRriParamInfo paramInfo = rriSect.listParamInfoes( classId ).findByKey( attrId );
          if( paramInfo != null ) {
            return paramInfo.attrInfo().dataType();
          }
        }
      }
      return null;
    }

  }

  /**
   * The UGWI kind ID.
   */
  public static final String KIND_ID = SK_ID + ".rri.attr"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final UgwiKindRriAttr INSTANCE = new UgwiKindRriAttr();

  /**
   * Constructor.
   */
  private UgwiKindRriAttr() {
    super( KIND_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_UK_RRI_ATTR, //
        TSID_DESCRIPTION, STR_UK_RRI_ATTR_D //
    ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractUgwiKindRegistrator
  //

  @Override
  protected ValidationResult doValidateEssence( String aEssence ) {
    if( IdChain.isValidCanonicalString( aEssence ) ) {
      IdChain chain = IdChain.of( aEssence );
      if( chain != IdChain.NULL ) {
        if( chain.branches().size() == NUM_BRANCHES ) {
          return ValidationResult.SUCCESS;
        }
      }
    }
    return makeGeneralInvalidEssenceVr( aEssence );
  }

  @Override
  protected AbstractSkUgwiKind<?> doCreateUgwiKind( ISkCoreApi aSkConn ) {
    return new RriAttrKind( this, aSkConn );
  }

  // ------------------------------------------------------------------------------------
  // API
  //

  /**
   * Extracts RRI section ID from the UGWI of this kind.
   *
   * @param aUgwi {@link Ugwi} - the UGWI
   * @return String - the RRI section ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException invalid UGWI for this kind
   */
  public static String getSectionId( Ugwi aUgwi ) {
    TsValidationFailedRtException.checkError( INSTANCE.validateUgwi( aUgwi ) );
    IdChain chain = IdChain.of( aUgwi.essence() );
    return chain.get( IDX_SECTION_ID );
  }

  /**
   * Extracts class ID from the UGWI of this kind.
   *
   * @param aUgwi {@link Ugwi} - the UGWI
   * @return String - the class ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException invalid UGWI for this kind
   */
  public static String getClassId( Ugwi aUgwi ) {
    TsValidationFailedRtException.checkError( INSTANCE.validateUgwi( aUgwi ) );
    IdChain chain = IdChain.of( aUgwi.essence() );
    return chain.get( IDX_CLASS_ID );
  }

  /**
   * Extracts object STRID from the UGWI of this kind.
   *
   * @param aUgwi {@link Ugwi} - the UGWI
   * @return String - the object STRID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException invalid UGWI for this kind
   */
  public static String getObjStrid( Ugwi aUgwi ) {
    TsValidationFailedRtException.checkError( INSTANCE.validateUgwi( aUgwi ) );
    IdChain chain = IdChain.of( aUgwi.essence() );
    return chain.get( IDX_OBJ_STRID );
  }

  /**
   * Extracts object SKID from the UGWI of this kind.
   *
   * @param aUgwi {@link Ugwi} - the UGWI
   * @return {@link Skid} - the object SKID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException invalid UGWI for this kind
   */
  public static Skid getSkid( Ugwi aUgwi ) {
    TsValidationFailedRtException.checkError( INSTANCE.validateUgwi( aUgwi ) );
    IdChain chain = IdChain.of( aUgwi.essence() );
    return new Skid( chain.get( IDX_CLASS_ID ), chain.get( IDX_OBJ_STRID ) );
  }

  /**
   * Extracts attribute ID from the UGWI of this kind.
   *
   * @param aUgwi {@link Ugwi} - the UGWI
   * @return String - the attribute ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException invalid UGWI for this kind
   */
  public static String getAttrId( Ugwi aUgwi ) {
    TsValidationFailedRtException.checkError( INSTANCE.validateUgwi( aUgwi ) );
    IdChain chain = IdChain.of( aUgwi.essence() );
    return chain.get( IDX_ATTR_ID );
  }

  /**
   * Extracts Gwid from the UGWI of this kind.
   *
   * @param aUgwi {@link Ugwi} - the UGWI
   * @return {@link Gwid} - the GWID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException invalid UGWI for this kind
   */
  public static Gwid getGwid( Ugwi aUgwi ) {
    TsValidationFailedRtException.checkError( INSTANCE.validateUgwi( aUgwi ) );
    IdChain chain = IdChain.of( aUgwi.essence() );
    return Gwid.createAttr( getClassId( aUgwi ), chain.get( IDX_OBJ_STRID ), chain.get( IDX_ATTR_ID ) );
  }

  /**
   * Creates the UGWI of this kind.
   *
   * @param aSectId String - the RRI section ID
   * @param aGwid {@link Gwid} - concrete GWID of kind {@link EGwidKind#GW_ATTR}
   * @return {@link Ugwi} - created UGWI
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   * @throws TsIllegalArgumentRtException GWID is abstract
   * @throws TsIllegalArgumentRtException GWID is not of kind {@link EGwidKind#GW_ATTR}
   */
  public static Ugwi makeUgwi( String aSectId, Gwid aGwid ) {
    StridUtils.checkValidIdPath( aSectId );
    TsNullArgumentRtException.checkNull( aGwid );
    TsIllegalArgumentRtException.checkTrue( aGwid.isAbstract() );
    TsIllegalArgumentRtException.checkTrue( aGwid.kind() != EGwidKind.GW_ATTR );
    IdChain chain = new IdChain( aSectId, aGwid.classId(), aGwid.strid(), aGwid.propId() );
    return Ugwi.of( KIND_ID, chain.canonicalString() );
  }

  /**
   * Creates the UGWI of this kind.
   *
   * @param aSectId String - the RRI section ID
   * @param aClassId String - class ID
   * @param aObjStrid String - object STRID
   * @param aAttrId String - attribute ID
   * @return {@link Ugwi} - created UGWI
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public static Ugwi makeUgwi( String aSectId, String aClassId, String aObjStrid, String aAttrId ) {
    StridUtils.checkValidIdPath( aSectId );
    StridUtils.checkValidIdPath( aClassId );
    StridUtils.checkValidIdPath( aObjStrid );
    StridUtils.checkValidIdPath( aAttrId );
    IdChain chain = new IdChain( aSectId, aClassId, aObjStrid, aAttrId );
    return Ugwi.of( KIND_ID, chain.canonicalString() );
  }

  /**
   * Creates the UGWI of this kind.
   *
   * @param aSectId String - the RRI section ID
   * @param aObjSkid {@link Skid} - SKID of the object
   * @param aAttrId String - attribute ID
   * @return {@link Ugwi} - created UGWI
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public static Ugwi makeUgwi( String aSectId, Skid aObjSkid, String aAttrId ) {
    StridUtils.checkValidIdPath( aSectId );
    TsNullArgumentRtException.checkNull( aObjSkid );
    TsIllegalArgumentRtException.checkTrue( aObjSkid == Skid.NONE );
    StridUtils.checkValidIdPath( aAttrId );
    IdChain chain = new IdChain( aSectId, aObjSkid.classId(), aObjSkid.strid(), aAttrId );
    return Ugwi.of( KIND_ID, chain.canonicalString() );
  }

  /**
   * Возвращает признак существования объекта и НСИ атрибута, на которые указывает {@link Ugwi}.<br>
   *
   * @param aUgwi {@link Ugwi} - ИД сущности
   * @param aCoreApi {@link ISkCoreApi} - API сервера
   * @return <b>true</b> - сущность есть<br>
   *         <b>false</b> - сущность отсутствует
   */
  public static boolean isEntityExists( Ugwi aUgwi, ISkCoreApi aCoreApi ) {
    if( INSTANCE.validateUgwi( aUgwi ) != ValidationResult.SUCCESS ) {
      return false;
    }
    TsIllegalArgumentRtException.checkFalse( aUgwi.kindId().equals( KIND_ID ) );
    ISkObject skObj = aCoreApi.objService().find( getSkid( aUgwi ) );
    if( skObj != null ) {
      ISkRegRefInfoService rriServ = aCoreApi.getService( ISkRegRefInfoService.SERVICE_ID );
      ISkRriSection section = rriServ.getSection( getSectionId( aUgwi ) );
      try {
        section.getAttrParamValue( skObj.skid(), getAttrId( aUgwi ) );
        return true;
      }
      catch( Throwable e ) {
        e.printStackTrace();
      }
    }
    return false;
  }

}
