package org.toxsoft.skf.rri.values.mws.e4.uiparts;

import static org.toxsoft.core.tsgui.m5.gui.mpc.IMultiPaneComponentConstants.*;
import static org.toxsoft.core.tslib.av.impl.AvUtils.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tsgui.widgets.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.struct.gui.km5.*;
import org.toxsoft.skf.rri.values.mws.e4.services.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;
import org.toxsoft.uskat.core.gui.e4.uiparts.*;

/**
 * Left part in perspective - a list view of rri sections.
 *
 * @author max
 */
public class UipartRriSectionList
    extends SkMwsAbstractPart {

  /**
   * Refreshes {@link #rriSectionListPanel} on changes in rri service.
   */
  private final ISkRegRefInfoServiceListener rriServiceListener =
      ( aOp, aSectionId ) -> this.rriSectionListPanel.refresh();

  private IWsRriSectionsManagementService   rrims;
  private IM5CollectionPanel<ISkRriSection> rriSectionListPanel;

  @Override
  protected void doCreateContent( TsComposite aParent ) {
    rrims = tsContext().get( IWsRriSectionsManagementService.class );
    ITsGuiContext ctx = new TsGuiContext( tsContext() );
    OPDEF_DBLCLICK_ACTION_ID.setValue( ctx.params(), AV_STR_EMPTY );
    OPDEF_IS_TOOLBAR_NAME.setValue( ctx.params(), AV_TRUE );

    ISkConnectionSupplier connSup = ctx.get( ISkConnectionSupplier.class );
    ISkConnection conn = connSup.defConn();

    ISkRegRefInfoService rriService =
        (ISkRegRefInfoService)conn.coreApi().services().getByKey( ISkRegRefInfoService.SERVICE_ID );

    IM5Model<ISkRriSection> model = m5().getModel( RriSectionModel.MODEL_ID, ISkRriSection.class );
    IM5LifecycleManager<ISkRriSection> lm = model.getLifecycleManager( rriService );

    rriSectionListPanel = model.panelCreator().createCollViewerPanel( ctx, lm.itemsProvider() );

    rriSectionListPanel.addTsDoubleClickListener( ( src, sel ) -> rrims.showRriSectionUipart( sel ) );

    rriService.eventer().addListener( rriServiceListener );
    rriSectionListPanel.createControl( aParent );
    rriSectionListPanel.getControl().setLayoutData( BorderLayout.CENTER );
    rriSectionListPanel.getControl()
        .addDisposeListener( aE -> rriService.eventer().removeListener( rriServiceListener ) );
  }

}
