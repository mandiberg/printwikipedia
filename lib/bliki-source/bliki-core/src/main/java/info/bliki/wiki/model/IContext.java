package info.bliki.wiki.model;

import info.bliki.wiki.template.extension.AttributeRenderer;

/**
 * Interface for rendering a wiki model
 * 
 */
public interface IContext {

	/**
	 * Resolve an attribute reference. It can be in four possible places:
	 * 
	 * 1. the attribute list for the current template 2. if self is an embedded
	 * template, somebody invoked us possibly with arguments--check the argument
	 * context 3. if self is an embedded template, the attribute list for the
	 * enclosing instance (recursively up the enclosing instance chain) 4. if
	 * nothing is found in the enclosing instance chain, then it might be a map
	 * defined in the group or the its supergroup etc...
	 * 
	 * Attribute references are checked for validity. If an attribute has a value,
	 * its validity was checked before template rendering. If the attribute has no
	 * value, then we must check to ensure it is a valid reference. Somebody could
	 * reference any random value like $xyz$; formal arg checks before rendering
	 * cannot detect this--only the ref can initiate a validity check. So, if no
	 * value, walk up the enclosed template tree again, this time checking formal
	 * parameters not attributes Map. The formal definition must exist even if no
	 * value.
	 * 
	 * To avoid infinite recursion in toString(), we have another condition to
	 * check regarding attribute values. If your template has a formal argument,
	 * foo, then foo will hide any value available from "above" in order to
	 * prevent infinite recursion.
	 * 
	 * This method is not static so people can override functionality.
	 */
	public Object getAttribute(String attribute);
	
	/**
	 * Set an attribute for this template. If you set the same attribute more than
	 * once, you get a multi-valued attribute. If you send in a StringTemplate
	 * object as a value, it's enclosing instance (where it will inherit values
	 * from) is set to 'this'. This would be the normal case, though you can set
	 * it back to null after this call if you want. If you send in a List plus
	 * other values to the same attribute, they all get flattened into one List of
	 * values. This will be a new list object so that incoming objects are not
	 * altered. If you send in an array, it is converted to an ArrayIterator.
	 */
	public void setAttribute(String name, Object value);
	
	/**
	 * What renderer is registered for this attributeClassType for this template.
	 * If not found, the template's group is queried.
	 */
	public AttributeRenderer getAttributeRenderer(Class attributeClassType);

	/**
	 * Register a renderer for all objects of a particular type. This overrides
	 * any renderer set in the group for this class type.
	 */
	public void registerRenderer(Class attributeClassType, AttributeRenderer renderer);
}
