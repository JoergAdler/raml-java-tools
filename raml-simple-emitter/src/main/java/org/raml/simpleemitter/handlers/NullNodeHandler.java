package org.raml.simpleemitter.handlers;

import org.raml.simpleemitter.HandlerList;
import org.raml.simpleemitter.NodeHandler;
import org.raml.simpleemitter.YamlEmitter;
import org.raml.yagi.framework.nodes.Node;
import org.raml.yagi.framework.nodes.NullNode;
import org.raml.yagi.framework.nodes.SimpleTypeNode;

import java.io.IOException;

/**
 * Created. There, you have it.
 */
public class NullNodeHandler extends NodeHandler<NullNode> {


    public NullNodeHandler() {
    }

    @Override
    public boolean handles(Node node) {

        return node instanceof NullNode;
    }

    @Override
    public boolean handleSafely(NullNode node, YamlEmitter emitter) throws IOException {


        /* TODO need better method call */
      //  emitter.writeNullValue();
        return true;
    }

}
