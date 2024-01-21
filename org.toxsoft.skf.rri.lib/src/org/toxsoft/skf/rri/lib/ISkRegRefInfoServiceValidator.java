package org.toxsoft.skf.rri.lib;

import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.opset.*;
import org.toxsoft.core.tslib.bricks.strid.coll.*;
import org.toxsoft.core.tslib.bricks.validator.*;
import org.toxsoft.core.tslib.gw.skid.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.sysdescr.*;

/**
 * {@link ISkRegRefInfoService} changes validator.
 *
 * @author hazard157
 */
public interface ISkRegRefInfoServiceValidator {

  /**
   * Checks if section can be created.
   *
   * @param aSectionId String - identifier of the section to be created
   * @param aName String - the section name
   * @param aDescription String - the section description
   * @param aParams {@link IOptionSet} - the values of the parameters
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canCreateSection( String aSectionId, String aName, String aDescription, IOptionSet aParams );

  /**
   * Checks if sections can be removed.
   *
   * @param aSectionId String - identifier of the section to be removed
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canRemoveSection( String aSectionId );

  /**
   * Checks if the section properties can be changed.
   *
   * @param aSection {@link ISkRriSection} - the section
   * @param aName String - the section name
   * @param aDescription String - the section description
   * @param aParams {@link IOptionSet} - values of the optional {@link ISkRriSection#params()}
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canSetSectionProps( ISkRriSection aSection, String aName, String aDescription, IOptionSet aParams );

  /**
   * @param aSection {@link ISkRriSection} - the section
   * @param aCompanionClassInfo {@link ISkClassInfo} - the class to be modified
   * @param aParamInfos {@link IStridablesList}&lt;{@link IDtoRriParamInfo}&gt; - the parameter definitions
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canChangeParams( ISkRriSection aSection, ISkClassInfo aCompanionClassInfo,
      IStridablesList<IDtoRriParamInfo> aParamInfos );

  /**
   * Checks if the RRI parameter can be removed.
   *
   * @param aSection {@link ISkRriSection} - the section
   * @param aClassId - the class ID
   * @param aParamId String - the parameter ID
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canRemoveParam( ISkRriSection aSection, String aClassId, String aParamId );

  /**
   * Checks if all RRI parameters of the specified class can be removed.
   *
   * @param aSection {@link ISkRriSection} - the section
   * @param aClassId String - the class ID
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canRemoveAll( ISkRriSection aSection, String aClassId );

  /**
   * Checks if the value of the attributre parameter can be set.
   *
   * @param aSection {@link ISkRriSection} - the section
   * @param aObjId {@link Skid} - SKID of the object to edit it's RRI parameter value
   * @param aParamId String - the RRI parameter ID
   * @param aValue {@link IAtomicValue} - new value
   * @param aReason String - the change reason
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canSetAttrParamValue( ISkRriSection aSection, Skid aObjId, String aParamId, IAtomicValue aValue,
      String aReason );

  /**
   * Checks if the value of the link parameter can be set.
   *
   * @param aSection {@link ISkRriSection} - the section
   * @param aObjId {@link Skid} - SKID of the object to edit it's RRI parameter value
   * @param aParamId String - the RRI parameter ID
   * @param aObjIds {@link ISkidList} - new value
   * @param aReason String - the change reason
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canSetLinkParamValue( ISkRriSection aSection, Skid aObjId, String aParamId, ISkidList aObjIds,
      String aReason );

  /**
   * Checks if batch editing can be performed.
   *
   * @param aSection {@link ISkRriSection} - the section
   * @param aValues {@link ISkRriParamValues} - the new values set
   * @param aReason String - the change reason
   * @return {@link ValidationResult} - the check result
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  ValidationResult canSetParamValues( ISkRriSection aSection, ISkRriParamValues aValues, String aReason );

}
