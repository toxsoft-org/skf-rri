package org.toxsoft.skf.rri.struct.gui.panels;

import org.eclipse.jface.dialogs.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.utils.layout.*;

/**
 * Диалог добавления атрибутов или связей НСИ для класса
 *
 * @author max
 */
public class EditListsDialog
    extends TitleAreaDialog {

  private ITsGuiContext context;

  private String sectionId;

  private String classId;

  protected EditListsDialog( Shell aParentShell, ITsGuiContext aContext, String aSectionId, String aClassId ) {
    super( aParentShell );

    setTitle( "Добавьте хотя бы один атрибут или связь НСИ" );

    context = aContext;
    sectionId = aSectionId;
    classId = aClassId;

    setShellStyle( SWT.RESIZE | SWT.CLOSE );

  }

  @Override
  protected Control createDialogArea( Composite aParent ) {
    getShell().setText( "Добавление атрибутов и связей НСИ" );
    Composite area = (Composite)super.createDialogArea( aParent );
    Composite container = new Composite( area, SWT.NONE );
    container.setLayoutData( new GridData( GridData.FILL_BOTH ) );
    container.setLayout( new BorderLayout() );

    PanelRriAttrLinkListsEditor listsEditor = new PanelRriAttrLinkListsEditor( area, context );
    // Control result = listsEditor.createControl( container );
    listsEditor.setSectionId( sectionId );
    listsEditor.setClassId( classId );
    listsEditor.setSize( 600, 700 );
    listsEditor.setLayoutData( BorderLayout.CENTER );

    return listsEditor;
  }

  @Override
  protected Control createButtonBar( Composite aParent ) {
    Control result = super.createButtonBar( aParent );
    getButton( IDialogConstants.OK_ID ).setText( "Close" );
    getButton( IDialogConstants.CANCEL_ID ).setVisible( false );
    return result;
  }

  @Override
  protected Rectangle getConstrainedShellBounds( Rectangle aPreferredSize ) {
    Rectangle newBounds =
        new Rectangle( aPreferredSize.x, aPreferredSize.y, aPreferredSize.width, aPreferredSize.height );

    newBounds.width = 400;
    newBounds.height = 600;

    return newBounds;
  }

}
