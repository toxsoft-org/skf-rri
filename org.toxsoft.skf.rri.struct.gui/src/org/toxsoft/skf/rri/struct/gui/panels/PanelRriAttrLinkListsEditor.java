package org.toxsoft.skf.rri.struct.gui.panels;

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.bricks.ctx.impl.*;
import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.panels.*;
import org.toxsoft.core.tsgui.utils.layout.*;
import org.toxsoft.core.tslib.utils.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.skf.rri.lib.*;
import org.toxsoft.skf.rri.struct.gui.km5.*;
import org.toxsoft.uskat.core.api.sysdescr.dto.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.conn.*;

/**
 * Панель редактирования списков атрибутов и связей конкретного класса.
 *
 * @author max
 */
public class PanelRriAttrLinkListsEditor
    extends TsPanel {

  private IM5CollectionPanel<IDtoAttrInfo> attrPanel;

  private AttributeLifeCycleManager alm;

  private IM5CollectionPanel<IDtoLinkInfo> linkPanel;

  private LinkLifeCycleManager llm;

  final ISkConnection conn;

  /**
   * Конструктор панели.
   * <p>
   * Конструктор просто запоминает ссылку на контекст, без создания копии.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aContext {@link ITsGuiContext} - контекст панели
   * @throws TsNullArgumentRtException любой аргумент = null
   */
  public PanelRriAttrLinkListsEditor( Composite aParent, ITsGuiContext aContext ) {
    super( aParent, aContext );

    this.setLayout( new BorderLayout() );
    ISkConnectionSupplier connSup = aContext.get( ISkConnectionSupplier.class );
    conn = connSup.defConn();

    IM5Domain m5 = conn.scope().get( IM5Domain.class );

    SashForm rightPane = new SashForm( aParent, SWT.VERTICAL );

    AttributeModel attrModel = (AttributeModel)m5.getModel( AttributeModel.MODEL_ID, IDtoAttrInfo.class );

    ITsGuiContext ctx = new TsGuiContext( aContext );

    ISkRegRefInfoService rriService =
        (ISkRegRefInfoService)conn.coreApi().services().getByKey( ISkRegRefInfoService.SERVICE_ID );
    alm = new AttributeLifeCycleManager( ctx, attrModel, rriService );

    attrPanel = attrModel.panelCreator().createCollEditPanel( ctx, alm.itemsProvider(), alm );
    attrPanel.createControl( rightPane );
    attrPanel.refresh();

    ITsGuiContext lCtx = new TsGuiContext( aContext );

    LinkModel linkModel = (LinkModel)m5.getModel( LinkModel.MODEL_ID, IDtoLinkInfo.class );
    llm = new LinkLifeCycleManager( lCtx, linkModel, rriService );

    linkPanel = linkModel.panelCreator().createCollEditPanel( lCtx, llm.itemsProvider(), llm );
    linkPanel.createControl( rightPane );
    linkPanel.refresh();

  }

  /**
   * Устанавливает класс, для которого редактируются списки атрибутов и связей
   *
   * @param aClassId String - идентификатор класса
   */
  public void setClassId( String aClassId ) {
    alm.setClassId( aClassId );
    attrPanel.refresh();

    llm.setClassId( aClassId );
    linkPanel.refresh();
  }

  /**
   * Устанавливает идентификатор редактируемой секции НСИ.
   *
   * @param aSectionId String - идентификатор редактируемой секции НСИ.
   */
  public void setSectionId( String aSectionId ) {
    alm.setSectionId( aSectionId );
    alm.setClassId( TsLibUtils.EMPTY_STRING );
    attrPanel.refresh();
    llm.setSectionId( aSectionId );
    llm.setClassId( TsLibUtils.EMPTY_STRING );
    linkPanel.refresh();
  }

}
