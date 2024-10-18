package org.toxsoft.skf.rri.struct.gui.km5;

import org.toxsoft.core.tsgui.m5.*;
import org.toxsoft.core.tslib.coll.primtypes.*;
import org.toxsoft.core.tslib.coll.primtypes.impl.*;
import org.toxsoft.core.tslib.utils.errors.*;
import org.toxsoft.uskat.core.connection.*;
import org.toxsoft.uskat.core.gui.km5.*;

/**
 * Contributes M5-models for opc ua entities.
 *
 * @author max
 * @author dima
 */
public class KM5RriStructContributor
    extends KM5AbstractContributor {

  /**
   * Creator singleton.
   */
  public static final IKM5ContributorCreator CREATOR = KM5RriStructContributor::new;

  private static final IStringList CONRTIBUTED_MODEL_IDS = new StringArrayList( //
      AttributeModel.MODEL_ID, //
      LinkModel.MODEL_ID, //
      RriSectionModel.MODEL_ID );

  private final IStringListEdit myModels = new StringArrayList();

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @param aDomain {@link IM5Domain} - connection domain
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public KM5RriStructContributor( ISkConnection aConn, IM5Domain aDomain ) {
    super( aConn, aDomain );
  }

  @Override
  protected IStringList papiCreateModels() {

    AttributeModel attributeModel = new AttributeModel();
    myModels.add( attributeModel.id() );
    m5().addModel( attributeModel );

    LinkModel linkModel = new LinkModel();
    myModels.add( linkModel.id() );
    m5().addModel( linkModel );

    RriSectionModel rriSectionModel = new RriSectionModel();
    myModels.add( rriSectionModel.id() );
    m5().addModel( rriSectionModel );

    return CONRTIBUTED_MODEL_IDS;
  }

}
