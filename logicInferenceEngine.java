import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

public class logicInferenceEngine {
	
	public static HashMap<String, ArrayList<String>> kbHmap = new HashMap<String, ArrayList<String>>();
	public static int loopBreaker = 0;
	
	
	public static String makeKBsentShorter(String kbSentence,HashMap<String, String> hm1,HashMap<String, String> hm2)
	{
		int predCount = 0;
		String shorterKbSent = "";
		for (int i = 0; i < kbSentence.length(); i++) 
        {
        	
        	if (kbSentence.charAt(i) >= 'A' && kbSentence.charAt(i) <= 'Z') 
        	{
        		String predicate = "";
        		predicate = predicate + kbSentence.charAt(i);
        		i++;
                
                while (kbSentence.charAt(i) != ')') 
                {
                	predicate += kbSentence.charAt(i);
                    i++;
                }
                i--;
                
                predCount++;
                
                if (predCount > 0 && predCount <= 9) 
                {
                    String ppart = "p00" + predCount;
                    predicate += ")";
                    hm1.put(ppart, predicate);
                    hm2.put(predicate, ppart);
                    shorterKbSent += ppart;
                } 
                else if (predCount >= 10 && predCount <= 99) 
                {
                    String ppart = "p0" + predCount;
                    predicate += ")";
                    hm1.put(ppart, predicate);
                    hm2.put(predicate, ppart);
                    shorterKbSent += ppart;
                    
                    
                } 
                else if (predCount >= 100 && predCount <= 999) 
                {
                    String ppart = "p" + predCount;
                    predicate += ")";
                    hm1.put(ppart, predicate);
                    hm2.put(predicate, ppart);
                    shorterKbSent += ppart;
                }
                i++;
        	}
        	else {
        		shorterKbSent += kbSentence.charAt(i);
            }
        }
		
		return shorterKbSent;
		
	}
	
	//Function to unify two rules.
	public static boolean unifyArgs(String a[],String b[])
    {
	   System.out.println("Inside unification function with Strings : " + a + "and  " + b);
	   
       int counter=0;
       for(int i=0;i<a.length;i++)
       {
           String x=a[i]; //Stack
           String y=b[i]; //kbString

          
           if(x.charAt(0)>='a'&& x.charAt(0)<='z' && y.charAt(0)>='a' && y.charAt(0)<='z')
           {//case 1 : Unify variable with variable
        	   counter++;

           }
           else if(x.charAt(0)>='a'&& x.charAt(0)<='z' && y.charAt(0)>='A' && y.charAt(0)<='Z')
           {//case 2 : Unify variable with constant
               counter++;
           }
           else if(x.charAt(0)>='A'&& x.charAt(0)<='Z' && y.charAt(0)>='a' && y.charAt(0)<='z')
           {//case 3 : Unify constant with variable
        	   counter++;
           }
           else if(x.equals(y))
           {//case 4 : Unify constant with constant
               counter++;
           }
       }
       if(counter==a.length) //If all cases (args) unify then return true. 
           return true;
        else
            return false;
    }
	
	//Function to return negation of the given query string.
	
	public static String negateQuery(String query)
    {
        if(query.contains("~"))
            return query.substring(1);
        else
            return "~"+query;
    }
	
	
	//Function to perform DFS on the KB rules tree. Takes input as Query by user.
	//It returns true if Query can be entailed by the KB otherwise false.
	
