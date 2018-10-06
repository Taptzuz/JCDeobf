package me.coley.bmf.attribute;

import java.util.List;

public interface AttributeOwner {
    /**
     * Adds an attribute to the owner.
     *
     * @param attribute
     */
    void addAttribute(Attribute attribute);

    /**
     * Collects and returns the attributes into a list.
     */
    List<Attribute> getAttributes();
}
