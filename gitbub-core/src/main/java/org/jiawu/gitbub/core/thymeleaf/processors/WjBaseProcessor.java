package org.jiawu.gitbub.core.thymeleaf.processors;


import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.dom.NestableNode;
import org.thymeleaf.processor.ProcessorResult;
import org.thymeleaf.processor.element.AbstractElementProcessor;

import java.util.Map;

/**
 * Created by jiawuwu on 14-7-21.
 */
public abstract class WjBaseProcessor extends AbstractElementProcessor {


    public WjBaseProcessor(String elementName) {
        super(elementName);
    }

    @Override
    protected ProcessorResult processElement(Arguments arguments, Element element) {
        modifyModelAttributes(arguments, element);

        // Remove the tag from the DOM
        final NestableNode parent = element.getParent();
        parent.removeChild(element);

        return ProcessorResult.OK;
    }

    /**
     * Helper method to add a value to the expression evaluation root (model) Map
     * @param key the key to add to the model
     * @param value the value represented by the key
     */
    @SuppressWarnings("unchecked")
    protected void addToModel(Arguments arguments, String key, Object value) {
        ((Map<String, Object>) arguments.getExpressionEvaluationRoot()).put(key, value);
    }


    @Override
    public int getPrecedence() {
        return 0;
    }

    /**
     * This method must be overriding by a processor that wishes to modify the model. It will
     * be called by this abstract processor in the correct precendence in the evaluation chain.
     * @param arguments
     * @param element
     */
    protected abstract void modifyModelAttributes(Arguments arguments, Element element);

}
