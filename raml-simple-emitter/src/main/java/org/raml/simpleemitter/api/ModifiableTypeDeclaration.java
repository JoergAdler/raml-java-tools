package org.raml.simpleemitter.api;

import org.raml.parsertools.ExtensionFactory;
import org.raml.simpleemitter.ApiAugmentationFactory;
import org.raml.simpleemitter.Visitable;
import org.raml.v2.api.model.v10.bodies.Response;
import org.raml.v2.api.model.v10.datamodel.TypeDeclaration;


/**
 * Created. There, you have it.
 */
@ExtensionFactory(factory = ApiAugmentationFactory.class)
public interface ModifiableTypeDeclaration extends Visitable, TypeDeclaration {



}