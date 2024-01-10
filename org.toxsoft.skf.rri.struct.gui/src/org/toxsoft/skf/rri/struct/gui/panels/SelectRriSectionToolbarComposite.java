package org.toxsoft.skf.rri.struct.gui.panels;

import static org.toxsoft.core.tsgui.graphics.icons.ITsStdIconIds.*;
import static org.toxsoft.skf.rri.struct.gui.panels.ISkResources.*;
import static org.toxsoft.uskat.core.ISkHardConstants.*;

import org.eclipse.jface.action.*;
import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.actions.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.dialogs.datarec.*;
import org.toxsoft.core.tsgui.graphics.icons.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.panels.toolbar.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.core.tslib.av.impl.*;
import org.toxsoft.core.tslib.bricks.geometry.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.struct.gui.km5.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Composite contains toolbar with button for selection (creation) RRI section.
 *
 * @author max
 */
public class SelectRriSectionToolbarComposite
    extends TsComposite {

  final static String ACTID_RRI_SECTION_SELECT = SK_ID + "rri.struct.edit.section.select"; //$NON-NLS-1$

  final static TsActionDef ACDEF_RRI_SECTION_SELECT = TsActionDef.ofPush2( ACTID_RRI_SECTION_SELECT,
      STR_N_SELECT_RRI_SECTION, STR_D_SELECT_RRI_SECTION, ICONID_VIEW_AS_LIST );

  private TextControlContribution textContr1;

  private ISkRriSection selectedRriSection;

  private boolean canCreate = false;

  /**
   * Constructor by parent
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @param aCanCreate true - rri sections can created.
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public SelectRriSectionToolbarComposite( Composite aParent, ITsGuiContext aContext, boolean aCanCreate ) {
    super( aParent );
    this.setLayout( new BorderLayout() );
    canCreate = aCanCreate;
    TsToolbar toolBar = new TsToolbar( aContext );
    toolBar.setIconSize( EIconSize.IS_24X24 );

    toolBar.addActionDef( ACDEF_RRI_SECTION_SELECT );

    toolBar.addSeparator();

    Control toolbarCtrl = toolBar.createControl( this );
    toolbarCtrl.setLayoutData( BorderLayout.CENTER );

    textContr1 = new TextControlContribution( "Label", 300, STR_RRI_SECTION, SWT.NONE ); //$NON-NLS-1$
    toolBar.addContributionItem( textContr1 );

    ITsGuiContext secCtx = new TsGuiContext( aContext );
    IMultiPaneComponentConstants.OPDEF_IS_ACTIONS_CRUD.setValue( secCtx.params(), AvUtils.avBool( aCanCreate ) );
    toolBar.addListener( aActionId -> {
      if( aActionId.equals( ACDEF_RRI_SECTION_SELECT.id() ) ) {
        selectRriSection( secCtx );
        return;
      }
    } );

    setRriSection( null );
  }

  private void selectRriSection( ITsGuiContext aContext ) {
    // select the section
    ISkConnectionSupplier connSup = aContext.get( ISkConnectionSupplier.class );
    ISkConnection conn = connSup.defConn();
    IM5Domain m5 = conn.scope().get( IM5Domain.class );

    ISkRegRefInfoService rriService =
        (ISkRegRefInfoService)conn.coreApi().services().getByKey( ISkRegRefInfoService.SERVICE_ID );

    IM5Model<ISkRriSection> model = m5.getModel( RriSectionModel.MODEL_ID, ISkRriSection.class );
    IM5LifecycleManager<ISkRriSection> lm = model.getLifecycleManager( rriService );
    TsDialogInfo di = new TsDialogInfo( aContext, STR_SELECTION_RRI_SECTION, STR_SELECTION_RRI_SECTION );
    // установим нормальный размер диалога
    di.setMinSize( new TsPoint( -30, -40 ) );
    ISkRriSection section = M5GuiUtils.askSelectItem( di, model, null, lm.itemsProvider(), canCreate ? lm : null );
    if( section != null ) {
      setRriSection( section );
    }
    else {
      if( selectedRriSection != null ) {
        if( !rriService.listSections().hasKey( selectedRriSection.id() ) ) {
          setRriSection( null );
        }
      }
    }
  }

  private void setRriSection( ISkRriSection aRriSection ) {
    textContr1.setText( STR_RRI_SECTION + (aRriSection != null ? aRriSection.id() : STR_NOT_SELECTED) );
    selectedRriSection = aRriSection;

    doSetRriSection( aRriSection );
  }

  /**
   * Override in subclass - action when rri section selected
   *
   * @param aRriSection ISkRriSection - selected section or null
   */
  protected void doSetRriSection( ISkRriSection aRriSection ) {
    //
  }

  static class TextControlContribution
      extends ControlContribution {

    private final int width;
    private final int swtStyle;
    private String    text;
    CLabel            label;

    /**
     * Конструктор.
     *
     * @param aId String - ИД элемента
     * @param aWidth int - ширина текстового поля
     * @param aText String - текст
     * @param aSwtStyle int - swt стиль
     */
    public TextControlContribution( String aId, int aWidth, String aText, int aSwtStyle ) {
      super( aId );
      width = aWidth;
      swtStyle = aSwtStyle;
      text = aText;
    }

    // ------------------------------------------------------------------------------------
    // ControlContribution
    //

    @Override
    protected Control createControl( Composite aParent ) {
      label = new CLabel( aParent, swtStyle );
      label.setText( text );
      label.setAlignment( SWT.LEFT );
      return label;
    }

    @Override
    protected int computeWidth( Control aControl ) {
      if( width == SWT.DEFAULT ) {
        return super.computeWidth( aControl );
      }
      return width;
    }

    // ------------------------------------------------------------------------------------
    // API
    //

    /**
     * Возвращает текстовое поле.
     *
     * @return CLabel - текстовое поле
     */
    public CLabel label() {
      return label;
    }

    void setText( String aText ) {
      label.setText( aText );
      label.redraw();
    }

  }

}
