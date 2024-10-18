package org.toxsoft.skf.rri.values.gui.km5;

import static org.toxsoft.skf.rri.values.gui.km5.ITsResources.*;

import org.toxsoft.core.tsgui.bricks.ctx.*;
import org.toxsoft.core.tsgui.m5.gui.mpc.impl.*;
import org.toxsoft.core.tsgui.m5.gui.panels.*;
import org.toxsoft.core.tsgui.m5.gui.panels.impl.*;
import org.toxsoft.core.tsgui.m5.model.*;
import org.toxsoft.core.tsgui.m5.model.impl.*;
import org.toxsoft.uskat.core.api.objserv.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * Модель отображения объектов ISkObject
 *
 * @author max
 */
public class ObjectCheckableM5Model
    extends KM5ModelBasic<ISkObject> {

  /**
   * Идентификатор модели.
   */
  public static final String MODEL_ID = "org.toxsoft.skf.rri.values.gui.km5.ObjectCheckableM5Model"; //$NON-NLS-1$

  /**
   * Конструктор.
   *
   * @param aConn - соединение.
   */
  public ObjectCheckableM5Model( ISkConnection aConn ) {
    super( MODEL_ID, ISkObject.class, aConn );

    setNameAndDescription( CHECKABLE_OBJECT_LIST_MODEL_NAME, CHECKABLE_OBJECT_LIST_MODEL_NAME );

    addFieldDefs( NAME, DESCRIPTION, STRID );

    setPanelCreator( new M5DefaultPanelCreator<ISkObject>() {

      @Override
      protected IM5CollectionPanel<ISkObject> doCreateCollViewerPanel( ITsGuiContext aContext,
          IM5ItemsProvider<ISkObject> aItemsProvider ) {
        // USE_FILTER_PANE.setValue( aContext.params(), DV_TRUE );
        // HAS_EDIT_ACTIONS.setValue( aContext.params(), DV_FALSE );

        MultiPaneComponentModown<ISkObject> mpc =
            new MultiPaneComponentModown<>( aContext, model(), aItemsProvider, null ) {

              // @Override
              // protected IMpcFilterPane<ISkObject> doCreateFilterPane() {
              // return new DefaultFilterPane<>( this ) {
              //
              // @Override
              // public IPolyFilter getFilter() {
              // IPolyFilter result = super.getFilter();
              // if( result == null || result == IPolyFilter.NULL ) {
              // return result;
              // }
              //
              // SingleFilterParams sfp = ((SingleFilterParams)result.params().singleParams());
              // TEXT_MATCH_MODE.setValue( sfp.params(), ETextMatchMode.REGEXP );
              //
              // return PolyFilter.create( sfp, SFF_LIST );
              // }
              //
              // };
              // }

            };
        return new M5CollectionPanelMpcModownWrapper<>( mpc, true );

      }

      @Override
      protected IM5CollectionPanel<ISkObject> doCreateCollChecksPanel( ITsGuiContext aContext,
          IM5ItemsProvider<ISkObject> aItemsProvider ) {

        IM5CollectionPanel<ISkObject> panel = super.doCreateCollChecksPanel( aContext, aItemsProvider );

        return panel;
      }

    } );
  }

}