	public static boolean dfsOnKBMap(Stack<String> queryStack,int loopBreaker)
    {
        System.out.println("The first element in stack is "+queryStack);
        while(!queryStack.isEmpty())
        {
        	String firstel=queryStack.pop();
            
        	System.out.println("the popped element is "+firstel);
            
            String queryToResolve = negateQuery(firstel);

            String predicate = "";
            int index=-1;

            //GET PREDICATE STARTS-------------
            for(int i=0;i<queryToResolve.length();i++)
            {
                while(queryToResolve.charAt(i)!='(')
                {
                	predicate+=queryToResolve.charAt(i);
                    i++;
                }
                index=i;
                break;
            }
            System.out.println(predicate);
            // GET PREDICATE ENDS--------------


            //STACK ARGUMENTS
            String arguments1[]=queryToResolve.substring(index+1,queryToResolve.length()-1).split(",");

            for(String x:arguments1)
                System.out.print(x+" ");
            //STACK ARGUMENT ENDS


            if(kbHmap.containsKey(predicate))
            {
                //GETS VALUES FOR A GIVEN PREDICATE KEY
                ArrayList<String> values=kbHmap.get(predicate);

                //This is first time we get predicate values.
                for(int i=0;i<values.size();i++)
                {

                    if(loopBreaker > 1000)
                    {
                        System.out.println("The counter is :" + loopBreaker);
                        return false;
                    }
                    String iter=values.get(i);
                    //System.out.println("iter "+iter);
                    ArrayList<String> ored=new ArrayList<String>();
                    String splitter[]=iter.split("\\|");

                    String match="";
                    for(String x:splitter) {
                        ored.add(x);
                        if(x.contains(predicate))
                            match=x;
                    }
                    String arguments2string="";
                    System.out.println("match "+match);
                    for(int j=0;j<match.length();j++)
                    {
                        if(match.charAt(j)=='(')
                        {
                            j++;
                            while(match.charAt(j)!=')')
                            {
                                arguments2string+=match.charAt(j);
                                j++;
                            }
                            break;
                        }
                    }
                    System.out.println("arg2 "+arguments2string);
                    //KB ARGUMENT LIST
                    String arguments2[]=arguments2string.split(",");

                    for(String x:arguments2)
                        System.out.print(x+" ");

                    //Here we do unification with stack arg and matched predicate arguments
                    boolean result=unifyArgs(arguments1,arguments2);
                    System.out.println("result is "+result);

                    //RESOLVING PART If unification result is true
                    if(result==true)
                    {
                        HashMap<String,String> hmapunify=new HashMap<String,String>();
                        for(int h=0;h<arguments1.length;h++)
                        {
                            String stackargs=arguments1[h];
                            String kbargs=arguments2[h];
                            if(!hmapunify.containsKey(kbargs))
                                hmapunify.put(kbargs,stackargs);

                        }
                        System.out.println("hmapunify "+hmapunify);
                        
                        //Copying querystack to copystack
                        Stack<String> newStack=new Stack<String>();
                        String stackarray[]= queryStack.toArray(new String[queryStack.size()]);
                        for(int si=0;si<queryStack.size();si++)
                        {
                        	newStack.push(stackarray[si]);
                        }

                        for(int m=0;m<ored.size();m++)
                        {
                            String currentkbelement=ored.get(m);
                            Iterator it = hmapunify.entrySet().iterator();
                            while (it.hasNext()) {
                                Map.Entry pair = (Map.Entry)it.next();
                                if(currentkbelement.contains((String)pair.getKey()))
                                    currentkbelement=currentkbelement.replace((String)pair.getKey(),(String)pair.getValue());                              
                            }
                            
                          //To avoid adding resolved predicates in stack eg.A(Alice)
                            String checking="";
                          for(int f=0;f<currentkbelement.length();f++)
                          {

                              while(currentkbelement.charAt(f)!='(')
                              {
                                  checking+=currentkbelement.charAt(f)+"";
                                  f++;
                              }
                              break;
                          }
                            System.out.println("checking "+checking);
                            System.out.println("pred is "+predicate);

                            if(!checking.equals(predicate))  //Remaining expression after resolution.
                          {
                              System.out.println("Inside the if loop");
                              newStack.push(currentkbelement);
                          }

                        }
                        System.out.println("Inside DFS with query stack "+ queryStack);

                        boolean printing=dfsOnKBMap(newStack,++loopBreaker);
                        
                        System.out.println("print dfs="+printing);
                        
                        if(printing==true)
                        {
                            return true;
                        }
                    }
                }
                return false;
            }
            else
            {
            	return false;
            }
        }
        System.out.println("exiting while loop");

      return true;
    }
	
	
	//This function takes standardize KB as argument and returns standard KB HashMap
	public static void kbmaker(ArrayList<String> standKB)
    {
		System.out.println("Inside KB Maker function with kb : " + standKB);
		
        for(int k=0;k<standKB.size();k++) {
            String w = standKB.get(k);

            for (int i = 0; i < w.length(); i++) {

                if (w.charAt(i) >= 'A' && w.charAt(i) <= 'Z') {
                    if (i - 1 != -1) {
                        if (w.charAt(i - 1) == '~') {
                            String predicate = "~";
                            int checkconstant = 0;
                            while (w.charAt(i) != '(') {
                                if (w.charAt(i) == ','|| w.charAt(i)==')') {
                                    checkconstant = 1;
                                    break;
                                }
                                predicate += w.charAt(i) + "";
                                i++;
                            }
                            if (checkconstant == 0) {
                                if(kbHmap.containsKey(predicate))
                                {
                                    ArrayList<String> temper=kbHmap.get(predicate);
                                    temper.add(w);
                                    kbHmap.remove(predicate);
                                    kbHmap.put(predicate,temper);
                                }
                                else {
                                    ArrayList<String> temp = new ArrayList<String>();
                                    temp.add(w);
                                    kbHmap.put(predicate, temp);
                                }
                            }
                        } else {
                            String predicate = "";
                            int checkconstant = 0;
                            while (w.charAt(i) != '(') {
                                if (w.charAt(i) == ',' || w.charAt(i)==')') {
                                    checkconstant = 1;
                                    break;
                                }
                                predicate += w.charAt(i) + "";
                                i++;
                            }
                            if (checkconstant == 0) {
                                if(kbHmap.containsKey(predicate))
                                {
                                    ArrayList<String> temper=kbHmap.get(predicate);
                                    temper.add(w);
                                    kbHmap.remove(predicate);
                                    kbHmap.put(predicate,temper);
                                }
                                else {
                                    ArrayList<String> temp = new ArrayList<String>();
                                    temp.add(w);
                                    kbHmap.put(predicate, temp);
                                }
                            }
                        }
                    } else {
                        String predicate = "";
                        int checkconstant = 0;
                        while (w.charAt(i) != '(') {
                            if (w.charAt(i) == ',') {
                                checkconstant = 1;
                                break;
                            }
                            predicate += w.charAt(i) + "";
                            i++;
                        }
                        if (checkconstant == 0) {
                            if(kbHmap.containsKey(predicate))
                            {
                                ArrayList<String> temper=kbHmap.get(predicate);
                                temper.add(w);
                                kbHmap.remove(predicate);
                                kbHmap.put(predicate,temper);
                            }
                            else {
                                ArrayList<String> temp = new ArrayList<String>();
                                temp.add(w);
                                kbHmap.put(predicate, temp);
                            }
                        }
                    }
                }
            }
        }
    }
	
