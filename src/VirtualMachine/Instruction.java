/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package VirtualMachine;

/**
 *
 * @author Tutti
 */
public class Instruction {
    
    private String operation;
    private String attr1;
    private String attr2;
    private String description;

    public Instruction(String operation, String attr1, String attr2, String description) {
        this.operation = operation;
        this.attr1 = attr1;
        this.attr2 = attr2;
        this.description = description;
    }

    public String getAttr1() {
        return attr1;
    }

    public void setAttr1(String attr1) {
        this.attr1 = attr1;
    }

    public String getAttr2() {
        return attr2;
    }

    public void setAttr2(String attr2) {
        this.attr2 = attr2;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    @Override
    public String toString() {
        return "Instruction{" + "operation=" + operation + "attr1=" + attr1 + "attr2=" + attr2 + "description=" + description + '}';
    }
    
    
    
}
