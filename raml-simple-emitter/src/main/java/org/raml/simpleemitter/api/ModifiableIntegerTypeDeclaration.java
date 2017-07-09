package org.raml.simpleemitter.api;

import org.raml.parsertools.ExtensionFactory;
import org.raml.simpleemitter.ApiAugmentationFactory;
import org.raml.simpleemitter.Visitable;
import org.raml.v2.api.model.v10.datamodel.IntegerTypeDeclaration;
import org.raml.v2.api.model.v10.datamodel.StringTypeDeclaration;


/**
 * Created. There, you have it.
 */
@ExtensionFactory(factory = ApiAugmentationFactory.class)
public interface ModifiableIntegerTypeDeclaration extends Visitable, IntegerTypeDeclaration {


}