	//This function apply distributive property on given KB rule and returns simplified expression
	public static String performDistributivity(String expression)
    {
		System.out.println("Inside perform Distributivity function with String : " + expression);
		//||p001~p002~p003
        Stack<String> stack =new Stack<String>();

        for(int i=expression.length()-1;i>=0;i--)
        {
           
            if(expression.charAt(i)=='p')
            {
                String predicate = expression.substring(i,i+4);
                stack.push(predicate);
            }
            
            if(expression.charAt(i)=='&')
            {
                String firstPart=stack.pop();
                String secondPart=stack.pop();
                String res=firstPart+expression.charAt(i)+secondPart;
                stack.push(res);
            }
            
            if(expression.charAt(i)=='~')
            {
                String newfirst="";
                String op=stack.pop();
                newfirst="~"+op;

                stack.push(newfirst);
            }
            
            if(expression.charAt(i) == '|')
            {
                String firstPart=stack.pop(); // P1
                String secondPart=stack.pop(); // P2 | P3
                String res = "";
                ArrayList<String> one=new ArrayList<String>(); // P1
                ArrayList<String> two=new ArrayList<String>(); // P2,P3
                if(firstPart.contains("&"))
                {

                    for(String d:firstPart.split("&"))
                        one.add(d);
                }
                if(secondPart.contains("&"))
                {
                    for(String d:secondPart.split("&"))
                        two.add(d);
                }

                if(one.isEmpty() && two.isEmpty())
                {
                	res+=firstPart+"|"+secondPart;
                }
                if(one.isEmpty() && !two.isEmpty())
                {// P2,P3
                    for(String ee:two)
                    {
                    	res+=firstPart+"|"+ee;
                    	res+="&";
                    }
                    // (P1|P2 & P1|P3) 
                }
                if(!one.isEmpty() && two.isEmpty())
                {
                    for(String ee:one)
                    {
                    	res+=secondPart+"|"+ee;
                    	res+="&";
                    }
                }
                if(!one.isEmpty() && !two.isEmpty())
                {
                    for(String ee:one)
                    {
                        for(String ee2:two)
                        {
                        	res+=ee+"|"+ee2;
                        	res+="&";
                        }
                    }
                }
                if(res.charAt(res.length()-1)=='&')
                	res=res.substring(0,res.length()-1);
                stack.push(res);
            }
        }
        return stack.pop();
    }
	public static String removeNegation(String expression)
    {
        System.out.println("Inside Remove Negation function with String : " + expression);
        //|p001~&p002p003
        Stack<String> stack = new Stack<String>();
        
        for(int i= expression.length()-1 ; i>= 0 ; i--)
        {
            if(expression.charAt(i)=='p')
            {
                String predicate = expression.substring(i,i+4);
                stack.push(predicate);
            }
            
            if(expression.charAt(i) == '&'|| expression.charAt(i) == '|')
            {
                String firstPart = stack.pop();
                String secondPart = stack.pop();
                String res= firstPart + expression.charAt(i)+ secondPart;
               
                stack.push(res);
            }
            if(expression.charAt(i) == '~')
            {
                String removeNegExp = "";
                String firstExp=stack.pop();
                
                for(int j=0;j<firstExp.length();j++)
                {
                    if(j==0 && firstExp.charAt(j)=='p')
                    {
                    	removeNegExp+="~";
                    	removeNegExp+=firstExp.substring(j,j+4);
                    }
                    else if(firstExp.charAt(j)=='|')
                    {
                    	removeNegExp+="&";
                    }
                    else if(firstExp.charAt(j)=='&')
                    {
                    	removeNegExp+="|";
                    }

                    else if(firstExp.charAt(j)=='p')
                    {
                        if(firstExp.charAt(j-1)=='~')
                        {
                        	removeNegExp+=firstExp.substring(j,j+4);
                        }
                        else
                        {
                        	removeNegExp+="~"+firstExp.substring(j,j+4);
                        }
                    }
                }  
                if(removeNegExp.contains("&"))
                {
                   
                    ArrayList<String> exp=new ArrayList<String>();
                    for(String e:removeNegExp.split("&"))
                    {
                        e="("+e+")";
                        exp.add(e);
                    }
                    String newExp="";
                    for(String temp:exp)
                    {
                    	newExp+=temp+"&";
                    }
                    
                    newExp=newExp.substring(0,newExp.length()-1);
                    
                    newExp="("+newExp+")";
                    
                    removeNegExp=newExp;

                }

                stack.push(removeNegExp);
            }

        }
        return stack.pop();
        
    }
	//Function to return Implication and return resultant form as String.
	public static String removeImplication(String expression)
    {
    	System.out.println("Inside Remove Implication Function with String : " + expression );
    	
    	Stack<String> stack =new Stack<String>();
    	
    	for(int i=expression.length()-1;i>=0;i--)
        {
            if(expression.charAt(i)=='p')
            {
                String predicate = expression.substring(i,i+4);
                stack.push(predicate);
            }
            if(expression.charAt(i) =='&'|| expression.charAt(i) =='|')
            {
                String firstPart  = stack.pop();
                String secondPart = stack.pop();
                
                String res = firstPart + expression.charAt(i)+ secondPart;
                stack.push(res);
            }
            if(expression.charAt(i)=='~')
            {
                
                String op = stack.pop();
                op= "~("+op+")";
                stack.push(op);
            }
            if(expression.charAt(i)=='>')
            {
                String premise  = "~("+stack.pop()+")";
                String conclusion = stack.pop();
                String res  = premise + "|" +conclusion;
                stack.push(res);
            }

        }
        return stack.pop();
     
    }
	
