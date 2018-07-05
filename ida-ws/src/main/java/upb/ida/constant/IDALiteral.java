package upb.ida.constant;

public interface IDALiteral {
	public static final String EXAMPLE = "this is an example constant string";
	
	//Action literals
	public static final int UIA_LOADDS = 1;
	public static final int UIA_FDG = 2;
	public static final int UIA_BG = 3;
	public static final int UIA_VWTABLE = 1;
	
	//Rivescript literals
	public static final String RS_INSTANCE = "RSbot";
	public static final String RS_USER = "user";
	public static final String RS_DIRPATH = "./rivescript";
	public static final String RS_LOADDATA_ROUTINE = "loadData";
	
	//Response literals
	public static final String RESP_PASS_ROUTINE = "pass";
	public static final String RESP_FAIL_ROUTINE = "fail";

}
