package org.toxsoft.skf.rri.values.gui.sol;

import static org.toxsoft.core.tsgui.valed.api.IValedControlConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.api.*;
import org.toxsoft.core.tsgui.valed.controls.helpers.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.api.objserv.*;

/**
 * Временный редактор RriId'а любого типа.
 * <p>
 * Allows to select {@link RriId} by accessing {@link ISkObjectService}.
 *
 * @author vs
 */
public class ValedRriIdEditor
    extends AbstractValedTextAndButton<RriId> {

  /**
   * The factory name.
   */
  public static final String FACTORY_NAME = VALED_EDNAME_PREFIX + ".RriIdEditor"; //$NON-NLS-1$

  /**
   * The factory class.
   *
   * @author hazard157
   */
  static class Factory
      extends AbstractValedControlFactory {

    protected Factory() {
      super( FACTORY_NAME );
    }

    @SuppressWarnings( "unchecked" )
    @Override
    protected IValedControl<RriId> doCreateEditor( ITsGuiContext aContext ) {
      AbstractValedControl<RriId, ?> e = new ValedRriIdEditor( aContext );
      return e;
    }

  }

  /**
   * The factory singleton.
   */
  public static final AbstractValedControlFactory FACTORY = new Factory();

  private RriId value = null;

  /**
   * Constructor for subclasses.
   *
   * @param aContext {@link ITsGuiContext} - the valed context
   * @throws TsNullArgumentRtException аргумент = null
   */
  public ValedRriIdEditor( ITsGuiContext aContext ) {
    super( aContext );
    setParamIfNull( OPDEF_IS_WIDTH_FIXED, AV_FALSE );
    setParamIfNull( OPDEF_IS_HEIGHT_FIXED, AV_TRUE );
    setParamIfNull( OPDEF_VERTICAL_SPAN, AV_1 );
  }

  @Override
  protected boolean doProcessButtonPress() {
    // create and dispaly RriId selector
    RriId rriId = PanelRriIdSelector.selectRriId( canGetValue().isOk() ? getValue() : null, tsContext() );
    if( rriId != null ) {
      value = rriId;
      return true;
    }
    return false;
  }

  // @Override
  // public ValidationResult canGetValue() {
  // try {
  // RriId.of( getTextControl().getText() );
  // return ValidationResult.SUCCESS;
  // }
  // catch( @SuppressWarnings( "unused" ) Exception ex ) {
  // return ValidationResult.error( "Неверный формат RriId" );
  // }
  // }

  @Override
  protected void doUpdateTextControl() {
    if( value != null ) {
      getTextControl().setText( value.toString() );
    }
    else {
      getTextControl().setText( TsLibUtils.EMPTY_STRING );
    }
  }

  @Override
  protected RriId doGetUnvalidatedValue() {
    return value;
  }

  @Override
  protected void doDoSetUnvalidatedValue( RriId aValue ) {
    value = aValue;
    if( value != null ) {
      getTextControl().setText( aValue.toString() );
    }
    else {
      getTextControl().setText( TsLibUtils.EMPTY_STRING );
    }
  }

}