	public static int returnPrecedence(char c)
    {
    	
        if(c=='~')
            return 4;
        else if(c=='&')
            return 3;
        else if(c=='|')
            return 2;
        else if(c=='>')
            return 1;
        else if(c=='(')
            return 0;
        else if(c==')')
            return 0;
        else
        	return 0;
        
    }
	
	public static String makePrefixExpression(String expression)
    {
		System.out.println("Inside prefix Function with String : " + expression );
        
		Stack<Character> stack =new Stack<Character>();
		
		String prefix = "";
		
		for(int i=expression.length()-1;i>=0;i--)
        {
			if(expression.charAt(i)==')')
            {
				stack.push(expression.charAt(i));
            }
			
			if(expression.charAt(i)=='p')
            {
                StringBuilder x1=new StringBuilder(expression.substring(i,i+4));
                x1.reverse();

                prefix+=x1.toString();
            }
			
			if(expression.charAt(i) =='&'|| expression.charAt(i) =='|'|| expression.charAt(i) =='>'|| expression.charAt(i) =='~')
            {

                if(stack.isEmpty())
                	stack.push(expression.charAt(i));
                else
                {
                    int precedence1 = returnPrecedence(expression.charAt(i)); //Current string op precedence.
                    int precedence2 = returnPrecedence(stack.peek()); // Top of stack op precedence.
                    
                    
                    if(precedence1>precedence2)
                    {
                        stack.push(expression.charAt(i));
                    }
                    else
                    {
                    	//IMP Already stack op has higher precedence case.
                        while(precedence1<precedence2 && (!(stack.isEmpty())))
                        {
                            prefix+=stack.pop();
                            if(!stack.isEmpty())
                            {
                            	precedence2= returnPrecedence(stack.peek());
                            }

                        }
                        stack.push(expression.charAt(i));
                    }
                }
            }
			
			if(expression.charAt(i)=='(')
            {

                while(stack.peek()!=')')
                {
                    prefix+= stack.pop();
                }
                stack.pop();
            }
        }

        while(!stack.isEmpty())
            prefix+= stack.pop();
        
        
        StringBuilder pf=new StringBuilder(prefix);
        
        System.out.println("PREFIX EXP : " + prefix);
        
        pf.reverse();
        return pf.toString();
	}
	
    

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		
		
