package support.utils.xml;

public enum TypeEnum {
	/**
	 * 何种查找类型
	 */
	EMPTY(0),BOOLEAN(1), BYTE(2), CHAR(3), DOUBLE(4), SHORT(5), FLOAT(6), INT(7), LONG(8), STRING(9);

	private int nCode;

	private TypeEnum(int _nCode) {
		this.nCode = _nCode;
	}
	
	public static TypeEnum valueOfTE(String tn) { //手写的从int到enum的转换函数
       if(tn.equalsIgnoreCase("BOOLEAN")) {
    	   return BOOLEAN;
       } else if(tn.equalsIgnoreCase("BYTE")){
    	   return BYTE;	
       }else if(tn.equalsIgnoreCase("CHAR")){
    	   return CHAR;		
       }else if(tn.equalsIgnoreCase("DOUBLE")){
    	   return DOUBLE;		
       }else if(tn.equalsIgnoreCase("SHORT")){
    	   return SHORT;	
       }else if(tn.equalsIgnoreCase("FLOAT")){
    	   return FLOAT;	
       }else if(tn.equalsIgnoreCase("INT")){
    	   return INT;		
       }else if(tn.equalsIgnoreCase("LONG")){
    	   return LONG;	
       }else if(tn.equalsIgnoreCase("STRING")){
    	   return STRING;		
       }
       
       return EMPTY;
    }
	
	public int value() {
		return this.nCode;
	}
	
	@Override
	public String toString() {
		return String.valueOf(this.nCode);
	}
}
