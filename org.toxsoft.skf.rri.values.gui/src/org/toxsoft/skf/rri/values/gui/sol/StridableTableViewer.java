package org.toxsoft.skf.rri.values.gui.sol;

import static org.toxsoft.skf.rri.values.gui.sol.ISkResources.*;

import org.eclipse.jface.viewers.*;
import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.toxsoft.core.tslib.bricks.strid.*;

/**
 * Вспомогательный просмотрщик {@link IStridable} сущностей в таблице с тремя колонками:
 * <ul>
 * <li>ИД</li>
 * <li>Наименование</li>
 * <li>Описание</li>
 * </ul>
 *
 * @author vs
 */
public class StridableTableViewer {

  private final TableViewer viewer;

  /**
   * Конструктор.
   *
   * @param aParent {@link Composite} - родительская панель
   * @param aStyle int - SWT стиль
   * @param aIdWidth - ширина колонки "ИД"
   * @param aNameWidth - ширина колонки "Наименование"
   * @param aDescrWidth - ширина колонки "Описание"
   */
  public StridableTableViewer( Composite aParent, int aStyle, int aIdWidth, int aNameWidth, int aDescrWidth ) {
    viewer = new TableViewer( aParent, aStyle );

    viewer.getTable().setHeaderVisible( true );
    viewer.getTable().setLinesVisible( true );

    TableViewerColumn columnId = new TableViewerColumn( viewer, SWT.NONE );
    columnId.getColumn().setWidth( aIdWidth );
    columnId.getColumn().setText( STR_CLMN_ID );
    columnId.setLabelProvider( new ColumnLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        IStridable s = (IStridable)aCell.getElement();
        aCell.setText( s.id() );
      }

      @Override
      public String getToolTipText( Object aElement ) {
        return ((IStridable)aElement).description();
      }

    } );

    TableViewerColumn columnName = new TableViewerColumn( viewer, SWT.NONE );
    columnName.getColumn().setWidth( aNameWidth );
    columnName.getColumn().setText( STR_CLMN_NAME );
    columnName.setLabelProvider( new CellLabelProvider() {

      @Override
      public void update( ViewerCell aCell ) {
        IStridable s = (IStridable)aCell.getElement();
        aCell.setText( s.nmName() );
      }

      @Override
      public String getToolTipText( Object aElement ) {
        return ((IStridable)aElement).description();
      }

    } );

    if( aDescrWidth > 0 ) {
      TableViewerColumn columnDescr = new TableViewerColumn( viewer, SWT.NONE );
      columnDescr.getColumn().setWidth( aDescrWidth );
      columnDescr.getColumn().setText( STR_CLMN_DESCR );
      columnDescr.setLabelProvider( new CellLabelProvider() {

        @Override
        public void update( ViewerCell aCell ) {
          IStridable s = (IStridable)aCell.getElement();
          aCell.setText( s.description() );
        }

      } );
    }

    ColumnViewerToolTipSupport.enableFor( viewer );
    viewer.setContentProvider( new ArrayContentProvider() );
  }

  public TableViewer viewer() {
    return viewer;
  }

  /**
   * Выделяет в списке элемент с указанным ИДом.
   *
   * @param aItemId String - ИД элемента
   */
  public void setSelectedItemById( String aItemId ) {
    IStridable[] items = (IStridable[])viewer.getInput();
    for( IStridable item : items ) {
      if( item.id().equals( aItemId ) ) {
        viewer.setSelection( new StructuredSelection( item ) );
        break;
      }
    }
  }

}
