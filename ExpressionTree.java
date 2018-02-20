package expressiontree;
import java.util.Scanner;
import java.util.Stack;

/**
 * An ADT that represents an expression tree, where the nodes of the tree can
 * contain operators or operands(numeric values). Each operator node must have
 * two children, one could be an operand and the other an operator, or both
 * children can be operands. Operand nodes cannot have children.
 * 
 * @author Jeff Rowell
 */
public class ExpressionTree 
{
      
   static String prefix = "";  // Builds the converted expression
   static Stack operatorStack = new Stack();; 
   
   /**
    * Main prompts the user to enter an expression to calculate in infix form,
    * converts the given expression into prefix notation, and computes the 
    * result using prefix arithmetic.
    * 
    * @param args the command line arguments
    */
   public static void main(String[] args) 
   {
      Scanner keyboard = new Scanner(System.in);
      ExpressionTree expr = new ExpressionTree();
      
      System.out.print("Enter an expression to evaluate in infix form: ");
      String input = keyboard.nextLine();
      String reverseInput = reverse(input);
      String result = infixToPrefix(reverseInput);
      expr.build(result);
      System.out.println("Prefix: " + result + "\nCalculated Result: " 
                                             + expr.getResult());
   }
   
   /**
    * Given an expression, checks each symbol in the expression, determining if
    * the current symbol is an operand or an operator. If the symbol is and 
    * operand, it is appended to the result string, otherwise the symbol is an
    * operator and the priority of that operator needs to be determined before
    * it can be pushed onto the stack.
    * 
    * @param input      An infix expression granted by the user. 
    * @return prefix    The equivalent expression converted to prefix form.
    */
   public static String infixToPrefix(String input) 
   {

      for (int i = 0; i < input.length(); i++) 
      {
         char current = input.charAt(i);
         
         if (current == '+' || current == '-') 
         {
            addOperator(current, 1); // 1 represents a lower priority
         } 
         else if (current == '*' || current == '/') 
         {
            addOperator(current, 2); // 2 represents a higher priority
         } 
         else 
         {
            prefix += current; //If it is not an operator, add it to the result
         }
      }
      
      //Empty any remaining operators from the stack
      while (!operatorStack.isEmpty()) 
      {
         char top = (Character) operatorStack.pop();
         prefix += " " + top;
      }
      
      prefix = reverse(prefix); // Reverses the string, it is currently postfix
      return prefix;
   }

   /**
    * Determines the priority of the operator on the top of the stack, and 
    * compares that to the current operator before pushing the current operator
    * onto the stack. If the priority of the operator on the top of the stack
    * is of greater or same priority as the current operator, that operator is
    * added to the result, otherwise, that operator is pushed onto the stack.
    * 
    * @param letter     The current operator symbol
    * @param priority   The priority of that current operator symbol.
    */
   public static void addOperator(char letter, int priority) 
   {
      while (!operatorStack.isEmpty()) 
      {
         char top = (Character) operatorStack.pop();
         int topPriority = 0;
         
         if (top == '+' || top == '-')  
         {
            topPriority = 1;
         } 
         
         else 
         {
            topPriority = 2;
         }
         
         if (topPriority >= priority) 
         {
            prefix += " " + top;
         } 
         
         else 
         {
            operatorStack.push(top);
            break;
         }
         
         if (letter == ')')
         {
            operatorStack.push(letter);
         }
         else if (letter == '(')
         {
            while ((Character)operatorStack.pop() != ')')
            {
               prefix += " " + top;
            }
         }
      }
      operatorStack.push(letter);
   }


   /**
    * Given a String, returns the backwards version of the String.
    * 
    * @param input      A given String to be reversed.
    * @return retStr    The given String, put backwards
    */
   public static String reverse(String input) 
   {
      int len = input.length();
      String retStr = "";
      char[] expr = new char[len];
      
      for (int i = 0; i < len; i++) 
      {
         expr[i] = input.charAt(i); //String to char array
      }
      
      //Swap first and last elements, swapping towards middle until reversed
      for (int i = 0; i < len / 2; i++) 
      {
         char temp = expr[i];
         expr[i] = expr[len - i - 1];
         expr[len - i - 1] = temp;
      }
      
      for (int j = 0; j < len; j++) 
      {
         retStr += expr[j]; //Char array back to a String
      }
      
      return retStr;
   }
   
   /**
    * A SubTree data type contains char data, and two Node references,
    * both of which can have operand data, or one of the references can have
    * operand data and the other can have operator data.
    * 
    */
   private static class SubTree
   {
      char data;
      SubTree left;
      SubTree right;
      
      
      /**
       * Constructs a new SubTree object. Initializes both left and right 
       * references to null.
       * 
       * @param data An operator or and operand.
       */
      public SubTree(char data)
      {
         this.data = data;
         this.left = null;
         this.right = null;
      }
   }
   
