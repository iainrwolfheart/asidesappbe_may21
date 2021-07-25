package com.project.asidesappbe.constants;

public final class RouteConstants {

//	MESSY
	public static final String LITE_ENDPOINT = "/litegroup";
	public static final String REGISTER_ENDPOINT = "/register";
	public static final String LOGOUT_ENDPOINT = "/logout";
//	SHARED
	public static final String CREATE_ENDPOINT = "/create";
	public static final String GETBYID_ENDPOINT = "/getbyid";
	public static final String GETALL_ENDPOINT = "/getall";

	public static final String DELETEBYID_ENDPOINT = "/deletebyid";
	//	GROUP SPECIFIC
	public static final String GETGROUPBYINVITECODE_ENDPOINT = "/getbyinvitecode";
	public static final String ADDGROUPPLAYERS = "/players/add";
	public static final String REMOVEGROUPPLAYERS = "/players/remove";
//	MATCH SPECIFIC
	public static final String UPDATEMATCHPLAYERS = "/addorremovematchplayer";

}
