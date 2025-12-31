package org.toxsoft.skf.rri.lib.ugwi;

import static org.toxsoft.core.tslib.av.EAtomicType.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;
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
import org.toxsoft.core.tslib.gw.gwid.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.gw.ugwi.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.uskat.core.*;
import org.toxsoft.uskat.core.api.ugwis.*;
import org.toxsoft.uskat.core.impl.*;

/**
 * UGWI kind: RRI (Regulatory Reference Info) link value.
 * <p>
 * Format is 4-branches {@link IdChain}: "rriSectionId/classId/objStrid/linkId".
 * <p>
 * {@link ISkRriSection#getLinkParamValue(Skid, String)} is used to retrieve RRI link value.
 *
 * @author dima
 */
public class UgwiKindRriLink
    extends AbstractUgwiKind<ISkidList> {

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
   * The index of the link ID in the {@link IdChain} made from {@link Ugwi#essence()}.
   */
  public static final int IDX_LINK_ID = 3;

  /**
   * Number of branches in {@link IdChain}.
   */
  private static final int NUM_BRANCHES = 4;

  private static final IDataType DT_SKID_LIST = DataType.create( VALOBJ, //
      // TSID_NAME, STR_PROP_SKID, //
      // TSID_DESCRIPTION, STR_PROP_SKID_D, //
      TSID_KEEPER_ID, SkidListKeeper.KEEPER_ID, //
      TSID_DEFAULT_VALUE, avValobj( ISkidList.EMPTY ) //
  );

  /**
   * {@link ISkUgwiKind} implementation.
   *
   * @author dima
   */
  public static class Kind
      extends AbstractSkUgwiKind<ISkidList> {

    Kind( AbstractUgwiKind<ISkidList> aStaticKind, ISkCoreApi aCoreApi ) {
      super( aStaticKind, aCoreApi );
    }

    @Override
    protected ISkidList doFindContent( Ugwi aUgwi ) {
      IdChain chain = IdChain.of( aUgwi.essence() );
      String sectId = chain.get( IDX_SECTION_ID );
      ISkRegRefInfoService rriServ = (ISkRegRefInfoService)coreApi().getService( ISkRegRefInfoService.SERVICE_ID );
      ISkRriSection rriSect = rriServ.findSection( sectId );
      if( rriSect != null ) {
        Skid skid = new Skid( chain.get( IDX_CLASS_ID ), chain.get( IDX_OBJ_STRID ) );
        return rriSect.getLinkParamValue( skid, chain.get( IDX_LINK_ID ) );
      }
      return null;
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
      return DT_SKID_LIST;
    }

  }

  /**
   * The UGWI kind ID.
   */
  public static final String KIND_ID = SK_ID + ".rri.link"; //$NON-NLS-1$

  /**
   * The singleton instance.
   */
  public static final UgwiKindRriLink INSTANCE = new UgwiKindRriLink();

  /**
   * Constructor.
   */
  private UgwiKindRriLink() {
    super( KIND_ID, true, OptionSetUtils.createOpSet( //
        TSID_NAME, STR_UK_RRI_LINK, //
        TSID_DESCRIPTION, STR_UK_RRI_LINK_D //
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

  @Override
  protected Gwid doGetGwid( Ugwi aUgwi ) {
    IdChain chain = IdChain.of( aUgwi.essence() );
    String classId = chain.get( IDX_CLASS_ID );
    String objStrid = chain.get( IDX_OBJ_STRID );
    String attrId = chain.get( IDX_LINK_ID );
    return Gwid.createLink( classId, objStrid, attrId );
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
   * Extracts link ID from the UGWI of this kind.
   *
   * @param aUgwi {@link Ugwi} - the UGWI
   * @return String - the link ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException invalid UGWI for this kind
   */
  public static String getAttrId( Ugwi aUgwi ) {
    TsValidationFailedRtException.checkError( INSTANCE.validateUgwi( aUgwi ) );
    IdChain chain = IdChain.of( aUgwi.essence() );
    return chain.get( IDX_LINK_ID );
  }

  /**
   * Creates the UGWI of this kind.
   *
   * @param aSectId String - the RRI section ID
   * @param aClassId String - class ID
   * @param aObjStrid String - object STRID
   * @param aLinkId String - link ID
   * @return {@link Ugwi} - created UGWI
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public static Ugwi makeUgwi( String aSectId, String aClassId, String aObjStrid, String aLinkId ) {
    StridUtils.checkValidIdPath( aSectId );
    StridUtils.checkValidIdPath( aClassId );
    StridUtils.checkValidIdPath( aObjStrid );
    StridUtils.checkValidIdPath( aLinkId );
    IdChain chain = new IdChain( aSectId, aClassId, aObjStrid, aLinkId );
    return Ugwi.of( KIND_ID, chain.canonicalString() );
  }

  /**
   * Creates the UGWI of this kind.
   *
   * @param aSectId String - the RRI section ID
   * @param aObjSkid {@link Skid} - SKID of the object
   * @param aLinkId String - link ID
   * @return {@link Ugwi} - created UGWI
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsIllegalArgumentRtException any ID is not an IDpath
   */
  public static Ugwi makeUgwi( String aSectId, Skid aObjSkid, String aLinkId ) {
    StridUtils.checkValidIdPath( aSectId );
    TsNullArgumentRtException.checkNull( aObjSkid );
    TsIllegalArgumentRtException.checkTrue( aObjSkid == Skid.NONE );
    StridUtils.checkValidIdPath( aLinkId );
    IdChain chain = new IdChain( aSectId, aObjSkid.classId(), aObjSkid.strid(), aLinkId );
    return Ugwi.of( KIND_ID, chain.canonicalString() );
  }

}
