package org.toxsoft.skf.rri.lib;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.av.utils.*;
import org.toxsoft.core.tslib.bricks.strid.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.strid.coll.impl.*;
import org.toxsoft.core.tslib.bricks.validator.impl.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.impl.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

/**
 * The RRI section.
 *
 * @author hazard157
 */
public interface ISkRriSection
    extends IStridable, IParameterized {

  /**
   * Changes the section properties.
   *
   * @param aName String - the section name
   * @param aDescription String - the section description
   * @param aParams {@link IOptionSet} - values of the optional {@link ISkRriSection#params()}
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed validation {@link ISkRegRefInfoServiceValidator}
   */
  void setSectionProps( String aName, String aDescription, IOptionSet aParams );

  /**
   * Returns the SKID of the Sk-object internally implementing this section.
   * <p>
   * This identifier is created of pair {@link ISkRriServiceHardConstants#CLASSID_RRI_SECTION} and the section ID. This
   * SKID is the source of the RRI parameters change event {@link ISkRriServiceHardConstants#EVDTO_RRI_PARAM_CHANGE}.
   *
   * @return {@link Skid} - the SKID of the section implementation object
   */
  Skid getSectionSkid();

  // ------------------------------------------------------------------------------------
  // RRI parameters definitions

  /**
   * Defines (changes exiting or edits existing) RRI parameters.
   *
   * @param aClassId - the class ID for which the RRI parameter is defined
   * @param aInfos {@link IStridablesList}&lt;{@link IDtoRriParamInfo}&gt; - the parameter definitions
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed validation {@link ISkRegRefInfoServiceValidator}
   */
  void defineParam( String aClassId, IStridablesList<IDtoRriParamInfo> aInfos );

  /**
   * Removes the RRI parameter including all values.
   * <p>
   * Both definition and values are permanently removed.
   *
   * @param aClassId - the class ID
   * @param aParamId String - the parameter ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed validation {@link ISkRegRefInfoServiceValidator}
   */
  void removeParam( String aClassId, String aParamId );

  /**
   * Removes all RRI parameter of the specified class including all subclasses.
   * <p>
   * Both definition and values are permanently removed.
   *
   * @param aClassId String - the class ID
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed validation {@link ISkRegRefInfoServiceValidator}
   */
  void removeAll( String aClassId );

  /**
   * Clears the section.
   * <p>
   * Permanently removes all classes, parameters and values of the section.
   *
   * @throws TsValidationFailedRtException failed validation {@link ISkRegRefInfoServiceValidator}
   */
  void clearAll();

  /**
   * Returns the classes having RRI parameters defined in this section.
   *
   * @return {@link IStringList} - list of class IDs
   */
  IStringList listClassIds();

  /**
   * Returns parameter definitions of this section of the requested class.
   *
   * @param aClassId String - the class ID
   * @return {@link IStridablesList}&lt;{@link IDtoRriParamInfo}&gt; - the parameter definitions or an empty list
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException class does not exists in {@link ISkSysdescr#listClasses()}
   */
  IStridablesList<IDtoRriParamInfo> listParamInfoes( String aClassId );

  // ------------------------------------------------------------------------------------
  // RRI parameters values

  /**
   * Returns the value of the attribute parameter.
   *
   * @param aObjId {@link Skid} - the object SKID
   * @param aParamId String - the RRI parameter IO
   * @return {@link IAtomicValue} - the RII parameter value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such object or parameter exists
   * @throws TsUnsupportedFeatureRtException RRI parameter is a link, not an attribute
   */
  IAtomicValue getAttrParamValue( Skid aObjId, String aParamId );

  /**
   * Returns the value of the link parameter.
   *
   * @param aObjId {@link Skid} - the object SKID
   * @param aParamId String - the RRI parameter IO
   * @return {@link ISkidList} - the RII parameter value
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such object or parameter exists
   * @throws TsUnsupportedFeatureRtException RRI parameter is an attribute, not a link
   */
  ISkidList getLinkParamValue( Skid aObjId, String aParamId );

  /**
   * Returns the values of all RRI parameters of this section of all requested objects.
   *
   * @param aObjIds {@link ISkidList} - requested objects SKIDs
   * @return {@link ISkRriParamValues} - parameter vales
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException argument contains SKIDs of non-existing objects
   */
  ISkRriParamValues getParamValuesByObjs( ISkidList aObjIds );

  /**
   * Returns the values of all RRI parameters of this section of all objects of the requested class.
   * <p>
   * Descendant class objects are <b>not</b> included.
   *
   * @param aClassId String - the exact class ID
   * @return {@link ISkRriParamValues} - parameter vales
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsItemNotFoundRtException no such class found in {@link ISkSysdescr#listClasses()}
   */
  ISkRriParamValues getParamValuesByClassId( String aClassId );

  // ------------------------------------------------------------------------------------
  // Edit parameter values

  /**
   * Sets the value of the attribute parameter.
   *
   * @param aObjId {@link Skid} - SKID of the object to edit it's RRI parameter value
   * @param aParamId String - the RRI parameter ID
   * @param aValue {@link IAtomicValue} - new value
   * @param aReason String - the change reason
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed validation {@link ISkRegRefInfoServiceValidator}
   */
  void setAttrParamValue( Skid aObjId, String aParamId, IAtomicValue aValue, String aReason );

  /**
   * Sets the value of the link parameter.
   *
   * @param aObjId {@link Skid} - SKID of the object to edit it's RRI parameter value
   * @param aParamId String - the RRI parameter ID
   * @param aObjIds {@link ISkidList} - new value
   * @param aReason String - the change reason
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed validation {@link ISkRegRefInfoServiceValidator}
   */
  void setLinkParamValue( Skid aObjId, String aParamId, ISkidList aObjIds, String aReason );

  /**
   * Batch editing - sets several RRI parameter values at once.
   *
   * @param aValues {@link ISkRriParamValues} - the new values set
   * @param aReason String - the change reason
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed validation {@link ISkRegRefInfoServiceValidator}
   */
  void setParamValues( ISkRriParamValues aValues, String aReason );

  // ------------------------------------------------------------------------------------
  // inline methods for convenience

  /**
   * Defines (changes exiting or edits existing) RRI parameter.
   *
   * @param aClassId - the class ID for which the RRI parameter is defined
   * @param aInfo {@link IDtoRriParamInfo} - the parameter definition
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   * @throws TsValidationFailedRtException failed validation {@link ISkRegRefInfoServiceValidator}
   */
  default void defineParam( String aClassId, IDtoRriParamInfo aInfo ) {
    defineParam( aClassId, new StridablesList<>( aInfo ) );
  }

}