		String fileName = "/Users/prathameshnaik/Documents/Testcases/AI_HW3/input.txt";
		//String fileName = "input.txt";
		String output = "output.txt";
		FileWriter writer = new FileWriter(output);
		
		
		// FileReader reads text files in the default encoding.
		FileReader fileReader = new FileReader(fileName);

		// Always wrap FileReader in BufferedReader.
		BufferedReader bufferedReader = new BufferedReader(fileReader);
		
		int querySize = Integer.parseInt(bufferedReader.readLine());
		
		//query :  ArrayList containing queries in the input file.
        ArrayList<String> query=new ArrayList<String>();
        
        for(int i=0;i<querySize;i++)
        {
            query.add(bufferedReader.readLine());
        }
		
        int kbSize=Integer.parseInt(bufferedReader.readLine());
        
        //givenkb :  ArrayList containing KB sentences in the input file.
        ArrayList<String> inputKb=new ArrayList<String>();
        
        for(int i=0;i<kbSize;i++)
        {
        	inputKb.add(bufferedReader.readLine());
        }
       
        System.out.println("querysize"+querySize);
        System.out.println("query"+query);
        System.out.println("kbsize "+kbSize);
        System.out.println("Given kb "+inputKb);
        
        
        HashMap<String, String> hm1 = new HashMap<String, String>();
        HashMap<String, String> hm2 = new HashMap<String, String>();
        
