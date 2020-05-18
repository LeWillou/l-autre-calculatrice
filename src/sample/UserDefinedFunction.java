package sample;

public class UserDefinedFunction {
    protected String functionName;
    protected String functionExpression;

    public UserDefinedFunction(String name, String expression){
        setFunctionName(name);
        setFunctionExpression(expression);
    }

    public void setFunctionName(String name){
        this.functionName = name;
    }

    public void setFunctionExpression(String expression){
        this.functionExpression = expression;
    }

    public String getFunctionName(){return this.functionName;}

    public String getFunctionExpression(){return this.functionExpression;}
}
