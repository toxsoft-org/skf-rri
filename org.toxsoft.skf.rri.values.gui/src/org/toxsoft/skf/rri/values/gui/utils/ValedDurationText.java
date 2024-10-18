package org.toxsoft.skf.rri.values.gui.utils;

import static org.toxsoft.core.tslib.av.metainfo.IAvMetaConstants.*;

import org.eclipse.swt.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.valed.impl.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;

/**
 * Редактор длительности, выраженной в минутах и секундах.
 *
 * @author max
 */
public class ValedDurationText
    extends AbstractValedControl<IAtomicValue, Control> {

  /**
   * Фабрика редактора.
   */
  public static final AbstractValedControlFactory FACTORY = new ValedDurationTextFactory();

  /**
   * Подложка для поля текстового ввода
   */
  private TsComposite backplane = null;

  /**
   * Собсно само поле для ввода значений
   */
  Text ssText = null;

  Text mmText = null;

  /**
   * введенное пользователм значение
   */
  IAtomicValue mmValue = IAtomicValue.NULL;

  IAtomicValue ssValue = IAtomicValue.NULL;

  /**
   * Конструкторe.
   *
   * @param aContext {@link ITsGuiContext} - контекст редактора
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public ValedDurationText( ITsGuiContext aContext ) {
    super( aContext );
  }

  private void recreateWidgets() {
    backplane.setLayoutDeferred( true );
    try {
      String ssSavedText = TsLibUtils.EMPTY_STRING;
      if( ssText != null ) {
        ssSavedText = ssText.getText();
        ssText.dispose();
        ssText = null;
      }

      String mmSavedText = TsLibUtils.EMPTY_STRING;
      if( mmText != null ) {
        mmSavedText = mmText.getText();
        mmText.dispose();
        mmText = null;
      }
      int style = SWT.BORDER;

      mmText = new Text( backplane, style );
      GridData gridData1 = new GridData();
      gridData1.horizontalAlignment = GridData.FILL;
      gridData1.grabExcessHorizontalSpace = true;
      mmText.setLayoutData( gridData1 );

      Label mmLabel = new Label( backplane, SWT.NONE );
      mmLabel.setText( " min, " );

      ssText = new Text( backplane, style );
      GridData gridData = new GridData();
      gridData.horizontalAlignment = GridData.FILL;
      gridData.grabExcessHorizontalSpace = true;
      ssText.setLayoutData( gridData );

      mmText.addVerifyListener( aEvent -> {
        // проверяем на пустую строку
        if( aEvent.text.trim().length() == 0 ) {
          aEvent.doit = true;
        }
        else {
          // get old text and create new text by using the VerifyEvent.text
          final String oldS = mmText.getText();
          String newS = oldS.substring( 0, aEvent.start ) + aEvent.text + oldS.substring( aEvent.end );

          boolean isInteger = true;
          try {
            Integer.parseInt( newS );
          }
          catch( @SuppressWarnings( "unused" ) NumberFormatException ex ) {
            isInteger = false;
          }

          if( !isInteger ) {
            aEvent.doit = false;
          }
        }
      } );

      ssText.addVerifyListener( aEvent -> {
        // проверяем на пустую строку
        if( aEvent.text.trim().length() == 0 ) {
          aEvent.doit = true;
        }
        else {
          // get old text and create new text by using the VerifyEvent.text
          final String oldS = ssText.getText();
          String newS = oldS.substring( 0, aEvent.start ) + aEvent.text + oldS.substring( aEvent.end );

          boolean isInteger = true;
          try {
            Integer.parseInt( newS );
          }
          catch( @SuppressWarnings( "unused" ) NumberFormatException ex ) {
            isInteger = false;
          }

          if( !isInteger ) {
            aEvent.doit = false;
          }
        }
      } );

      Label label = new Label( backplane, SWT.NONE );
      label.setText( " sec " );

      String msg = params().getStr( TSID_DEFAULT_VALUE, null );
      if( msg != null ) {
        ssText.setMessage( msg );
      }
      internalSetValueToWidget( ssText, ssSavedText );
      internalSetValueToWidget( mmText, mmSavedText );

      // text.setToolTipText( getTooltipText() );
      ssText.addModifyListener( aE -> {
        if( ssText.getText().trim().length() == 0 ) {
          ssValue = IAtomicValue.NULL;
        }
        else {
          ssValue = AvUtils.avInt( Integer.parseInt( ssText.getText() ) );
        }
        fireModifyEvent( true );
      } );

      mmText.addModifyListener( aE -> {
        if( mmText.getText().trim().length() == 0 ) {
          mmValue = IAtomicValue.NULL;
        }
        else {
          mmValue = AvUtils.avInt( Integer.parseInt( mmText.getText() ) );
        }
        fireModifyEvent( true );
      } );

      ssText.addFocusListener( notifyEditFinishedOnFocusLostListener );
      ssText.setEditable( isEditable() );

      mmText.addFocusListener( notifyEditFinishedOnFocusLostListener );
      mmText.setEditable( isEditable() );
    }
    finally {
      backplane.setLayoutDeferred( false );
      backplane.getParent().layout( true );
      backplane.layout( true );
    }
  }

  private void displayValue() {
    if( mmValue.isAssigned() ) {
      mmText.setText( mmValue.asString() );
    }
    else {
      mmText.setText( TsLibUtils.EMPTY_STRING );
    }

    if( ssValue.isAssigned() ) {
      ssText.setText( ssValue.asString() );
    }
    else {
      ssText.setText( TsLibUtils.EMPTY_STRING );
    }
  }

  @Override
  protected Control doCreateControl( Composite aParent ) {
    backplane = new TsComposite( aParent );
    GridLayout layout = new GridLayout( 4, false );
    layout.marginLeft = 0;
    layout.marginRight = 0;
    layout.marginBottom = 0;
    layout.marginTop = 0;
    layout.marginHeight = 0;
    layout.marginWidth = 0;
    backplane.setLayout( layout );
    recreateWidgets();
    return backplane;
  }

  @Override
  protected void doSetEditable( boolean aEditable ) {
    if( isWidget() ) {
      ssText.setEditable( isEditable() );
      mmText.setEditable( isEditable() );
    }
  }

  @Override
  protected void doClearValue() {
    mmValue = IAtomicValue.NULL;
    ssValue = IAtomicValue.NULL;
    displayValue();
  }

  private void internalSetValueToWidget( Text aText, String aValue ) {
    setSelfEditing( true );
    try {
      aText.setText( aValue );
    }
    finally {
      setSelfEditing( false );
    }
  }

  @Override
  protected void doSetUnvalidatedValue( IAtomicValue aValue ) {
    if( aValue == null || aValue == IAtomicValue.NULL || !aValue.isAssigned() ) {
      mmValue = IAtomicValue.NULL;
      ssValue = IAtomicValue.NULL;
    }
    else {
      long time = aValue.asLong();
      long mm = time / 60;
      long ss = time % 60;
      mmValue = AvUtils.avInt( mm );
      ssValue = AvUtils.avInt( ss );
    }

    displayValue();
  }

  @Override
  protected IAtomicValue doGetUnvalidatedValue() {
    int time = 0;
    boolean isNull = true;
    if( mmValue != IAtomicValue.NULL ) {
      time += mmValue.asLong() * 60;
      isNull = false;
    }
    if( ssValue != IAtomicValue.NULL ) {
      time += ssValue.asLong();
      isNull = false;
    }
    return isNull ? IAtomicValue.NULL : AvUtils.avInt( time );
  }
}