      //Resultant KB List
        ArrayList<String> kbList=new ArrayList<String>();
        
        
        for(int x=0;x<inputKb.size();x++)
        {
        	String a = inputKb.get(x);
        	
        	String arr[] = a.split(" ");
        	String kbSentence = "";
        	
        	for (int j = 0; j < arr.length; j++)
        		kbSentence += arr[j];
        	
            System.out.println(kbSentence);
            kbSentence = kbSentence.replace("=>", ">");
            
            String shorterKbSent = "";
            
            shorterKbSent = makeKBsentShorter(kbSentence,hm1,hm2);

            System.out.println("HASHMAP : " + hm1);
            System.out.println("Shorter KB Sentence :" + shorterKbSent);
            
            
            String prefixConversion = makePrefixExpression(shorterKbSent);
            System.out.println("Prefix for the first time : " + prefixConversion);
            
            String removeImplicationForm = "";
            String prefixAfterImplicationRemoval = "";
            
            //Remove implication
            if(prefixConversion.contains(">"))
            {
            	removeImplicationForm = removeImplication(prefixConversion);
            	prefixAfterImplicationRemoval = makePrefixExpression(removeImplicationForm);
            	
            }
            else
            {
            	prefixAfterImplicationRemoval = prefixConversion;
            }
            
            System.out.println("prefixAfterImplicationRemoval  : "+ prefixAfterImplicationRemoval);
            
            String moveNegationInwardForm = "";
            String prefixAfterInwardNegation = "";
          
            if(prefixAfterImplicationRemoval.contains("~"))
            {
            	moveNegationInwardForm = removeNegation(prefixAfterImplicationRemoval);
            	prefixAfterInwardNegation = makePrefixExpression(moveNegationInwardForm);
            }
            else
            {
            	prefixAfterInwardNegation = prefixAfterImplicationRemoval;
            }
            
            System.out.println("prefixAfterInwardNegation  : "+ prefixAfterInwardNegation);
            
            //part2 handling distributivity
            String distributiveForm = performDistributivity(prefixAfterInwardNegation);
            
            System.out.println("After Distributive law : " + distributiveForm);
            
            
            ArrayList<String> splitAtAnd = new ArrayList<String>();
            for (String t: distributiveForm.split("&"))
            	splitAtAnd.add(t);
            
            for (String iter : splitAtAnd) {
                String ans = "";
                for (int i = 0; i < iter.length(); i++) {

                    if (iter.charAt(i) == 'p') {
                        String temp = iter.substring(i, i + 4);
                        if (hm1.containsKey(temp)) {
                            String newtemp = hm1.get(temp);
                            ans += newtemp;
                            i += 3;
                        }
                    } else {
                        ans += iter.charAt(i) + "";
                    }
                }
                kbList.add(ans);
            }     
           }
        System.out.println("Current KB is :  "+kbList);	
        
      //Storing KB
      
      //Step1 standardize the variables now :
        
