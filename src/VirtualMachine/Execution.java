package VirtualMachine;

import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.JTextArea;


/**
 *
 * @author 08106239
 */
public class Execution {


    public static void execute(Instruction instruction, ArrayList<StackElement> stack, int i, JTextArea txtSaida)
    {
        if(instruction.getOperation().equals("LDC"))
        {    //S:=s + 1 ; M [s]: = k
            stack.add(new StackElement(stack.size()-1, Integer.parseInt(instruction.getAttr1()) ));
        }
        else if(instruction.getOperation().equals("LDV"))
        {
            int addr = Integer.parseInt(instruction.getAttr1());
            StackElement tmp = stack.get(addr);
            //S:=s + 1 ; M[s]:=M[n]
            stack.add( new StackElement(stack.size()-1, tmp.getValue()) );
        }
        else if(instruction.getOperation().equals("ADD"))
        {
            int sum = stack.get(stack.size()-2).getValue()+stack.get(stack.size()-1).getValue();
            //M[s-1]:=M[s-1] + M[s]; s:=s - 1
            stack.set(stack.size()-2, new StackElement(stack.size()-2, sum) );
            stack.remove(stack.size()-1);
        }
        else if(instruction.getOperation().equals("SUB"))
        {
            int sub = stack.get(stack.size()-2).getValue()-stack.get(stack.size()-1).getValue();
            //M[s-1]:=M[s-1] - M[s]; s:=s - 1
            stack.set(stack.size()-2, new StackElement(stack.size()-2, sub) );
            stack.remove(stack.size()-1);
        }
        else if(instruction.getOperation().equals("MULT"))
        {
            int mult = stack.get(stack.size()-2).getValue()*stack.get(stack.size()-1).getValue();
            //M[s-1]:=M[s-1] * M[s]; s:=s - 1
            stack.set(stack.size()-2, new StackElement(stack.size()-2, mult) );
            stack.remove(stack.size()-1);
        }
        else if(instruction.getOperation().equals("DIVI"))
        {
            int div = stack.get(stack.size()-2).getValue()/stack.get(stack.size()-1).getValue();
            //M[s-1]:=M[s-1] div M[s]; s:=s - 1
            stack.set(stack.size()-2, new StackElement(stack.size()-2, div) );
            stack.remove(stack.size()-1);
        }
        else if(instruction.getOperation().equals("INV"))
        {
            int inv = -stack.get(stack.size()-1).getValue();
            //M[s]:= -M[s]
            stack.set(stack.size()-1, new StackElement(stack.size()-1, inv) );
        }
        else if(instruction.getOperation().equals("AND"))
        {
            //se M [s-1] = 1 e M[s] = 1 então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
            if(stack.get(stack.size()-2).getValue()==1 &&
                    stack.get(stack.size()-1).getValue()==1)
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 1) );
            else
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 0) );
            stack.remove(stack.size()-1);
        }
        else if(instruction.getOperation().equals("OR"))
        {
            //se M[s-1] = 1 ou M[s] = 1 então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
            if(stack.get(stack.size()-2).getValue()==1 ||
                    stack.get(stack.size()-1).getValue()==1)
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 1) );
            else
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 0) );
            stack.remove(stack.size()-1);
        }
        else if(instruction.getOperation().equals("NEG"))
        {
            int neg = 1-stack.get(stack.size()-1).getValue();
            //M[s]:= 1-M[s]
            stack.set(stack.size()-1, new StackElement(stack.size()-1, neg) );
        }
        else if(instruction.getOperation().equals("CME"))
        {
            //se M[s-1] < M[s] então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
            if(stack.get(stack.size()-2).getValue() < stack.get(stack.size()-1).getValue())
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 1) );
            else
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 0) );
            stack.remove(stack.size()-1);
        }
        else if(instruction.getOperation().equals("CMA"))
        {
            //se M[s-1] > M[s] então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
            if(stack.get(stack.size()-2).getValue() > stack.get(stack.size()-1).getValue())
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 1) );
            else
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 0) );
            stack.remove(stack.size()-1);
        }
        else if(instruction.getOperation().equals("CEQ"))
        {
            //se M[s-1] = M[s] então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
            if(stack.get(stack.size()-1).getValue() == stack.get(stack.size()-1).getValue())
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 1) );
            else
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 0) );
            stack.remove(stack.size()-1);
        }
        else if(instruction.getOperation().equals("CDIF"))
        {
            //se M[s-1] != M[s] então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
            if(stack.get(stack.size()-2).getValue() != stack.get(stack.size()-1).getValue())
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 1) );
            else
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 0) );
            stack.remove(stack.size()-1);
        }
        else if(instruction.getOperation().equals("CMEQ"))
        {
            //se M[s-1] <= M[s] então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
            if(stack.get(stack.size()-2).getValue() <= stack.get(stack.size()-1).getValue())
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 1) );
            else
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 0) );
            stack.remove(stack.size()-1);
        }
        else if(instruction.getOperation().equals("CMAQ"))
        {
            //se M[s-1] >= M[s] então M[s-1]:=1 senão M[s-1]:=0; s:=s - 1
            if(stack.get(stack.size()-2).getValue() >= stack.get(stack.size()-1).getValue())
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 1) );
            else
                stack.set(stack.size()-2, new StackElement(stack.size()-2, 0) );
            stack.remove(stack.size()-1);
        }
        else if(instruction.getOperation().equals("START"))
        {
            //S:=-1
            stack = new ArrayList<StackElement>();
        }
        else if(instruction.getOperation().equals("STR"))
        {
            int addr = Integer.parseInt(instruction.getAttr1());
            StackElement tmp = stack.get(addr);
            //S:=s + 1 ; M[s]:=M[n]
            stack.add( new StackElement(stack.size(), tmp.getValue()) );
        }
        else if(instruction.getOperation().equals("HLT"))
        {
            //para a execução
        }

        else if(instruction.getOperation().equals("STR"))
        {
            //M[n]:=M[s]; s;=s-1;
            int addres = Integer.parseInt(instruction.getAttr1());
            stack.set(addres, new StackElement(addres, stack.get(stack.size()-1).getValue()));
            stack.remove(stack.size()-1);
        }

        else if(instruction.getOperation().equals("JMP"))
        {//desviar sempre i:=att1
            i = Integer.parseInt(instruction.getAttr1());
        }

         else if(instruction.getOperation().equals("JMPF"))
        {// desviar caso falso  se M[s]=0, entao i:=att1, senao i:=i+1
             int zer = stack.get(stack.size()-1).getValue();
             if (zer == 0)
                 i = Integer.parseInt(instruction.getAttr1());
             stack.remove(stack.size()-1);
        }

         else if(instruction.getOperation().equals("NULL"))
        {
             //NADA
        }

         else if(instruction.getOperation().equals("RD"))
        {
            // ler a entrada   S:=s+1; M[s]:=proxima entra
             String value = JOptionPane.showInputDialog(null, "Entrada:", "Entrada de valor", JOptionPane.QUESTION_MESSAGE);
             stack.add( new StackElement(stack.size(), Integer.parseInt(value)) );
        }

         else if(instruction.getOperation().equals("PRN"))
        {     // imprimir M[s];s:=s-1;
            txtSaida.append(String.valueOf(stack.get(stack.size()-1).getValue()));
            stack.remove(stack.size()-1);
        }

         else if(instruction.getOperation().equals("ALLOC"))
        {
             int att1 = Integer.parseInt(instruction.getAttr1());
             int att2 = Integer.parseInt(instruction.getAttr2());
             for(int k=0; k<(att2-1);k++){
                 stack.add(new StackElement(stack.size(), att1+k));
             }
        }

         else if(instruction.getOperation().equals("DALLOC"))
        {
             int att1 = Integer.parseInt(instruction.getAttr1());
             int att2 = Integer.parseInt(instruction.getAttr2());
             
             for(int k =(att2-1);k==0;k--){
                 stack.set(att1+k, new StackElement(att1+1, stack.get(stack.size()-1).getValue()));
                 stack.remove(stack.size());
             }

        }
         else if(instruction.getOperation().equals("CALL"))
        {
             stack.add(new StackElement(stack.size()+1, i+1));
             i = Integer.parseInt(instruction.getAttr1());

        }

         else if(instruction.getOperation().equals("RETURN"))
        {
             i = stack.get(stack.size()).getValue();
             stack.remove(stack.size());

        }

    }

}