   /**
    * A MainTree data type contains SubTree data, and only one MainTree 
    * reference. The MainTree references act as subtrees.
    * 
    */
   private static class MainTree
   {
      SubTree subTree;
      MainTree nextNode;
      
      /**
       * Constructs a new MainTree object. Initializes the nextNode reference 
       * to null. Initializes its data to a SubTree object.
       * 
       * @param subTree A SubTree object that contains 'sub' expressions.
       */
      public MainTree(SubTree subTree)
      {
         this.subTree = subTree;
         nextNode = null;
      }
   }
   
   private static MainTree top;
   
   /**
    * Constructs a new ExpressionTree, initializing the MainTree branches to
    * null.
    */
   public ExpressionTree()
   {
      top = null;
   }
   
   /**
    * Adds a new SubTree to the ExpressionTree.
    * 
    * @param pointer    The reference to the new SubTree.
    */
   private static void push(SubTree pointer)
   {
      if (top == null)
      {
         top = new MainTree(pointer);
      }
      else
      {
         MainTree newPointer = new MainTree(pointer);
         newPointer.nextNode = top;
         top = newPointer;
      }
   }
   
   /**
    * Removes a SubTree from the end of the list.
    * 
    * @return  The reference to the removed SubTree
    */
   private static SubTree pop() 
   {
      if (top == null)
      {
         throw new RuntimeException("Empty");
      }
      else
      {
         SubTree pointer = top.subTree;
         top = top.nextNode;
         return pointer;
      }
   }
   
   /**
    * Returns a references to the SubTree at the end of the list.
    * 
    * @return 
    */
   private static SubTree peek()
   {
      return top.subTree;
   }
   
   /**
    * If the given symbol is a numeric value, a new Node is created with this
    * symbol as its data. If the given symbol is an operator, set the left 
    * reference to whatever is at the end of the list, set the right reference
    * to whatever is second to the end of the list.
    * 
    * @param symbol 
    */
   private static void add(char symbol)
   {
      try
      {
         if (Character.isDigit(symbol))
         {
            SubTree newPointer = new SubTree(symbol);
            push(newPointer);
         }
         else if (isOperator(symbol))
         {
            SubTree newPointer = new SubTree(symbol);
            newPointer.left = pop();
            newPointer.right = pop();
            push(newPointer);
         }
      }
      catch (Exception e)
      {
         System.out.println("Invalid Expression");
         System.exit(0);
      }
   }
   
   /**
    * Given a symbol, determines if the symbol is and operator.
    * 
    * @param symbol  A symbol from the input expression.
    * @return  true  If the symbol is the operator.
    * @return  false If the symbol is not an operator.
    */
   private static boolean isOperator(char symbol)
   {
      boolean result = false;
      
      if (symbol == '+' || symbol == '-' || symbol == '*' || symbol == '/')
      {
         result = true;
      }
      return result;
   }
   
   /**
    * Given a symbol, converts the symbol into a digit.
    * 
    * @param symbol  The symbol to convert into a digit.
    * @return digit  The digit form of the symbol.
    */
   private static int toDigit(char symbol)
   {
      int digit = (symbol - '0');
      return digit;
   }
   
   /**
    * Constructs a new expression tree.
    * 
    * @param expression The expression to construct within the tree.
    */
   private static void build(String expression)
   {
      for (int i = expression.length() - 1; i >= 0; i--)
      {
         add(expression.charAt(i));
      }
   }
   
   /**
    * Deletes all of the nodes in the tree, reseting the expression tree.
    */
   private static void clear()
   {
      top = null;
   }
   
   /**
    * Calculates the expression.
    * 
    * @return result The result of the calculated expression.
    */
   private static double getResult()
   {
      double result = getResult(peek()); 
      return result;
   }
   
   /**
    * Given a reference to a subtree, computes the 'sub' result of that subtree,
    * and connects that result as a numeric value to the rest of the tree.
    * 
    * @param pointer    A subtree to calculate the sub expression of.
    * @return result    The result of the expression defined in the subtree.
    */
   private static double getResult(SubTree pointer)
   {
      if ((pointer.left == null) && (pointer.right == null))
      {
            return toDigit(pointer.data);
      }
      else
      {
         double result = 0.0;
         double left = getResult(pointer.left);
         double right = getResult(pointer.right);
         char operator = pointer .data;
         
         switch (operator)
         {
            case '+':
               result = left + right;
               break;
            case '-':
               result = left - right;
               break;
            case '*':
               result = left * right;
               break;
            case '/':
               result = left / right;
               break;
            default:
               result = left + right;
               break;
         }
         return result;
         }
      }
   }