      ArrayList<String> standardizedKB = new ArrayList<String>();
      int hmaptrycount = 0;
      
      
      for(int k=0;k<kbList.size();k++) {

          String trying = kbList.get(k);
          HashMap<String, String> hmaptry = new HashMap<String, String>();

          for (int i = 0; i < trying.length(); i++) {

              if (trying.charAt(i) == '(') {
                  
                  String parameters = "";
                  i++;
                  while (trying.charAt(i) != ')') {
                      parameters += trying.charAt(i) + "";

                      i++;
                  }

                  if (parameters.contains(",")) {

                      String parlist[] = parameters.split(",");


                      for (String par1 : parlist) {
                          if (par1.charAt(0) >= 'a' && par1.charAt(0) <= 'z') {
                              if (!hmaptry.containsKey(par1)) {
                                  hmaptrycount++;
                                  String pping = "";
                                  if (hmaptrycount > 0 && hmaptrycount <= 9) {
                                      pping = "p00" + hmaptrycount;
                                  } else if (hmaptrycount > 9 && hmaptrycount <= 99) {
                                      pping = "p0" + hmaptrycount;
                                  } else if (hmaptrycount > 99 && hmaptrycount <= 999) {
                                      pping = "p" + hmaptrycount;
                                  }
                                  
                                  hmaptry.put(par1, pping);
                              }
                              
                          }
                      }

                  } else {
                      if (parameters.charAt(0) >= 'a' && parameters.charAt(0) <= 'z') {
                          if (!hmaptry.containsKey(parameters)) {
                              hmaptrycount++;
                              String pping = "";
                              if (hmaptrycount > 0 && hmaptrycount <= 9) {
                                  pping = "p00" + hmaptrycount;
                              } else if (hmaptrycount > 9 && hmaptrycount <= 99) {
                                  pping = "p0" + hmaptrycount;
                              } else if (hmaptrycount > 99 && hmaptrycount <= 999) {
                                  pping = "p" + hmaptrycount;
                              }
                             
                              hmaptry.put(parameters, pping);
                          }
                      }
                  }
              }
          }
         
          Iterator it = hmaptry.entrySet().iterator();
          String newtrying = trying;

          while (it.hasNext()) {
          	
              Map.Entry pair = (Map.Entry) it.next();
              newtrying = newtrying.replace(pair.getKey().toString(), pair.getValue().toString());
              it.remove(); 
          }
          
          standardizedKB.add(newtrying);
      }
      
      System.out.println("\nStandardized KB is : "+standardizedKB);

      //end standardizing variables
        
     
      //start storing the kb
      //HASHMAP FOR KB-STORAGE
      kbmaker(standardizedKB);
      System.out.println("Standard KB Map is :" + kbHmap);
   
      
      Stack<String> queryStack=new Stack<String>();
	  
	  String givenQuery = "H(John)";
	  
	  if(givenQuery.contains("~"))
    	  givenQuery=givenQuery.substring(1);
      else
    	  givenQuery="~"+givenQuery;
      
      queryStack.push(givenQuery);

      //DFS function on KB HashMap starts here:

      loopBreaker=0;
      boolean res=dfsOnKBMap(queryStack,loopBreaker);
      
      if(res==true)
      {	
    	  System.out.println("TRUE");
    	  writer.write("TRUE");
    	  writer.write("\n");
      }
      else
      {
          System.out.println("FALSE");
          writer.write("FALSE");
          writer.write("\n");
      }
      
      
      
      // Perform Inference on each query :
     /* 
      for(int i=0;i<query.size();i++)
      {
    	  Stack<String> queryStack=new Stack<String>();
    	  
    	  String givenQuery = query.get(i);
    	  
    	  if(givenQuery.contains("~"))
        	  givenQuery=givenQuery.substring(1);
          else
        	  givenQuery="~"+givenQuery;
          
          queryStack.push(givenQuery);

          //DFS function on KB HashMap starts here:

          loopBreaker=0;
          boolean res=dfsOnKBMap(queryStack,loopBreaker);
          
          if(res==true)
          {	
        	  System.out.println("TRUE");
        	  writer.write("TRUE");
        	  writer.write("\n");
          }
          else
          {
              System.out.println("FALSE");
              writer.write("FALSE");
              writer.write("\n");
          }
      }*/
      
      writer.close();
      
    }
}