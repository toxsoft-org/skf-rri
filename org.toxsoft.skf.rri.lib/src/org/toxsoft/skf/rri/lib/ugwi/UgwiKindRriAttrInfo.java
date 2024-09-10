package org.toxsoft.skf.rri.lib.ugwi;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;
import static org.toxsoft.skf.rri.lib.ugwi.ISkResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.av.metainfo.*;
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
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * UGWI kind: Sk-object RRI attribute info.
 * <p>
 * Format is 3-branches {@link IdChain}: "sectionId/classId/attrId".
 *
 * @author vs
 */
public class UgwiKindRriAttrInfo
    extends AbstractUgwiKind<IDtoAttrInfo> {

  /**
   * The index of the RRI section ID in the {@link IdChain} made from {@link Ugwi#essence()}.
   */
  public static final int IDX_SECTION_ID = 0;

  /**
   * The index of the class ID in the {@link IdChain} made from {@link Ugwi#essence()}.
   */
  public static final int IDX_CLASS_ID = 1;

  /**
   * The index of the attribute ID in the {@link IdChain} made from {@link Ugwi#essence()}.
   */
  public static final int IDX_ATTR_ID = 2;

  /**
   * Number of branches in {@link IdChain}.
   */
  private static final int NUM_BRANCHES = 3;

  /**
   * {@link ISkUgwiKind} implementation.
   *
   * @author hazard157
   */
  public static class Kind
      extends AbstractSkUgwiKind<IDtoAttrInfo> {

    Kind( AbstractUgwiKind<IDtoAttrInfo> aRegistrator, ISkCoreApi aCoreApi ) {
      super( aRegistrator, aCoreApi );
    }

    @Override
    public IDtoAttrInfo doFindContent( Ugwi aUgwi ) {
      ISkRegRefInfoService rriServ = coreApi().getService( ISkRegRefInfoService.SERVICE_ID );
      ISkRriSection rriSection = rriServ.findSection( getSectionId( aUgwi ) );
      IDtoRriParamInfo paramInfo = rriSection.listParamInfoes( getClassId( aUgwi ) ).findByKey( getAttrId( aUgwi ) );
      if( paramInfo != null ) {
        return paramInfo.attrInfo();
      }
      return null;
    }

    @Override
    protected boolean doCanRegister( SkCoreServUgwis aUgwiService ) {
      return true;
    }

    @Override
    protected boolean doIsNaturalAtomicValue( Ugwi aUgwi ) {
      return false;
    }

    @Override
    protected IAtomicValue doFindAtomicValue( Ugwi aUgwi ) {
      IDtoAttrInfo attrInfo = doFindContent( aUgwi );
      if( attrInfo != null ) {
        return AvUtils.avValobj( attrInfo );
      }
      return IAtomicValue.NULL;
    }

    @Override
    protected IDataType doGetAtomicValueDataType( Ugwi aUgwi ) {
      ISkRegRefInfoService rriServ = coreApi().getService( ISkRegRefInfoService.SERVICE_ID );
      ISkRriSection rriSection = rriServ.findSection( getSectionId( aUgwi ) );
      IDtoRriParamInfo paramInfo = rriSection.listParamInfoes( getClassId( aUgwi ) ).findByKey( getAttrId( aUgwi ) );
      if( paramInfo != null ) {
        return paramInfo.attrInfo().dataType();
      }
      return null;
    }

  }

  /**
   * The UGWI kind ID.
   */
  public static final String KIND_ID = SK_ID + ".rri.attr.info"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final UgwiKindRriAttrInfo INSTANCE = new UgwiKindRriAttrInfo();

  /**
   * Constructor.
   */
  private UgwiKindRriAttrInfo() {
    super( KIND_ID, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_UK_ATTR_INFO, //
        TSID_DESCRIPTION, STR_UK_ATTR_INFO_D //
    ) );
  }

  // ------------------------------------------------------------------------------------
  // AbstractUgwiKind
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
   * Creates the UGWI of this kind.
   *
   * @param aSectionId String - RRI section ID
   * @param aClassId String - class ID
   * @param aAttrId String - attribute ID
   * @return {@link Ugwi} - created UGWI
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public static Ugwi makeUgwi( String aSectionId, String aClassId, String aAttrId ) {
    StridUtils.checkValidIdPath( aSectionId );
    StridUtils.checkValidIdPath( aClassId );
    StridUtils.checkValidIdPath( aAttrId );
    IdChain chain = new IdChain( aSectionId, aClassId, aAttrId );
    return Ugwi.of( KIND_ID, chain.canonicalString() );
  }

  /**
   * Creates the UGWI of {@link UgwiKindRriAttr} kind.
   *
   * @param aSectionId String - RRI section ID
   * @param aClassId String - class ID
   * @param aObjStrid String - object STRID
   * @param aAttrId String - attribute ID
   * @return {@link Ugwi} - created UGWI
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public static Ugwi makeUgwi( String aSectionId, String aClassId, String aObjStrid, String aAttrId ) {
    StridUtils.checkValidIdPath( aSectionId );
    StridUtils.checkValidIdPath( aClassId );
    StridUtils.checkValidIdPath( aObjStrid );
    StridUtils.checkValidIdPath( aAttrId );
    IdChain chain = new IdChain( aSectionId, aClassId, aObjStrid, aAttrId );
    return Ugwi.of( UgwiKindRriAttr.KIND_ID, chain.canonicalString() );
  }

  /**
   * Creates the UGWI of {@link UgwiKindRriAttr} kind.
   *
   * @param aSectionId String - RRI section ID
   * @param aObjSkid {@link Skid} - SKID of the object
   * @param aAttrId String - attribute ID
   * @return {@link Ugwi} - created UGWI
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public static Ugwi makeUgwi( String aSectionId, Skid aObjSkid, String aAttrId ) {
    TsNullArgumentRtException.checkNull( aObjSkid );
    TsIllegalArgumentRtException.checkTrue( aObjSkid == Skid.NONE );
    StridUtils.checkValidIdPath( aAttrId );
    IdChain chain = new IdChain( aSectionId, aObjSkid.classId(), aObjSkid.strid(), aAttrId );
    return Ugwi.of( UgwiKindRriAttr.KIND_ID, chain.canonicalString() );
  }

}
