package org.toxsoft.skf.rri.values.gui.km5;

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
public class KM5RriValuesContributor
    extends KM5AbstractContributor {

  /**
   * Creator singleton.
   */
  public static final IKM5ContributorCreator CREATOR = KM5RriValuesContributor::new;

  private static final IStringList CONRTIBUTED_MODEL_IDS = new StringArrayList( //
      RriSectionModel.MODEL_ID, //
      ObjectCheckableM5Model.MODEL_ID, //
      AttrParamM5Model.MODEL_ID, //
      LinkParamM5Model.MODEL_ID );

  private final IStringListEdit myModels = new StringArrayList();

  /**
   * Constructor.
   *
   * @param aConn {@link ISkConnection} - the connection
   * @param aDomain {@link IM5Domain} - connection domain
   * @throws TsNullArgumentRtException any argument = <code>null</code>
   */
  public KM5RriValuesContributor( ISkConnection aConn, IM5Domain aDomain ) {
    super( aConn, aDomain );
  }

  @Override
  protected IStringList papiCreateModels() {

    RriSectionModel rriSectionModel = new RriSectionModel();
    myModels.add( rriSectionModel.id() );
    m5().addModel( rriSectionModel );

    ObjectCheckableM5Model objCheckModel = new ObjectCheckableM5Model( skConn() );
    myModels.add( objCheckModel.id() );
    m5().addModel( objCheckModel );

    AttrParamM5Model attrModel = new AttrParamM5Model();
    myModels.add( attrModel.id() );
    m5().addModel( attrModel );

    LinkParamM5Model linkModel = new LinkParamM5Model();
    myModels.add( linkModel.id() );
    m5().addModel( linkModel );

    return CONRTIBUTED_MODEL_IDS;
  }
}
