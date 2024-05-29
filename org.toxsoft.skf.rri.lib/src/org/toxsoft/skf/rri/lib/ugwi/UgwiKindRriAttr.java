package org.toxsoft.skf.rri.lib.ugwi;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.rri.lib.ugwi.ISkResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.impl.*;
import org.toxsoft.core.tslib.bricks.strid.impl.*;
import org.toxsoft.core.tslib.bricks.strid.more.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.ugwis.*;

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
    extends AbstractUgwiKindRegistrator<IAtomicValue> {

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
   * {@link IUgwiKind} implementation.
   *
   * @author hazard157
   */
  public static class Kind
      extends AbstractUgwiKind<IAtomicValue> {

    Kind( AbstractUgwiKindRegistrator<IAtomicValue> aRegistrator, ISkCoreApi aCoreApi ) {
      super( aRegistrator, aCoreApi );
    }

    @Override
    protected IAtomicValue doFindContent( Ugwi aUgwi ) {
      IdChain chain = IdChain.of( aUgwi.essence() );
      String sectId = chain.branches().get( IDX_SECTION_ID );
      ISkRegRefInfoService rriServ = (ISkRegRefInfoService)coreApi().getService( ISkRegRefInfoService.SERVICE_ID );
      ISkRriSection rriSect = rriServ.findSection( sectId );
      if( rriSect != null ) {
        Skid skid = new Skid( chain.branches().get( IDX_CLASS_ID ), chain.branches().get( IDX_OBJ_STRID ) );
        return rriSect.getAttrParamValue( skid, chain.branches().get( IDX_ATTR_ID ) );
      }
      return IAtomicValue.NULL;
    }

  }

  /**
   * The UGWI kind ID.
   */
  public static final String KIND_ID = SK_ID + ".rri.attr"; //$NON-NLS-1$

  /**
   * The registrator instance.
   */
  public static final UgwiKindRriAttr REGISTRATOR = new UgwiKindRriAttr();

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
  protected AbstractUgwiKind<?> doCreateUgwiKind( ISkCoreApi aSkConn ) {
    return new Kind( this, aSkConn );
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
    TsValidationFailedRtException.checkError( REGISTRATOR.validateUgwi( aUgwi ) );
    IdChain chain = IdChain.of( aUgwi.essence() );
    return chain.branches().get( IDX_SECTION_ID );
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
    TsValidationFailedRtException.checkError( REGISTRATOR.validateUgwi( aUgwi ) );
    IdChain chain = IdChain.of( aUgwi.essence() );
    return chain.branches().get( IDX_CLASS_ID );
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
    TsValidationFailedRtException.checkError( REGISTRATOR.validateUgwi( aUgwi ) );
    IdChain chain = IdChain.of( aUgwi.essence() );
    return chain.branches().get( IDX_OBJ_STRID );
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
    TsValidationFailedRtException.checkError( REGISTRATOR.validateUgwi( aUgwi ) );
    IdChain chain = IdChain.of( aUgwi.essence() );
    return new Skid( chain.branches().get( IDX_CLASS_ID ), chain.branches().get( IDX_OBJ_STRID ) );
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
    TsValidationFailedRtException.checkError( REGISTRATOR.validateUgwi( aUgwi ) );
    IdChain chain = IdChain.of( aUgwi.essence() );
    return chain.branches().get( IDX_ATTR_ID );
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

}
