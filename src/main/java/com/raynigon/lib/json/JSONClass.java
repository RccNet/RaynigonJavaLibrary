package com.raynigon.lib.json;

/**
 * @author Simon Schneider
 *
 */
public @interface JSONClass{

    
    
    /** States if the attributes of the class were used to match the JSON names.
     * If this is true, the value for a single attribute is overridden by {@link com.raynigon.lib.json.JSONAttribute JSONAttribute} 
     * @return true, the attributes were used. false, no attribute will be used
     */
    public boolean AffectAttributes() default true;

    /** States if the methods of the class were used to match the JSON names.
     * <br />
     * <br />
     * <b> THIS HAS CURRENTLY NO EFFECT </b> 
     * <br />
     * <br />
     * If this is false, the value for a single method is overridden 
     * by {@link com.raynigon.lib.json.JSONMethod JSONMethod}
     * @return true, the methods were used. 
     * false, if not marked with the JSONMethod Annotation, no method will be used
     */
    public boolean AffectMethods() default false;
    
    
    /** States if the parent class should be used to match JSON Values
     * @return true, the Parent Class will be used. False only this class will be used to match JSON Attributes.
     */
    public boolean ParentSearch() default true;
}
