/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package VirtualMachine;

/**
 *
 * @author 08106239
 */
public class StackElement {

    private int address;
    private int value;

    public StackElement(int address, int value) {
        this.address = address;
        this.value = value;
    }

    public int getAddress() {
        return address;
    }

    public void setAddress(int address) {
        this.address = address;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    

}
